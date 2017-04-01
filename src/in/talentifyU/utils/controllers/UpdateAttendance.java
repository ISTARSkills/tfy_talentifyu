package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.istarindia.apps.dao.BatchScheduleEvent;
import com.istarindia.apps.dao.BatchScheduleEventDAO;
import com.viksitpro.core.dao.entities.BatchStudents;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class UpdateAttendance
 */
@WebServlet("/update_attendance")
public class UpdateAttendance extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateAttendance() {
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
		String student_ids = request.getParameter("student_ids");
		student_ids = StringUtils.stripEnd(student_ids, ",");
		System.out.println(student_ids);
		ArrayList<Integer> presentStudents = new ArrayList<>();
		for (String str : student_ids.split(",")) {
			if (!str.equalsIgnoreCase("")) {
				presentStudents.add(Integer.parseInt(str));
			}
		}

		DBUTILS util = new DBUTILS();
		BatchScheduleEvent ev = new BatchScheduleEventDAO().findById(UUID.fromString(event_id));
		String sql = "DELETE FROM attendance WHERE event_id='" + event_id + "';";
		util.executeUpdate(sql);

		for (BatchStudents s : ev.getBatch().getBatchGroup().getBatchStudentses()) {
			if (presentStudents.contains(s.getStudent().getId())) {
				sql = sql
						+ " INSERT INTO attendance ( taken_by, user_id, event_id, status,created_at, updated_at ) VALUES "
						+ " ( '" + ev.getActor().getId() + "', '" + s.getStudent().getId() + "', '" + event_id
						+ "', 'PRESENT', now(), now()); \n";
			} else {
				sql = sql
						+ " INSERT INTO attendance ( taken_by, user_id, event_id, status,created_at, updated_at ) VALUES "
						+ " ( '" + ev.getActor().getId() + "', '" + s.getStudent().getId() + "', '" + event_id
						+ "', 'ABSENT', now(), now()); \n";
			}
		}

		util.executeUpdate(sql);

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
