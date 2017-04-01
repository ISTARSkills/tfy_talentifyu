package in.talentifyU.auth.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.AppResponseObject;
import in.talentifyU.complexData.pojo.XMLStudent;
import in.talentifyU.complexData.services.StudentInitializer;
import in.talentifyU.utils.services.GoogleUserService;

/**
 * Servlet implementation class LoginGoogleUser
 */
@WebServlet("/login_google_user")
public class LoginGoogleUser extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginGoogleUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String deployment_type = "local";

		try {
			Properties properties = new Properties();
			String propertyFileName = "app.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName);
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propertyFileName + "' not found in the classpath");
			}

			deployment_type = properties.getProperty("deployment_type");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		printParams(request);
		long iii = System.currentTimeMillis();
		AppResponseObject obj = new AppResponseObject();
		XMLStudent sxs = new XMLStudent();
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		System.err.println("Login Time take in seconds -> " + (System.currentTimeMillis() - iii) / 1000);

		if (request.getParameterMap().containsKey("email")) 
		{
			
				IstarUserDAO dao = new IstarUserDAO();
				List<IstarUser> userList = dao.findByEmail(request.getParameter("email").toString().trim().toLowerCase()); 
				int google_user_id =0;
				if(userList.size()>0)
				{
					//user already exist
					IstarUser user = dao.findByEmail(request.getParameter("email").toString().trim().toLowerCase()).get(0);
					if(user.getUserRoles().contains(new String("STUDENT"))|| user.getUserRoles().contains("TRAINER"))
					{
						google_user_id = user.getId();
					}
					else
					{
						google_user_id=-1;
					}	
				}
				else
				{
					//user do not exist, create a new user 
					String email = request.getParameter("email").toString().trim().toLowerCase();
					String name = request.getParameter("name").toString().trim().toLowerCase();
					String user_type="STUDENT";
					if(request.getParameterMap().containsKey("user_type"))
					{
						user_type = request.getParameter("user_type").toString().trim();
					}
					 google_user_id = new GoogleUserService().createGoogleUser(email, name, user_type);
				}
								
				if(google_user_id!=-1)
				{
					try {

						
						StudentInitializer si = new StudentInitializer();
						sxs = si.initializeStudent(google_user_id, request);

						obj.setResponseCode("200");
						obj.setResponseMessage("SUCCESS");

					} catch (NullPointerException e) {
						e.printStackTrace();
						obj.setResponseCode("500");
						obj.setResponseMessage("Somthing wrong with your User Details. Please contact Administrator.");
					} 
				}
				else
				{
					obj.setResponseCode("200");
					obj.setResponseMessage("This email already belongs to an Istar Internal User.");
				}	
				
			

		} 
		else {
			obj.setResponseCode("200");
			obj.setResponseMessage("Missing Username or password");
		}

		try {

			// File file = new File("C:\\file.xml");

			JAXBContext jaxbContext = JAXBContext.newInstance(AppResponseObject.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			obj.setStudent(sxs);
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// of.setCDataElements(obj);
			jaxbMarshaller.marshal(obj, out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.err.println("Organization Time take in seconds -> " + (System.currentTimeMillis() - iii) / 1000);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
