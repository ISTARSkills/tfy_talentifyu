/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ComplexObject
 *
 */
public class XMLReportTest {
	Integer assessmentId;
	String testName;
	Integer percentage;
	String date;
	String imageUrl;
	Integer pointsEarned;
	Integer maxPoints;
	Integer rank;
	ArrayList<XMLReportQuestion> questions;

	public XMLReportTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getAssessmentId() {
		return assessmentId;
	}

	@XmlAttribute
	public void setAssessmentId(Integer assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getTestName() {
		return testName;
	}

	@XmlAttribute
	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Integer getPercentage() {
		return percentage;
	}

	@XmlAttribute
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getDate() {
		return date;
	}

	@XmlAttribute
	public void setDate(String date) {
		this.date = date;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@XmlAttribute
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ArrayList<XMLReportQuestion> getQuestions() {
		return questions;
	}

	@XmlElement(name = "questions")
	@XmlElementWrapper(name = "question")
	public void setQuestions(ArrayList<XMLReportQuestion> questions) {
		this.questions = questions;
	}

	public Integer getPointsEarned() {
		return pointsEarned;
	}

	@XmlAttribute
	public void setPointsEarned(Integer pointsEarned) {
		this.pointsEarned = pointsEarned;
	}

	public Integer getMaxPoints() {
		return maxPoints;
	}

	@XmlAttribute
	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	public Integer getRank() {
		return rank;
	}

	@XmlAttribute
	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
