package in.talentifyU.auth.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import com.viksitpro.core.dao.entities.BatchGroup;
import com.viksitpro.core.dao.entities.BatchGroupDAO;
import com.viksitpro.core.dao.entities.BatchStudents;
import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.AppResponseObject;
import in.talentifyU.complexData.pojo.XMLStudent;
import in.talentifyU.complexData.services.StudentInitializer;
import in.talentifyU.utils.services.GoogleUserService;

/**
 * Servlet implementation class UpdateBatchForGoogleUser
 */
@WebServlet("/update_bg_google_user")
public class UpdateBatchForGoogleUser extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateBatchForGoogleUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		AppResponseObject obj = new AppResponseObject();
		XMLStudent sxs = new XMLStudent();
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		
		String batch_code = request.getParameter("batch_code").trim();
		int student_id = Integer.parseInt(request.getParameter("student_id"));
		BatchGroup batchgrp = null;
		DBUTILS util = new DBUTILS();
		List<BatchGroup> bgs = new BatchGroupDAO().findAll();
		for(BatchGroup bg : bgs )
		{
			
	//		System.out.println("batch--"+bg.getBatchCode()+" comparing  "+ batch_code);
			if (bg.getBatchCode()!=null && bg.getBatchCode().equalsIgnoreCase(batch_code))
			{
				batchgrp = bg;
				break;
			}
		}
		
		if(batchgrp!= null)
		{
			boolean already_exist= false;
			for(BatchStudents bss : batchgrp.getBatchStudentses())
			{
				if(bss.getIstarUser().getId().intValue()==student_id)
				{
					already_exist=true;
					break;
				}
			}
			
			if(!already_exist)
			{
				
				String delete_from_old_bg = "delete from batch_students where student_id="+student_id;
				util.executeUpdate(delete_from_old_bg);
				
				String add_in_org ="update student set organization_id="+batchgrp.getOrganization().getId()+" where id ="+student_id+";";
				util.executeUpdate(add_in_org);
				
				String add_in_batch = "insert into batch_students (id,student_id, batch_group_id, user_type) values ((SELECT max(id) +1 FROM batch_students ),"+student_id+","+batchgrp.getId()+", 'STUDENT')";
				util.executeUpdate(add_in_batch);
				
				/*remove from google batch group*/
				List<BatchGroup> bgss = new BatchGroupDAO().findByName("GOOGLE_BATCH_GROUP");
				if(bgss.size()>0)
				{
					String remove_from_google ="delete from batch_students where batch_group_id="+bgss.get(0).getId()+" and student_id="+student_id ;
					util.executeUpdate(remove_from_google);
				}
				
				if(batchgrp.getAssessmentId()!=null && batchgrp.getAssessmentId()!=0)
				{
					String s = "delete from istar_assessment_event where actor_id="+student_id;
					util.executeUpdate(s);
					String delete_noti ="delete from istar_notification where receiver_id="+student_id+" and type='ASSESSMENT_EVENT'";
					util.executeUpdate(delete_noti);
					new GoogleUserService().createAssessmentEvent(batchgrp, student_id, batchgrp.getAssessmentId());
				}
			}
			else
			{
				System.out.println("student is already present in batch group");
			}				
		}
		else
		{
			System.out.println("no such bg  found");
		}	
		
		try {

			
			
				StudentInitializer si = new StudentInitializer();
				sxs = si.initializeStudent(student_id, request);
			

			if(batchgrp!=null)
			{
				obj.setResponseCode("200");
				obj.setResponseMessage("SUCCESS");
			}
			else
			{
				obj.setResponseCode("200");
				obj.setResponseMessage("FAILURE");
			}
			
		

		} catch (NullPointerException e) {
			e.printStackTrace();
			obj.setResponseCode("500");
			obj.setResponseMessage("Somthing wrong with your User Details. Please contact Administrator.");
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
		
		System.out.println("updated batch group and complex object sent");
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
