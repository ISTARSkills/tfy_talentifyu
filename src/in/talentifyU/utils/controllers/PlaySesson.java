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

import org.hibernate.Query;

import com.viksitpro.core.dao.entities.Cmsession;
import com.viksitpro.core.dao.entities.CmsessionDAO;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.LessonDAO;
import com.viksitpro.core.dao.entities.ModuleDAO;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class PlaySesson
 */
@WebServlet("/play_session")
public class PlaySesson extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlaySesson() {
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
		String url = "/lessons/content_not_unlocked.jsp";

		ArrayList<Cmsession> sessions = new ArrayList<>();
		ArrayList<Lesson> lessons = new ArrayList<>();
		int cm_session_id = 0;
		int course_id = 0;
		int module_id = 0;
		int ppt_id = -1;
		String source = "session_play";
		StringBuffer session_list = new StringBuffer();

		if (request.getParameterMap().containsKey("source")) {
			source = request.getParameter("source");
		}

		cm_session_id = Integer.parseInt(request.getParameter("cm_session_id"));
		module_id = new CmsessionDAO().findById(cm_session_id).getModule().getId();
		course_id = new ModuleDAO().findById(module_id).getCourse().getId();
		
		String hql = "from Module as model where model.course=:sid order by order_id";
		Query q = (new IstarUserDAO()).getSession().createQuery(hql);
		q.setInteger("sid", course_id);
		List<Module> moduleList = q.list();
		boolean lesson_available = false;
		boolean is_empty = true;
		for (Module module : moduleList) {
			String hql1 = "from Cmsession as model where model.module=:sid  order by order_id";
			Query q1 = (new IstarUserDAO()).getSession().createQuery(hql1);
			q1.setInteger("sid", module.getId());
			List<Cmsession> sessionList = q1.list();
			
			for (Cmsession cms : sessionList) {
				String hql2 = "from Lesson as model where model.cmsession=:sid  order by order_id";
				Query q2 = (new IstarUserDAO()).getSession().createQuery(hql2);
				q2.setInteger("sid", cms.getId());
				List<Lesson> lls = q2.list();
				try {
					for (Lesson lesson : lls) {
						try {
							if (lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {														
								is_empty = false;
								break;
							}		
						} catch (Exception e) {
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// add session to the list only if at least one of it's lesson
				// is published
				if (!is_empty) {					
					sessions.add(cms);
				}
				
				
				for (Lesson lesson : lls) {
					// System.err.println(lesson.getId());
					try {
						if(lesson.getCmsession().getId()==cm_session_id && lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED"))
						{
							lesson_available =  true;
							lessons.add(lesson);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				
				
			}
		}
		System.out.println("lesson_available"+lesson_available);
		System.out.println("is_empty"+is_empty);
		if(cm_session_id <=0 || !lesson_available || is_empty)
		{
			//end of session should display here
			url = "/lessons/content_not_unlocked.jsp";
		}
		
		else
		{
			//int next_session_id = -1;
			//int previous_session_id = -1;
			
			for (int i = 0; i < sessions.size(); i++) {
				session_list.append(sessions.get(i).getId() + "__");					
			}
			
			int previous_lesson_id = -1;		
			if(request.getParameterMap().containsKey("previous_lesson_id"))
			{
				previous_lesson_id = Integer.parseInt(request.getParameter("previous_lesson_id"));
				Lesson PrevLesson = new LessonDAO().findById(previous_lesson_id);
				System.out.println("prev_lesson_id----"+previous_lesson_id);
				int next_lesson_id = getNextLessonID(PrevLesson);
				if(next_lesson_id!=-1)
				{
					Lesson lesson = new LessonDAO().findById(next_lesson_id);
					if ((lesson.getPresentaion() != null) && lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
						ppt_id = lesson.getPresentaion().getId();
						StringBuffer params = new StringBuffer();
						params.append("user_id=" + request.getParameter("user_id"));
						params.append("&next_session_id=" + getNextSessionId(lesson, source));
						params.append("&previous_session_id=" + lesson.getCmsession().getId());
						params.append("&ppt_id=" + ppt_id);
						params.append("&module_id=" + module_id);
						params.append("&course_id=" + course_id);
						params.append("&cm_session_id=" + cm_session_id);
						params.append("&source=" + source);
						params.append("&previous_lesson_id=" + lesson.getId());
						params.append("&session_list=" + session_list);

						url = "/lessons/play_session.jsp?" + params.toString();
					} else if ((lesson.getAssessment() != null)
							&& lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
						StringBuffer params = new StringBuffer();
						params.append("user_id=" + request.getParameter("user_id"));
						params.append("&next_session_id=" +  getNextSessionId(lesson, source));
						params.append("&previous_session_id=" + lesson.getCmsession().getId());
						params.append("&ppt_id=" + ppt_id);
						params.append("&module_id=" + module_id);
						params.append("&course_id=" + course_id);
						params.append("&cm_session_id=" + cm_session_id);
						params.append("&source=" + source);
						params.append("&previous_lesson_id=" + lesson.getId());
						params.append("&session_list=" + session_list);
						url = "/launch_learn_assessment?type=session_play&assessment_id=" + lesson.getAssessment().getId()
								+ "&user_id=" + request.getParameter("user_id") + "&" + params.toString();
					}
				}
				else
				{
					url = "/lessons/content_not_unlocked.jsp";
				}	

			}
			else
			{
				Lesson lesson  = lessons.get(0);
				if ((lesson.getPresentaion() != null) && lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
					ppt_id = lesson.getPresentaion().getId();
					StringBuffer params = new StringBuffer();
					params.append("user_id=" + request.getParameter("user_id"));
					params.append("&next_session_id=" + getNextSessionId(lesson, source));
					params.append("&previous_session_id=" + lesson.getCmsession().getId());
					params.append("&ppt_id=" + ppt_id);
					params.append("&module_id=" + module_id);
					params.append("&course_id=" + course_id);
					params.append("&cm_session_id=" + cm_session_id);
					params.append("&source=" + source);
					params.append("&previous_lesson_id=" + lesson.getId());
					params.append("&session_list=" + session_list);

					url = "/lessons/play_session.jsp?" + params.toString();
				} else if ((lesson.getAssessment() != null)
						&& lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
					StringBuffer params = new StringBuffer();
					params.append("user_id=" + request.getParameter("user_id"));
					params.append("&next_session_id=" +  getNextSessionId(lesson, source));
					params.append("&previous_session_id=" + lesson.getCmsession().getId());
					params.append("&ppt_id=" + ppt_id);
					params.append("&module_id=" + module_id);
					params.append("&course_id=" + course_id);
					params.append("&cm_session_id=" + cm_session_id);
					params.append("&source=" + source);
					params.append("&previous_lesson_id=" + lesson.getId());
					params.append("&session_list=" + session_list);
					url = "/launch_learn_assessment?type=session_play&assessment_id=" + lesson.getAssessment().getId()
							+ "&user_id=" + request.getParameter("user_id") + "&" + params.toString();
				}
			}							
			session_list.append("-1");
		}	
		
		
		System.err.println(url);
		response.sendRedirect(url);
	}

	private int getNextLessonID(Lesson prevLesson) {
		String sql = "select cast (row_number() over (ORDER BY MODULE .order_id,cmsession.order_id,lesson.order_id) as integer) as rownum, lesson.id as l_id, lesson.title as l_title, lesson.order_id as l_order,  cmsession.id as cm_id , cmsession.title as cm_titile, cmsession.order_id as cm_order, module.order_id as m_order from lesson, task, cmsession, module where module.id  = cmsession.module_id and cmsession.id = lesson.session_id and lesson.id = task.item_id and module.course_id ="+prevLesson.getCmsession().getModule().getCourse().getId()+" and task.status='PUBLISHED' order by module.order_id, cmsession.order_id , lesson.order_id";
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

	private int getNextSessionId(Lesson lesson, String source) {
		String sql = "select cast (row_number() over (ORDER BY MODULE .order_id,cmsession.order_id,lesson.order_id)as integer) as rownum,lesson.id as l_id, lesson.title as l_title, lesson.order_id as l_order,  cmsession.id as cm_id , cmsession.title as cm_titile, cmsession.order_id as cm_order, module.order_id as m_order from lesson, task, cmsession, module where module.id  = cmsession.module_id and cmsession.id = lesson.session_id and lesson.id = task.item_id and module.course_id ="+lesson.getCmsession().getModule().getCourse().getId()+" and task.status='PUBLISHED' order by module.order_id, cmsession.order_id , lesson.order_id";
		System.out.println("get next session--"+sql);
		DBUTILS util = new DBUTILS();
		int next_session_id=-1;
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		int curr_row_num = 0;
		
		for(HashMap<String, Object> row: data)
		{
			int less_id = (int)row.get("l_id");
			if(less_id==lesson.getId())
			{
				curr_row_num =  (int) row.get("rownum");
				break;
			}
		}
		for(HashMap<String, Object> row: data)
		{
			int temp_row_num = (int) row.get("rownum");
			
			if(temp_row_num>curr_row_num)
			{
				int session_id  =  (int) row.get("cm_id");
				next_session_id = session_id;
				break;
			}
			
			
			
		}	
		System.out.println("next_session_id --------"+next_session_id);	
		if(source.contains("noticeCMSession__") && next_session_id != Integer.parseInt(source.replace("noticeCMSession__", "")))
		{
			return -1;
		}
		else
		{
			return next_session_id;
		}		
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