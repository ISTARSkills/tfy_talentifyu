package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.IStarBaseServelet;

import websocket.server.CustomEndPoint;

/**
 * Servlet implementation class GetEventQueueInfo
 */
@WebServlet("/get_event_queue_info")
public class GetEventQueueInfo extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEventQueueInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printParams(request);
		String eventQString = request.getParameter("eventQ");
		
		int user_size = 0;
		
		ArrayList<String> eventQList = new ArrayList<>();
		ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, javax.websocket.Session>> eq = CustomEndPoint.event_user_queue;
		
		String finaResponse="";
		String[] eventQ_split = eventQString.split("!!!");
		
		for(String eventId : eventQ_split ){
			//eventQList.add(eventId);
			if(eq.containsKey(UUID.fromString(eventId)))
			{
				ConcurrentHashMap<Integer, javax.websocket.Session> userque = eq.get(UUID.fromString(eventId));
				 user_size = userque.size();
				 finaResponse+=eventId+"!!!"+user_size+"!!!!";
			}
			else{finaResponse+=eventId+"!!!0!!!!";}
				
			
			}
		    //finaResponse+="";

			response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.print(finaResponse);

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
