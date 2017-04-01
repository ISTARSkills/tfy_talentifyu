/**
 * 
 */
package in.talentifyU.utils.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.BatchGroup;
import com.viksitpro.core.dao.entities.BatchGroupDAO;
import com.viksitpro.core.dao.entities.BatchStudents;
import com.viksitpro.core.dao.entities.Cmsession;
import com.viksitpro.core.dao.entities.Course;
import com.viksitpro.core.dao.entities.CourseDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.utilities.DBUTILS;

/**
 * @author ComplexObject
 *
 */
public class TestStatAssessment {

	/**
	 * @param args
	 */
	public static int getRandomInteger(int maximum, int minimum){ return ((int) (Math.random()*(maximum - minimum))) + minimum; }

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
								System.out.println("student id"+student_id);
								if(sales_student.contains(student_id))
								{
									int random_month = getRandomInteger(2, 12);
									int random_day = getRandomInteger(1, 29);
									int score = (int)row.get("score");
									int accuracy = (int)(score*100/total);
									int batch_percentile = (int) row.get("batch_percentile");
									
								String insert_into_assess="INSERT INTO public.stats_student_assessment ( student_id, assessment_id, percentile_batch, time_taken, percentile_organization, percentile_country, percentile_global, accuracy, created_at) "
										+ "VALUES ( "+student_id+", "+assessment_id+", "+batch_percentile+", "+getRandomInteger(20,60)+", "+batch_percentile+", "+batch_percentile+", "+batch_percentile+", "+accuracy+", '2016-"+random_month+"-"+random_day+" 17:16:09');";
								util.executeUpdate(insert_into_assess);	
								//System.out.println(insert_into_assess);
								}
							}
						
 							
						}
					}
				}
			}
			
		}
		
		System.out.println("completed   ");
		
		
	}

}
