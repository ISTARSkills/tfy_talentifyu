/**
 * 
 */
package in.talentifyU.utils.services;

import java.util.HashMap;
import java.util.List;

import com.istarindia.apps.dao.LearningObjective;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.Cmsession;
import com.viksitpro.core.dao.entities.Course;
import com.viksitpro.core.dao.entities.CourseDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.utilities.DBUTILS;

/**
 * @author Vaibhav
 *
 */
public class testLObj {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBUTILS util = new DBUTILS();
	
	List<Course> courses = new CourseDAO().findAll();
	for(Course c: courses)
	{
		String course_name = c.getCourseName().trim();
		String sql ="insert into skill (skill_title, parent_skill) values('"+course_name+"', 5); ";
		System.out.println(sql);	
		util.executeUpdate(sql);;
		String getParentskill ="select id from skill where skill_title ='"+course_name+"' limit 1;";		
		
		List<HashMap<String, Object>> moddata =  util.executeQuery(getParentskill);
		int parent_skill_id = (int)moddata.get(0).get("id");
		
		for(Module mod : c.getModules())
		{		
			String module_name = mod.getModuleName().trim();
			String sql_module ="insert into skill (skill_title, parent_skill) values('"+module_name+"', "+parent_skill_id+"); ";
			System.out.println(sql_module);
			util.executeUpdate(sql_module);;
			String getParentskill2 ="select id from skill where skill_title ='"+module_name+"' limit 1;";		
			List<HashMap<String, Object>> sessiondata =  util.executeQuery(getParentskill2);
			int parent_skill_id2 = (int)sessiondata.get(0).get("id");
			
			for(Cmsession cms : mod.getCmsessions())
			{
				String cmsname= cms.getTitle().trim();
				String sql_session ="insert into skill (skill_title, parent_skill) values('"+cmsname+"', "+parent_skill_id2+"); ";
				System.out.println(sql_session);
				util.executeUpdate(sql_session);;
				
			}
		}	
		
	}
	
	
	
	try {
		List<Assessment> assessments = new AssessmentDAO().findAll();
		for(Assessment assess : assessments)
		{
			Lesson lesson  = assess.getLesson();
			for(LearningObjective lobj : lesson.getLearningObjectives())
			{
				int lobj_id = lobj.getId();
				for(AssessmentQuestion assessque : assess.getAssessmentQuestions())
				{
					int question_id = assessque.getQuestion().getId();
					String check = "select * from learning_objective_question where learning_objectiveid="+lobj_id+" and questionid="+question_id+";";
					if(util.executeQuery(check).size()==0)
					{
						String lobj_que = "insert into learning_objective_question (learning_objectiveid,questionid) values ("+lobj_id+","+question_id+");";						
						util.executeUpdate(lobj_que);
						//System.out.println(lobj_que);
					}							
				}
			
				String getSkillId ="select id from skill where skill_title = '"+assess.getLesson().getCmsession().getTitle().trim()+"' limit 1; ";
				//System.out.println(getSkillId);
				if(util.executeQuery(getSkillId).size()>0)
				{
					int parent_skill_id = (int)util.executeQuery(getSkillId).get(0).get("id"); 						
					String lobj_skill ="insert into skill_learning_obj_mapping (learning_objective_id,skill_id) values("+lobj_id+","+parent_skill_id+");";
					util.executeUpdate(lobj_skill);
					
				}
				
				
				//System.out.println(lobj_skill);
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
		
	}

}
