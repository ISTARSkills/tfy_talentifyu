package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.istarindia.apps.dao.JobsEvent;
import com.istarindia.apps.dao.JobsEventDAO;
import com.istarindia.apps.dao.Student;
import com.istarindia.apps.dao.Vacancy;
import com.istarindia.apps.dao.VacancyDAO;
import com.viksitpro.core.utilities.IStarBaseServelet;

@WebServlet("/StudentActivityForJob")
public class StudentActivityForJobController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	public StudentActivityForJobController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestJobsEventId = request.getParameter("event_id");
		String requestStageName = request.getParameter("stage_name");
		String requestStatus = request.getParameter("new_status");

		JobsEventDAO jobsEventDAO = new JobsEventDAO();
		JobsEvent jobsEvent = jobsEventDAO.findById(UUID.fromString(requestJobsEventId));

		if(requestStageName.equalsIgnoreCase("Applied") && requestStatus.equalsIgnoreCase("ACCEPTED")){
		promoteStudent("Applied", jobsEvent.getVacancy(), jobsEvent.getActor(), jobsEvent);
		}else if(requestStageName.equalsIgnoreCase("Offered") && requestStatus.equalsIgnoreCase("ACCEPTED")){
			markJobsEventAsInactive(jobsEvent);
		}else{
			markJobsEventAsInactive(jobsEvent);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void promoteStudent(String state, Vacancy vacancy, Student student, JobsEvent jobsEvent) {
		JobsEvent newJobsEvent = createJobsEventForStudent(Calendar.getInstance().getTime(), 1, 30,
				vacancy.getRecruiter().getId(), vacancy.getId(), jobsEvent.getTaskId().toString(), state, student);

		if (newJobsEvent != null) {
			markJobsEventAsInactive(jobsEvent);
		}
	}

	private void markJobsEventAsInactive(JobsEvent jobsEvent) {
		jobsEvent.setIsactive(false);
		updateJobsEventToDAO(jobsEvent);
	}

	private JobsEvent updateJobsEventToDAO(JobsEvent jobsEvent) {

		JobsEventDAO jobsEventDAO = new JobsEventDAO();

		Session jobsEventSession = jobsEventDAO.getSession();

		Transaction jobsEventTransaction = null;
		try {
			jobsEventTransaction = jobsEventSession.beginTransaction();
			jobsEventDAO.attachDirty(jobsEvent);
			jobsEventTransaction.commit();
		} catch (HibernateException e) {
			if (jobsEventTransaction != null)
				jobsEventTransaction.rollback();
			e.printStackTrace();
		} finally {
			jobsEventSession.flush();
			jobsEventSession.close();
		}

		return jobsEvent;
	}

	private JobsEvent createJobsEventForStudent(Date eventDate, int eventHour, int eventMinutes, int creatorID,
			int vacancy_id, String task_id, String jobStage, Student student) {

		VacancyDAO vacancyDAO = new VacancyDAO();
		Vacancy vacancy = vacancyDAO.findById(vacancy_id);

		JobsEvent jobsEvent = new JobsEvent();
		JobsEventDAO jobsEventDAO = new JobsEventDAO();

		Date current = (Date) Calendar.getInstance().getTime();

		jobsEvent.setCreatedAt(current);
		jobsEvent.setType("JOBS_EVENT");
		jobsEvent.setUpdatedAt(current);
		jobsEvent.setEventdate(eventDate);
		jobsEvent.setEventhour(eventHour);
		jobsEvent.setEventminute(eventMinutes);
		jobsEvent.setIsactive(true);
		jobsEvent.setCreatorId(creatorID);
		jobsEvent.setActor(student);
		jobsEvent.setStatus(jobStage);
		jobsEvent.setVacancy(vacancy);
		jobsEvent.setTaskId(java.util.UUID.fromString(task_id));

		Session jobsEventSession = jobsEventDAO.getSession();

		Transaction jobsEventTransaction = null;
		try {
			jobsEventTransaction = jobsEventSession.beginTransaction();
			jobsEventDAO.save(jobsEvent);
			jobsEventTransaction.commit();
		} catch (HibernateException e) {
			if (jobsEventTransaction != null)
				jobsEventTransaction.rollback();
			e.printStackTrace();
		} finally {
			jobsEventSession.flush();
			jobsEventSession.close();
		}
		return jobsEvent;
	}
}
