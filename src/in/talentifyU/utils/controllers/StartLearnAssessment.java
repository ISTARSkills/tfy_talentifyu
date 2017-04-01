package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.utils.AppUtils;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.AssessmentOption;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.XMLAssessment;
import in.talentifyU.complexData.pojo.XMLOption;
import in.talentifyU.complexData.pojo.XMLQuestion;
import in.talentifyU.utils.services.AssessmentRegistry;

/**
 * Servlet implementation class StartLearnAssessment
 */
@WebServlet("/start_learn_assessment")
public class StartLearnAssessment extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartLearnAssessment() {
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
		HashMap<String, String> reqMAP = (new AppUtils()).getReqMap(request);
		String ppt_id = request.getParameter("ppt_id");
		String course_id = request.getParameter("course_id");
		String slide_id = request.getParameter("slide_id");
		String module_id = request.getParameter("module_id");
		String cmsession_id = request.getParameter("cm_session_id");
		String session_list = request.getParameter("session_list");
		String next_session_id = request.getParameter("next_session_id");
		String previous_session_id = request.getParameter("previous_session_id");
		String source = request.getParameter("source");
		String previous_lesson_id = reqMAP.get("previous_lesson_id");
		System.out.println("in start assess" + next_session_id);
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

		int assessment_id = Integer.parseInt(request.getParameter("assessment_id"));

		int duration = Integer.parseInt(request.getParameter("duration"));
		request.getSession().setAttribute("score", 0);
		AssessmentRegistry reg = AssessmentRegistry.getInstance();
		XMLAssessment assess_details = getAssessmentDetails(assessment_id);
		reg.setAssessment(assess_details);
		// int total_que =
		// AssessmentRegistry.getInstance().getAssessment().getNumber_of_questions();
		AssessmentRegistry.getInstance().setCount_of_questions(assess_details.getQuestions().size());
		AssessmentRegistry.getInstance().setCount_of_correct(0);
		Calendar c = Calendar.getInstance();
		long start_time = c.getTimeInMillis();
		System.out.println("asdd" + reg.getAssessment().getAssessmentTitle());
		if (assess_details.getQuestions().size() > 0) {
			response.sendRedirect("/learn_assessment/start_assessment_test.jsp?duration=" + duration + "&assessment_id="
					+ assessment_id + "&start_time=" + start_time + "&" + params.toString());
		} else {
			response.sendRedirect("/learn_assessment/assess_with_error.jsp?duration=" + duration + "&assessment_id="
					+ assessment_id + "&start_time=" + start_time);

		}

	}

	private XMLAssessment getAssessmentDetails(int assessment_id) {

		XMLAssessment assess = new XMLAssessment();
		Assessment assessment = new AssessmentDAO().findById(assessment_id);

		assess.setAssessmentDurationMinutes(assess.getAssessmentDurationMinutes());
		if (assess.getAssessmentDurationMinutes() == null) {
			assess.setAssessmentDurationMinutes(30);
		}
		assess.setAssessmentId(assessment.getId());

		assess.setAssessmentTitle(assessment.getAssessmenttitle());
		if (assessment.getAssessmenttitle() == null) {
			assess.setAssessmentTitle("NOT_PROVIDED");
		}

		// assess.setNumber_of_questions(assessment.getNumber_of_questions());
		if (assessment.getAssessmentQuestions().size() > 0) {

			ArrayList<XMLQuestion> q = getQuestions(assessment);
			assess.setNumber_of_questions(q.size());
			System.out.println("sdsd d d@@@@@@@@@@@2" + q.size());
			assess.setQuestions(q);
		} else {
			assess.setNumber_of_questions(0);
			assess.setQuestions(new ArrayList<XMLQuestion>());
		}

		return assess;
	}

	private ArrayList<XMLQuestion> getQuestions(Assessment assessment) {
		ArrayList<XMLQuestion> question_set = new ArrayList<>();
		for (AssessmentQuestion aq : assessment.getAssessmentQuestions()) {
			System.out.println("que" + aq.getQuestion().getId());
			XMLQuestion que = new XMLQuestion();
			que.setQueId(aq.getQuestion().getId());
			que.setQuestionText(aq.getQuestion().getQuestionText());
			que.setOrderId(aq.getOrderId());
			que.setQuestionType(aq.getQuestion().getQuestionType());
			que.setDurationInSec(aq.getQuestion().getDurationInSec());

			ArrayList<XMLOption> option = getOptions(aq.getQuestion());
			if (option.size() >= 2) {
				System.out.println("option size" + option.size());
				try {
					System.out.println("sssssssssssssssssss" + option.get(0).getOptionText());
					System.out.println("sssssssssssssssssss" + option.get(1).getOptionText());
					//System.out.println("sssssssssssssssssss" + option.get(2).getOptionText());
					//System.out.println("sssssssssssssssssss" + option.get(3).getOptionText());
					//System.out.println("sssssssssssssssssss" + option.get(4).getOptionText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("exception in StartLearnAssessment with question id "+aq.getQuestion().getId());
				}
				System.out.println("----------------------------------------------");
				que.setOptions(option);
				question_set.add(que);
			}

		}
		return question_set;
	}

	private ArrayList<XMLOption> getOptions(Question question) {
		ArrayList<XMLOption> data = new ArrayList<>();
		for (AssessmentOption aop : question.getAssessmentOptions()) {
			if(aop.getText()!=null && !aop.getText().trim().isEmpty()) {
			XMLOption op = new XMLOption();
			op.setOptionId(aop.getId());
			op.setOptionText(aop.getText());
			if (aop.getMarkingScheme() != null && aop.getMarkingScheme() == 1) {
				op.setCorrect(true);
			} else {
				op.setCorrect(false);
			}
			data.add(op);
			}
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
