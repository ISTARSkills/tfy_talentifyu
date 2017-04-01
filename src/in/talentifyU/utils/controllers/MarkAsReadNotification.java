package in.talentifyU.utils.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.publisher.utils.PublishDelegator;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class MarkAsReadNotification
 */
@WebServlet("/mark_as_read")
public class MarkAsReadNotification extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MarkAsReadNotification() {
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

		String notification_id = request.getParameter("notification_id");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		PublishDelegator pd = new PublishDelegator();
		pd.publishNotificationChange(user_id, notification_id);

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
