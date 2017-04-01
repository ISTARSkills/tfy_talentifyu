package in.talentifyU.complexData.controllers;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.complexData.pojo.XMLStudentReport;
import in.talentifyU.utils.services.XMLReportService;

/**
 * Servlet implementation class GetReportXMLData
 */
@WebServlet("/get_report_xml_data")
public class GetReportXMLData extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetReportXMLData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printParams(request);
		int studentID = Integer.parseInt(request.getParameter("student_id"));
		int skillID = 0;
		if(request.getParameterMap().containsKey("skill_id"))
		{
			skillID = Integer.parseInt(request.getParameter("skill_id"));
		}
		XMLReportService service = new XMLReportService();
		XMLStudentReport report = service.getStudentReport(studentID, skillID);
		
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(XMLStudentReport.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() { public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException { writer.write( ac, i, j ); } });

			jaxbMarshaller.marshal(report, response.getWriter());

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
