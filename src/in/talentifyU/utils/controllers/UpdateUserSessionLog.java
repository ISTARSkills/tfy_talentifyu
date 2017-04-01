package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.DBUTILS;

/**
 * Servlet implementation class update_user_session_log
 */
@WebServlet("/update_user_session_log")
public class UpdateUserSessionLog extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserSessionLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		int module_id = Integer.parseInt(request.getParameter("module_id"));
		int cmsession_id = Integer.parseInt(request.getParameter("cmsession_id"));
		int lesson_id = Integer.parseInt(request.getParameter("lesson_id"));
		int ppt_id = Integer.parseInt(request.getParameter("ppt_id"));
		int slide_id = Integer.parseInt(request.getParameter("slide_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String curr_url = request.getParameter("curr_url");

		DBUTILS util = new DBUTILS();

		Calendar cal = Calendar.getInstance();
		Date dt = cal.getTime();

		if (slide_id != 99999999) {
			String sql_for_log = "insert into user_session_log (user_id, course_id, cmsession_id, lesson_id, ppt_id, slide_id,created_at, updated_at, module_id,assessment_id, lesson_type,url ) "
					+ "values(" + user_id + ", " + course_id + "," + cmsession_id + "," + lesson_id + "," + ppt_id
					+ ", " + slide_id + ", '" + dt + "','" + dt + "', " + module_id + ",0,'PPT','" + curr_url + "')";

			util.executeUpdate(sql_for_log);

		}
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
