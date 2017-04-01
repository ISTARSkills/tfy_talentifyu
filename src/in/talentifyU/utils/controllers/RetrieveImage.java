package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.istarindia.apps.dao.Student;
import com.istarindia.apps.dao.StudentDAO;
import com.istarindia.apps.dao.StudentProfileData;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class RetrieveImage
 */
@WebServlet("/retrieve_image")
public class RetrieveImage extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RetrieveImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printParams(request);
		String url = request.getRequestURL().toString();
		String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		Student student = new StudentDAO().findById(Integer.parseInt(request.getParameter("user_id")));
		StudentProfileData profile  = student.getStudentProfileData();
		
		String image_url =baseURL+"video/android_images/ic_account_circle_black_48dp.png";
		if(profile.getProfileImage()!=null){
			image_url = baseURL+profile.getProfileImage();			
		}
		System.out.println(image_url);
		response.setContentType("application/text;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(image_url.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
