/**
 * 
 */
package in.talentifyU.utils.services;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.istarindia.apps.dao.LearningObjective;
import com.istarindia.apps.dao.Skill;
import com.istarindia.apps.dao.SkillDAO;
import com.istarindia.apps.dao.SkillLearningObjMapping;
import com.istarindia.apps.dao.Student;
import com.istarindia.apps.dao.StudentDAO;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.AssessmentOption;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.Cmsession;
import com.viksitpro.core.dao.entities.Course;
import com.viksitpro.core.dao.entities.CourseDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.dao.entities.StudentAssessment;
import com.viksitpro.core.utilities.DBUTILS;

/**
 * @author ComplexObject
 *
 */
public class NewReportUtils {

	
	public HashMap <String, String> getRepoDetailForAssessment(int user_id, Assessment assessment)
	{
		DBUTILS util =new DBUTILS();
		String accuracy="20%";
		String time_taken ="15 m";
		String percentile ="45%";
		String created_at="";
		int id = assessment.getId();
		String assessment_title= assessment.getLesson().getTitle();
		String wizard_image="wizard_not_available";		
		
		int total_que = assessment.getAssessmentQuestions().size();
		
		HashMap <String, String> data = new HashMap<>();
		
		String det="select score, time_taken, created_at from report where user_id="+user_id+" and assessment_id= "+assessment.getId();
		List<HashMap<String, Object>> res = util.executeQuery(det);
		for(HashMap<String, Object> row: res)
		{
			int score =(int) row.get("score");
			if(total_que!=0)
			{
				accuracy = (score*100/total_que)+"%";
			}
			Timestamp tmp = (Timestamp) row.get("created_at");
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			created_at = format.format(tmp).toString();
			time_taken= (int)row.get("time_taken")+" m";
			
		}
		
		String getPercentile ="select * from ( SELECT 	user_id, 	cast((cume_dist() OVER (ORDER BY score)*100) as integer)  as percentile FROM 	report WHERE 	assessment_id = "+id+" AND user_id IN ( 	SELECT 		bs.student_id 	FROM 		batch_students bs 	WHERE 		bs.batch_group_id IN ( 			SELECT 				bc.batch_group_id 			FROM 				batch_students bc 			WHERE 				bc.student_id = "+user_id+" 		) ) order by score desc   )TFinal where TFinal.user_id="+user_id;
		List<HashMap<String, Object>> percdata = util.executeQuery(getPercentile);
		if(percdata.size()>0)
		{
			percentile = (int)percdata.get(0).get("percentile")+"%";
			if((int)percdata.get(0).get("percentile")<=30 )
			{
				wizard_image ="rookie_half";
			}
			else if((int)percdata.get(0).get("percentile")>30 && (int)percdata.get(0).get("percentile") <=60)
			{
				wizard_image ="apprentice_half";
			}if((int)percdata.get(0).get("percentile")>60 && (int)percdata.get(0).get("percentile") <=85)
			{
				wizard_image="master_half";
			}
			else if((int)percdata.get(0).get("percentile")>85)
			{
				wizard_image="wizard_half";
			}
		}
		
			
		data.put("accuracy", accuracy);
		data.put("time_taken", time_taken);
		data.put("created_at", created_at);
		data.put("wizard_image", wizard_image);
		data.put("assessment_title", assessment_title);
		data.put("percentile", percentile);
		
		return data;
	}
	
	
	public boolean hasQuestionData(int skill_id, int user_id)
	{
		boolean result = false;
		HashMap<String, Object> data =  getQuestionWithDetailsPerSkill(0, skill_id,  user_id);
		HashMap<Integer, HashMap<String, String>> que_deatils = (HashMap<Integer, HashMap<String, String>>)data.get("question_details");
		if(que_deatils!= null && que_deatils.size()>0)
		{
			result=true;
		}
		return result;
	}
	
	public boolean hasData(int skill_id, int user_id)
	{
		DBUTILS util = new DBUTILS();
		boolean res = false;
		String sql = "select * from skill_precentile where skill_id="+skill_id+" and student_id="+user_id;
		if(util.executeQuery(sql).size()>0)
		{
			res = true;
		}
		return res;
	}
	
	
	public StringBuffer getGraph(int skill_id)
	{
		StringBuffer sb = new StringBuffer();
		
		
		
		return sb;
	}
	
	
	public boolean isChildSkill(int skill_id)
	{
		boolean res = false;
		DBUTILS util = new DBUTILS();
		List<Skill> skills = new SkillDAO().findAll();
		for(Skill skill : skills)
		{
			
			ArrayList<Skill> modules = getChildSkills(skill);
			for(Skill sk : modules)
			{
				ArrayList<Skill> sessions = getChildSkills(sk);
				for(Skill ss: sessions)
				{
					if(ss.getId()==skill_id)
					{
						res= true;
					}
				}
			}
		}
		return res;
	}
	
	
	
	/*public HashMap<Integer, HashMap<String, String>> getQuestionDeatilsPerSkill(int parent_skill_id,int skill_id, int user_id)
	{
		HashMap<Integer, HashMap<String, String>> que_deatils = new HashMap<Integer, HashMap<String, String>>();
		DBUTILS util = new DBUTILS();
		String sql ="SELECT DISTINCT 	Q. ID, 	Q.duration_in_sec, 	Q.question_text, 	Q.question_type, 	Q.difficulty_level, 	SA.correct is_correct, 	( 		CASE SA.option1 		WHEN 't' THEN 			( 				( 					SELECT 						AO. TEXT || ', ' AS TEXT 					FROM 						assessment_option AO 					WHERE 						AO.question_id = SA.question_id 					ORDER BY 						AO. ID 					LIMIT 1 OFFSET 0 				) 			) 		ELSE 			'' 		END 	) || ( 		CASE SA.option2 		WHEN 't' THEN 			( 				( 					SELECT 						AO. TEXT || ', ' AS TEXT 					FROM 						assessment_option AO 					WHERE 						AO.question_id = SA.question_id 					ORDER BY 						AO. ID 					LIMIT 1 OFFSET 1 				) 			) 		ELSE 			'' 		END 	) || ( 		CASE SA.option3 		WHEN 't' THEN 			( 				( 					SELECT 						AO. TEXT || ', ' AS TEXT 					FROM 						assessment_option AO 					WHERE 						AO.question_id = SA.question_id 					ORDER BY 						AO. ID 					LIMIT 1 OFFSET 2 				) 			) 		ELSE 			'' 		END 	) || ( 		CASE SA.option4 		WHEN 't' THEN 			( 				( 					SELECT 						AO. TEXT || ', ' AS TEXT 					FROM 						assessment_option AO 					WHERE 						AO.question_id = SA.question_id 					ORDER BY 						AO. ID 					LIMIT 1 OFFSET 3 				) 			) 		ELSE 			'' 		END 	) || ( 		CASE SA.option5 		WHEN 't' THEN 			( 				( 					SELECT 						AO. TEXT AS TEXT 					FROM 						assessment_option AO 					WHERE 						AO.question_id = SA.question_id 					ORDER BY 						AO. ID 					LIMIT 1 OFFSET 4 				) 			) 		ELSE 			'' 		END 	) AS selected_options, 	( 		SELECT 			string_agg (AOO. TEXT, ', ') 		FROM 			assessment_option AOO 		WHERE 			AOO.marking_scheme = 1 		AND AOO.question_id = SA.question_id 	) AS correct_options FROM 	skill_learning_obj_mapping SLOM, 	learning_objective_question LOQ, 	question Q, 	student_assessment SA WHERE 	SLOM.learning_objective_id = LOQ.learning_objectiveid AND LOQ.questionid = Q. ID AND SLOM.skill_id = "+skill_id+" AND SA.student_id = "+user_id+" AND SA.question_id = Q. ID ";
		List<HashMap<String, Object>> data = util.executeQuery(sql); 
		
		for(HashMap<String, Object> row: data)
		{
			HashMap<String, String> que_data = new HashMap<String, String>();
			String question_text = (String) row.get("question_text");
			int que_id = (int)row.get("id");
			String selected_ans = "SKIPPED";
            if(row.get("selected_options")!=null)
            {
            	selected_ans = (String)row.get("selected_options");
            }	
            String correct_ans = "NOT_AVAILABLE";
            if(row.get("correct_options")!=null)
            {
            	correct_ans = (String)row.get("correct_options");
            }
        
            boolean is_corect = (boolean) row.get("is_correct");            
	        String difficulty_level = "Easy";
	        HashMap<Integer, String> diif_level= new HashMap<>();
	        diif_level.put(1, "Easy");
	        diif_level.put(2, "Medium");
	        diif_level.put(3, "Hard");
	        if(row.get("difficulty_level") != null )
	        {
	        	difficulty_level =  diif_level.get((int)row.get("difficulty_level"));
	        }
	        String time_taken = "5 secs";
	        String percentile = "85%";	     	
	     	que_data.put("question_text", question_text);
	     	que_data.put("selected_ans", selected_ans);
	     	que_data.put("difficulty_level", difficulty_level);
	     	que_data.put("time_taken", time_taken);
	     	que_data.put("percentile", percentile);
	     	que_data.put("correct_ans", correct_ans);
	     	que_data.put("is_corect", is_corect+"");  	     	
	     	que_deatils.put(que_id, que_data);
		}
		
		return que_deatils;
	}*/
	
	/*
	 * Will give two data
	 * a) HashMap<Integer, ArrayList<String>> que_lobj (key is question id and value is Arraylist of Learning Objective)
	 * b) HashMap<Integer, HashMap<String, String>> que_deatils (key is question id and value is hashmap of question details like
	 *    question text, options, coreect or not )
	 * */
	public HashMap<String, Object> getQuestionWithDetailsPerSkill(int parent_skill_id,int skill_id, int user_id)
	{
		HashMap<String, Object> data = new HashMap<>();
		try {
			
			HashMap<Integer, HashSet<LearningObjective>> que_lobj = new HashMap<Integer, HashSet<LearningObjective>>();
			HashMap<Integer, HashMap<String, String>> que_deatils = new HashMap<Integer, HashMap<String, String>>();
			
			Student student = new StudentDAO().findById(user_id);		
			Skill skill  = new SkillDAO().findById(skill_id);
			Set<SkillLearningObjMapping> skill_lobjs =  skill.getSkillLearningObjMappings();
			//System.out.println("skill lobj mapping----"+skill_lobjs.size());
			for(SkillLearningObjMapping lob_map : skill_lobjs)
			{
				
				LearningObjective obj = lob_map.getLearningObjective();
			//	System.out.println("lobj id-----"+obj.getId());
				Collection<Question> questions = obj.getQuestionCollection();
				//System.out.println("questions size-----"+questions.size());
				for(Question que : questions)
				{
					//System.out.println("que id-----"+que.getId());
					String question_text = que.getQuestionText();				
			        HashMap<Integer, String> diif_level= new HashMap<>();
			        diif_level.put(0, "Easy");
			        diif_level.put(1, "Medium");
			        diif_level.put(2, "Hard");
			        String difficulty_level = diif_level.get(que.getDifficultyLevel());
			        String correct_ans = "";
			        String explanation="N/A";
			        if(que.getExplanation()!=null)
			        {
			        	explanation= que.getExplanation();
			        }
			        HashMap<Integer, String> options = new HashMap<>();
			        int i=1;
			        for(AssessmentOption op : que.getAssessmentOptions())
			        {
			        	options.put(i, op.getText());
			        	if(op.getMarkingScheme()!= null && op.getMarkingScheme()==1)
			        	{
			        		correct_ans += op.getText()+",";
			        	}
			        	i++;
			        }
			        
			        if(correct_ans.equalsIgnoreCase(""))
			        {
			        	correct_ans="NOT_AVAILABLE";
			        }		        
			        
			        String selected_ans = "";
			        String is_corect =  "Incorrect";
			        String time_taken ="N/A";
			        //System.out.println("in student Assessment-----"+student.getStudentAssessments().size());
			      
			        for(StudentAssessment stuAss: student.getStudentAssessments())
			        {
			        	if(stuAss.getQuestion().getId() ==  que.getId())
			        	{
			        		//System.out.println("in is correct-----"+stuAss.getCorrect());
				        	//System.out.println("in student Assessment id -----"+stuAss.getId());
				        	
				        	if(stuAss.getCorrect())
				        	{
				        		is_corect= "Correct";	
				        	}
				        	
				        	
				        		if(stuAss.getOption1())
				        		{
				        			//System.out.println("option 1 choosen");
				        			selected_ans += options.get(1)+",";
				        		}
				        		if(stuAss.getOption2())
				        		{
				        			//System.out.println("option 2 choosen");
				        			selected_ans += options.get(2)+",";
				        		}
				        		if(stuAss.getOption3())
				        		{
				        			//System.out.println("option 3 choosen");
				        			selected_ans += options.get(3)+",";
				        		}
				        		if(stuAss.getOption4())
				        		{
				        			//System.out.println("option 4 choosen");
				        			selected_ans += options.get(4)+",";
				        		}
				        		
				        		if(stuAss.getTime_taken()<=0)
				        		{
				        			time_taken="50 sec";
				        		}
				        		else
				        		{
				        			time_taken=stuAss.getTime_taken()+" min";
				        		}
				        	
			        	}
			        }
			        
			        if(selected_ans.equals(""))
			        {
			        	selected_ans="SKIPPED";
			        }
			        
			       
			        String percentile ="N/A";
			        String prof_level="Rookie";
			        DBUTILS u = new DBUTILS();
			        String sql1 =" SELECT 	COUNT (*) as tot, 	( 		SELECT 			count(bs.student_id) AS stud 		FROM 			batch_students bs 		WHERE 			bs.batch_group_id IN ( 				SELECT 					batch_group_id 				FROM 					batch_students 				WHERE 					student_id = "+user_id+" 			) 	) AS ttt FROM 	student_assessment WHERE 	correct = 't' AND question_id = "+que.getId()+" AND student_id IN ( 	SELECT 		bs.student_id AS stud 	FROM 		batch_students bs 	WHERE 		bs.batch_group_id IN ( 			SELECT 				batch_group_id 			FROM 				batch_students 			WHERE 				student_id = "+user_id+" 		) )";
			        List<HashMap<String, Object>> qbp = u.executeQuery(sql1);
			        int b_perc = 0;
			        if(qbp.size()>0)
			        {
			        	for(HashMap<String, Object> row: qbp)
			        	{
				        	BigInteger cor_stu = (BigInteger) row.get("tot");
				        	BigInteger  tot_stu= (BigInteger) row.get("ttt");
				        	if(tot_stu.intValue()!=0)
				        	{
				        		b_perc= (cor_stu.intValue()*100)/tot_stu.intValue();
				        	}
			        	}
			        }
			        if(b_perc<=30 )
	        		{
	        			prof_level ="Rookie";
	        		}
	        		else if(b_perc>30 && b_perc <=60)
	        		{
	        			prof_level ="Apprentice";
	        		}if(b_perc>60 && b_perc <=85)
	        		{
	        			prof_level="Master";
	        		}
	        		else if(b_perc>85)
	        		{
	        			prof_level="Wizard";
	        		}
	        		if(b_perc!=0)
	        		{
	        		percentile = b_perc+"%";
	        		}
			        
			        HashMap<String, String> que_data = new HashMap<String, String>();
			        que_data.put("question_text", question_text);
			     	que_data.put("selected_ans", selected_ans);
			     	que_data.put("difficulty_level", difficulty_level);
			     	que_data.put("time_taken", time_taken);
			     	que_data.put("percentile", percentile+"");
			     	que_data.put("correct_ans", correct_ans);
			     	que_data.put("is_corect", is_corect+"");  	
			     	que_data.put("explanation", explanation);
			     	que_data.put("prof_level", prof_level);  
			     	if(!time_taken.equalsIgnoreCase("N/A"))
			     	{
			     		que_deatils.put(que.getId(), que_data);
				        /*Got all question details, below code consist of learning objective per question */		        		        
				     	HashSet<LearningObjective> learningObjs = new HashSet<LearningObjective>(que.getLearningObjectives())  ;		        
				        que_lobj.put(que.getId(), learningObjs);
			     	}
			     	
				}						
			}
			//System.out.println("que data-------"+que_deatils.size());
			data.put("question_details", que_deatils);
			data.put("learning_obj_details", que_lobj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
		return data;
	}
	
	
	public ArrayList<Assessment> getAssessmentForCourse(int course_id)
	{
		Course c = new CourseDAO().findById(course_id);
		ArrayList<Assessment> data = new ArrayList<>();
		for(Module m : c.getModules())
		{
			for(Cmsession cms : m.getCmsessions())
			{
				for(Lesson l : cms.getLessons())
				{
					if(l.getTask().getStatus().equalsIgnoreCase("PUBLISHED") && l.getDtype().equalsIgnoreCase("ASSESSMENT") )
					{
						data.add(l.getAssessment());
					}
				}
			}
			
		}
		
		return data;
	}
	
	public ArrayList<Skill> getAllSkillsForAssessment(int assessment_id)
	{
		Assessment assessment = new AssessmentDAO().findById(assessment_id); 
		ArrayList<Skill> data = new ArrayList<>();
		ArrayList<Integer> skillAdded = new ArrayList<>();
		
		String getskillToUpdate="WITH RECURSIVE supplytree AS (SELECT id, skill_title, parent_skill FROM skill WHERE id in (SELECT DISTINCT 				skill_learning_obj_mapping.skill_id 			FROM 				skill_learning_obj_mapping, 				lesson, 				learning_objective_lesson, 				assessment 			WHERE 				skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 			AND learning_objective_lesson.lessonid = lesson. ID 			AND lesson. ID = assessment.lesson_id 			AND assessment.id = "+assessment_id+") UNION ALL SELECT si.id,si.skill_title, 	si.parent_skill 	 FROM skill As si 	INNER JOIN supplytree AS sp 	ON (si.id = sp.parent_skill) ) SELECT distinct id FROM supplytree ";
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> skillss= util.executeQuery(getskillToUpdate);
		for(HashMap<String, Object> row: skillss)
		{
			int skill_id = (int)row.get("id");
			Skill skill = new SkillDAO().findById(skill_id);
			if(!skillAdded.contains(skill_id) && skill.getSkillLearningObjMappings().size()>0)
			{								
				skillAdded.add(skill_id);
				data.add(skill);
			}
			
		}
		/*for(AssessmentQuestion question : assessment.getAssessmentQuestions())
		{			
			for(LearningObjective lobj : question.getQuestion().getLearningObjectives())
			{
				for(SkillLearningObjMapping lobjMap : lobj.getSkillLearningObjMappings())
				{
					Skill skill = lobjMap.getSkill();
					if(!skillAdded.contains(skill.getId()))
					{
						skillAdded.add(skill.getId());
						data.add(skill);
					}
					
				}
			}
		}*/
		return data;
	}
	
	public String queryForChildSkill(int parent_skill_id)
	{
		String sql = "select "+parent_skill_id+" as id union (WITH RECURSIVE supplytree AS ( 	SELECT 		ID, 		skill_title, 		parent_skill 	FROM 		skill 	WHERE 		parent_skill = "+parent_skill_id+" 	UNION ALL 		SELECT 			si. ID, 			si.skill_title, 			si.parent_skill 		FROM 			skill AS si 		INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) ) SELECT 	ID FROM 	supplytree ) ";
		return sql;
	}
	
	
	public HashMap<String, String> getPercentileForSkill(int skill_id, int user_id)
	{
		 HashMap<String, String> dat = new HashMap<>();
		 	String percentile_batch = "N/A";
		 	String percentile_organization = "N/A";
		 	String percentile_country = "N/A";
		 	String percentile_globe ="N/A";
		DBUTILS util = new DBUTILS();
		String batch_percentile_query = "select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE report.user_id IN ( SELECT DISTINCT 		BC.student_id 	FROM 		batch_students BC 	WHERE 		BC.batch_group_id IN ( 			SELECT 				B.batch_group_id 			FROM 				batch_students B 			WHERE 				B.student_id = "+user_id+"  		)  order by 	BC.student_id ) AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id = "+user_id+"  ";
		
		/*batch level*/
		//batch_percentile_query=" 		SELECT 			batch.name AS batch_name, 			( 				CUME_DIST () OVER (ORDER BY SUM(report.score)) * 100 			) AS batch_percentile 		FROM 			report, batch_students, batch 		WHERE 		report.user_id= batch_students.student_id and batch_students.batch_group_id =  batch.batch_group_id and user_type = 'STUDENT' 		AND report.assessment_id IN ( 			SELECT DISTINCT 				assessment. ID 			FROM 				skill_learning_obj_mapping, 				lesson, 				learning_objective_lesson, 				assessment 			WHERE 				skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 			AND learning_objective_lesson.lessonid = lesson. ID 			AND lesson. ID = assessment.lesson_id 			AND skill_learning_obj_mapping.skill_id IN ( 				SELECT 					0 AS ID 				UNION 					( 						WITH RECURSIVE supplytree AS ( 							SELECT 								ID, 								skill_title, 								parent_skill 							FROM 								skill 							WHERE 								parent_skill = 0 							UNION ALL 								SELECT 									si. ID, 									si.skill_title, 									si.parent_skill 								FROM 									skill AS si 								INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 						) SELECT 							ID 						FROM 							supplytree 					) 			) 		) 		GROUP BY 			batch_name";
		/*batch level changes ends here*/
		System.out.println("batch percentile query--"+batch_percentile_query);
		List<HashMap<String , Object>> data_batch_percentile = util.executeQuery(batch_percentile_query);
		for(HashMap<String , Object> row : data_batch_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_batch= (int)row.get("batch_percentile")+"%";
			}
		}
		String org_percentile_query ="select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE report.user_id IN ( SELECT DISTINCT 		BC.id 	FROM 		student BC 	WHERE 		BC.organization_id IN ( 			SELECT 				B.organization_id 			FROM 				student B 			WHERE 				B.id = "+user_id+"  		)  order by 	BC.id ) AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id = "+user_id;
		System.out.println("org_percentile_query--"+org_percentile_query);
		List<HashMap<String , Object>> data_org_percentile = util.executeQuery(org_percentile_query);
		for(HashMap<String , Object> row : data_org_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_organization= (int)row.get("batch_percentile")+"%";
			}
		}
		String country_percentile_query ="select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE  report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id ="+user_id;
		System.out.println("country_percentile_query--"+country_percentile_query);
		List<HashMap<String , Object>> data_country_percentile = util.executeQuery(country_percentile_query);
		for(HashMap<String , Object> row : data_country_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_country= (int)row.get("batch_percentile")+"%";
				percentile_globe = percentile_country;
			}
		}
	//	System.out.println(percentile_batch);
		//System.out.println(percentile_organization);
		//System.out.println(percentile_country);
		//System.out.println(percentile_globe);
		 
			dat.put("percentile_batch", percentile_batch);
			dat.put("percentile_organization", percentile_organization);
			dat.put("percentile_country", percentile_country);
			dat.put("percentile_globe", percentile_globe);
			
		
		return dat;
	
	}
	
	
	
	public static int getRandomInteger(int maximum, int minimum){ return ((int) (Math.random()*(maximum - minimum))) + minimum; }

	public HashMap<String, String> getPercentileForChild(int skill_id, int user_id)
	{
		 HashMap<String, String> dat = new HashMap<>();
		 	String percentile_batch = "N/A";
		 	
		 	String accuracy ="N/A";
		 	String industry_benchmark ="N/A";
		 	
		int user_org_percentile =  0; 
		double org_percentile =  0;
		
		DBUTILS util = new DBUTILS();
		String batch_percentile_query = "select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE report.user_id IN ( SELECT DISTINCT 		BC.student_id 	FROM 		batch_students BC 	WHERE 		BC.batch_group_id IN ( 			SELECT 				B.batch_group_id 			FROM 				batch_students B 			WHERE 				B.student_id = "+user_id+"  		)  order by 	BC.student_id ) AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+queryForChildSkill(skill_id)+") )  group by stud_id ) MT  where MT.stud_id = "+user_id+"  ";
		List<HashMap<String , Object>> data_batch_percentile = util.executeQuery(batch_percentile_query);
		for(HashMap<String , Object> row : data_batch_percentile)
		{
			if(row.get("batch_percentile")!=null)
			{
				percentile_batch= (int)row.get("batch_percentile")+"%";
			}
		}
		String org_percentile_query =" SELECT sum(report.score) as totall, report.user_id FROM 	report WHERE 	report.user_id IN ( 		SELECT DISTINCT 			BC. ID 		FROM 			student BC 		WHERE 			BC.organization_id IN ( 				SELECT 					B.organization_id 				FROM 					student B 				WHERE 					B. ID = "+user_id+" 			) 		ORDER BY 			BC. ID 	)	 AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id IN ( 		WITH RECURSIVE supplytree AS ( 			SELECT 				ID, 				skill_title, 				parent_skill, 				CAST (skill_title AS VARCHAR(1000)) AS si_item_fullname 			FROM 				skill 			WHERE 				parent_skill = "+skill_id+" 			UNION ALL 				SELECT 					si. ID, 					si.skill_title, 					si.parent_skill, 					CAST ( 						sp.skill_title || '->' || si.parent_skill AS VARCHAR (1000) 					) AS si_item_fullname 				FROM 					skill AS si 				INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 		) SELECT 			ID 		FROM 			supplytree 		ORDER BY 			si_item_fullname 	) ) group by report.user_id ";
		List<HashMap<String , Object>> data_org_percentile = util.executeQuery(org_percentile_query);
		for(HashMap<String , Object> row : data_org_percentile)
		{
			if(row.get("totall")!=null)
			{
				if(row.get("user_id")!=null && (int)row.get("user_id")==user_id)
				{
					BigInteger xyz = (BigInteger) row.get("totall");
					user_org_percentile= xyz.intValue();
				}
				BigInteger xyz = (BigInteger) row.get("totall");
				org_percentile= org_percentile+xyz.doubleValue();				
			}			
		}
		if(data_org_percentile.size()>0)
		{
			org_percentile= org_percentile/data_org_percentile.size();
		}
/*		String forAccuracy="select cast (avg(T3.ttttt) as integer) as accuracy from (select report.score*100/count(assessment_question.*) as ttttt, report.assessment_id as ass from assessment_question , report where assessment_question.assessmentid=report.assessment_id and report.user_id="+user_id+" and report.assessment_id in (	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id IN ( 		WITH RECURSIVE supplytree AS ( 			SELECT 				ID, 				skill_title, 				parent_skill 			FROM 				skill 			WHERE 				parent_skill = "+skill_id+" 			UNION ALL 				SELECT 					si. ID, 					si.skill_title, 					si.parent_skill 				FROM 					skill AS si 				INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 		) SELECT 			ID 		FROM 			supplytree 		 	)) group by report.assessment_id,report.score )T3 ";
		List<HashMap<String , Object>> accuData = util.executeQuery(forAccuracy);
	int avg=0;
		for(HashMap<String , Object> row: accuData)
		{
			if(row.get("accuracy")!=null)
			{
				avg = (int)row.get("accuracy");
			}
		}
		*/
		/*for industry benchmark*/
		int max_percentage =0;
		int user_percentage=0;
		
		String industry_benchmark_query="select cast (sum(TFINAL.score) as integer) as total_score, cast (sum(TFINAL.total_ques) as integer) as total_ques, TFINAL.user_id from (select DISTINCT score, user_id, count(assessment_question.questionid) as total_ques ,report.assessment_id from report , assessment_question where report.assessment_id= assessment_question.assessmentid and report.assessment_id in ( SELECT DISTINCT 				assessment. ID 			FROM 				skill_learning_obj_mapping, 				lesson, 				learning_objective_lesson, 				assessment 			WHERE 				skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 			AND learning_objective_lesson.lessonid = lesson. ID 			AND lesson. ID = assessment.lesson_id 			AND skill_learning_obj_mapping.skill_id = "+skill_id+" ) group by report.user_id,report.assessment_id, report.score ) TFINAL  GROUP BY TFINAL.user_id";
		
		System.out.println(industry_benchmark_query);
		List<HashMap<String , Object>> data_industry_benchmark= util.executeQuery(industry_benchmark_query);
		for(HashMap<String , Object> row: data_industry_benchmark)
		{
			int score = (int)row.get("total_score");
			int u_id = (int)row.get("user_id");
			int total_quest = (int)row.get("total_ques");
			if(total_quest!=0)
			{
				int perc = (score*100)/(total_quest);
				if(perc>max_percentage)
				{
					//System.out.println("(score)"+(score));
					//System.out.println("total_quest"+total_quest);
					max_percentage= perc;
				}
			}
			if(u_id==user_id)
			{
				user_percentage=((score*100)/(total_quest));
			}
		}
		//System.out.println("user_percentage"+user_percentage);
		//System.out.println("max_percentage"+max_percentage);
		int industtry_benchmark_value = user_percentage-max_percentage;
		//System.out.println("industtry_benchmark_value----"+industtry_benchmark_value);
		//System.out.println("user_percentage-----"+user_percentage);
		
		if(user_percentage!=0)
		{
			industry_benchmark= industtry_benchmark_value+"";
		}
		else if(user_percentage==0)
		{
			industry_benchmark="-99";
		}	
			
		
	
		/**/
		String qqq = "select distinct student_assessment.question_id, student_assessment.correct, student_assessment.time_taken  from student_assessment, skill_learning_obj_mapping, learning_objective_question where skill_learning_obj_mapping.learning_objective_id = learning_objective_question.learning_objectiveid and learning_objective_question.questionid = student_assessment.question_id and skill_id in ( "+new NewReportUtils().queryForChildSkill(skill_id)+" ) and student_assessment.student_id="+user_id+" ";
		List<HashMap<String, Object>> qqdata = util.executeQuery(qqq);
		int total_que = 0;
		int ans_corr= 0;
		ArrayList<Integer> alreadyadded = new ArrayList<>();

		for(HashMap<String, Object> rows: qqdata)
		{
			int question_id = (int) rows.get("question_id");
			if(!alreadyadded.contains(question_id))
			{
				alreadyadded.add(question_id);
				boolean corr  = (boolean) rows.get("correct");
				int t_taken = (int) rows.get("time_taken");
				if(corr)
				{
					ans_corr++;
				}
				total_que++;
							
			}		
		}

		
		if(total_que!=0)
		{
			accuracy = ((ans_corr*100)/total_que)+"%";
		}
		
		
		
		
	
		
		
		String final_percentile = "";

		double diff_in_percentile = (double)user_org_percentile - org_percentile;
		if(org_percentile ==0 && org_percentile == user_org_percentile)
		{
			final_percentile= "image_not_available##N/A";
			
		}
		else if(org_percentile != 0 && org_percentile == user_org_percentile)
		{
			final_percentile= "positive##"+100+"%";
			
		}
		else if(diff_in_percentile >0)
		{
			final_percentile= "positive##"+(int)diff_in_percentile+"%";
			
		}
		else
		{
			final_percentile= "negetive##"+((int)diff_in_percentile*-1)+"%";
			
			
			
		}
		
		dat.put("college_avg_diff", final_percentile);
		dat.put("percentile_batch", percentile_batch);
		dat.put("accuracy", accuracy);
		dat.put("industry_benchmark", industry_benchmark);
		
		return dat;
	
	}
	
	public ArrayList<LearningObjective> getLearningObjectivePerSkill(Skill skill)
	{
		//System.out.println("finding learning objective for skill "+skill.getId());
		ArrayList<LearningObjective> data = new ArrayList<>();
		//System.out.println(skill.getId()+"skill lobj mappinga "+ skill.getSkillLearningObjMappings().size());
			try {
				for(SkillLearningObjMapping lobjmapping : skill.getSkillLearningObjMappings())
				{			
					LearningObjective lobj = lobjmapping.getLearningObjective();
					data.add(lobj);
				}
				
				for(Skill sk : getChildSkills(skill))
				{
					//System.out.println(skill.getId()+"---calling lobj for skill --"+sk.getId());
					data.addAll(getLearningObjectivePerSkill(sk));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
		
		//System.out.println("lobj size"+ data.size());
		return data;
	}
	
	public ArrayList<Skill> getChildSkills(Skill skill)
	{
		DBUTILS util = new DBUTILS();
		ArrayList<Skill> data = (ArrayList<Skill>) (new SkillDAO()).findByParentSkill(skill.getId());
		
		return data;
	}
	
	public ArrayList<Assessment> getAssessmentForSkill(int skill_id, int user_id)
	{
		//System.out.println(" finding asssessment for skill id "+skill_id);
		ArrayList<Assessment> data = new ArrayList<>();	
		ArrayList<Integer> already_added = new ArrayList<>();
		Skill skill = new SkillDAO().findById(skill_id);
		ArrayList<LearningObjective> lobjs = getLearningObjectivePerSkill(skill);
		for(LearningObjective lobj : lobjs)
		{			
			for(Question question : lobj.getQuestionCollection())
			{
				
				for(AssessmentQuestion aq : question.getAssessmentQuestions())
				{
					if(hasDataInAssessmentStats(aq.getAssessment().getId(), user_id))
					{
						if(!already_added.contains(aq.getAssessment().getId()))
						{
							already_added.add(aq.getAssessment().getId());
							data.add(aq.getAssessment());
							break;
						}
					}
					else
					{
						break;
					}
					
				}
			}
		}				
		return data;
	}
	
	public boolean hasDataInAssessmentStats(int assessment_id, int user_id)
	{
		DBUTILS util = new DBUTILS();
		boolean res = false;
		String sql = "select * from stats_student_assessment where assessment_id="+assessment_id+" and student_id="+user_id;
		if(util.executeQuery(sql).size()>0)
		{
			res = true;
		}
		return res;
	}
	
}
