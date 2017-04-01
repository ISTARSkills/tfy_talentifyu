package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.utils.AppUtils;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;

import in.talentifyU.utils.services.LessonPlayUtils;

/**
 * Servlet implementation class LauchLearnAssessment
 */
@WebServlet("/launch_learn_assessment")
public class LauchLearnAssessment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LauchLearnAssessment() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// int assessment_id, int duration, String session_title, String title
		HashMap<String, String> reqMAP = (new AppUtils()).getReqMap(request);
		String ppt_id = reqMAP.get("ppt_id");
		String course_id = reqMAP.get("course_id");
		String slide_id = reqMAP.get("slide_id");
		String module_id = reqMAP.get("module_id");
		String cmsession_id = reqMAP.get("cm_session_id");
		String session_list = reqMAP.get("session_list");
		String next_session_id = reqMAP.get("next_session_id");
		String previous_session_id = reqMAP.get("previous_session_id");
		String source = reqMAP.get("source");
		String previous_lesson_id = reqMAP.get("previous_lesson_id");
		String assessment_id = request.getParameter("assessment_id");
		Assessment a = new AssessmentDAO().findById(Integer.parseInt(assessment_id));
		String duration = 60 + "";
		if (a.getAssessmentdurationminutes() != null) {
			duration = a.getAssessmentdurationminutes().toString();
		}

		String session_title = a.getLesson().getCmsession().getTitle();
		if (a.getLesson().getCmsession().getTitle() == null) {
			session_title = "NOT_PROVIDED";
		}
		String title = a.getAssessmenttitle();
		if (title == null || title.equalsIgnoreCase("")) {
			title = "NOT_PROVIDED";
		}

		StringBuffer params = new StringBuffer();
		params.append("user_id=" + request.getParameter("user_id"));
		params.append("&next_session_id=" + next_session_id);
		params.append("&previous_session_id=" + previous_session_id);
		params.append("&module_id=" + module_id);
		params.append("&ppt_id=" + ppt_id);
		params.append("&course_id=" + course_id);
		params.append("&cm_session_id=" + cmsession_id);
		params.append("&source=" + source);
		params.append("&session_list=" + session_list);
		params.append("&previous_lesson_id=" + previous_lesson_id);
		String user_id = request.getParameter("user_id");
		IstarUser user = new IstarUserDAO().findById(Integer.parseInt(user_id));
		request.getSession().setAttribute("user", user);
		StringBuffer sb = new LessonPlayUtils().getLaunchTestForLearn(assessment_id, duration, session_title, title,
				ppt_id, course_id, slide_id, module_id, cmsession_id, session_list, next_session_id,
				previous_session_id, source,previous_lesson_id);
		request.getSession().setAttribute("launch_test_learn", sb);
		request.getSession().setAttribute("assessmentId", assessment_id);
		
		/* 
		 * /start_learn_assessment?assessment_id=" + assessment_id + "&duration=" + duration + ""
								+ "&course_id=" + course_id + "&slide_id=" + slide_id + "&module_id=" + module_id + "&cmsession_id="
								+ cmsession_id + "&next_session_id=" + next_session_id + "&previous_session_id=" + previous_session_id
								+ "&source=" + source + "&previous_lesson_id="+previous_lesson_id+"
		 */
		
		String url = "/start_learn_assessment?assessment_id=" + assessment_id + "&duration=" + duration + ""
								+ "&course_id=" + course_id + "&slide_id=" + slide_id + "&module_id=" + module_id + "&cmsession_id="
								+ cmsession_id + "&next_session_id=" + next_session_id + "&previous_session_id=" + previous_session_id
								+ "&source=" + source + "&previous_lesson_id="+previous_lesson_id;
		//request.getRequestDispatcher("/learn_assessment/launch_assessment_test.jsp").forward(request, response);
		response.sendRedirect(url);
		

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
