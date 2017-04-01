package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.AssessmentOption;
import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.XMLAssessment;
import in.talentifyU.complexData.pojo.XMLOption;
import in.talentifyU.complexData.pojo.XMLQuestion;
import in.talentifyU.utils.services.AssessmentRegistry;

/**
 * Servlet implementation class StartAssessmentEvent
 */
@WebServlet("/start_assessment_event")
public class StartAssessmentEvent extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartAssessmentEvent() {
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

		int assessment_id = Integer.parseInt(request.getParameter("assessment_id"));

		int duration = Integer.parseInt(request.getParameter("duration"));
		request.getSession().setAttribute("score", 0);
		IstarUser user = (IstarUser)request.getSession().getAttribute("user");
		
		
		//returns the list of question not yet answered
		ArrayList<Integer> unansweredQuestion = getUnAnsweredQuestions(user,assessment_id);
		
		AssessmentRegistry reg = AssessmentRegistry.getInstance();
		XMLAssessment assess_details = getAssessmentDetails(assessment_id, unansweredQuestion);
		reg.setAssessment(assess_details);
		// int total_que =
		// AssessmentRegistry.getInstance().getAssessment().getNumber_of_questions();
		AssessmentRegistry.getInstance().setCount_of_questions(assess_details.getQuestions().size());
		AssessmentRegistry.getInstance().setCount_of_correct(0);
		Calendar c = Calendar.getInstance();
		long start_time = c.getTimeInMillis();
		System.out.println("asdd " + reg.getAssessment().getAssessmentTitle());
		if (assess_details.getQuestions().size() > 0) {
			response.sendRedirect("/assess_event/start_assessment_test.jsp?duration=" + duration + "&assessment_id="
					+ assessment_id + "&start_time=" + start_time);
		} else {
			response.sendRedirect("/assess_event/assess_with_error.jsp?duration=" + duration + "&assessment_id="
					+ assessment_id + "&start_time=" + start_time);
		}

	}

	private ArrayList<Integer> getUnAnsweredQuestions(IstarUser user, int assessment_id) {
		ArrayList<Integer> questions =  new  ArrayList<>();
		DBUTILS util = new DBUTILS();
		String sql = "select assessment_question.questionid from assessment_question , assessment_option where assessmentid="+assessment_id+" and assessment_option.question_id= assessment_question.questionid and trim(assessment_option.text) !='' and assessment_question.questionid not in (select question_id from student_assessment where student_id = "+user.getId()+" and assessment_id="+assessment_id+") group by assessment_question.questionid having (count(assessment_option)>=2)";
		
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		for(HashMap<String, Object> row: data)
		{
			int question_id = (int) row.get("questionid");
			questions.add(question_id);
		}		
		return questions;
	}

	private XMLAssessment getAssessmentDetails(int assessment_id,ArrayList<Integer> unansweredQuestion) {

		
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
			ArrayList<XMLQuestion> q = getQuestions(assessment,unansweredQuestion);
			assess.setNumber_of_questions(q.size());
			System.out.println("sdsd d d@@@@@@@@@@@2" + q.size());
			assess.setQuestions(q);
		} else {
			assess.setNumber_of_questions(0);
			assess.setQuestions(new ArrayList<XMLQuestion>());
		}

		return assess;
	}

	private ArrayList<XMLQuestion> getQuestions(Assessment assessment ,ArrayList<Integer>not_attempted) {
		
		boolean resuming =not_attempted.size()> 0 ?true :false; 
		
		
		ArrayList<XMLQuestion> question_set = new ArrayList<>();
		
		String sql_question ="select question.id as q_id , question.question_text as q_txt, question.question_type as q_type, "
				+ "assessment_question.order_id as q_order, question.duration_in_sec as q_duration from assessment_question, question"
				+ " where assessment_question.assessmentid="+assessment.getId()+" and question.id = assessment_question.questionid";
		DBUTILS util = new DBUTILS();
		List<HashMap<String , Object>> data = util.executeQuery(sql_question);
		for(HashMap<String , Object> row: data)
		{
			if(!resuming)
			{
				System.out.println("---------------->!resuming");
				int q_id = row.get("q_id") != null ?(int)row.get("q_id"): 0;
				int q_order = row.get("q_order") != null ?(int)row.get("q_order"): 1;
				String q_type = row.get("q_type") != null ?(String)row.get("q_type"): "1";
				String q_txt  =row.get("q_txt")!= null ?(String)row.get("q_txt"): "not provided";
				int q_duration = row.get("q_duration") !=null ?(int)row.get("q_duration"): 2;
				
				XMLQuestion que = new XMLQuestion();
				que.setQueId(q_id);
				que.setQuestionText(q_txt);
				que.setOrderId(q_order);
				que.setQuestionType(q_type+"");
				que.setDurationInSec(q_duration);
				ArrayList<XMLOption> option = getOptions(q_id);
				
				if (option.size() >= 2) {
					try {
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					que.setOptions(option);
					question_set.add(que);
					}
				
			}	else
			{
				
				
				System.out.println("---------------->resuming");

				int q_id = row.get("q_id") != null ?(int)row.get("q_id"): 0;
				int q_order = row.get("q_order") != null ?(int)row.get("q_order"): 1;
				String q_type = row.get("q_type") != null ?(String)row.get("q_type"): "1";
				String q_txt  =row.get("q_txt")!= null ?(String)row.get("q_txt"): "not provided";
				int q_duration = row.get("q_duration") !=null ?(int)row.get("q_duration"): 2;
				
				if( not_attempted.contains(q_id))
				{
				XMLQuestion que = new XMLQuestion();
				que.setQueId(q_id);
				que.setQuestionText(q_txt);
				que.setOrderId(q_order);
				que.setQuestionType(q_type+"");
				que.setDurationInSec(q_duration);
				ArrayList<XMLOption> option = getOptions(q_id);
				
				if (option.size() >= 2) {
					try {
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					que.setOptions(option);
					question_set.add(que);
					}
				}
				
			}
			
		}
		/*for (AssessmentQuestion aq : assessment.getAssessmentQuestions()) {
			
			if(!resuming)
			{
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
						//e.printStackTrace();
					}
					System.out.println("----------------------------------------------");
					que.setOptions(option);
					question_set.add(que);
				}
			}
			else if(resuming)
			{
				if( not_attempted.contains(aq.getQuestion().getId()))
				{
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
							//e.printStackTrace();
						}
						System.out.println("----------------------------------------------");
						que.setOptions(option);
						question_set.add(que);
					}
				}				
			}		
		}*/
		return question_set;
	}

	private ArrayList<XMLOption> getOptions(Question question) {
		ArrayList<XMLOption> data = new ArrayList<>();
		for (AssessmentOption aop : question.getAssessmentOptions()) {
			if(aop.getText()!=null && !aop.getText().trim().isEmpty()) {
				XMLOption op = new XMLOption();
				op.setOptionId(aop.getId());
				op.setOptionText(aop.getText());
				if (aop.getMarkingScheme() != null && aop.getMarkingScheme() == 1 ) {
					op.setCorrect(true);
				} else {
					op.setCorrect(false);
				}
				data.add(op);
			}
		}
		return data;
	}
	
	private ArrayList<XMLOption> getOptions(int question_id) {
		ArrayList<XMLOption> data = new ArrayList<>();
		String sql = "SELECT id, text, marking_scheme FROM assessment_option WHERE question_id="+question_id;
		DBUTILS util = new  DBUTILS();
		List<HashMap<String, Object >> data_q = util.executeQuery(sql) ;
		for(HashMap<String, Object > row: data_q){
			if(row.get("text")!=null && row.get("text").toString().length()!=0)
			{
				XMLOption op = new XMLOption();
				int id = (int)row.get("id");
				String text = (String)row.get("text");
				op.setOptionId(id);
				op.setOptionText(text);
				if (row.get("id") != null && row.get("id").toString().equalsIgnoreCase("1") ) {
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
