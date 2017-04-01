/**
 * 
 */
package in.talentifyU.utils.services;

/**
 * @author ComplexObject
 *
 */
public class TestStats {

	/**
	 * @param args
	 */
	
	public static int getRandomInteger(int maximum, int minimum){ return ((int) (Math.random()*(maximum - minimum))) + minimum; }

	//'2016-"+random_month+"-"+random_day+" 17:16:09'
	
	public static void main(String[] args) {
		
		
		
		
		
		// TODO Auto-generated method stub
		/*BatchGroup bg = new BatchGroupDAO().findById(98);
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
							int assessment_id = assessment.getId();	
							
							String sql ="SELECT 	T1.student_id, 	T1.question_id, 	T1.score, 	cast ((( 		CUME_DIST () OVER (ORDER BY T1.score) 	) * 100) as integer ) AS batch_percentile FROM 	( 		SELECT 			student_id, 			question_id, 			CASE WHEN correct='t' THEN 1 			else 0			 			END as score 			 		FROM 			student_assessment 		WHERE 			assessment_id = "+assessment_id+" 		GROUP BY 			student_id, 			question_id, score  	) T1 ORDER BY 	batch_percentile DESC, student_id";
							List<HashMap<String, Object>> data = util.executeQuery(sql);
							for(HashMap<String, Object> row: data)
							{
								int student_id = (int)row.get("student_id");
								if(sales_student.contains(student_id))
								{
									int random_month = getRandomInteger(2, 12);
									int random_day = getRandomInteger(1, 29);
									int question_id = (int)row.get("question_id");
									int batch_percentile = (int) row.get("batch_percentile");
									String insert_into_quest = "INSERT INTO stats_student_question ( student_id, question_id, percentile_batch, time_taken, percentile_organization, percentile_country, percentile_global, created_at) "
											+ "VALUES ( "+student_id+", "+question_id+", "+batch_percentile+", "+getRandomInteger(20,60)+", "+batch_percentile+", "+batch_percentile+", "+batch_percentile+", '2016-"+random_month+"-"+random_day+" 17:16:09');";
									util.executeUpdate(insert_into_quest);
									//System.out.println(insert_into_quest);
								}
							}
						}
					}
				}
			}
			
		}
		*/
		System.out.println("completed   ");
		

	}

}
