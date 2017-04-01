package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.istarindia.apps.dao.BatchScheduleEvent;
import com.istarindia.apps.dao.BatchScheduleEventDAO;
import com.istarindia.apps.service.WorkflowStatusService;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class UpdateWorkflowStatus
 */
@WebServlet("/update_workflow_status")
public class UpdateWorkflowStatus extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateWorkflowStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		printParams(request);
		String event_id = request.getParameter("event_id");
		String new_status = request.getParameter("new_status");
		BatchScheduleEvent eve = new BatchScheduleEventDAO().findById(UUID.fromString(event_id));
		DBUTILS util = new DBUTILS();
		Calendar cal = Calendar.getInstance();
		Date dt = cal.getTime();
		String sql_for_log = "insert into trainer_event_log (trainer_id, event_id, course_id, cmsession_id, lesson_id, ppt_id, slide_id,created_at, updated_at, batch_id, module_id, assessment_id, lesson_type, event_status ) "
				+ "values(" + eve.getActor().getId() + ",'" + event_id + "', " + 0 + "," + 0 + "," + 0 + "," + 0 + ", "
				+ 0 + ", '" + dt + "','" + dt + "'," + eve.getBatch().getId() + "," + 0 + ", " + 0
				+ ",'STATUS_CHANGED','" + new_status + "')";
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				util.executeUpdate(sql_for_log);
			}
		});

		t1.start();
		new WorkflowStatusService().changeStatus(UUID.fromString(event_id), new_status, eve.getActor().getId());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
