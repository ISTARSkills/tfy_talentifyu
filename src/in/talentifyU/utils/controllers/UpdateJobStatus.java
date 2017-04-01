package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.istarindia.apps.service.WorkflowStatusService;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class UpdateJobStatus
 */
@WebServlet("/update_job_status")
public class UpdateJobStatus extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateJobStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printParams(request);
		WorkflowStatusService serv = new WorkflowStatusService();
		String event_id = request.getParameter("event_id");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String new_status =  request.getParameter("new_status");		
		serv.changeStatus(UUID.fromString(event_id), new_status, user_id);
				
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
