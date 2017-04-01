package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.LessonDAO;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class StartPresentor
 */
@WebServlet("/start_presentor")
public class StartPresentor extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartPresentor() {
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
		int classroom_id = Integer.parseInt(request.getParameter("classroom_id"));
		ArrayList<HashMap<String, String>> data = getEventId(classroom_id);
		System.out.println("dsdsd" + data.size());
		if (data.size() <= 0) {
			request.setAttribute("error_message", "No event is scheduled at this time in this ClassRoom (ID: "
					+ classroom_id + " ).<br/> Contact Operation Team Representative.");
			request.getRequestDispatcher("/trainer/error.jsp").forward(request, response);
			com.istarindia.apps.dao.UUIUtils.printlog("no event found");
		} else if (data.size() == 1) {
			HashMap<String, String> details = data.get(0);
			String event_id = details.get("event_id");
			if (event_id != "null") {
				int actor_id = Integer.parseInt(details.get("actor_id"));
				request.getSession().removeAttribute("user");
				IstarUserDAO dao = new IstarUserDAO();
				IstarUser user = dao.findById(actor_id);
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("event_id", event_id);
				int batch_id = Integer.parseInt(details.get("batch_id"));
				// got batch, extracting last session details
				HashMap<String, Object> previousData = getPreviousSessionDetails(batch_id);
				String go_to_url = "";
				if (previousData.size() > 0) {
					System.out.println("previous events found");
					// previous details found in event_session_logs
					int course_id = (int) previousData.get("course_id");
					int module_id = (int) previousData.get("module_id");
					int cmsession_id = (int) previousData.get("cmsession_id");
					int lesson_id = (int) previousData.get("lesson_id");
					int ppt_id = (int) previousData.get("ppt_id");
					int last_slide_found = (int) previousData.get("slide_id");
					int first_slide = 0;
					int assessment_id = 0;
					int actual_last_slide_id = getLastslideFromPPT(ppt_id);

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

							go_to_url = "lessons/play_session_presentor.jsp?event_id=" + event_id + "&last_slide_id="
									+ actual_last_slide_id + "&first_slide_id=" + last_slide_found + "&lesson_id="
									+ lesson_id + "!#" + last_slide_found;
							response.sendRedirect(go_to_url);
							// request.getRequestDispatcher(go_to_url).forward(request,
							// response);
						} else {
							// end of course
							go_to_url = "/trainer/error.jsp?msg=No more sessions are published.";
							response.sendRedirect(go_to_url);
							// request.getRequestDispatcher(go_to_url).forward(request,
							// response);

						}
					} else {
						System.out.println("moving to next slide");
						go_to_url = "lessons/play_session_presentor.jsp?event_id=" + event_id + "&last_slide_id="
								+ actual_last_slide_id + "&first_slide_id=" + last_slide_found + "&lesson_id="
								+ lesson_id + "!#" + last_slide_found;
						response.sendRedirect(go_to_url);

						// request.getRequestDispatcher(go_to_url).forward(request,
						// response);
					}

				} else {
					// previous details not found in event session_log, start
					// from first slide of fisrt session.
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

						go_to_url = "lessons/play_session_presentor.jsp?event_id=" + event_id + "&last_slide_id="
								+ actual_last_slide_id + "&first_slide_id=" + last_slide_found + "&lesson_id="
								+ lesson_id + "!#" + last_slide_found;
						response.sendRedirect(go_to_url);
						// request.getRequestDispatcher(go_to_url).forward(request,
						// response);
					} else {
						// nothing is published
						go_to_url = "/trainer/error.jsp?msg=No sessions are published.";
						response.sendRedirect("/trainer/error.jsp");

						// request.setAttribute("error_message", "Content is not
						// yet published for this session. Contact Operation
						// Team Representative.");
						// request.getRequestDispatcher("/trainer/error.jsp").forward(request,
						// response);
						// go_to_url="/lessons/end_of_course.jsp";
					}

				}

			} else {
				request.getRequestDispatcher("/trainer/no_events_found.jsp").forward(request, response);
				com.istarindia.apps.dao.UUIUtils.printlog("no event found");
			}
		} else if (data.size() > 1) {
			request.setAttribute("error_message",
					"More than one event are scheduled at this time in this ClassRoom (ID: " + classroom_id
							+ " ). Contact Operation Team Representative.");
			request.getRequestDispatcher("/trainer/error.jsp").forward(request, response);
			com.istarindia.apps.dao.UUIUtils
					.printlog("More than one event are scheduled at this time in this ClassRoom (ID: " + classroom_id
							+ " ). Contact Operation Team Representative.");
		}
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
		// System.out.println(get_first_last_slide);
		List<HashMap<String, Object>> ppt_res = util.executeQuery(get_first_last_slide);
		if (ppt_res.size() > 0) {
			data = ppt_res.get(0);
		}

		return data;
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

	private HashMap<String, Object> getPreviousSessionDetails(int batch_id) {
		HashMap<String, Object> data = new HashMap<>();
		DBUTILS util = new DBUTILS();
		String sql2 = "select course_id, cmsession_id, module_id, lesson_id, ppt_id, slide_id "
				+ "from event_session_log where batch_id = " + batch_id + " order by id desc limit 1;";
		com.istarindia.apps.dao.UUIUtils.printlog("sql2" + sql2);
		List<HashMap<String, Object>> prev_event_details = util.executeQuery(sql2);
		if (prev_event_details.size() > 0) {
			data = prev_event_details.get(0);
		}
		return data;
	}

	private ArrayList<HashMap<String, String>> getEventId(int classroom_id) {
		ArrayList<HashMap<String, String>> dataa = new ArrayList<>();

		String event_id = "null";
		String find_event_id = "SELECT CAST (ID AS VARCHAR(70)) AS ID, eventdate, actor_id, batch_id, eventhour, eventminute, CASE WHEN eventdate > now() THEN CAST ( eventdate - now() AS VARCHAR (70) ) ELSE CAST ( now() - eventdate AS VARCHAR (70) ) END AS difference FROM batch_schedule_event WHERE now() > batch_schedule_event.eventdate AND now() < eventdate + ( (eventhour * 60 + eventminute) * INTERVAL '1 minute') AND TYPE = 'BATCH_SCHEDULE_EVENT_PRESENTOR' AND classroom_id = "
				+ classroom_id;
		com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), find_event_id);
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> ress = util.executeQuery(find_event_id);
		int actor_id = 0;
		int batch_id = 0;
		for (HashMap<String, Object> r : ress) {
			HashMap<String, String> data = new HashMap<>();
			com.istarindia.apps.dao.UUIUtils.printlog(r.get("id").toString());
			event_id = ((String) r.get("id"));
			actor_id = (int) r.get("actor_id");
			batch_id = (int) r.get("batch_id");
			data.put("event_id", event_id);
			data.put("actor_id", actor_id + "");
			data.put("batch_id", batch_id + "");
			dataa.add(data);
			// break;
		}

		return dataa;
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
