package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class XMLInterview {
	private String companyName;
	private String imageUrl;
	private String jobTitle;
	private String location;
	private String interviewDate;
	private String interviewTime;
	private String description;
	private String eventId;
	private int vacancyId;
	private String recruiterName;
	private int durationInMinutes;
	private String interviewDay;
	private String postedHoursAgo;
	private String interviwType;
	private String panelist;
	private String hostUrl;
	private String joinUrl;
	private String meetingId;
	private String meetingPasword;

	public String getInterviwType() {
		return interviwType;
	}

	@XmlAttribute
	public void setInterviwType(String interviwType) {
		this.interviwType = interviwType;
	}

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

	public String getInterviewDate() {
		return interviewDate;
	}

	@XmlAttribute
	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewTime() {
		return interviewTime;
	}

	@XmlAttribute
	public void setInterviewTime(String interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getDescription() {
		return description;
	}

	@XmlAttribute
	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventId() {
		return eventId;
	}

	@XmlAttribute
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getVacancyId() {
		return vacancyId;
	}

	@XmlAttribute
	public void setVacancyId(int vacancyId) {
		this.vacancyId = vacancyId;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	@XmlAttribute
	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	@XmlAttribute
	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public String getInterviewDay() {
		return interviewDay;
	}

	@XmlAttribute
	public void setInterviewDay(String interviewDay) {
		this.interviewDay = interviewDay;
	}

	public String getPostedHoursAgo() {
		return postedHoursAgo;
	}

	@XmlAttribute
	public void setPostedHoursAgo(String postedHoursAgo) {
		this.postedHoursAgo = postedHoursAgo;
	}

	public String getPanelist() {
		return panelist;
	}

	@XmlAttribute
	public void setPanelist(String panelist) {
		this.panelist = panelist;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	@XmlAttribute
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getJoinUrl() {
		return joinUrl;
	}

	@XmlAttribute
	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	public String getMeetingId() {
		return meetingId;
	}

	@XmlAttribute
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingPasword() {
		return meetingPasword;
	}

	@XmlAttribute
	public void setMeetingPasword(String meetingPasword) {
		this.meetingPasword = meetingPasword;
	}
}