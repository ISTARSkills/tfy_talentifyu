/**
 * 
 */
package in.talentifyU.offline.pojo;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ComplexObject
 *
 */

@XmlRootElement(name = "result")
public class CMSAssessmentResult {

	Integer userId;	
	HashMap<Integer, String> questionOtionMap;
	HashMap<Integer, Integer> questiontimeMap;
	Integer assessmentId;
	Integer timetaken;
	
	
	
	
	public CMSAssessmentResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getUserId() {
		return userId;
	}
	
	@XmlAttribute(name = "user_id")
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	public HashMap<Integer, String> getQuestionOtionMap() {
		return questionOtionMap;
	}
	
	@XmlElementWrapper(name="question_map")
	public void setQuestionOtionMap(HashMap<Integer, String> questionOtionMap) {
		this.questionOtionMap = questionOtionMap;
	}
	
	
	public Integer getAssessmentId() {
		return assessmentId;
	}
	
	@XmlAttribute(name = "assessment_id")
	public void setAssessmentId(Integer assessmentId) {
		this.assessmentId = assessmentId;
	}
	
	
	public HashMap<Integer, Integer> getQuestiontimeMap() {
		return questiontimeMap;
	}
	
	@XmlElementWrapper(name="question_time")
	public void setQuestiontimeMap(HashMap<Integer, Integer> questiontimeMap) {
		this.questiontimeMap = questiontimeMap;
	}
	public Integer getTimetaken() {
		return timetaken;
	}
	
	@XmlAttribute(name = "total_time")
	public void setTimetaken(Integer timetaken) {
		this.timetaken = timetaken;
	}
	
	
	
	
	
}
