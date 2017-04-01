package in.talentifyU.offline.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentDAO;
import com.viksitpro.core.dao.entities.AssessmentOption;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.dao.entities.UiThemeDAO;
import com.viksitpro.core.utilities.IStarBaseServelet;

import in.talentifyU.offline.pojo.CMSAssessment;
import in.talentifyU.offline.pojo.CMSOption;
import in.talentifyU.offline.pojo.CMSQuestion;

/**
 * Servlet implementation class getOfflineAssessment
 */
@WebServlet("/get_offline_assessment")
public class getOfflineAssessment extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getOfflineAssessment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printParams(request);
		
		
		int assessment_id = Integer.parseInt(request.getParameter("assessment_id"));
		Assessment assesment = new AssessmentDAO().findById(assessment_id);		
		CMSAssessment a  = new CMSAssessment();		
		
		Set<AssessmentQuestion> assessQuestions = assesment.getAssessmentQuestions();
		
		//if number of questions in assessment is 0 then set number of question as 0
		int number_of_questions = assessQuestions!=null ? assessQuestions.size() : 0;
		
		//if assessment duration is not given then set duration equal to number of questions
		int assess_duration = assesment.getAssessmentdurationminutes()!=null ? assesment.getAssessmentdurationminutes() : number_of_questions;
		a.setAssessmentDurationMinutes(assess_duration);
		a.setAssessmentId(assesment.getId());
		
		//if assessment title is not given then set assessment title = lesson title
		String title = assesment.getAssessmenttitle()!=null ? assesment.getAssessmenttitle():"NOT_AVAILABLE";
		a.setAssessmentTitle(title);
		
		//defines type of assessment. default is static
		String assessmentType = assesment.getAssessmentType()!=null ? assesment.getAssessmentType() : "STATIC";
		a.setAssessmentType(assessmentType);
		
		//defines category of assessment wheather it is job assessment or normala assessment.
		String assessmentCategory = assesment.getCategory() !=null ? assesment.getCategory() : "NORMAL";
		a.setCategory(assessmentCategory);
		
		//check if assessment is retryable. default is false.
		boolean retryable = assesment.getRetryAble()!=null ? assesment.getRetryAble() : true;
		a.setIsRetryAble(retryable);
		
		//set number of questions in assessment
		a.setNumber_of_questions(number_of_questions);
		
		
		List<CMSQuestion> questions = new LinkedList<>();
		int total_time_per_question = 0;
		for(AssessmentQuestion aq : assesment.getAssessmentQuestions() )
		{
			Question q = aq.getQuestion();
			CMSQuestion cmsq = new CMSQuestion();
			
			if(q.getComprehensivePassageText() != null && !q.getComprehensivePassageText().equalsIgnoreCase("") && !q.getComprehensivePassageText().contains("<p>null</p>")){
				cmsq.setComprehensive_passage(q.getComprehensivePassageText());
	
			}
			cmsq.setDifficultyLevel(q.getDifficultyLevel());
			cmsq.setDurationInSec(q.getDurationInSec());
			total_time_per_question+=q.getDurationInSec();
			cmsq.setId(q.getId());
			cmsq.setOrderId(q.getId());
			String qTest = q.getQuestionText();
			if(qTest.contains("height:900px; width:900px")) {
				qTest = qTest.replaceAll("height:900px; width:900px", "width:100%");
			}
			cmsq.setQuestionText(q.getQuestionText());
			cmsq.setTemplate(q.getQuestionType());
			cmsq.setTheme(new UiThemeDAO().findById(Integer.parseInt(assesment.getLesson().getLesson_theme())));
			List<CMSOption> options = new LinkedList<CMSOption>();
			
			for(AssessmentOption aop : q.getAssessmentOptions())
			{
				if(aop.getText()!=null && !aop.getText().isEmpty())
				{
					CMSOption op = new CMSOption();
					op.setId(aop.getId());
					op.setMarkingScheme(aop.getMarkingScheme());
					op.setOptionOrder(aop.getId());
					String optionText = aop.getText().replaceAll("&nbsp;","").replaceAll("<br />","").replaceAll("null","");
					org.jsoup.nodes.Document doc = Jsoup.parse(optionText);
					optionText = doc.text();
					 String cleanText = optionText.replaceAll("[^\\p{ASCII}]", "").replaceAll("\\?{2,}", "'");
						op.setOptionText(cleanText);
					if(!optionText.trim().contains("<p></p>") && !optionText.contains("<p> </p>") && !optionText.equalsIgnoreCase(" ") && !optionText.equalsIgnoreCase("")){
						System.out.println("-optionText-->"+optionText);
						options.add(op);
					}
						
					//
				}
			}
			
			if(options.size()>0)
			{
				cmsq.setOptions(options);
				questions.add(cmsq);					
			}							
		}
		
		total_time_per_question= total_time_per_question/60;
		System.out.println("total_time_per_question"+total_time_per_question);
		System.out.println("a.getAssessmentDurationMinutes()"+a.getAssessmentDurationMinutes());
		if(total_time_per_question!=a.getAssessmentDurationMinutes())
		{
			a.setAssessmentDurationMinutes(total_time_per_question);
		}
		
		a.setQuestions(questions);
		a.setTheme(new UiThemeDAO().findById(Integer.parseInt(assesment.getLesson().getLesson_theme())));				
		
		PrintWriter out = response.getWriter();
		
		if(request.getParameterMap().containsKey("content_type") && request.getParameter("content_type").equalsIgnoreCase("JSON"))
		{
			//return in json
			System.out.println("retun json");
			response.setContentType("application/json;charset=UTF-8");
			ObjectMapper mapper = new ObjectMapper();
	        
			AnnotationIntrospector introspector
			    = new JacksonAnnotationIntrospector();
			mapper.setAnnotationIntrospector(introspector);
			         
			// Printing JSON
			String result = mapper.writeValueAsString(a);
			out.println(result);
		}
		else 
		{
			// return in xml
			try {	
				response.setContentType("text/xml;charset=UTF-8");
				JAXBContext jaxbContext = JAXBContext.newInstance(CMSAssessment.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				// output pretty printed
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() { public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException { writer.write( ac, i, j ); } });
				jaxbMarshaller.marshal(a, out);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
		
	//	System.err.println("Maaaaaaaaaaaaaa");
	}

}
