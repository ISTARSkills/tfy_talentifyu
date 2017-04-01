package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.istarindia.apps.dao.BatchScheduleEvent;
import com.istarindia.apps.dao.BatchScheduleEventDAO;
import com.istarindia.apps.service.WorkflowStatusService;
import com.viksitpro.core.dao.entities.TrainerFeedback;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class SubmitFeedback
 */
@WebServlet("/submit_feedback")
public class SubmitFeedback extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubmitFeedback() {
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
		int rating = Integer.parseInt(request.getParameter("rating"));
		String event_id = request.getParameter("event_id");
		String wrong_things = request.getParameter("wrong_things");
		String comments = request.getParameter("comments");
		wrong_things = StringUtils.stripEnd(wrong_things, ",");
		BatchScheduleEvent e = new BatchScheduleEventDAO().findById(UUID.fromString(event_id));
		String wrong_list[] = wrong_things.split(",");

		TrainerFeedback f = new TrainerFeedback();
		DBUTILS db = new DBUTILS();

		String sql_check = "select * from trainer_feedback where cast(event_id as varchar(50)) like '%" + event_id
				+ "%'";

		if (db.executeQuery(sql_check).size() > 0) {
			String delete_sql = "delete from trainer_feedback where cast(event_id as varchar(50)) like '%" + event_id
					+ "%'";
			db.executeUpdate(delete_sql);
		}

		for (String wrong : wrong_list) {
			if (wrong.trim().equalsIgnoreCase("noise")) {
				f.setNoise(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("attendance")) {
				f.setAttendance(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("sick")) {
				f.setSick(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("content")) {
				f.setContent(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("assignment")) {
				f.setAssignment(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("internals")) {
				f.setInternals(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("internet")) {
				f.setInternet(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("electricity")) {
				f.setElectricity(true);
				continue;
			} else if (wrong.trim().equalsIgnoreCase("time")) {
				f.setTime(true);
				continue;
			}
		}

		String sql = "INSERT INTO trainer_feedback (batch_id, event_id, user_id, rating, noise, attendance, sick, content, assignment, internals, internet, "
				+ "  electricity, time, comments) VALUES ('" + e.getBatch().getId() + "', '" + event_id + "', '"
				+ e.getActor().getId() + "', '" + rating + "', '" + f.getNoise() + "', '" + f.getAttendance() + "', '"
				+ f.getSick() + "', '" + f.getContent() + "', '" + f.getAssignment() + "', '" + f.getInternals()
				+ "', '" + f.getInternet() + "', '" + f.getElectricity() + "', '" + f.getTime() + "', '" + comments
				+ "')";
		db.executeUpdate(sql);
		new WorkflowStatusService().changeStatus(UUID.fromString(event_id), "COMPLETED", e.getActor().getId());

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
