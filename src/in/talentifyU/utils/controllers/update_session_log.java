package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class update_session_log
 */
@WebServlet("/update_session_log")
public class update_session_log extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public update_session_log() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		printAttrs(request);
		printParams(request);
		/*
		 * param_name -> course_id : param_value ->3 param_name -> module_id :
		 * param_value ->32 param_name -> cmsession_id : param_value ->58
		 * param_name -> lesson_id : param_value ->343 param_name -> ppt_id :
		 * param_value ->343 param_name -> slide_id : param_value ->4192
		 * param_name -> event_id : param_value
		 * ->e85295ed-f4ba-4367-afdb-4f010279468d param_name -> user_id :
		 * param_value ->50
		 */
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		int module_id = Integer.parseInt(request.getParameter("module_id"));
		int cmsession_id = Integer.parseInt(request.getParameter("cmsession_id"));
		int lesson_id = Integer.parseInt(request.getParameter("lesson_id"));
		int ppt_id = Integer.parseInt(request.getParameter("ppt_id"));
		System.out.println(request.getParameter("curr_url").split("#")[1]);
		int slide_id = Integer.parseInt(request.getParameter("slide_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String event_id = request.getParameter("event_id");
		String curr_url = request.getParameter("curr_url");

		String sql = "select batch_id, event_name from batch_schedule_event where now() > eventdate AND now() < eventdate + ( (eventhour * 60 + eventminute) * INTERVAL '1 minute' ) and id ='"
				+ event_id + "'";
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		if (res.size() > 0 && !((String) res.get(0).get("event_name")).contains("EVENT FOR TESTING")) {
			int batch_id = 0;
			for (HashMap<String, Object> row : res) {
				batch_id = (int) row.get("batch_id");
			}
			Calendar cal = Calendar.getInstance();
			Date dt = cal.getTime();

			if (slide_id != 99999999) {
				String sql_for_log = "insert into event_session_log (trainer_id, event_id, course_id, cmsession_id, lesson_id, ppt_id, slide_id,created_at, updated_at, batch_id, module_id,assessment_id, lesson_type,url ) "
						+ "values(" + user_id + ",'" + event_id + "', " + course_id + "," + cmsession_id + ","
						+ lesson_id + "," + ppt_id + ", " + slide_id + ", '" + dt + "','" + dt + "'," + batch_id + ","
						+ module_id + ",0,'PPT','" + curr_url + "')";
				String sql_for_trianer_log = "insert into trainer_event_log (trainer_id, event_id, course_id, cmsession_id, lesson_id, ppt_id, slide_id,created_at, updated_at, batch_id, module_id, assessment_id, lesson_type, event_status,url ) "
						+ "values(" + user_id + ",'" + event_id + "', " + course_id + "," + cmsession_id + ","
						+ lesson_id + "," + ppt_id + ", " + slide_id + ", '" + dt + "','" + dt + "'," + batch_id + ","
						+ module_id + ",0,'PPT','SLIDE_CHANGED','" + curr_url + "')";

				util.executeUpdate(sql_for_log);
				util.executeUpdate(sql_for_trianer_log);
			}
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
