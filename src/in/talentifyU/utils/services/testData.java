package in.talentifyU.utils.services;

import java.util.HashMap;
import java.util.List;

import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.dao.entities.QuestionDAO;
import com.viksitpro.core.utilities.DBUTILS;

public class testData {

	public static int getRandomInteger(int maximum, int minimum){ return ((int) (Math.random()*(maximum - minimum))) + minimum; }

	
	public static void main(String[] args) {
		
		DBUTILS util  = new DBUTILS();
		String sql="select distinct id from skill";
		List<HashMap<String,Object>> data = util.executeQuery(sql);
		int i=0;
		for(HashMap<String,Object> row : data )
		{
			i++;
			int skill_id = (int) row.get("id");
			int random_month = getRandomInteger(2, 12);
			int random_day = getRandomInteger(1, 29);
			String insert_into_skill_percentile  ="INSERT INTO skill_precentile (student_id, skill_id, timestamp, percentile_country, percentile_globe, percentile_batch, percentile_organization, accuracy, industry_benchmark) "
					+ "VALUES ('18', '"+skill_id+"', '2016-"+random_month+"-"+random_day+" 17:16:09', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"');";
			util.executeUpdate(insert_into_skill_percentile);
			System.out.println(insert_into_skill_percentile);
		
		}
		
		List<Question> questions =	new QuestionDAO().findAll();
		
		for(Question q : questions)
		{
			int random_month = getRandomInteger(2, 12);
			int random_day = getRandomInteger(1, 29);
			String insert_into_question_stats= "INSERT INTO stats_student_question (student_id, question_id, percentile_batch, time_taken, percentile_organization, percentile_country, percentile_global, created_at) "
					+ "VALUES ( 18, "+q.getId()+", '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 10)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '2016-"+random_month+"-"+random_day+" 17:16:09');";
			util.executeUpdate(insert_into_question_stats);
			System.out.println(insert_into_question_stats);
		}
		
		List<Assessment> assessments = new AssessmentDAO().findAll();
		for(Assessment assess : assessments)
		{
			int random_month = getRandomInteger(2, 12);
			int random_day = getRandomInteger(1, 29);
			String insert_into_assessment_stats= "INSERT INTO stats_student_assessment ( student_id, assessment_id, percentile_batch, time_taken, percentile_organization, percentile_country, percentile_global, accuracy, created_at) "
					+ "VALUES ( 18, "+assess.getId()+", '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 30)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '"+getRandomInteger(1, 100)+"', '2016-"+random_month+"-"+random_day+" 17:16:09');";
			util.executeUpdate(insert_into_assessment_stats);
			System.out.println(insert_into_assessment_stats);
		}
		System.out.println("count of id"+i);
		
			
	}

}
