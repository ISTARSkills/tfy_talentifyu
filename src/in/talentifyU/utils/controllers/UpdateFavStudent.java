package in.talentifyU.utils.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.publisher.utils.PublishDelegator;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class UpdateFavStudent
 */
@WebServlet("/update_fav")
public class UpdateFavStudent extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateFavStudent() {
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
		String trainer_id = request.getParameter("trainer_id");
		String student_id = request.getParameter("student_id");
		String favstatus = request.getParameter("favstatus");
		DBUTILS util = new DBUTILS();

		try {

			String sql1 = "select * from trainer_favourite where student_id=" + student_id + " and trainer_id = "
					+ trainer_id;
			if (util.executeQuery(sql1).size() > 0) {
				if (favstatus.equalsIgnoreCase("false")) {
					sql1 = "UPDATE trainer_favourite SET favourite_status='t' WHERE (student_id='" + student_id
							+ "' and trainer_id='" + trainer_id + "')";
				} else {
					sql1 = "UPDATE trainer_favourite SET favourite_status='f' WHERE (student_id='" + student_id
							+ "' and trainer_id='" + trainer_id + "')";
				}
				util.executeUpdate(sql1);
			} else {
				sql1 = "INSERT INTO trainer_favourite (student_id, trainer_id, favourite_status) VALUES ('" + student_id
						+ "', '" + trainer_id + "', '" + favstatus + "')";
				util.executeUpdate(sql1);
			}

			PublishDelegator pd = new PublishDelegator();
			pd.publish(Integer.parseInt(trainer_id));

		} catch (Exception e) {

			e.printStackTrace();
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
