package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.istarindia.apps.dao.BatchScheduleEvent;
import com.istarindia.apps.dao.BatchScheduleEventDAO;
import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.LessonDAO;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class PlayEventWorkflow
 */
@WebServlet("/play_event_workflow")
public class PlayEventWorkflow extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlayEventWorkflow() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		printParams(request);

		try {
			if (request.getSession() != null) {
				request.getSession(false).invalidate();
				request.getSession(true);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String event_id = request.getParameter("event_id");
		String user_id = request.getParameter("user_id");
		request.getSession().removeAttribute("user");
		IstarUserDAO dao = new IstarUserDAO();
		IstarUser user = dao.findById(Integer.parseInt(user_id));
		request.getSession().setAttribute("user", user);
		request.getSession().setAttribute("event_id", event_id);

		DBUTILS util = new DBUTILS();
		BatchScheduleEvent event = new BatchScheduleEventDAO().findById(UUID.fromString(event_id));
		int batch_id = event.getBatch().getId();

		// get previous session details

		HashMap<String, Object> previousDetails = getPreviousDetails(batch_id);
		String go_to_url = "";
		if (previousDetails.size() > 0) {
			System.out.println("previous events found");
			// previous details found in event_session_logs
			int course_id = (int) previousDetails.get("course_id");
			int module_id = (int) previousDetails.get("module_id");
			int cmsession_id = (int) previousDetails.get("cmsession_id");
			int lesson_id = (int) previousDetails.get("lesson_id");
			int ppt_id = (int) previousDetails.get("ppt_id");
			int last_slide_found = (int) previousDetails.get("slide_id");
			System.out.println("last slid id from event session log" + last_slide_found);
			int assessment_id = 0;
			int actual_last_slide_id = getLastslideFromPPT(ppt_id);
			// int actual_last_slide_id = 99999999;

			if (actual_last_slide_id != 0 && actual_last_slide_id == last_slide_found) {
				// move to next lesson
				Lesson ll = new LessonDAO().findById(lesson_id);
				int nextLesson_id = getNextLessonID(ll);
				if (nextLesson_id!=-1) {
					Lesson nextLesson = new LessonDAO().findById(nextLesson_id); 
					ppt_id = nextLesson.getPresentaion().getId();
					module_id = nextLesson.getCmsession().getModule().getId();
					cmsession_id = nextLesson.getCmsession().getId();
					lesson_id = nextLesson.getId();
					assessment_id = 0;
					String lesson_type = "PPT";
					HashMap<String, Object> get_first_slide = getFirstSlide(ppt_id);
					last_slide_found = (int) get_first_slide.get("first_slide_id");
					actual_last_slide_id = (int) get_first_slide.get("last_slide_id");
					go_to_url = "/lessons/play_session_trainer.jsp?course_id=" + course_id + "&user_id=" + user_id
							+ "&module_id=" + module_id + "&cmsession_id=" + cmsession_id + "&event_id=" + event_id
							+ "&last_slide_id=99999999&current_slide_id=" + last_slide_found + "&lesson_id=" + lesson_id
							+ "!#" + last_slide_found;
					System.out.println(go_to_url);
					response.sendRedirect(go_to_url);
					// CustomEndPoint.sendAllWithEvent("http"+go_to_url,
					// UUID.fromString(event_id));
				} else {
					// end of course
					go_to_url = "/lessons/content_not_unlocked.jsp";
					response.sendRedirect(go_to_url);
					// CustomEndPoint.sendAllWithEvent("http"+go_to_url,
					// UUID.fromString(event_id));
				}
			} else {
				System.out.println("moving to next slide");

				// moving to next slide
				go_to_url = "/lessons/play_session_trainer.jsp?course_id=" + course_id + "&user_id=" + user_id
						+ "&module_id=" + module_id + "&cmsession_id=" + cmsession_id + "&event_id=" + event_id
						+ "&last_slide_id=99999999&current_slide_id=" + last_slide_found + "&lesson_id=" + lesson_id
						+ "!#" + last_slide_found;
				System.out.println(go_to_url);
				response.sendRedirect(go_to_url);
				// CustomEndPoint.sendAllWithEvent("http"+go_to_url,
				// UUID.fromString(event_id));
			}

		} else {
			// previous details not found in event session_log, start from first
			// slide of fisrt session.
			System.out.println("previous details not found");

			HashMap<String, Object> startCourseDeatils = getStartCourseDetails(batch_id);
			if (startCourseDeatils.size() > 0) {
				int course_id = (int) startCourseDeatils.get("course_id");
				int ppt_id = (int) startCourseDeatils.get("ppt_id");
				int module_id = (int) startCourseDeatils.get("module_id");
				int cmsession_id = (int) startCourseDeatils.get("cm_id");
				int lesson_id = (int) startCourseDeatils.get("l_id");
				String lesson_type = (String) startCourseDeatils.get("lesson_type");
				int assessment_id = (int) startCourseDeatils.get("assessment_id");
				HashMap<String, Object> get_first_slide = getFirstSlide(ppt_id);
				int last_slide_found = (int) get_first_slide.get("first_slide_id");
				int actual_last_slide_id = (int) get_first_slide.get("last_slide_id");
				go_to_url = "/lessons/play_session_trainer.jsp?course_id=" + course_id + "&user_id=" + user_id
						+ "&module_id=" + module_id + "&cmsession_id=" + cmsession_id + "&event_id=" + event_id
						+ "&last_slide_id=99999999&current_slide_id=" + last_slide_found + "&lesson_id=" + lesson_id
						+ "!#" + last_slide_found;
				// CustomEndPoint.sendAllWithEvent("http"+go_to_url,
				// UUID.fromString(event_id));
				System.out.println(go_to_url);
				response.sendRedirect(go_to_url);

			} else {
				// nothing is published
				System.out.println("end of course");
				go_to_url = "/lessons/content_not_unlocked.jsp";
				response.sendRedirect(go_to_url);
				// CustomEndPoint.sendAllWithEvent("http"+go_to_url,
				// UUID.fromString(event_id));
			}

		}

	}

	private HashMap<String, Object> getStartCourseDetails(int batch_id) {
		// TODO Auto-generated method stub
		HashMap<String, Object> data = new HashMap<>();
		DBUTILS util = new DBUTILS();
		String move_next_lesson = " SELECT DISTINCT 	P . ID AS ppt_id, 	0 AS assessment_id, 	M . ID AS module_id, 	M .order_id AS module_order, 	CM. ID AS cm_id, 	CM.order_id AS cm_order, 	L. ID AS l_id, 	L.order_id AS l_order_id, 	'PPT' AS lesson_type, C.id as course_id FROM 	slide S, 	task T, 	presentaion P, 	lesson L, 	cmsession CM, 	MODULE M, 	course C, batch WHERE 	C . ID = M .course_id AND M . ID = CM.module_id AND CM. ID = L.session_id AND L. ID = P .lesson_id AND P . ID = S.presentation_id AND T .status = 'PUBLISHED' AND T .item_id = L. ID and L.dtype != 'ASSESSMENT' AND T .item_type = 'LESSON' AND C . ID = batch.course_id and batch.id ="
				+ batch_id
				+ " GROUP BY 	L. ID, 	P . ID, 	M . ID, 	CM. ID, C.id HAVING 	(COUNT(S. ID) > 1) ORDER BY 	module_order, 	cm_order, 	l_order_id LIMIT 1 					";
		// System.out.println(move_next_lesson);
		List<HashMap<String, Object>> next_lesson_res = util.executeQuery(move_next_lesson);
		if (next_lesson_res.size() > 0) {
			data = next_lesson_res.get(0);
		}
		return data;
	}

	private HashMap<String, Object> getFirstSlide(int ppt_id) {
		HashMap<String, Object> data = new HashMap<>();
		DBUTILS util = new DBUTILS();
		String get_first_last_slide = "(SELECT 	 		P . ID AS ppt_id, 		S. ID AS first_slide_id, 		0 AS last_slide_id 	FROM 		presentaion P, 		slide S 	WHERE 	P.id = "
				+ ppt_id
				+ " 	AND S.presentation_id = P . ID ORDER BY 		S.order_id 	LIMIT 1 )  union (SELECT 	 		P . ID AS ppt_id, 		0 AS first_slide_id, 		S. ID AS last_slide_id 	FROM 		presentaion P, 		slide S 	WHERE 	P.id = "
				+ ppt_id + " 	AND S.presentation_id = P . ID ORDER BY 		S.order_id desc 	LIMIT 1 )";
		System.out.println(get_first_last_slide);
		List<HashMap<String, Object>> ppt_res = util.executeQuery(get_first_last_slide);
		if (ppt_res.size() > 0) {
			data = ppt_res.get(1);
		}

		return data;
	}

	private int getNextLessonID(Lesson prevLesson) {
		String sql = "select cast (row_number() over (ORDER BY MODULE .order_id,cmsession.order_id,lesson.order_id) as integer) as rownum, lesson.id as l_id, lesson.title as l_title, lesson.order_id as l_order,  cmsession.id as cm_id , cmsession.title as cm_titile, cmsession.order_id as cm_order, module.order_id as m_order from lesson, task, cmsession, module where module.id  = cmsession.module_id and cmsession.id = lesson.session_id and lesson.id = task.item_id and module.course_id ="+prevLesson.getCmsession().getModule().getCourse().getId()+" and task.status='PUBLISHED' and lesson.dtype!='ASSESSMENT' order by module.order_id, cmsession.order_id , lesson.order_id";
		System.out.println("get next lesson id--"+sql);
		DBUTILS util = new DBUTILS();
		int next_lession_id=-1;
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		
		int curr_row_num = 0;
		
		for(HashMap<String, Object> row: data)
		{
			int less_id = (int)row.get("l_id");
			if(less_id==prevLesson.getId())
			{	 
				curr_row_num = (int) row.get("rownum") ;
				break;
			}
		}
		
		for(HashMap<String, Object> row: data)
		{
			int temp_row_num = (int) row.get("rownum");
			
			if(temp_row_num>curr_row_num)
			{
				int l_id  =  (int) row.get("l_id");				
				next_lession_id = l_id;
				break;
			}
			
		
		}
	
	System.out.println("next lesson_id--------"+next_lession_id);	
	return next_lession_id;
	
	}
	private HashMap<String, Object> getnextLessonDetails(int course_id, int lesson_id) {
		DBUTILS util = new DBUTILS();
		HashMap<String, Object> data = new HashMap<>();
		String move_next_lesson = "SELECT DISTINCT 				P . ID AS ppt_id, 				0 AS assessment_id, 				M . ID AS module_id, 				M .order_id AS module_order, 				CM. ID AS cm_id, 				CM.order_id AS cm_order, 				L. ID AS l_id, 				L.order_id AS l_order_id, 				'PPT' AS lesson_type 			FROM 				slide S, 				task T, 				presentaion P, 				lesson L, 				cmsession CM, 				MODULE M, 				course C, 				lesson L_old 			WHERE 				C . ID = M .course_id 			AND M . ID = CM.module_id 			AND CM. ID = L.session_id 			AND L. ID = P .lesson_id 			AND P . ID = S.presentation_id 			AND T .status = 'PUBLISHED' 			AND T .item_id = L. ID 			AND T .item_type = 'LESSON' 			AND C . ID = "
				+ course_id + " 	AND  L.dtype !='ASSESSMENT'		AND L_old. ID = " + lesson_id
				+ " 			AND L.order_id > L_old.order_id 			GROUP BY 				L. ID, 				P . ID, 				M . ID, 				CM. ID 			HAVING 				(COUNT(S. ID) > 1) 			ORDER BY 				module_order, 				cm_order, 				l_order_id limit 1";
		System.out.println(move_next_lesson);
		List<HashMap<String, Object>> next_lesson_res = util.executeQuery(move_next_lesson);
		if (next_lesson_res.size() > 0) {
			data = next_lesson_res.get(0);
		}
		return data;
	}

	private int getLastslideFromPPT(int ppt_id) {
		DBUTILS util = new DBUTILS();
		int last_slide_id = 0;
		String last_slide_sql = "select id as last_slide_id from slide where presentation_id=" + ppt_id
				+ " order by order_id desc limit 1";
		System.out.println(last_slide_sql);
		List<HashMap<String, Object>> slide_res = util.executeQuery(last_slide_sql);
		for (HashMap<String, Object> rows : slide_res) {
			last_slide_id = (int) rows.get("last_slide_id");
		}
		return last_slide_id;
	}

	private HashMap<String, Object> getPreviousDetails(int batch_id) {
		HashMap<String, Object> data = new HashMap<>();
		String sql2 = "select course_id, cmsession_id, module_id, lesson_id, ppt_id, slide_id from event_session_log where batch_id = "
				+ batch_id + " order by id desc limit 1;";
		System.out.println(sql2);
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> prev_event_details = util.executeQuery(sql2);
		if (prev_event_details.size() > 0) {
			data = prev_event_details.get(0);
		}
		return data;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
