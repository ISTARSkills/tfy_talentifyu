/**
 * 
 */
package in.talentifyU.utils.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.istarindia.apps.IstarEventTypes;
import com.istarindia.apps.IstarTaskTypes;
import com.istarindia.apps.dao.IstarAssessmentEvent;
import com.istarindia.apps.dao.IstarAssessmentEventDAO;
import com.istarindia.apps.dao.IstarTaskType;
import com.istarindia.apps.dao.IstarTaskTypeDAO;
import com.istarindia.apps.dao.Student;
import com.istarindia.apps.dao.StudentDAO;
import com.istarindia.apps.service.WorkflowConcreteService;
import com.istarindia.apps.service.WorkflowStatusService;
import com.viksitpro.chat.services.NotificationService;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.BatchGroup;
import com.viksitpro.core.dao.entities.BatchGroupDAO;
import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.utilities.DBUTILS;

/**
 * @author ComplexObject
 *
 */
public class GoogleUserService {

	public int createGoogleUser(String email, String name, String Utype)
	{
		int id=0;
		try {
			DBUTILS util = new DBUTILS();
			IstarUserDAO dao = new IstarUserDAO();
			ArrayList<Integer> userids = new ArrayList<>();
			List<IstarUser> userss = dao.findAll();
			for(IstarUser user : userss)
			{
				userids.add(user.getId());
			}
			
			id = Collections.max(userids)+1;
			String insert_sql = "INSERT INTO student ( 	id, 	email, 	gender, 	is_verified, 	mobile, 	name, 	password, 	"
					+ "user_type, 	father_name, 	mother_name, 	address_id, 	organization_id , phone	 ) "
					+ "VALUES 	("+id+", 		'"+email+"', 		'MALE', 		't', 		9999999999, 		'"+name+"', 		'test123', 		'"+Utype+"', 		'Not Provided', 		'Not Provided', 		2, 	2,9999999999 ) ";
			util.executeUpdate(insert_sql);
			List<BatchGroup> bgs = new BatchGroupDAO().findByName("GOOGLE_BATCH_GROUP");
			if(bgs.size()>0)
			{
				BatchGroup googlebg = bgs.get(0);
				String add_in_batch = "insert into batch_students (id,student_id, batch_group_id, user_type) values ((SELECT max(id) +1 FROM batch_students ),"+id+","+bgs.get(0).getId()+", '"+Utype+"')";
				util.executeUpdate(add_in_batch);
				
				if(googlebg.getAssessmentId()!= null && googlebg.getAssessmentId()!=0)
				{
					createAssessmentEvent(googlebg,id, googlebg.getAssessmentId());
				}
				
			}
			
			if(Utype.equalsIgnoreCase("TRAINER"))
			{
				ArrayList<Integer> ts = new ArrayList<>();
				List<IstarUser> trainers = dao.findAll();
				for(IstarUser user : trainers)
				{
					ts.add(user.getId());
				}
				int pres_id = Collections.max(ts)+1;
				 String update_TP_mapping_sql = "INSERT INTO trainer_presentor ( id, trainer_id, 	presentor_id ) VALUES 	((SELECT max(id) +1 FROM trainer_presentor ), "+id+", "+pres_id+")";				
				 util.executeUpdate(update_TP_mapping_sql);
			}
		} catch (Exception e) {
			System.out.println("error in google user service");
		}
		
		return  id;
	}

	public void createAssessmentEvent(BatchGroup googlebg, int student_id, Integer assessmentId) {
		Student stu = new StudentDAO().findById(student_id);
		if(googlebg.getBatchs().size()>0)
		{
			Batch bb= new Batch();
			for(Batch b: googlebg.getBatchs())
			{
				bb= b;
				break;
			}
			Assessment assessment = new AssessmentDAO().findById(assessmentId);		
			IstarAssessmentEvent be = new IstarAssessmentEvent();
			IstarAssessmentEventDAO dao = new IstarAssessmentEventDAO();
			be.setAssessment(assessment);
			be.setBatchId(bb.getId());
			UUID  parent_id = UUID.randomUUID();
			be.setParentEventId(parent_id);
			be.setActor(stu);
			Calendar cal = Calendar.getInstance();
			be.setCreatedAt(cal.getTime());
			be.setCreatorId(stu.getId());
			be.setEventdate(cal.getTime());
			be.setEventhour(0);
			be.setEventminute(assessment.getAssessmentdurationminutes());
			be.setIsactive(true);
			IstarTaskType itt = new IstarTaskType();
			IstarTaskTypeDAO ittdao = new IstarTaskTypeDAO();
			itt.setType(IstarTaskTypes.ASSESSMENT_TASK);
			if (ittdao.findByExample(itt).size() > 0) {
				itt = ittdao.findByExample(itt).get(0);
			}
			be.setTaskId(itt.getId());
			be.setStatus(new WorkflowStatusService().getInitialStatus(itt.getId()));
			be.setType(IstarEventTypes.ISTAR_ASSESSMENT_EVENT);
			be.setUpdatedAt(cal.getTime());
			
			Session session = dao.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.save(be);
				
				tx.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			

			new WorkflowConcreteService().createConcreteBatchScheduleWorkflow(be.getTaskId(), be.getId());
			
			new WorkflowStatusService().createWorkflowStatus(be.getId(), be.getActor().getId());
			
			int receiver_id = stu.getId();
			int sender_id = stu.getId();
			DateFormat todate ;
			   todate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String details = "Scheduled Assessment of  " + assessment.getLesson().getTitle()  + " at "+todate.format(be.getEventdate())+" . Duration of assessment: " +assessment.getAssessmentdurationminutes()+ " mins";			
			String title = "Assessment of  " + assessment.getLesson().getTitle()+" has been scheduled." ;				
			String action = be.getAction();
			String type = be.getType();
			new NotificationService().createEventBasedNotification(details, be.getId(), receiver_id, sender_id, title, type,
					action);
			System.out.println("assessment created");
		}
		else
		{
			System.out.println("no batch in this batch group");
		}	
		
		
	}
	
}
