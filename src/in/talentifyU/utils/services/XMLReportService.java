/**
 * 
 */
package in.talentifyU.utils.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.istarindia.apps.dao.Skill;
import com.istarindia.apps.dao.SkillDAO;
import com.istarindia.apps.dao.Student;
import com.istarindia.apps.dao.StudentDAO;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.dao.entities.StudentAssessment;
import com.viksitpro.core.utilities.DBUTILS;

import in.talentifyU.complexData.pojo.XMLGraphPoints;
import in.talentifyU.complexData.pojo.XMLReportOption;
import in.talentifyU.complexData.pojo.XMLReportQuestion;
import in.talentifyU.complexData.pojo.XMLReportTest;
import in.talentifyU.complexData.pojo.XMLSkillGraph;
import in.talentifyU.complexData.pojo.XMLSkillReportLAData;
import in.talentifyU.complexData.pojo.XMLStudentReport;

/**
 * @author ComplexObject
 *
 */
public class XMLReportService {

	public XMLStudentReport getStudentReport(int studentId, int skillID) {		
		 
		Skill parentSkill = new SkillDAO().findById(skillID);
		XMLStudentReport report = new XMLStudentReport();
		DBUTILS util = new DBUTILS();
		
		//calculating total students in batch and batch rank		
		String findrank="SELECT (SELECT 			cast (count(*) as integer) as total_students 		FROM 			batch_students 		WHERE 			batch_group_id IN ( 				SELECT DISTINCT 					batch_group_id 				FROM 					batch_students 				WHERE 					student_id = "+studentId+" 			)), 	total_score, 	user_id, 	RANK FROM 	( 		SELECT 			total_score, 			user_id, 			CAST ( 				RANK () OVER (ORDER BY total_score DESC) AS INTEGER 			) 		FROM 			( 				SELECT DISTINCT 					CAST (SUM(score) AS INTEGER) AS total_score, 					user_id 				FROM 					report 				WHERE 					user_id IN ( 						SELECT 							student_id 						FROM 							batch_students 						WHERE 							batch_group_id IN ( 								SELECT DISTINCT 									batch_group_id 								FROM 									batch_students 								WHERE 									student_id = "+studentId+" 							) 					) 				GROUP BY 					user_id 				ORDER BY 					total_score 			) AS RankTable 	) FINALTABLE WHERE 	FINALTABLE.user_id = "+studentId;
		System.out.println(findrank);
		List<HashMap<String, Object>> rankdata = util.executeQuery(findrank);
		// output: total_students, total_score, user_id, rank
		if(rankdata.size()>0 )
		{
			int total_students = (int) rankdata.get(0).get("total_students");
			int total_score = (int) rankdata.get(0).get("total_score");			
			int rank = (int) rankdata.get(0).get("rank");
			
			report.setStudentId(studentId);
			report.setBatchRank(rank);
			report.setTotalBatchStudents(total_students);
			report.setTotalScore(total_score);			
			
			//get all child skill data at once 
			
			//HashMap<Integer, ArrayList<HashMap<String, Object>>> tree = getDataTree(skillID, studentId);	
			HashMap<Integer, HashMap<Integer, Integer>> parentChild= new HashMap<>();
			HashMap<Integer, List<HashMap<String, Object>>> skill_data = new HashMap<>();
			
			String sql ="select id as skill_id,  parent_skill  from skill where id in (SELECT 			skill_id 		FROM 			skill_learning_obj_mapping 		WHERE 			learning_objective_id IN ( 			select lobj_id from lobj_student_aggregate where student_id="+studentId+" ) 		UNION 			( 				WITH RECURSIVE supplytree AS ( 					SELECT 						ID, 						skill_title, 						parent_skill 					FROM 						skill 					WHERE 						ID IN ( 							SELECT 								skill_id 							FROM 								skill_learning_obj_mapping 							WHERE 								learning_objective_id IN ( 									select lobj_id from lobj_student_aggregate where student_id="+studentId+" 					) 						) 					UNION ALL 						SELECT 							si. ID, 							si.skill_title, 							si.parent_skill 						FROM 							skill AS si 						INNER JOIN supplytree AS sp ON (si. ID = sp.parent_skill) 				) SELECT 					ID AS skill_id 				FROM 					supplytree 			))";
			System.out.println(sql);
			List<HashMap<String, Object>> data = util.executeQuery(sql);
			for(HashMap<String, Object> row: data)
			{
				int skill_id = (int) row.get("skill_id");
				int parent= (int) row.get("parent_skill");
				String getdata="SELECT  (select skill_title from skill where id ="+skill_id+"),	cast(FF.countrypercentile  as integer) as globe_percentile, 		cast(FF.countrypercentile as integer), 		cast(FF.batchpercentile as integer), 		cast(FF.orgpercentile as integer), 		cast(CASE FF.batch_max_points WHEN 0 THEN 	0 ELSE 	FF.batch_points_earned * 100 / FF.batch_max_points END as integer ) as percentage,   	cast(FF.batch_points_earned as integer),  	cast(FF.batch_max_points as integer),  	cast(FF.countryrank as integer) as globerank,  	cast(FF.countryrank as integer),  	cast(FF.batchrank as integer),  	cast(FF.orgrank as integer) FROM 	( 		( 			SELECT 				TF.student_id AS country_stu_id, 				TF.points_earned AS country_points_earned, 				TF.max_points AS country_max_points, 				CAST ( 					RANK () OVER (  						ORDER BY 							TF.points_earned DESC 					) AS INTEGER 				) AS countryrank, 				CAST ( 					( 						CUME_DIST () OVER (ORDER BY TF.points_earned) * 100 					) AS INTEGER 				) AS countrypercentile 			FROM 				( 					SELECT 						student_id, 						SUM (total_points) AS points_earned, 						SUM (max_points) AS max_points 					FROM 						lobj_student_aggregate 					WHERE 						lobj_id IN ( 							SELECT 								learning_objective_id 							FROM 								skill_learning_obj_mapping 							WHERE 								skill_id IN ( 									( 										WITH RECURSIVE supplytree AS ( 											SELECT 												ID, 												skill_title, 												parent_skill 											FROM 												skill 											WHERE 												parent_skill = "+skill_id+" 											UNION ALL 												SELECT 													si. ID, 													si.skill_title, 													si.parent_skill 												FROM 													skill AS si 												INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 										) SELECT 											ID 										FROM 											supplytree 										ORDER BY 											ID 									) 									UNION 										( 											SELECT 												"+skill_id+" AS ID 											FROM 												skill 										) 								) 						) 					GROUP BY 						student_id 				) TF 		) TFCOUNTRY 		JOIN ( 			SELECT 				TF.student_id AS batch_stu_id, 				TF.points_earned AS batch_points_earned, 				TF.max_points AS batch_max_points, 				CAST ( 					RANK () OVER (  						ORDER BY 							TF.points_earned DESC 					) AS INTEGER 				) AS batchrank, 				CAST ( 					( 						CUME_DIST () OVER (ORDER BY TF.points_earned) * 100 					) AS INTEGER 				) AS batchpercentile 			FROM 				( 					SELECT 						student_id, 						SUM (total_points) AS points_earned, 						SUM (max_points) AS max_points 					FROM 						lobj_student_aggregate 					WHERE 						lobj_id IN ( 							SELECT 								learning_objective_id 							FROM 								skill_learning_obj_mapping 							WHERE 								skill_id IN ( 									( 										WITH RECURSIVE supplytree AS ( 											SELECT 												ID, 												skill_title, 												parent_skill 											FROM 												skill 											WHERE 												parent_skill = "+skill_id+" 											UNION ALL 												SELECT 													si. ID, 													si.skill_title, 													si.parent_skill 												FROM 													skill AS si 												INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 										) SELECT 											ID 										FROM 											supplytree 										ORDER BY 											ID 									) 									UNION 										( 											SELECT 												"+skill_id+" AS ID 											FROM 												skill 										) 								) 						) 					AND student_id IN ( 						SELECT DISTINCT 							student_id 						FROM 							batch_students 						WHERE 							batch_students.batch_group_id IN ( 								SELECT 									batch_group_id 								FROM 									batch_students 								WHERE 									student_id = "+studentId+" 							) 					) 					GROUP BY 						student_id 				) TF 		) TFBATCH ON ( 			TFCOUNTRY.country_stu_id = TFBATCH.batch_stu_id 		) 		JOIN ( 			SELECT 				TF.student_id AS org_stu_id, 				TF.points_earned AS org_points_earned, 				TF.max_points AS org_max_points, 				CAST ( 					RANK () OVER (  						ORDER BY 							TF.points_earned DESC 					) AS INTEGER 				) AS orgrank, 				CAST ( 					( 						CUME_DIST () OVER (ORDER BY TF.points_earned) * 100 					) AS INTEGER 				) AS orgpercentile 			FROM 				( 					SELECT 						student_id, 						SUM (total_points) AS points_earned, 						SUM (max_points) AS max_points 					FROM 						lobj_student_aggregate 					WHERE 						lobj_id IN ( 							SELECT 								learning_objective_id 							FROM 								skill_learning_obj_mapping 							WHERE 								skill_id IN ( 									( 										WITH RECURSIVE supplytree AS ( 											SELECT 												ID, 												skill_title, 												parent_skill 											FROM 												skill 											WHERE 												parent_skill = "+skill_id+" 											UNION ALL 												SELECT 													si. ID, 													si.skill_title, 													si.parent_skill 												FROM 													skill AS si 												INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 										) SELECT 											ID 										FROM 											supplytree 										ORDER BY 											ID 									) 									UNION 										( 											SELECT 												"+skill_id+" AS ID 											FROM 												skill 										) 								) 						) 					AND student_id IN ( 						SELECT 							ID 						FROM 							student 						WHERE 							organization_id = ( 								SELECT 									organization_id 								FROM 									student 								WHERE 									ID = "+studentId+" 							) 					) 					GROUP BY 						student_id 				) TF 		) TFORG ON ( 			TFORG.org_stu_id = TFBATCH.batch_stu_id 		) 	) FF WHERE 	country_stu_id = "+studentId+" ";
//				System.out.println(getdata);
				List<HashMap<String, Object>> agg_data = util.executeQuery(getdata);
				if(agg_data.size()>0)
				{				
					if(parentChild.containsKey(parent))
					{
						HashMap<Integer, Integer> children = parentChild.get(parent);
						children.put(skill_id, skill_id);
						parentChild.put(parent, children);
					}
					else
					{
						HashMap<Integer, Integer> children = new  HashMap<Integer, Integer>();
						children.put(skill_id, skill_id);
						parentChild.put(parent, children);
					}	
					
					skill_data.put(skill_id, agg_data);					
				}
				
			}
			
			
			
			HashMap<Integer, ArrayList<HashMap<String, Object>>> graphBaap = getBaapGraph(skillID, studentId);
				
			
			
			
			
			XMLSkillReportLAData parent = new XMLSkillReportLAData();
			parent.setGraphData(getSkillGraph(skillID, studentId,graphBaap));
			parent.setImageUrl(null);
			parent.setRank(rank);
			
			//getting subskills and respective data of overall
			ArrayList<XMLSkillReportLAData> childs= getChildXMLSkillRuntime(studentId,skillID,parentChild, skill_data,graphBaap); 
			
			
			parent.setSubSkills(childs); 
			int totalPointEarned =0;
			int maxPoints =0;
			for(XMLSkillReportLAData child : childs)
			{
				totalPointEarned+=child.getPointsEarned();
				maxPoints+=child.getTotalPoints();
			}
			parent.setPointsEarned(totalPointEarned);
			parent.setTotalPoints(maxPoints);
			report.setPointsEarned(totalPointEarned);
			report.setTotalPoints(maxPoints);
			int rating = 0;
			if(maxPoints>0)
			{
				rating = (totalPointEarned*100)/maxPoints;
			}
			parent.setRating(rating);
			String imageUrl ="CADET";
			if(rating>=75)
			{
				imageUrl="BULLSEYE";
			}
			else if(rating >= 50 && rating <75)
			{
				imageUrl="SCOUT";
			}
			else if(rating >= 25 && rating <50)
			{
				imageUrl = "TENDERFOOT";
			}
			else
			{
				imageUrl="CADET";
			}
			
			parent.setImageUrl(imageUrl);
			parent.setSkillId(skillID);
			parent.setSkillName(parentSkill.getSkillTitle());			
			report.setOverAllData(parent);		
			
			ArrayList<XMLReportTest> tests = getTestList(studentId);
			report.setTestList(tests);
			
			
			
		}
		
		return report;
	}

	private ArrayList<XMLReportTest> getTestList(int studentId) {
		
		HashMap<Integer, HashMap<String, String>> attemptedQuestionData= getAttemptedQuestionData(studentId);
		
		
		ArrayList<XMLReportTest> tests = new ArrayList<>();
		String sql ="SELECT report.score, COALESCE(report.rank_country,0) as rank, COALESCE(report.points_earned,0) as points_earned, COALESCE(report.total_points,0) as total_points, COALESCE(report.time_taken,0) as time_taken, assessment.id, COALESCE(assessment.assessmenttitle,'NOT_PROVIDED') as assessmenttitle, COALESCE(report.percentage,0) as percentage, cast(report.created_at as date) as date from report, assessment where assessment.id = report.assessment_id and report.user_id="+studentId;
		System.out.println(sql);
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		if(data.size()>0)
		{
			for(HashMap<String, Object> row: data)
			{
				int score=(int)row.get("score");
				int rank=(int)row.get("rank");
				int points_earned=(int)row.get("points_earned");
				int total_points=(int)row.get("total_points");
				int time_taken=(int)row.get("time_taken");
				int id=(int)row.get("id");
				String assessmenttitle=(String)row.get("assessmenttitle");
				assessmenttitle= assessmenttitle.replace("&", "and");
				//System.out.println("title--"+assessmenttitle);
				int percentage=(int)row.get("percentage");
				Date date = (Date)row.get("date");
				XMLReportTest test = new XMLReportTest();
				test.setAssessmentId(id);
				test.setDate(date.toString());
				test.setPercentage(percentage);
				test.setTestName(assessmenttitle);
				test.setMaxPoints(total_points);
				test.setPointsEarned(points_earned);
				test.setRank(rank);
				//System.out.println("test title--"+test.getTestName());
				String imageUrl ="CADET";
				if(percentage>75)
				{
					imageUrl="BULLSEYE";
				}
				else if(percentage > 50 && percentage <75)
				{
					imageUrl="SCOUT";
				}
				else if(percentage > 25 && percentage <50)
				{
					imageUrl = "TENDERFOOT";
				}
				else
				{
					imageUrl="CADET";
				}
				test.setImageUrl(imageUrl);
				
				ArrayList<XMLReportQuestion> questions = getQuestions(id, studentId,attemptedQuestionData);
				test.setQuestions(questions);
				tests.add(test);
			}
		}
		return tests;
	}

	private HashMap<Integer, HashMap<String, String>> getAttemptedQuestionData(int studentId) {
		HashMap<Integer, HashMap<String, String>> questionData= new HashMap<>();
		
		Student st = new StudentDAO().findById(studentId);
		Set<StudentAssessment> stuassess = st.getStudentAssessments();		
		String sql = "select correct, option1, option2, option3, option4, option5, question_id, time_taken, question.difficulty_level from student_assessment , question  where student_id="+studentId+" and student_assessment.question_id=question.id ;";
		DBUTILS util = new DBUTILS();
		
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		
		for(HashMap<String, Object> row: data)
		{
			int data_que_id = (int)row.get("question_id");
			
			HashMap<String, String> myque= new HashMap<>();
			//Question que = stass.getQuestion();
			
			String correct_ans = "";
			HashMap<Integer, String> options = new HashMap<>();
		
			
			String sql_for_option = "select id, text, marking_scheme from assessment_option where question_id ="+data_que_id+" order by id";
			List<HashMap<String, Object>> option_data = util.executeQuery(sql_for_option);
	        int i=1;
	       
	        for(HashMap<String, Object> row2: option_data )
	        {
	        	if(row2.get("text")!=null && !row2.get("text").toString().isEmpty())
	        	{
	        		String option_text = (String)row2.get("text");	        	
	        		options.put(i, option_text);
		        	int marking_scheme = (int)row2.get("marking_scheme");
		        	if(marking_scheme==1)
		        	{
		        		correct_ans += option_text+",";
		        	}
	        	}
	        	else
	        	{
	        		options.put(i, "none");
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
	        if(row.get("correct")!=null && (boolean)row.get("correct")!=false )
        	{
        		is_corect= "Correct";	
        	}
	        if(options.size()>0 && row.get("option1")!=null && (boolean)row.get("option1")!=false)
    		{
    			
    			selected_ans += options.get(1)+",";
    			//System.out.println("option 1 choosen ------"+selected_ans);
    		}
    		if(options.size()>1 &&  row.get("option2")!=null && (boolean)row.get("option2")!=false)
    		{
    			
    			selected_ans += options.get(2)+",";
    			//System.out.println("option 2 choosen ------"+selected_ans);
    		}
    		if(options.size()>2 && row.get("option3")!=null && (boolean)row.get("option3")!=false)
    		{
    			
    			selected_ans += options.get(3)+",";
    			//System.out.println("option 3 choosen ------"+selected_ans);
    		}
    		if(options.size()>3 &&  row.get("option4")!=null && (boolean)row.get("option4")!=false)
    		{
    			
    			selected_ans += options.get(4)+",";
    			//System.out.println("option 4 choosen ------"+selected_ans);
    		}
    		if(options.size()>4 && row.get("option5")!=null && (boolean)row.get("option5")!=false)
    		{
    			
    			selected_ans += options.get(5)+",";
    			//System.out.println("option 5 choosen ------"+selected_ans);
    		}
    		//time_taken
    		if(row.get("time_taken")!=null && (int)row.get("time_taken")>0)
    		{
    			time_taken=row.get("time_taken").toString();
    		}
    		else
    		{
    			time_taken="50 sec";
    		}	
    		
    		if(selected_ans.equals(""))
	        {
	        	selected_ans="SKIPPED";
	        }
    		//System.out.println("que"+que.getId()+"--selected--"+selected_ans);
    		int difficultyLevel=1;
    		if(row.get("difficulty_level")!=null )
    		{difficultyLevel= (int)row.get("difficulty_level");
    			
    		}
    		myque.put("selected_ans", selected_ans );
    		myque.put("difficulty_level", difficultyLevel+"");
    		myque.put("time_taken", time_taken);	
    		
    		
    		myque.put("correct_ans",correct_ans );
    		myque.put("is_corect", is_corect+"");  	
	     	
    		questionData.put(data_que_id, myque);
		}
		
		
		
		return questionData;
	}

	private String getParsedHTML(String html)
	{
		String parsedString = html;
		
		
		parsedString=	parsedString.replaceAll("/video/", "http://api.talentify.in:8080/video/");
		byte ptext[] = parsedString.getBytes();
		try {
		parsedString = new String(ptext, "UTF-8");
		} catch (Exception e) {
			System.out.println("error at xml"+html);
		}
		/*String parsedString = html.replaceAll("&nbsp;", "");
		
		
		
		
			//parsedString = new String(ptext, "UTF-8");
			//parsedString =  URLDecoder.decode(parsedString.replaceAll("&nbsp;", ""), "UTF-8").replaceAll("&#150;", "-").replaceAll("&#146;", "'");
			parsedString.replaceAll("&#150;", "-").replaceAll("&#146;", "'")
			.replaceAll("&nbsp;", "").replaceAll("&#39;", "'")
			.replaceAll("&times;", "*").replaceAll("&prime;", "'").replaceAll("&rArr;", "==>")
			.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&minus;", "-");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			parsedString.replaceAll("&#150;", "-").replaceAll("&#146;", "'")
			.replaceAll("&nbsp;", "").replaceAll("&#39;", "'")
			.replaceAll("&times;", "*").replaceAll("&prime;", "'").replaceAll("&rArr;", "==>")
			.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&minus;", "-");
		}
		
		//System.out.println("parsed-"+parsedString);
		Document doc = Jsoup.parse(parsedString);
		org.jsoup.examples.HtmlToPlainText text = new HtmlToPlainText();
		parsedString = doc.text();*/
		
		return parsedString;
	}
	
	private ArrayList<XMLReportQuestion> getQuestions(int assessment_id, int studentId, HashMap<Integer, HashMap<String, String>> attemptedQuestionData) {
		ArrayList<XMLReportQuestion> questions = new ArrayList<>();
		
		Assessment assessment = new AssessmentDAO().findById(assessment_id);
		for(AssessmentQuestion assque : assessment.getAssessmentQuestions())
		{
			Question que = assque.getQuestion();
			if(attemptedQuestionData.containsKey(que.getId()))
			{
				XMLReportQuestion question = new XMLReportQuestion();
				question.setQuestionId(que.getId());
				question.setQuestionText(getParsedHTML(que.getQuestionText()));
				//System.out.println(que.getId()+"---"+que.getExplanation());
				question.setExplanation(getParsedHTML(que.getExplanation().toString()));
				
				ArrayList<XMLReportOption> options = getOptions(que.getId());
				question.setOptions(options);
				
				HashMap<String, String> getAttempetdQuestionData= attemptedQuestionData.get(que.getId());
				question.setCorrectAnswer(getParsedHTML(getAttempetdQuestionData.get("correct_ans")));
				question.setCorrectness(getAttempetdQuestionData.get("is_corect"));
				question.setSelectedAnswer(getParsedHTML(getAttempetdQuestionData.get("selected_ans")));
				question.setDificultyLevel(getAttempetdQuestionData.get("difficulty_level"));
				question.setTimeTaken(getAttempetdQuestionData.get("time_taken"));
				questions.add(question);
			}
			
		}
		
		return questions;
	}

	private ArrayList<XMLReportOption> getOptions(int  que_id) {
		ArrayList<XMLReportOption> options = new ArrayList<>();
		String sql = "select id, text, question_id, marking_scheme from assessment_option where question_id="+que_id;
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		for(HashMap<String, Object> row: data)
		{
			if(row.get("text")!=null && !row.get("text").toString().isEmpty())
			{
				XMLReportOption rop = new XMLReportOption();
				rop.setMarkingScheme((int)row.get("marking_scheme"));
				rop.setOptionId((int)row.get("id"));
				rop.setOptionText(getParsedHTML(row.get("text").toString()));
				options.add(rop);
			}
		}
		
		/*for(AssessmentOption op : que.getAssessmentOptions())
		{
			if(op.getText()!=null && !op.getText().equalsIgnoreCase(""))
			{
				XMLReportOption rop = new XMLReportOption();
				rop.setMarkingScheme(op.getMarkingScheme());
				rop.setOptionId(op.getId());
				rop.setOptionText(getParsedHTML(op.getText()));
				options.add(rop);
			}						
		}*/
		return options;
	}

	private HashMap<Integer, ArrayList<HashMap<String, Object>>> getBaapGraph(int skillID, int studentId) {
		HashMap<Integer, ArrayList<HashMap<String, Object>>> graph = new  HashMap<>();
		String sql ="SELECT 	CPTABLE.skill_title, 	CPTABLE. ID, 	CPTABLE.LEVEL, 	CPTABLE.parent_skill, 	cast(timestamp as date) as date , 		coalesce(skill_precentile.percentile_batch, 0) as percentile_batch,   	coalesce(skill_precentile.percentile_country, 0) as percentile_country,   	coalesce(skill_precentile.percentile_globe, 0) as percentile_globe,   	coalesce(skill_precentile.percentile_organization, 0) as percentile_organization  FROM 	( 		WITH RECURSIVE menu_tree ( 			ID, 			skill_title, 			LEVEL, 			parent_skill 		) AS ( 			SELECT 				ID, 				skill_title, 				0, 				parent_skill 			FROM 				skill 			WHERE 				parent_skill = "+skillID+" 			UNION ALL 				SELECT 					mn. ID, 					mn.skill_title, 					mt. LEVEL + 1, 					mt. ID 				FROM 					skill mn, 					menu_tree mt 				WHERE 					mn.parent_skill = mt. ID 		) SELECT 			* 		FROM 			menu_tree 		WHERE 			LEVEL >= 0 		ORDER BY 			LEVEL, 			parent_skill 	) CPTABLE JOIN skill_precentile ON ( 	CPTABLE.ID = skill_precentile.skill_id ) WHERE 	skill_precentile.student_id = "+studentId;
		System.out.println(sql);
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		for(HashMap<String, Object> row: data)
		{
			int parent_skill= (int)row.get("id");
			if(graph.containsKey(parent_skill))
			{
				ArrayList<HashMap<String, Object>> child_nodes = graph.get(parent_skill);
				child_nodes.add(row);
				graph.put(parent_skill, child_nodes);
			}
			else
			{
				ArrayList<HashMap<String, Object>> child_nodes = new ArrayList<>();
				child_nodes.add(row);
				graph.put(parent_skill, child_nodes);
			}
		}

		return graph;
	}

	private HashMap<Integer, ArrayList<HashMap<String, Object>>> getDataTree(int skillID, int studentId) {
		HashMap<Integer, ArrayList<HashMap<String, Object>>> tree = new  HashMap<>();
		String sql ="SELECT  CPTABLE.skill_title, 	CPTABLE. ID, 	CPTABLE.LEVEL, 	CPTABLE.parent_skill, 	coalesce(skill_pointer.points_earned, 0) as points_earned, 		coalesce(skill_pointer.max_points, 0)as max_points, 	skill_pointer.updated_at, 		coalesce(skill_pointer.rank_batch, 0) as rank, 		coalesce(skill_pointer.percentile_batch, 0) as percentile_batch,   	coalesce(skill_pointer.percentile_country, 0) as percentile_country,   	coalesce(skill_pointer.percentile_globe, 0) as percentile_globe,   	coalesce(skill_pointer.percentile_organization, 0) as percentile_organization	  FROM 	( 		WITH RECURSIVE menu_tree ( 			ID, 			skill_title, 			LEVEL, 			parent_skill 		) AS ( 			SELECT 				ID, 				skill_title, 				0, 				parent_skill 			FROM 				skill 			WHERE 				parent_skill = "+skillID+" 			UNION ALL 				SELECT 					mn. ID, 					mn.skill_title, 					mt. LEVEL + 1, 					mt. ID 				FROM 					skill mn, 					menu_tree mt 				WHERE 					mn.parent_skill = mt. ID 		) SELECT 			* 		FROM 			menu_tree 		WHERE 			LEVEL >= 0 		ORDER BY 			LEVEL, 			parent_skill 	) CPTABLE JOIN skill_pointer ON ( 	CPTABLE. ID = skill_pointer.skill_id ) WHERE 	skill_pointer.student_id ="+studentId;
		System.out.println(sql);
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		for(HashMap<String, Object> row: data)
		{
			int parent_skill= (int)row.get("parent_skill");
			if(tree.containsKey(parent_skill))
			{
				ArrayList<HashMap<String, Object>> child_nodes = tree.get(parent_skill);
				child_nodes.add(row);
				tree.put(parent_skill, child_nodes);
			}
			else
			{
				ArrayList<HashMap<String, Object>> child_nodes = new ArrayList<>();
				child_nodes.add(row);
				tree.put(parent_skill, child_nodes);
			}
		}
		return tree;
	}
	
	
	private ArrayList<XMLSkillReportLAData> getChildXMLSkillRuntime(int student_id,int parent_id,HashMap<Integer, HashMap<Integer, Integer>> parentChild, HashMap<Integer, List<HashMap<String, Object>>> skill_data, HashMap<Integer, ArrayList<HashMap<String, Object>>> graphBaap) {
		ArrayList<XMLSkillReportLAData> childSubskill = new ArrayList<>();
		if(parentChild.containsKey(parent_id))
		{
			HashMap<Integer, Integer> children = parentChild.get(parent_id);
			for(int child_id  : children.keySet())
			{
				if(skill_data.containsKey(child_id))
				{
					List<HashMap<String, Object>> child_data= skill_data.get(child_id);
					for(HashMap<String, Object> row: child_data)
					{
						int points_earned = (int) row.get("batch_points_earned");
						int max_points = (int) row.get("batch_max_points");
						String skill_title = (String) row.get("skill_title");
						int rank       =(int)row.get("batchrank");
						int percentile_batch       =(int)row.get("batchpercentile");
						int percentile_country     =(int)row.get("countrypercentile");
						int percentile_globe       =(int)row.get("globe_percentile");
						int percentile_organization=(int)row.get("orgpercentile");
						XMLSkillReportLAData child = new XMLSkillReportLAData();
						child.setSkillId(child_id);
						child.setSkillName(skill_title);
						child.setPercentileBatch(percentile_batch);
						child.setPercentileCountry(percentile_country);
						child.setPercentileglobe(percentile_globe);
						child.setPercentileOrganozation(percentile_organization);
						child.setRank(rank);
						int rating = 0;
						if(max_points>0)
						{
							rating = (points_earned*100)/max_points;
						}
						child.setRating(rating);
						child.setPointsEarned(points_earned);
						child.setTotalPoints(max_points);
						String imageUrl ="CADET";
						if(rating>=75)
						{
							imageUrl="BULLSEYE";
						}
						else if(rating >= 50 && rating <75)
						{
							imageUrl="SCOUT";
						}
						else if(rating >= 25 && rating <50)
						{
							imageUrl = "TENDERFOOT";
						}
						else
						{
							imageUrl="CADET";
						}
						child.setImageUrl(imageUrl);
						ArrayList<XMLSkillReportLAData> grandChildren =getChildXMLSkillRuntime(student_id,child_id,parentChild, skill_data,graphBaap);  	
						if(grandChildren!=null && grandChildren.size()>0)
						{
							XMLSkillGraph graph = getSkillGraph(child_id,student_id,graphBaap);
							child.setGraphData(graph);
						}
						
						child.setSubSkills(grandChildren);
						childSubskill.add(child);
					}		
					
				}								
			}
		}
	
	return childSubskill;
	
	}
	
	private ArrayList<XMLSkillReportLAData> getChildXMLSkill(int parentSkillId, int student_id, HashMap<Integer, ArrayList<HashMap<String, Object>>> tree, HashMap<Integer, ArrayList<HashMap<String, Object>>> graphBaap) {
	
		
		ArrayList<XMLSkillReportLAData> children = new ArrayList<XMLSkillReportLAData>();
		
		List<HashMap<String, Object>> data = tree.get(parentSkillId);
		if(data!=null && data.size()>0)
		{
			for(HashMap<String, Object> row : data )
			{
				int skill_id = (int) row.get("id");
				int points_earned = (int) row.get("points_earned");
				int max_points = (int) row.get("max_points");
				String skill_title = (String) row.get("skill_title");
				int rank       =(int)row.get("rank");
				int percentile_batch       =(int)row.get("percentile_batch");
				int percentile_country     =(int)row.get("percentile_country");
				int percentile_globe       =(int)row.get("percentile_globe");
				int percentile_organization=(int)row.get("percentile_organization");
				
				ArrayList<XMLSkillReportLAData> grandChildren = getChildXMLSkill(skill_id, student_id, tree, graphBaap);
				
				XMLSkillReportLAData child = new XMLSkillReportLAData();				
				child.setSkillId(skill_id);
				child.setSkillName(skill_title);
				child.setPercentileBatch(percentile_batch);
				child.setPercentileCountry(percentile_country);
				child.setPercentileglobe(percentile_globe);
				child.setPercentileOrganozation(percentile_organization);
				child.setRank(rank);
				
				child.setSubSkills(grandChildren);
				int rating = 0;
				if(max_points>0)
				{
					rating = (points_earned*100)/max_points;
				}
				child.setRating(rating);
				child.setPointsEarned(points_earned);
				child.setTotalPoints(max_points);
				String imageUrl ="CADET";
				if(rating>=75)
				{
					imageUrl="BULLSEYE";
				}
				else if(rating >= 50 && rating <75)
				{
					imageUrl="SCOUT";
				}
				else if(rating >= 25 && rating <50)
				{
					imageUrl = "TENDERFOOT";
				}
				else
				{
					imageUrl="CADET";
				}
				child.setImageUrl(imageUrl);
				if(grandChildren!=null && grandChildren.size()>0)
				{
					XMLSkillGraph graph = getSkillGraph(skill_id,student_id,graphBaap);
					child.setGraphData(graph);
				}
				
				children.add(child);				
			}
		}
		return children;
		
	}

	private XMLSkillGraph getSkillGraph(int skill_id, int student_id, HashMap<Integer, ArrayList<HashMap<String, Object>>> graphBaap) {
		
		XMLSkillGraph graph = new XMLSkillGraph();
		List<HashMap<String, Object>> data = graphBaap.get(skill_id);
		if(data!=null && data.size()>0)
		{
			ArrayList<XMLGraphPoints> points = new ArrayList<>();
			for(HashMap<String, Object> row: data)
			{
				Date timestamp = (Date) row.get("date");
				int batch_percentile = (int) row.get("percentile_batch");
				int percentile_country = (int) row.get("percentile_country");
				int percentile_globe = (int) row.get("percentile_globe");
				int percentile_organization = (int) row.get("percentile_organization");
				
				XMLGraphPoints point = new XMLGraphPoints();
				point.setDate(timestamp.toString());
				point.setPercentileBatch(batch_percentile);
				point.setPercentileCountry(percentile_country);
				point.setPercentileglobe(percentile_globe);
				point.setPercentileOrganozation(percentile_organization);
				points.add(point);				
			}
			graph.setGraphPoints(points);
		}
		return graph;
	}

	
	
}
