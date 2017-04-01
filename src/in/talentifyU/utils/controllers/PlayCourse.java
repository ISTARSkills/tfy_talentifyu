package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.ArrayList;
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
import com.viksitpro.core.utilities.DBUTILS;

/**
 * Servlet implementation class PlayCourse
 */
@WebServlet("/play_course")
public class PlayCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlayCourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/lessons/content_not_unlocked.jsp";
		// for a given session find the ppt id and redirect it to that
		// int lessonID =
		// Integer.parseInt(request.getParameter("ppt_id").replaceAll("/", ""));
		// TODO Auto-generated method stub
		ArrayList<Lesson> lessons = new ArrayList<>();
		DBUTILS util = new DBUTILS();
		CmsessionDAO dao = new CmsessionDAO();
		int cm_session_id = -1;
		int next_session_id = -1;
		int previous_session_id = -1;
		int module_id = -1;
		int ppt_id = -1;

		int course_id = Integer.parseInt(request.getParameter("course_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		StringBuffer session_list = new StringBuffer();
		int slide_id = -1;

		ArrayList<Cmsession> sessions = new ArrayList<>();
		String hql = "from Module as model where model.course=:sid order by order_id";
		Query q = (new IstarUserDAO()).getSession().createQuery(hql);
		q.setInteger("sid", course_id);
		List<Module> moduleList = q.list();
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
					
						if (lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {														
							is_empty = false;
							lessons.add(lesson);
							break;
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
				
				/*for (Lesson lesson : lls) {
					// System.err.println(lesson.getId());
					if(lesson.getCmsession().getId()==cm_session_id && lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED"))
					{
						lesson_available =  true;
						lessons.add(lesson);
					}					
				}*/
			}
		}

		/*
		 * String get_history_sql =
		 * "SELECT * from user_session_log where course_id = " + course_id +
		 * " and user_id= " + user_id + " order by created_at desc";
		 * List<HashMap<String, Object>> history =
		 * util.executeQuery(get_history_sql);
		 * 
		 * if (history.size() > 0) { slide_id =
		 * Integer.parseInt(history.get(0).get("slide_id").toString());
		 * module_id =
		 * Integer.parseInt(history.get(0).get("module_id").toString());
		 * cm_session_id =
		 * Integer.parseInt(history.get(0).get("cmsession_id").toString());
		 * ppt_id = Integer.parseInt(history.get(0).get("ppt_id").toString());
		 * 
		 * } else {
		 */
		
		if(lessons.size() <=0 || is_empty)
		{
			//end of session should display here
			url = "/lessons/content_not_unlocked.jsp";
		}

		else
		{
			//if (sessions.size() > 0) {
				cm_session_id = lessons.get(0).getCmsession().getId();
			//}

			Cmsession cms = dao.findById(cm_session_id);
			module_id = cms.getModule().getId();
			Lesson lesson = lessons.get(0);
			//for (Lesson lesson : lessons) {
				System.err.println(lesson.getStatus());
				System.err.println(lesson.getId());

				if ((lesson.getPresentaion() != null) && lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
					System.err.println("111");

					ppt_id = lesson.getPresentaion().getId();

					for (int i = 0; i < sessions.size(); i++) {
						session_list.append(sessions.get(i).getId() + "__");
						if (sessions.get(i).getId() == cm_session_id) {
							if (i < (sessions.size() - 1)) {
								next_session_id = sessions.get(i + 1).getId();
							}
							if (i > 0) {
								previous_session_id = sessions.get(i - 1).getId();
							}
						}
					}

					session_list.append("-1");

					StringBuffer params = new StringBuffer();
					params.append("user_id=" + user_id);
					params.append("&previous_session_id=" + previous_session_id);
					params.append("&next_session_id=" + next_session_id);
					params.append("&slide_id=" + slide_id);
					params.append("&ppt_id=" + ppt_id);
					params.append("&module_id=" + module_id);
					params.append("&course_id=" + course_id);
					params.append("&cm_session_id=" + cm_session_id);
					params.append("&source=course_play");
					params.append("&previous_lesson_id=" + lesson.getId());
					params.append("&session_list=" + session_list);
					url = "/lessons/play_session.jsp?" + params.toString();
				} else if ((lesson.getAssessment() != null) && lesson.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
					System.err.println("143");
					StringBuffer params = new StringBuffer();
					params.append("user_id=" + user_id);
					params.append("&previous_session_id=" + previous_session_id);
					params.append("&next_session_id=" + next_session_id);
					params.append("&slide_id=" + slide_id);
					params.append("&ppt_id=" + ppt_id);
					params.append("&module_id=" + module_id);
					params.append("&course_id=" + course_id);
					params.append("&cm_session_id=" + cm_session_id);
					params.append("&previous_lesson_id=" + lesson.getId());
					params.append("&source=course_play");
					params.append("&session_list=" + session_list);
					url = "/launch_learn_assessment?type=session_play&assessment_id=" + lesson.getAssessment().getId()
							+ "&user_id=" + request.getParameter("user_id") + "&" + params.toString();

				} else {
					// url ="/lessons/end_of_course.jsp";

					System.err.println("150");

				}
			//}
		}
		
		

		response.sendRedirect(url);

		// }

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
