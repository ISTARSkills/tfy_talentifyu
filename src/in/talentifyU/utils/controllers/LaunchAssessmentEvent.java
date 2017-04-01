package in.talentifyU.utils.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.utils.services.LessonPlayUtils;

/**
 * Servlet implementation class LaunchAssessmentEvent
 */
@WebServlet("/launch_assessment_event")
public class LaunchAssessmentEvent extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LaunchAssessmentEvent() {
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
		String assessment_id = request.getParameter("assessment_id");
		String user_id = request.getParameter("user_id");
		Assessment a = new AssessmentDAO().findById(Integer.parseInt(assessment_id));
		DBUTILS util = new DBUTILS();
		String  redirection_url= "";
		String event_sql ="select cast(id as varchar(50)) from istar_assessment_event where eventdate <=now() and assessment_id ="+assessment_id+" and actor_id ="+user_id;
		System.out.println(event_sql);
		try {
			if(util.executeQuery(event_sql).size()>0)
			{
				boolean resuming_assessment = getResumeState(assessment_id, user_id);
				
				String duration = 60 + "";
				String session_title="NOT_AVAILABLE";
				String title="NOT_AVAILABLE";
				if(resuming_assessment || a.getIsRetryAble())
				{
					
					try {
						if (a.getAssessmentdurationminutes() != null) {
							duration = a.getAssessmentdurationminutes().toString();
						}

						session_title = a.getLesson().getCmsession().getTitle();
						if (a.getLesson().getCmsession().getTitle() == null) {
							session_title = "NOT_PROVIDED";
						}
						title = a.getAssessmenttitle();
						if (title == null || title.equalsIgnoreCase("")) {
							title = "NOT_PROVIDED";
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					IstarUser user = new IstarUserDAO().findById(Integer.parseInt(user_id));
					request.getSession().removeAttribute("user");
					request.getSession().removeAttribute("score");
					
					request.getSession().removeAttribute("launch_test");
					
					StringBuffer sb = new LessonPlayUtils().getLaunchTest(assessment_id, duration, session_title, title);
					request.getSession().setAttribute("launch_test", sb);
					request.getSession().setAttribute("user", null);
					request.getSession().setAttribute("score", 0);
					
					request.getSession().setAttribute("user", user);
					request.getSession().setAttribute("assessmentId", assessment_id);
					request.getRequestDispatcher("/assess_event/launch_assessment_test.jsp").forward(request, response);
				}
				else
				{
					System.out.println("another attempet not allowed");
					request.getRequestDispatcher("/assess_event/already_attempted.jsp").forward(request, response);
				}				
			}
			else
			{
				response.sendRedirect("/lessons/content_not_unlocked.jsp");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private boolean getResumeState(String assess_id, String user_id) {
		String sql = "select assessment_question.questionid from assessment_question , assessment_option where assessmentid="+assess_id+" and assessment_option.question_id= assessment_question.questionid and trim(assessment_option.text) !='' and assessment_question.questionid not in (select question_id from student_assessment where student_id = "+user_id+" and assessment_id="+assess_id+") group by assessment_question.questionid having (count(assessment_option)>=2)";
		DBUTILS util = new DBUTILS();
		if(util.executeQuery(sql).size()>0)
		{
			// questions are remaining
			return true;
		}
		else
		{	return false;
		
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
