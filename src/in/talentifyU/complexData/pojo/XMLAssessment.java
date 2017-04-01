/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author vaibhav
 *
 */
public class XMLAssessment {
	private int assessmentId;
	private Integer assessmentDurationMinutes;
	private ArrayList<XMLQuestion> questions;
	private Integer number_of_questions;
	private String assessmentTitle;

	public int getAssessmentId() {
		return assessmentId;
	}

	@XmlAttribute
	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Integer getAssessmentDurationMinutes() {
		return assessmentDurationMinutes;
	}

	@XmlAttribute
	public void setAssessmentDurationMinutes(Integer assessmentDurationMinutes) {
		this.assessmentDurationMinutes = assessmentDurationMinutes;
	}

	public ArrayList<XMLQuestion> getQuestions() {
		return questions;
	}

	@XmlElement(name = "question")
	@XmlElementWrapper(name = "questions")
	public void setQuestions(ArrayList<XMLQuestion> questions) {
		this.questions = questions;
	}

	public Integer getNumber_of_questions() {
		return number_of_questions;
	}

	@XmlAttribute
	public void setNumber_of_questions(Integer number_of_questions) {
		this.number_of_questions = number_of_questions;
	}

	public String getAssessmentTitle() {
		return assessmentTitle;
	}

	@XmlAttribute
	public void setAssessmentTitle(String assessmentTitle) {
		this.assessmentTitle = assessmentTitle;
	}

	public XMLAssessment(int assessmentId, Integer assessmentDurationMinutes, ArrayList<XMLQuestion> questions, Integer number_of_questions, String assessmentTitle) {
		super();
		this.assessmentId = assessmentId;
		this.assessmentDurationMinutes = assessmentDurationMinutes;
		this.questions = questions;
		this.number_of_questions = number_of_questions;
		this.assessmentTitle = assessmentTitle;
	}

	public XMLAssessment() {
		super();
		// TODO Auto-generated constructor stub
	}
}
