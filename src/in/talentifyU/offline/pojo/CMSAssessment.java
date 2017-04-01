/**
 * 
 */
package in.talentifyU.offline.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.viksitpro.core.dao.entities.UiTheme;

/**
 * @author ComplexObject
 *
 */
@XmlRootElement(name = "assessment")
public class CMSAssessment {

	private int assessmentId;
	private Integer assessmentDurationMinutes;
	private Integer number_of_questions;
	private String assessmentTitle;
	private Boolean isRetryAble;
	private String category;
	private String assessmentType;
	private List<CMSQuestion> questions;
	private UiTheme theme;

	public CMSAssessment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	@XmlAttribute(name = "assessmentId")
	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Integer getAssessmentDurationMinutes() {
		return assessmentDurationMinutes;
	}

	@XmlAttribute(name = "assessmentDurationMinutes", required = false)
	public void setAssessmentDurationMinutes(Integer assessmentDurationMinutes) {
		this.assessmentDurationMinutes = assessmentDurationMinutes;
	}

	public Integer getNumber_of_questions() {
		return number_of_questions;
	}

	@XmlAttribute(name = "number_of_questions", required = false)
	public void setNumber_of_questions(Integer number_of_questions) {
		this.number_of_questions = number_of_questions;
	}

	public String getAssessmentTitle() {
		return assessmentTitle;
	}

	@XmlAttribute(name = "assessmentTitle", required = false)
	public void setAssessmentTitle(String assessmentTitle) {
		this.assessmentTitle = assessmentTitle;
	}

	public Boolean getIsRetryAble() {
		return isRetryAble;
	}

	@XmlAttribute(name = "isRetryAble", required = false)
	public void setIsRetryAble(Boolean isRetryAble) {
		this.isRetryAble = isRetryAble;
	}

	public String getCategory() {
		return category;
	}

	@XmlAttribute(name = "category", required = false)
	public void setCategory(String category) {
		this.category = category;
	}

	public List<CMSQuestion> getQuestions() {
		return questions;
	}

	@XmlElementWrapper(name = "questions")
	@XmlElement(name = "question", required = false)
	public void setQuestions(List<CMSQuestion> questions) {
		this.questions = questions;
	}

	public String getAssessmentType() {
		return assessmentType;
	}

	@XmlAttribute(name = "assessmentType", required = false)
	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}

	public UiTheme getTheme() {
		return theme;
	}

	@XmlElement(name = "theme", required = false)
	public void setTheme(UiTheme theme) {
		this.theme = theme;
	}

}
