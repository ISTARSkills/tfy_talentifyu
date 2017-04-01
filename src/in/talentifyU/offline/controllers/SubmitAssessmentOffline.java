package in.talentifyU.offline.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.QuestionDAO;
import com.viksitpro.core.dao.entities.StudentAssessmentDAO;
import com.viksitpro.core.utilities.DBUTILS;

import in.talentifyU.offline.pojo.CMSAssessmentResult;

/**
 * Servlet implementation class SubmitAssessmentOffline
 */
@WebServlet("/submit_assessment_offline")
public class SubmitAssessmentOffline extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmitAssessmentOffline() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//printParams(request);
		System.out.println("called in get");
		
		
	}

	private boolean hasAlreadyGivenAssessment(int student_id, int assessment_id) {
		boolean res = false;
		IstarUserDAO dao = new IstarUserDAO();
		Session session = dao.getSession();
		SQLQuery query = session.createSQLQuery(
				"select * from report where user_id=" + student_id + " AND  assessment_id=" + assessment_id + ";");
		// System.out.println(query);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> results = query.list();
		// System.out.println("query size "+ results.size());
		if (results.size() > 0) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}
	
	private void submitResult(CMSAssessmentResult result) {
		
		
		
		DBUTILS util = new DBUTILS();

		
		int user_id = result.getUserId();
		int assessment_id = result.getAssessmentId();
		int total_time = result.getTimetaken();
		total_time=total_time/60000;
		
		String deleteOldData="delete from student_assessment where assessment_id="+assessment_id+" and student_id="+user_id+" ;"
				+ " delete from report where user_id="+user_id+" and assessment_id="+assessment_id+"; ";
    System.out.println("executing delteOldData>>>>"+deleteOldData);
		util.executeUpdate(deleteOldData);
		
		int score = 0;
		HashMap<Integer, String> questionMAp = result.getQuestionOtionMap();
		HashMap<Integer, Integer> queTimeMap = result.getQuestiontimeMap();
		
		HashMap<Integer, String>queCorrectOptionMap = new HashMap<>(); 
		HashMap<Integer, Integer> optionOrderMap = new HashMap<>();
		
		String sql = "select question_id,  id,  marking_scheme from assessment_option where question_id in (select assessment_question.questionid from assessment_question where assessmentid ="+assessment_id+") order by question_id, id";
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		int current_que=0;
		String current_que_correct_ans="";
		int option_index=0;
		for(HashMap<String, Object> row: data)
		{
			int question_id = (int) row.get("question_id");
			int option_id = (int) row.get("id");
			int marking_scheme = (int) row.get("marking_scheme");
			if(question_id==current_que)
			{
				//still making hashmap for same question and its options
				option_index++;
				if(marking_scheme==1)
				{
					current_que_correct_ans+=","+option_id;
				}
				optionOrderMap.put(option_id, option_index);
			}
			else
			{
				//update old question correct answer				
				if(current_que!=0)
				{
					if(current_que_correct_ans.startsWith(","))
					{
						current_que_correct_ans= current_que_correct_ans.substring(1, current_que_correct_ans.length());
					}
					queCorrectOptionMap.put(current_que, current_que_correct_ans);
				}
				//start with new question
				current_que = question_id;
				current_que_correct_ans="";
				option_index=1;
				if(marking_scheme==1)
				{
					current_que_correct_ans=option_id+"";
				}
				optionOrderMap.put(option_id, option_index);
				
			}			
		}
		
		//to update last question data
		if(current_que!=0)
		{
			if(current_que_correct_ans.startsWith(","))
			{
				current_que_correct_ans= current_que_correct_ans.substring(1, current_que_correct_ans.length());
			}
			queCorrectOptionMap.put(current_que, current_que_correct_ans);
		}
		
		
		/*for(int i : optionOrderMap.keySet())
		{
			System.out.println("option id "+i+"   ----- order "+ optionOrderMap.get(i));
		}
		
		for(int i : queCorrectOptionMap.keySet())
		{
			System.out.println("correct option of que "+i+" is "+queCorrectOptionMap.get(i));
		}*/
		
		boolean already_given_assessment = hasAlreadyGivenAssessment(user_id, assessment_id);
		if(already_given_assessment)
		{
			//update assessment
			
			//start updating student_assessment
			for(int question_id : questionMAp.keySet())
			{
				
				if(queCorrectOptionMap.containsKey(question_id))
				{
					//System.out.println("submitting question "+question_id);
					String selected_option = questionMAp.get(question_id);
					String correct_option = queCorrectOptionMap.get(question_id);
					boolean isCorrect = false; 
					//assume no option is selected by user
					HashMap<Integer, Boolean> optionselected = new HashMap<>();
					optionselected.put(1, false);
					optionselected.put(2, false);
					optionselected.put(3, false);
					optionselected.put(4, false);
					optionselected.put(5, false);
					
					
					if(selected_option.equalsIgnoreCase("-1"))
					{
						isCorrect=false;
					}
					if(selected_option.equalsIgnoreCase(correct_option))
					{
						isCorrect= true;
						score++;
					}
					
					
					
					//set true to all the selecetd options				
					for(String str: selected_option.split(","))
					{
						int option_id = Integer.parseInt(str);	
						System.out.println("1111-----"+option_id);
						if(optionOrderMap.get(option_id) != null){						
							int order_id  = optionOrderMap.get(option_id);
							System.out.println("selected option is "+option_id);
							optionselected.put(order_id, true);					
						}
					}
					
					int time_taken_to_answer = queTimeMap.get(question_id);
					
					String student_assessment = "update student_assessment SET  ";
					student_assessment = student_assessment + " correct ='" + String.valueOf((isCorrect + "").charAt(0))
							+ "', ";
					student_assessment = student_assessment + " option1='"
							+ String.valueOf((optionselected.get(1) + "").charAt(0)) + "', ";
					student_assessment = student_assessment + " option2='"
							+ String.valueOf((optionselected.get(2) + "").charAt(0)) + "', ";
					student_assessment = student_assessment + " option3='"
							+ String.valueOf((optionselected.get(3) + "").charAt(0)) + "', ";
					student_assessment = student_assessment + " option4='"
							+ String.valueOf((optionselected.get(4) + "").charAt(0)) + "', ";
					student_assessment = student_assessment + " option5='"
							+ String.valueOf((optionselected.get(5) + "").charAt(0)) + "', ";
					student_assessment = student_assessment+" time_taken="+time_taken_to_answer;	
					student_assessment = student_assessment + " where student_id=" + user_id + " AND assessment_id="
							+ assessment_id + " AND question_id=" + question_id + ";";	
					
					//System.out.println("student_assessment=="+student_assessment);
					util.executeUpdate(student_assessment);
					
					
				}
				
				
			}
			
			//update report table
			Calendar cal1 = Calendar.getInstance();
			Date dt = cal1.getTime();
			String reeport_query = "update report set progress=0, score= " + score + ",  created_at='"+dt+"', time_taken = "+total_time+" where user_id=" + user_id
					+ " AND assessment_id=" + assessment_id + ";";
			//System.out.println("final report query"+reeport_query);
			util.executeUpdate(reeport_query);	
		}
		else
		{
			//insert new assessment
			
			for(int question_id : questionMAp.keySet())
			{
				//System.out.println("submitting question "+question_id);
				
				if(queCorrectOptionMap.containsKey(question_id))
				{
					String selected_option = questionMAp.get(question_id);
					String correct_option = queCorrectOptionMap.get(question_id);
					System.out.println("selected_option"+selected_option);
					System.out.println("correct_option"+correct_option);


					

					boolean isCorrect = false; 
					//assume no option is selected by user
					HashMap<Integer, Boolean> optionselected = new HashMap<>();
					optionselected.put(1, false);
					optionselected.put(2, false);
					optionselected.put(3, false);
					optionselected.put(4, false);
					optionselected.put(5, false);
					
					if(selected_option.equalsIgnoreCase("-1"))
					{
						isCorrect=false;
					}
					else
					{
						if(selected_option.split(",").length == correct_option.split(",").length)
						{
							String 	correct_option_ids[] = correct_option.split(",");
							for(String str: correct_option_ids)
							{
								if(selected_option.contains(str))
								{
									isCorrect=true;
								}
								else
								{
									isCorrect=false;
									break;
								}	
							}
						}										

					
					
					//set true to all the selecetd options
					System.out.println("selected options---"+selected_option);
					for(String str: selected_option.split(","))
					{
						int option_id = Integer.parseInt(str);	
						System.out.println("checking -----"+option_id);
						if(optionOrderMap.get(option_id) != null){						
							int order_id  = optionOrderMap.get(option_id);
							//System.out.println("selected option marked true is  "+option_id);
							optionselected.put(order_id, true);					
						}
					}
					
					}
					
					for(int key : optionselected.keySet())
					{
						//System.out.println("key >>>>>>"+key+", value "+optionselected.get(key));
					}
					
					int time_taken_to_answer = queTimeMap.get(question_id);
					
					String student_assessment = "insert into student_assessment (student_id , assessment_id, question_id, correct, option1, option2, option3, option4, option5, time_taken) VALUES ("
							+ user_id + ", " + assessment_id + ", ";
					student_assessment = student_assessment + question_id + ", ";
					student_assessment = student_assessment + "'" + String.valueOf((isCorrect + "").charAt(0)) + "', ";
					student_assessment = student_assessment + "'" + String.valueOf((optionselected.get(1) + "").charAt(0))
							+ "', ";
					student_assessment = student_assessment + "'" + String.valueOf((optionselected.get(2) + "").charAt(0))
							+ "', ";
					student_assessment = student_assessment + "'" + String.valueOf((optionselected.get(3) + "").charAt(0))
							+ "', ";
					student_assessment = student_assessment + "'" + String.valueOf((optionselected.get(4) + "").charAt(0))
							+ "', ";
					student_assessment = student_assessment + "'" + String.valueOf((optionselected.get(5) + "").charAt(0))
							+ "', ";
					student_assessment = student_assessment+ time_taken_to_answer;
					student_assessment = student_assessment + ");";									

					//System.out.println("student_assessment=="+student_assessment);
					util.executeUpdate(student_assessment);
					
				
				
	
				}
				
			}
		
			//insert into report table
			Calendar cal1 = Calendar.getInstance();
			Date dt = cal1.getTime();
			String reeport_query = "insert into report (progress, score, user_id, assessment_id, created_at, time_taken) VALUES (0, " + score
					+ "," + user_id + ", " + assessment_id + ",'"+dt+"', "+total_time+");";
			//System.out.println("final query=="+reeport_query);
			util.executeUpdate(reeport_query);
			
			updateStatistics(user_id, assessment_id);
			new StudentAssessmentDAO().findAll();
	    new QuestionDAO().findAll();
				
		}
		
	}

	private void updateStatistics(int student_id, int assessment_id) {
		AssessmentV2Services serv = new AssessmentV2Services();
		serv.updateReport(student_id, assessment_id);
		serv.reportdataNew(student_id, assessment_id);
		serv.updateLOBAggregate(student_id, assessment_id);
		serv.updateSkillPErcentile(student_id, assessment_id);		
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("got post request");
		String response_code="200";
		StringBuffer XMLresult = new StringBuffer(request.getParameter("result").replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "")); 
		//System.out.println(XMLresult);
		try{
		
		JAXBContext jaxbContext = JAXBContext.newInstance(CMSAssessmentResult.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();				
		StringReader reader = new StringReader(XMLresult.toString());
		CMSAssessmentResult result = (CMSAssessmentResult) unmarshaller.unmarshal(reader);		
		submitResult(result);		
		
		}
		catch(Exception e)
		{
			response_code="500";
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		out.print(response_code);
	}

}
