/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author vaibhav
 *
 */
public class XMLEvents {
	private String id;
	private String eventdate;
	private int eventHour;
	private int eventMin;
	private String eventType;
	private String status;
	private int cmsessionId;
	private String cmsessionTitle;
	private int batchId;
	private int bgroupId;
	private String batchName;
	private String eventName;
	private String description;
	private int classId;
	private String className;
	private String vacancyTitle;
	private String location;
	private String vacancyDescription;
	private String recruiter;
	private String company;
	private int assessmentId;
	private String assessmentTitle;
	private String sessionTitle;

	public int getBgroupId() {
		return bgroupId;
	}

	@XmlAttribute
	public void setBgroupId(int bgroupId) {
		this.bgroupId = bgroupId;
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	@XmlAttribute
	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getAssessmentTitle() {
		return assessmentTitle;
	}

	@XmlAttribute
	public void setAssessmentTitle(String assessmentTitle) {
		this.assessmentTitle = assessmentTitle;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	@XmlAttribute
	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getDescription() {
		return description;
	}

	@XmlAttribute
	public void setDescription(String description) {
		this.description = description;
	}

	public String getVacancyTitle() {
		return vacancyTitle;
	}

	@XmlAttribute
	public void setVacancyTitle(String vacancyTitle) {
		this.vacancyTitle = vacancyTitle;
	}

	public String getLocation() {
		return location;
	}

	@XmlAttribute
	public void setLocation(String location) {
		this.location = location;
	}

	public String getVacancyDescription() {
		return vacancyDescription;
	}

	@XmlAttribute
	public void setVacancyDescription(String vacancyDescription) {
		this.vacancyDescription = vacancyDescription;
	}

	public String getRecruiter() {
		return recruiter;
	}

	@XmlAttribute
	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	public String getCompany() {
		return company;
	}

	@XmlAttribute
	public void setCompany(String company) {
		this.company = company;
	}

	public int getCmsessionId() {
		return cmsessionId;
	}

	@XmlAttribute
	public void setCmsessionId(int cmsessionId) {
		this.cmsessionId = cmsessionId;
	}

	public String getCmsessionTitle() {
		return cmsessionTitle;
	}

	@XmlAttribute
	public void setCmsessionTitle(String cmsessionTitle) {
		this.cmsessionTitle = cmsessionTitle;
	}

	public int getBatchId() {
		return batchId;
	}

	@XmlAttribute
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	@XmlAttribute
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public int getClassId() {
		return classId;
	}

	@XmlAttribute
	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	@XmlAttribute
	public void setClassName(String className) {
		this.className = className;
	}

	public XMLEvents() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLEvents(String id, Timestamp eventdate, int eventHour, int eventMin, String eventType, String status, XMLSession cmsession_id, XMLBatch batch_id, String eventName, XMLClassroom classroom) {
		super();
		this.id = id;
		this.eventHour = eventHour;
		this.eventMin = eventMin;
		this.eventType = eventType;
		this.status = status;
		this.eventName = eventName;
	}

	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public String getEventdate() {
		return eventdate;
	}

	@XmlAttribute
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}

	public int getEventHour() {
		return eventHour;
	}

	@XmlAttribute
	public void setEventHour(int eventHour) {
		this.eventHour = eventHour;
	}

	public int getEventMin() {
		return eventMin;
	}

	@XmlAttribute
	public void setEventMin(int eventMin) {
		this.eventMin = eventMin;
	}

	public String getEventType() {
		return eventType;
	}

	@XmlAttribute
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getStatus() {
		return status;
	}

	@XmlAttribute
	public void setStatus(String status) {
		this.status = status;
	}

	public String getEventName() {
		return eventName;
	}

	@XmlAttribute
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
}
