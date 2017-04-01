package in.talentifyU.utils.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.DBUTILS;

/**
 * Servlet implementation class SavePhone
 */
@WebServlet("/SavePhone")
public class SavePhone extends HttpServlet {
	private Long phonenos;
	private int user_id;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SavePhone() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		phonenos = Long.parseLong(request.getParameter("phonenos"));
		user_id = Integer.parseInt(request.getParameter("user_id"));
		
		String sql = "update student set phone="+phonenos+" where id ="+user_id;
		System.out.println("sql "+sql);
		DBUTILS dbutils = new  DBUTILS();
		dbutils.executeUpdate(sql);
		response.getWriter().append("Sucessfull");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
