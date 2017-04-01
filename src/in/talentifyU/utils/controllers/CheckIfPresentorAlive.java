package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.utils.services.EventUtils;

/**
 * Servlet implementation class CheckIfPresentorAlive
 */
@WebServlet("/check_if_presentor_alive")
public class CheckIfPresentorAlive extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckIfPresentorAlive() {
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
		int user_id = Integer.parseInt(request.getParameter("user_id"));

		String color = new EventUtils().isPresentorAlive(user_id, event_id);
		response.setHeader("Content-Type", "text/plain");
		PrintWriter out = response.getWriter();
		out.write(color);
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
