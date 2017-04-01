/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ComplexObject
 *
 */
@XmlRootElement(name = "report")
public class XMLStudentReport {
	Integer studentId;
	Integer batchRank;
	Integer totalBatchStudents;
	Integer pointsEarned;
	Integer totalPoints;
	Integer totalScore;
	XMLSkillReportLAData overAllData;
	ArrayList<XMLReportTest> testList;

	public XMLStudentReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getStudentId() {
		return studentId;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	@XmlAttribute
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	@XmlAttribute
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getBatchRank() {
		return batchRank;
	}

	@XmlAttribute
	public void setBatchRank(Integer batchRank) {
		this.batchRank = batchRank;
	}

	public Integer getTotalBatchStudents() {
		return totalBatchStudents;
	}

	@XmlAttribute
	public void setTotalBatchStudents(Integer totalBatchStudents) {
		this.totalBatchStudents = totalBatchStudents;
	}

	public Integer getPointsEarned() {
		return pointsEarned;
	}

	@XmlAttribute
	public void setPointsEarned(Integer pointsEarned) {
		this.pointsEarned = pointsEarned;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	@XmlAttribute
	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	public XMLSkillReportLAData getOverAllData() {
		return overAllData;
	}

	@XmlElement(name = "skill_la_data")
	public void setOverAllData(XMLSkillReportLAData overAllData) {
		this.overAllData = overAllData;
	}

	public ArrayList<XMLReportTest> getTestList() {
		return testList;
	}

	@XmlElement(name = "test")
	@XmlElementWrapper(name = "tests")
	public void setTestList(ArrayList<XMLReportTest> testList) {
		this.testList = testList;
	}
}
