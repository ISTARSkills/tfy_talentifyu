package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class XMLTest {
	private String companyName;
	private String imageUrl;
	private String jobTitle;
	private String location;
	private String studentInviteId;
	private String assessmentTitle;
	private int assessmentId;
	private int vacancyId;
	private String postedHoursAgo;
	private int numberOfQuestions;
	private int duration;
	private String testDate;
	private String testTime;

	public String getCompanyName() {
		return companyName;
	}

	@XmlAttribute
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@XmlAttribute
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	@XmlAttribute
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getLocation() {
		return location;
	}

	@XmlAttribute
	public void setLocation(String location) {
		this.location = location;
	}

	public String getStudentInviteId() {
		return studentInviteId;
	}

	@XmlAttribute
	public void setStudentInviteId(String studentInviteId) {
		this.studentInviteId = studentInviteId;
	}

	public String getAssessmentTitle() {
		return assessmentTitle;
	}

	@XmlAttribute
	public void setAssessmentTitle(String assessmentTitle) {
		this.assessmentTitle = assessmentTitle;
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	@XmlAttribute
	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public int getVacancyId() {
		return vacancyId;
	}

	@XmlAttribute
	public void setVacancyId(int vacancyId) {
		this.vacancyId = vacancyId;
	}

	public String getPostedHoursAgo() {
		return postedHoursAgo;
	}

	@XmlAttribute
	public void setPostedHoursAgo(String postedHoursAgo) {
		this.postedHoursAgo = postedHoursAgo;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	@XmlAttribute
	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public int getDuration() {
		return duration;
	}

	@XmlAttribute
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTestDate() {
		return testDate;
	}

	@XmlAttribute
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public String getTestTime() {
		return testTime;
	}

	@XmlAttribute
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

	public XMLTest() {
		super();
		// TODO Auto-generated constructor stub
	}
}
