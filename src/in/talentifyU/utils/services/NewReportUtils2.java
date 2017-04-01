/**
 * 
 */
package in.talentifyU.utils.services;

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
import com.istarindia.apps.dao.StatsStudentQuestion;
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
public class NewReportUtils2 {

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
	
	
	
	public HashMap<Integer, HashMap<String, String>> getQuestionDeatilsPerSkill(int parent_skill_id,int skill_id, int user_id)
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
	}
	
	/*
	 * Will give two data
	 * a) HashMap<Integer, ArrayList<String>> que_lobj (key is question id and value is Arraylist of Learning Objective)
	 * b) HashMap<Integer, HashMap<String, String>> que_deatils (key is question id and value is hashmap of question details like
	 *    question text, options, coreect or not )
	 * */
	public HashMap<String, Object> getQuestionWithDetailsPerSkill(int parent_skill_id,int skill_id, int user_id)
	{
		HashMap<String, Object> data = new HashMap<>();
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
			        	
		        	}
		        }
		        
		        if(selected_ans.equals(""))
		        {
		        	selected_ans="SKIPPED";
		        }
		        
		        String time_taken ="N/A";
		        String percentile ="N/A";
		        String prof_level="Rookie";
		        
		        
		        for(StatsStudentQuestion stat_que : que.getStatsStudentQuestions())
		        {
		        	if(stat_que.getStudent().getId() == student.getId())
		        	{
		        		time_taken = stat_que.getTimeTaken()+"";
		        		if(stat_que.getPercentileBatch()<=30 )
		        		{
		        			prof_level ="Rookie";
		        		}
		        		else if(stat_que.getPercentileBatch()>30 && stat_que.getPercentileBatch() <=60)
		        		{
		        			prof_level ="Apprentice";
		        		}if(stat_que.getPercentileBatch()>60 && stat_que.getPercentileBatch() <=85)
		        		{
		        			prof_level="Master";
		        		}
		        		else if(stat_que.getPercentileBatch()>85)
		        		{
		        			prof_level="Wizard";
		        		}
		        		percentile = stat_que.getPercentileBatch()+"";
		        	}
		        }
		        
		        HashMap<String, String> que_data = new HashMap<String, String>();
		        que_data.put("question_text", question_text);
		     	que_data.put("selected_ans", selected_ans);
		     	que_data.put("difficulty_level", difficulty_level);
		     	que_data.put("time_taken", time_taken);
		     	que_data.put("percentile", percentile+"%");
		     	que_data.put("correct_ans", correct_ans);
		     	que_data.put("is_corect", is_corect+"");  	
		     	que_data.put("explanation", explanation);
		     	que_data.put("prof_level", prof_level);  	
		     	que_deatils.put(que.getId(), que_data);
		        /*Got all question details, below code consist of learning objective per question */		        		        
		     	HashSet<LearningObjective> learningObjs = new HashSet<LearningObjective>(que.getLearningObjectives())  ;		        
		        que_lobj.put(que.getId(), learningObjs);
			}						
		}
		//System.out.println("que data-------"+que_deatils.size());
		data.put("question_details", que_deatils);
		data.put("learning_obj_details", que_lobj);						
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
		for(AssessmentQuestion question : assessment.getAssessmentQuestions())
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
		}
		return data;
	}
	
	public HashMap<String, String> getPercentileForSkill(int skill_id, int user_id)
	{
		 HashMap<String, String> dat = new HashMap<>();
		 	String percentile_batch = "N/A";
		 	String percentile_organization = "N/A";
		 	String percentile_country = "N/A";
		 	String percentile_globe ="N/A";
		DBUTILS util = new DBUTILS();
		String sql= "SELECT student_id, batch_group_id, organization_id, score, ( CUME_DIST () OVER ( PARTITION BY batch_group_id ORDER BY T1.score ) ) * 100 AS percentile_batch, ( CUME_DIST () OVER ( PARTITION BY organization_id ORDER BY T1.score ) ) * 100 AS percentile_organization FROM ( SELECT sa.batch_group_id, sa.organization_id, sa.student_id AS student_id, COUNT (sa.correct) AS score FROM student_assessment sa, skill_assessment_mapping sam WHERE sa.assessment_id = sam.assessment_id AND skill_id = "+skill_id+" GROUP BY sa.organization_id, sa.batch_group_id, sa.student_id ) T1 ORDER BY student_id DESC";
		System.out.println(sql);
		List<HashMap<String , Object>> data = util.executeQuery(sql);
		for(HashMap<String , Object> row : data)
		{
			if(row.get("student_id").toString().equalsIgnoreCase(user_id+"")) {
				if(row.get("percentile_batch")!= null) {
					percentile_batch = row.get("percentile_batch").toString()+"%";
				}
				if(row.get("percentile_organization")!= null) {
					percentile_organization = row.get("percentile_organization").toString()+"%";
					percentile_country = row.get("percentile_organization").toString()+"%";
					percentile_globe = row.get("percentile_organization").toString()+"%";
				}
			}
			
		}
		
			dat.put("percentile_batch", percentile_batch);
			dat.put("percentile_organization", percentile_organization);
			dat.put("percentile_country", percentile_country);
			dat.put("percentile_globe", percentile_globe);
			
		
		return dat;
	
	}
	
	
	public HashMap<String, String> getPercentileForChild(int skill_id, int user_id)
	{
		 HashMap<String, String> dat = new HashMap<>();
		 	String percentile_batch = "N/A";
		 	
		 	String accuracy ="N/A";
		 	String industry_benchmark ="N/A";
		 	
		int user_org_percentile =  0; 
		double org_percentile =  0;
		
		DBUTILS util = new DBUTILS();
		String sql= "SELECT 	 percentile_organization,percentile_batch, accuracy, industry_benchmark FROM 	skill_precentile WHERE 	student_id = "+user_id+" AND skill_id = "+skill_id+" order by timestamp desc limit 1  ";
		List<HashMap<String , Object>> data = util.executeQuery(sql);
		for(HashMap<String , Object> row : data)
		{
			if(row.get("percentile_batch")!=null)
			{
				percentile_batch = ((int)row.get("percentile_batch"))+"%";
			}
			if(row.get("percentile_organization")!=null)
			{
				user_org_percentile = (int)row.get("percentile_organization");
			}
			if(row.get("industry_benchmark")!=null)
			{
				industry_benchmark = ((int)row.get("industry_benchmark"))+"%";
			}
			if(row.get("accuracy")!=null)
			{
				accuracy = ((int)row.get("accuracy"))+"%";
			}
		}
		
		String getCollegeAverage="   select sum(T1.percentile_organization)/count(T1.*) as college_average from (SELECT (sum(skill_precentile.percentile_organization)/count(*)) AS percentile_organization,  skill_precentile.student_id FROM 	skill_precentile WHERE 	 skill_id ="+skill_id+" GROUP BY 	skill_precentile.student_id ) as T1";
		List<HashMap<String , Object>> data2 = util.executeQuery(getCollegeAverage);
		for(HashMap<String , Object> row : data)
		{
			if(row.get("college_average")!=null)
			{
				org_percentile = (double)row.get("percentile_organization");
			}			
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
			final_percentile= "positive##"+diff_in_percentile+"%";			
		}
		else
		{
			final_percentile= "negetive##"+(diff_in_percentile*-1)+"%";
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
