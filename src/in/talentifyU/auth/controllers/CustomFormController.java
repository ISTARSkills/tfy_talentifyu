package in.talentifyU.auth.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.ProfessionalProfile;
import com.viksitpro.core.dao.entities.UserProfile;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.AppResponseObject;
import in.talentifyU.complexData.pojo.XMLStudent;
import in.talentifyU.complexData.services.StudentInitializer;
import in.talentifyU.metadata.services.MetadataServices;

/**
 * Servlet implementation class CustomFormController
 */
@WebServlet("/user_form")
public class CustomFormController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public CustomFormController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		printAttrs(request);
		printParams(request);
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
		MetadataServices serv = new MetadataServices();
		long statrt1 = System.currentTimeMillis();
		System.out.println("start1---"+statrt1);
		IstarUser student = new IstarUserDAO().findById(Integer.parseInt(request.getParameter("user_id")));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (request.getParameter("type").equalsIgnoreCase("create_update")) {
			String firstname = request.getParameter("first_name") !=null ? request.getParameter("first_name"):"";
			String lastname = request.getParameter("last_name") !=null ? request.getParameter("last_name"):"";
			String email = request.getParameter("email_id") !=null ? request.getParameter("email_id"):"";
			Long mobileno = request.getParameter("phone") !=null ? Long.parseLong(request.getParameter("phone")):0l;
			Integer pincode = request.getParameter("pincode") !=null ? Integer.parseInt(request.getParameter("pincode")):0;
			Long aadharno = request.getParameter("aadhaar") !=null ? Long.parseLong(request.getParameter("aadhaar")):0l;
			String dob = "";
			
			dob	 = request.getParameter("dob") !=null ? "'"+request.getParameter("dob").toString()+"'":"NULL";
			String gender = request.getParameter("gender") !=null ? request.getParameter("gender"):null;

			
			Float marks_10 = request.getParameter("10th_marks") !=null ? Float.parseFloat(request.getParameter("10th_marks")):0;
			Integer yop_10 = request.getParameter("10th_yop") !=null ? Integer.parseInt(request.getParameter("10th_yop")):0;
			Float marks_12 = request.getParameter("12th_marks") !=null ?  Float.parseFloat(request.getParameter("12th_marks")):0;
			Integer yop_12 = request.getParameter("12th_yop") !=null ? Integer.parseInt(request.getParameter("12th_yop")):0;

			
			
			
			boolean has_under_graduation = request.getParameter("graduation") !=null ? Boolean.parseBoolean(request.getParameter("graduation")):false;
			String degree_name = request.getParameter("degree_name") !=null ? request.getParameter("degree_name"):" ";
			String degree_specialization_name =request.getParameter("degree_specialization_name") !=null ? request.getParameter("degree_specialization_name"):" ";
			Float under_gradution_marks = request.getParameter("graduation_aggregate") !=null ? Float.parseFloat(request.getParameter("graduation_aggregate")):0;
			
			boolean has_post_graduation = request.getParameter("post_graduation") !=null ? Boolean.parseBoolean(request.getParameter("post_graduation")):false;
			String post_graduation_name = request.getParameter("post_graduation_name") !=null ? request.getParameter("post_graduation_name"):" ";
			String post_graduation_specialization = request.getParameter("post_graduation_specialization") !=null ? request.getParameter("post_graduation_specialization"):" ";
			Float post_gradution_marks = request.getParameter("post_graduation_aggregate") !=null ? Float.parseFloat(request.getParameter("post_graduation_aggregate")):0;
			
			String job_sector = request.getParameter("sector") !=null ? request.getParameter("sector"):" ";
			String preferred_location = request.getParameter("prefered_location") !=null ? request.getParameter("prefered_location"):" ";

			String company_name = request.getParameter("companmy_name") !=null ? request.getParameter("companmy_name"):" ";
			String description = request.getParameter("description") !=null ? request.getParameter("description"):" ";
			String duration = request.getParameter("duration") !=null ? request.getParameter("duration"):" ";
			String position = request.getParameter("position") !=null ? request.getParameter("position"):" ";

			boolean is_studying_further_after_degree = request.getParameter("further_studies") !=null ? Boolean.parseBoolean(request.getParameter("further_studies")):false;
			String interested_in_type_of_course = request.getParameter("interested_short_term_courses") !=null ? request.getParameter("interested_short_term_courses"):null;
			String area_of_interest = request.getParameter("area_of_interest") !=null ? request.getParameter("area_of_interest"):null;
			String resume=request.getParameter("resume") !=null ? request.getParameter("resume"):"";;
			
			long statrt2 = System.currentTimeMillis()-statrt1;
			System.out.println("start2---"+statrt2);

			serv.saveStudentProfile(student, firstname, lastname, dob, mobileno, gender, pincode, aadharno, email, yop_10, marks_10, yop_12, marks_12,
					has_under_graduation, degree_specialization_name, under_gradution_marks, has_post_graduation, post_graduation_specialization,
					post_gradution_marks, is_studying_further_after_degree, job_sector, preferred_location, company_name, position, duration, description,
					interested_in_type_of_course, area_of_interest,degree_name, post_graduation_name);
			
			long statrt3 = System.currentTimeMillis()-statrt2;
			System.out.println("statrt3---"+statrt3);
			
			// return the comples object as a response
			
			AppResponseObject obj = new AppResponseObject();
			PrintWriter out = response.getWriter();
			XMLStudent sxs = new XMLStudent();
			response.setContentType("text/xml");
			try {

				//PublishDelegator p = new PublishDelegator();
				//String data = p.getUserData(student.getId());
				//if (data != null && data != "" && deployment_type.equalsIgnoreCase("production")) {
					// System.out.println(data);
					/*StringReader reader = new StringReader(data);

					JAXBContext jaxbContext = JAXBContext.newInstance(AppResponseObject.class);

					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					obj = (AppResponseObject) jaxbUnmarshaller.unmarshal(reader);
					sxs = obj.getStudent();*/
				//} else {
					StudentInitializer si = new StudentInitializer();
					sxs = si.initializeStudent(student.getId(), request);
				//}

				obj.setResponseCode("200");
				obj.setResponseMessage("SUCCESS");

			} catch (NullPointerException e) {
				e.printStackTrace();
				obj.setResponseCode("500");
				obj.setResponseMessage("Somthing wrong with your User Details. Please contact Administrator.");
			}
			
			long statrt4 = System.currentTimeMillis()-statrt3;
			System.out.println("statrt4---"+statrt4);
			
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
			
			long statrt5 = System.currentTimeMillis()-statrt4;
			System.out.println("statrt5---"+statrt5);
		} 
			
			
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	

	

}