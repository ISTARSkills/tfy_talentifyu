package in.talentifyU.offline.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.dao.entities.Slide;
import com.viksitpro.core.dao.entities.SlideDAO;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class UpdateLastSlidePointer
 */
@WebServlet("/update_last_slide_pointer")
public class UpdateLastSlidePointer extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateLastSlidePointer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printParams(request);
		//int ppt_id = Integer.parseInt(request.getParameter("ppt_id"));
		int slide_id = Integer.parseInt(request.getParameter("slide_id"));
		Slide s = new SlideDAO().findById(slide_id);
		int ppt_id = s.getPresentation().getId();
		int course_id = s.getPresentation().getLesson().getCmsession().getModule().getCourse().getId();
		int module_id = s.getPresentation().getLesson().getCmsession().getModule().getId();
		int cmsession_id = s.getPresentation().getLesson().getCmsession().getId();
		int lesson_id = s.getPresentation().getLesson().getId();
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String curr_url = "not_available_in_android";
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
