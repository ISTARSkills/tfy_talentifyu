package in.talentifyU.auth.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.AppResponseObject;
import in.talentifyU.complexData.pojo.XMLStudent;
import in.talentifyU.complexData.services.StudentInitializer;
import in.talentifyU.utils.services.FetchAppDataService;

/**
 * Servlet implementation class AuthController
 */
public class AuthController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	
	@Override
	public  void init(ServletConfig config) throws ServletException {
		//SkillHolder.createTree();
	}
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		printParams(request);
		long iii = System.currentTimeMillis();
		request.getSession().removeAttribute("user");
		AppResponseObject obj = new AppResponseObject();
		XMLStudent sxs = new XMLStudent();
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		if (request.getParameterMap().containsKey("email") && request.getParameterMap().containsKey("password")) {
			try {
				IstarUserDAO dao = new IstarUserDAO();
				IstarUser user = dao.findByEmail(request.getParameter("email").toString().trim().toLowerCase()).get(0);
				System.err.println("Login Time take in seconds -> " + (System.currentTimeMillis() - iii) / 1000);

				if (user.getPassword().equals(request.getParameter("password"))) {
					try {

						
						if(request.getParameter("type") != null && !request.getParameter("type").toString().equalsIgnoreCase("") ){
							response.setContentType("application/json");
							new FetchAppDataService().sendData(request.getParameter("type").toString(),user.getId(),response,request);
						}else{
							StudentInitializer si = new StudentInitializer();

						sxs = si.initializeStudent(user.getId(), request);
						obj.setResponseCode("200");
						obj.setResponseMessage("SUCCESS");
						}

						

					} catch (NullPointerException e) {
						e.printStackTrace();
						obj.setResponseCode("500");
						obj.setResponseMessage("Somthing wrong with your User Details. Please contact Administrator.");
					}

				} else {
					obj.setResponseCode("200");
					obj.setResponseMessage("Wrong Username or password");

				}
			} catch (java.lang.IndexOutOfBoundsException e) {
				e.printStackTrace();
				obj.setResponseCode("200");
				obj.setResponseMessage("Missing Username or password");
			}

		} else {
			obj.setResponseCode("200");
			obj.setResponseMessage("Missing Username or password");
		}

		try {

			// File file = new File("C:\\file.xml");
			if(request.getParameter("type") == null ){

			JAXBContext jaxbContext = JAXBContext.newInstance(AppResponseObject.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			obj.setStudent(sxs);
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// of.setCDataElements(obj);
			jaxbMarshaller.marshal(obj, out);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.err.println("Organization Time take in seconds -> " + (System.currentTimeMillis() - iii) / 1000);
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
