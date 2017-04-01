package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.istarindia.assessment.v2.AssessmentV2Services;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.QuestionDAO;
import com.viksitpro.core.dao.entities.StudentAssessmentDAO;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.XMLAssessment;
import in.talentifyU.complexData.pojo.XMLOption;
import in.talentifyU.utils.services.AssessmentRegistry;

/**
 * Servlet implementation class SubmitAssessmentNew
 */
@WebServlet("/submit_assessment_new")
public class SubmitAssessmentNew extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
	XMLAssessment single_assessment;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubmitAssessmentNew() {
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
		// submit_assessment
		int option_id = 0;

		if (request.getParameterMap().containsKey("option_id")) {
			option_id = Integer.parseInt(request.getParameter("option_id"));
		}
		long start_time = Long.parseLong(request.getParameter("start_time"));
		long question_start_time = Long.parseLong(request.getParameter("question_start_time"));
		Calendar c = Calendar.getInstance();
		long current_time = c.getTimeInMillis();
		System.out.println("current time "+current_time);
		int question_id = Integer.parseInt(request.getParameter("question_id"));
		int student_id = Integer.parseInt(request.getParameter("student_id"));
		single_assessment = AssessmentRegistry.getInstance().getAssessment();
		int assessment_id = single_assessment.getAssessmentId();
		int quest_index = Integer.parseInt(request.getParameter("quest_index"));
		int total_questions = single_assessment.getQuestions().size();
		//System.out.println("total ques---- "+total_questions);
		HashMap<Integer, Boolean> option_selected = new HashMap<Integer, Boolean>();
		// ArrayList<XMLOption> hmap =
		// single_assessment.getQuestions().get(question_id).getOptions();
		String find_options = "select id,  text , marking_scheme from assessment_option where question_id = "
				+ question_id + " order by id ";
		// System.out.println("-----------------" +find_options);
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = util.executeQuery(find_options);
		int i = 1;
		for (HashMap<String, Object> row : data) {
			int id = (int) row.get("id");
			if (id == option_id) {
				// System.out.println("selected option is "+id);
				option_selected.put(i, true);
			} else {
				// System.out.println("not selected option is "+id);
				option_selected.put(i, false);
			}
			i++;
		}

		boolean is_correct = getCorrectNess(question_id, option_id);
		// getResult(question_id, option_selected);
		int score = 0;
		if (request.getSession().getAttribute("score") == null) {
			// System.out.println(" no score in session");
			request.getSession().setAttribute("score", score);
		} else {
			// System.out.println(" got score in session");
			score = (int) request.getSession().getAttribute("score");
			// System.out.println("score value "+ score);
			if (is_correct) {
				score++;
				request.getSession().removeAttribute("score");
				request.getSession().setAttribute("score", score);
			}
		}
		boolean already_given_assessment = hasAlreadyGivenAssessment(student_id, assessment_id, question_id);
		// System.out.println("already_given_assessment"+already_given_assessment);
		boolean alredy_answered_question = hasAlreadyAttemtedQuestion(student_id, assessment_id, question_id);
		// System.out.println("alredy_answered_question"+alredy_answered_question);
		
		int to_answer_question = (int)(question_start_time/60000);
		if (!alredy_answered_question) {
			// insert into table
			String student_assessment = "insert into student_assessment (student_id , assessment_id, question_id, correct, option1, option2, option3, option4, option5, time_taken) VALUES ("
					+ student_id + ", " + assessment_id + ", ";
			student_assessment = student_assessment + question_id + ", ";
			student_assessment = student_assessment + "'" + String.valueOf((is_correct + "").charAt(0)) + "', ";
			student_assessment = student_assessment + "'" + String.valueOf((option_selected.get(1) + "").charAt(0))
					+ "', ";
			student_assessment = student_assessment + "'" + String.valueOf((option_selected.get(2) + "").charAt(0))
					+ "', ";
			student_assessment = student_assessment + "'" + String.valueOf((option_selected.get(3) + "").charAt(0))
					+ "', ";
			student_assessment = student_assessment + "'" + String.valueOf((option_selected.get(4) + "").charAt(0))
					+ "', ";
			student_assessment = student_assessment + "'" + String.valueOf((option_selected.get(4) + "").charAt(0))
					+ "', ";
			student_assessment = student_assessment+ to_answer_question;
			student_assessment = student_assessment + ");";
			System.out.println(student_assessment);
			IstarUserDAO dao = new IstarUserDAO();
			Session session = dao.getSession();
			SQLQuery query = session.createSQLQuery(student_assessment);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			int result = query.executeUpdate();
			com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "result ===" + student_assessment);
			session.beginTransaction().commit();
		} else {
			// update into table
			String student_assessment = "update student_assessment SET  ";
			student_assessment = student_assessment + " correct ='" + String.valueOf((is_correct + "").charAt(0))
					+ "', ";
			student_assessment = student_assessment + " option1='"
					+ String.valueOf((option_selected.get(1) + "").charAt(0)) + "', ";
			student_assessment = student_assessment + " option2='"
					+ String.valueOf((option_selected.get(2) + "").charAt(0)) + "', ";
			student_assessment = student_assessment + " option3='"
					+ String.valueOf((option_selected.get(3) + "").charAt(0)) + "', ";
			student_assessment = student_assessment + " option4='"
					+ String.valueOf((option_selected.get(4) + "").charAt(0)) + "', ";
			student_assessment = student_assessment+" time_taken="+to_answer_question;	
			student_assessment = student_assessment + " where student_id=" + student_id + " AND assessment_id="
					+ assessment_id + " AND question_id=" + question_id + ";";
			System.out.println(student_assessment);
			IstarUserDAO dao = new IstarUserDAO();
			Session session = dao.getSession();
			SQLQuery query = session.createSQLQuery(student_assessment);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			int result = query.executeUpdate();
			com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "result ===" + student_assessment);
			session.beginTransaction().commit();

		}

		 //System.out.println("report " + already_given_assessment);
		int to_ans_assessment = (int)((current_time-start_time)/60000); 
		Calendar cal1 = Calendar.getInstance();
		Date dt = cal1.getTime();
		if (!already_given_assessment) {
			// insert into report
			String reeport_query = "insert into report (progress, score, user_id, assessment_id, created_at, time_taken) VALUES (0, " + score
					+ "," + student_id + ", " + assessment_id + ",'"+dt+"', "+to_ans_assessment+");";
			// com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(),
			// reeport_query);
			// System.out.println(reeport_query);
			IstarUserDAO dao1 = new IstarUserDAO();
			Session session1 = dao1.getSession();
			SQLQuery query1 = session1.createSQLQuery(reeport_query);
			query1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			int result1 = query1.executeUpdate();
			session1.beginTransaction().commit();
		} else {
			/// update report

			String reeport_query = "update report set progress=0, score= " + score + ",  created_at='"+dt+"', time_taken = "+to_ans_assessment+" where user_id=" + student_id
					+ " AND assessment_id=" + assessment_id + ";";
			// System.out.println(reeport_query);
			IstarUserDAO dao = new IstarUserDAO();
			Session session = dao.getSession();
			SQLQuery query = session.createSQLQuery(reeport_query);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			int result = query.executeUpdate();
			session.beginTransaction().commit();
		}
		
		if(quest_index == total_questions-1)
		{
			///updatePercentileForStudents(student_id,assessment_id);
			updateStatistics(student_id, assessment_id);
			 Thread thread = new Thread(){
				    public void run(){
				    	System.out.println("thread started");
				      new StudentAssessmentDAO().findAll();
				      new QuestionDAO().findAll();
				      System.out.println("thread completed");
				    }
				  };

				  thread.start();
		}

	}

	/*private void updatePercentileForStudents(int student_id, int assessment_id) {
		DBUTILS util = new DBUTILS();
		String getskillToUpdate="WITH RECURSIVE supplytree AS (SELECT id, skill_title, parent_skill FROM skill WHERE id in (SELECT DISTINCT 				skill_learning_obj_mapping.skill_id 			FROM 				skill_learning_obj_mapping, 				lesson, 				learning_objective_lesson, 				assessment 			WHERE 				skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 			AND learning_objective_lesson.lessonid = lesson. ID 			AND lesson. ID = assessment.lesson_id 			AND assessment.id = "+assessment_id+") UNION ALL SELECT si.id,si.skill_title, 	si.parent_skill 	 FROM skill As si 	INNER JOIN supplytree AS sp 	ON (si.id = sp.parent_skill) ) SELECT distinct id FROM supplytree ";
		System.out.println("skills to update"+getskillToUpdate);
		List<HashMap<String, Object>> data = util.executeQuery(getskillToUpdate);
		System.out.println("skill size is "+data.size());
		
		for(HashMap<String, Object> row : data)
		{
			
			int skill_id = (int)row.get("id");
			System.out.println("updating skill"+skill_id);
			updatePerSkill(student_id,skill_id);
		}
		
	}*/

	private void updateStatistics(int student_id, int assessment_id) {
		AssessmentV2Services serv = new AssessmentV2Services();
		serv.updateReport(student_id, assessment_id);
		serv.reportdataNew(student_id, assessment_id);
		serv.updateLOBAggregate(student_id, assessment_id);
		serv.updateSkillPErcentile(student_id, assessment_id);		
	}

	
	
	public String queryForChildSkill(int parent_skill_id)
	{
		String sql = "select "+parent_skill_id+" as id union (WITH RECURSIVE supplytree AS ( 	SELECT 		ID, 		skill_title, 		parent_skill 	FROM 		skill 	WHERE 		parent_skill = "+parent_skill_id+" 	UNION ALL 		SELECT 			si. ID, 			si.skill_title, 			si.parent_skill 		FROM 			skill AS si 		INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) ) SELECT 	ID FROM 	supplytree ) ";
		return sql;
	}
	
	public static int getRandomInteger(int maximum, int minimum){ return ((int) (Math.random()*(maximum - minimum))) + minimum; }

	/*private void updatePerSkill(int user_id, int skill_id) {
		System.out.println("skill"+skill_id+" stude "+user_id);
		String percentile_batch = "0";
	 	String percentile_organization = "0";
	 	String percentile_country = "0";
	 	String percentile_globe ="0";
		DBUTILS util = new DBUTILS();
		String batch_percentile_query = "select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE report.user_id IN ( SELECT DISTINCT 		BC.student_id 	FROM 		batch_students BC 	WHERE 		BC.batch_group_id IN ( 			SELECT 				B.batch_group_id 			FROM 				batch_students B 			WHERE 				B.student_id = "+user_id+"  		)  order by 	BC.student_id ) AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id = "+user_id+"  ";
		List<HashMap<String , Object>> data_batch_percentile = util.executeQuery(batch_percentile_query);
		for(HashMap<String , Object> row : data_batch_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_batch= (int)row.get("batch_percentile")+"";
			}
		}
		String org_percentile_query ="select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE report.user_id IN ( SELECT DISTINCT 		BC.id 	FROM 		student BC 	WHERE 		BC.organization_id IN ( 			SELECT 				B.organization_id 			FROM 				student B 			WHERE 				B.id = "+user_id+"  		)  order by 	BC.id ) AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id = "+user_id;
		List<HashMap<String , Object>> data_org_percentile = util.executeQuery(org_percentile_query);
		for(HashMap<String , Object> row : data_org_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_organization= (int)row.get("batch_percentile")+"";
			}
		}
		String country_percentile_query ="select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE  report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id ="+user_id;
		List<HashMap<String , Object>> data_country_percentile = util.executeQuery(country_percentile_query);
		for(HashMap<String , Object> row : data_country_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_country= (int)row.get("batch_percentile")+"";
				percentile_globe = percentile_country;
			}
		}
		Calendar cal = Calendar.getInstance();
		Date dt = cal.getTime();
		String insert_into_skill_percentile  ="INSERT INTO skill_precentile (student_id, skill_id, timestamp, percentile_country, percentile_globe, percentile_batch, percentile_organization, accuracy, industry_benchmark) "
				+ "VALUES ("+user_id+", '"+skill_id+"', '"+dt+"', '"+Integer.parseInt(percentile_country)+"', '"+Integer.parseInt(percentile_globe)+"', '"+Integer.parseInt(percentile_batch)+"', '"+Integer.parseInt(percentile_organization)+"', '"+0+"', '"+getRandomInteger(0, 20)+"');";
		util.executeUpdate(insert_into_skill_percentile);

		
	}*/

	private boolean hasAlreadyGivenAssessment(int student_id, int assessment_id, int question_id) {
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

	private boolean hasAlreadyAttemtedQuestion(int student_id, int assessment_id, int question_id) {
		boolean res = false;
		IstarUserDAO dao = new IstarUserDAO();
		Session session = dao.getSession();
		SQLQuery query = session.createSQLQuery("select * from student_assessment where student_id=" + student_id
				+ " AND  question_id= " + question_id + "  and assessment_id=" + assessment_id + ";");
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

	private boolean getCorrectNess(int question_id, int option_id) {
		boolean res = false;
		// System.out.println("option _id = "+option_id+" and question id=
		// "+question_id);
		String sql = "select * from assessment_option where id = " + option_id + " and question_id = " + question_id
				+ " and marking_scheme=1";
		// System.out.println(sql);
		DBUTILS util = new DBUTILS();
		if (util.executeQuery(sql).size() > 0) {
			res = true;
		}
		return res;
	}

	private Boolean getResult(int question_id, HashMap<Integer, Boolean> option_selected) {
		com.istarindia.apps.dao.UUIUtils.printlog("qqqqqqqq" + question_id);
		String correct_ans = "";
		for (XMLOption op : single_assessment.getQuestions().get(question_id).getOptions()) {
			if (op.isCorrect()) {
				correct_ans = correct_ans + ";" + op.getOptionText();
			}
		}
		// int start_option_id =
		// single_assessment.getQuestions().get(question_id).getOptions().get(0).getOptionId();
		int start_option_id = 0;

		if (!correct_ans.contains(";")) {

			int count = 0;
			for (int i : option_selected.keySet()) {

				try {

					String option_val = single_assessment.getQuestions().get(question_id).getOptions()
							.get(start_option_id + i - 1).getOptionText();
					if (option_selected.get(i)) {
						if (correct_ans.equalsIgnoreCase(option_val)) {
							count++;
							if (count > 1) {
								return false;
							}
						} else {
							return false;
						}
					}
				} catch (Exception e) {

				}
			}
			if (count == 1) {
				return true;
			}
		} else {
			// com.istarindia.apps.dao.UUIUtils.printlog("question value
			// -----------------"+correct_ans);
			// com.istarindia.apps.dao.UUIUtils.printlog("option size
			// -----------------"+option_selected.size());
			int count_should_be = correct_ans.split(";").length;
			// com.istarindia.apps.dao.UUIUtils.printlog("option size
			// -----------------"+option_selected.size());

			int count = 0;
			for (int i : option_selected.keySet()) {
				try {
					String option_val = single_assessment.getQuestions().get(question_id).getOptions()
							.get(start_option_id + i - 1).getOptionText();
					if (option_selected.get(i)) {
						// com.istarindia.apps.dao.UUIUtils.printlog("option
						// value -----------------"+option_val);
						if (correct_ans.contains(option_val)) {
							count++;
						} else {
							return false;
						}
					}
				} catch (Exception e) {
					com.istarindia.apps.dao.UUIUtils.printlog("Bad Question -> " + question_id);
				}
			}
			if (count_should_be == count) {
				return true;
			}
		}
		return false;
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
