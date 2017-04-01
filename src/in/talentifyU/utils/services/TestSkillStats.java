/**
 * 
 */
package in.talentifyU.utils.services;

import java.util.ArrayList;

import com.viksitpro.core.dao.entities.BatchGroup;
import com.viksitpro.core.dao.entities.BatchGroupDAO;
import com.viksitpro.core.dao.entities.BatchStudents;
import com.viksitpro.core.utilities.DBUTILS;

/**
 * @author ComplexObject
 *
 */
public class TestSkillStats {

	/**
	 * @param args
	 */
	
	public static int getRandomInteger(int maximum, int minimum){ return ((int) (Math.random()*(maximum - minimum))) + minimum; }

	public static void main(String[] args) {
		System.out.println("started   ");
		BatchGroup bg = new BatchGroupDAO().findById(98);
		DBUTILS util = new DBUTILS();
		ArrayList<Integer> sales_student = new ArrayList<>();
		for(BatchStudents bstu : bg.getBatchStudentses())
		{
			if(bstu.getUserType().equalsIgnoreCase("STUDENT"))
			{
				if(!sales_student.contains(bstu.getStudent().getId()))
				{
					sales_student.add(bstu.getStudent().getId());
					
					
				}
				
			}
		}
		
		
		
		for(int student_id : sales_student)
		{
			DBUTILS uuuu = new DBUTILS();
			/*for(Skill sk : new SkillDAO().findAll())
			{
				int skill_id = sk.getId();
				String mysql1 = "select * from skill_precentile where skill_id =0 and student_id="+student_id;
				if(uuuu.executeQuery(mysql1).size()==0)
				{*/
					for(int j=1; j<10;j++)
					{
						int random_month = getRandomInteger(2, 12);
						int random_day = getRandomInteger(1, 29);
						int batch_percentile  =getRandomInteger(10, 100);
						int accuracy = getRandomInteger(10, 100);
						String insert_into_skill_percentile  ="INSERT INTO skill_precentile (student_id, skill_id, timestamp, percentile_country, percentile_globe, percentile_batch, percentile_organization, accuracy, industry_benchmark) "
								+ "VALUES ("+student_id+", '0', '2016-"+random_month+"-"+random_day+" 17:16:09', '"+batch_percentile+"', '"+batch_percentile+"', '"+batch_percentile+"', '"+batch_percentile+"', '"+accuracy+"', '"+getRandomInteger(0, 20)+"');";
						//util.executeUpdate(insert_into_skill_percentile);
						System.out.println(insert_into_skill_percentile);
					}
			//	}
			//}
		}
		
		System.out.println("completed");
		
		/*
		for(int i =22;i<=26;i++)
		{
			
			Course c= new CourseDAO().findById(i);
			for(Module mod : c.getModules())
			{
				for(Cmsession cms : mod.getCmsessions())
				{
					for(Lesson l : cms.getLessons())
					{
						if(l.getAssessment()!=null)
						{
							
							Assessment assessment= l.getAssessment();
							int total = assessment.getAssessmentQuestions().size();
							
							int assessment_id = assessment.getId();	
							String sql= "SELECT 	T1.student_id, 	T1.assessment_id, 	cast (T1.score as integer), 	CAST ( 		( 			( 				CUME_DIST () OVER (ORDER BY T1.score) 			) * 100 		) AS INTEGER 	) AS batch_percentile FROM 	( 		SELECT 			student_id, 			assessment_id, 			count(*) filter (where student_assessment.correct ='t') as score 		FROM 			student_assessment 		WHERE 			assessment_id = "+assessment_id+" 		GROUP BY 			student_id, 			assessment_id 	) T1 ORDER BY 	batch_percentile DESC, 	student_id ";
							List<HashMap<String, Object>> data = util.executeQuery(sql);
							for(HashMap<String, Object> row: data)
							{
								int student_id = (int)row.get("student_id");
								//System.out.println("student_id   "+student_id);
								if(sales_student.contains(student_id))
								{
									
									int score = (int)row.get("score");
									int accuracy = (int)(score*100/total);
									int batch_percentile = (int) row.get("batch_percentile");
									
									int skill_id = 0;
									
									
									
									for(int j=1; j<10;j++)
									{
										int random_month = getRandomInteger(2, 12);
										int random_day = getRandomInteger(1, 29);
										if(batch_percentile>20)
										{
											batch_percentile= batch_percentile-getRandomInteger(1, 10);
										}
										
										String insert_into_skill_percentile  ="INSERT INTO skill_precentile (student_id, skill_id, timestamp, percentile_country, percentile_globe, percentile_batch, percentile_organization, accuracy, industry_benchmark) "
												+ "VALUES ("+student_id+", '"+skill_id+"', '2016-"+random_month+"-"+random_day+" 17:16:09', '"+batch_percentile+"', '"+batch_percentile+"', '"+batch_percentile+"', '"+batch_percentile+"', '"+accuracy+"', '"+getRandomInteger(0, 20)+"');";
										//util.executeUpdate(insert_into_skill_percentile);
										System.out.println(insert_into_skill_percentile);
									}
									
									
									
									
									
									ArrayList<Integer> lobjadded = new ArrayList<>();
											
									for(AssessmentQuestion assessque : assessment.getAssessmentQuestions())
									{
										Question que = assessque.getQuestion();	 
										
										for(LearningObjective lobj :que.getLearningObjectives())
										{
											if(!lobjadded.contains(lobj.getId()))
											{
												lobjadded.add(lobj.getId());
												for(SkillLearningObjMapping skillLobjmap:	lobj.getSkillLearningObjMappings())
												{
													Skill skill = skillLobjmap.getSkill();
													int skill_id = skill.getId();
													int random_month = getRandomInteger(2, 12);
													int random_day = getRandomInteger(1, 29);
													System.out.println("skill id "+ skill.getId());
													
													String insert_into_skill_percentile  ="INSERT INTO skill_precentile (student_id, skill_id, timestamp, percentile_country, percentile_globe, percentile_batch, percentile_organization, accuracy, industry_benchmark) "
															+ "VALUES ("+student_id+", '"+skill_id+"', '2016-"+random_month+"-"+random_day+" 17:16:09', '"+batch_percentile+"', '"+batch_percentile+"', '"+batch_percentile+"', '"+batch_percentile+"', '"+accuracy+"', '"+getRandomInteger(0, 20)+"');";
													util.executeUpdate(insert_into_skill_percentile);
													//System.out.println(insert_into_skill_percentile);
													
												}
												
											}
											
											
											
										}
										
									
									
									}
									
								}
							}
							
							
 					 							
 							
 							
						}
					}
				}
			}
			
		}*/
		
	}

}
