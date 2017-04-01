package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author vaibhav
 *
 */
public class XMLStudent {
	private XMLOrganization organization;
	private XMLAddress address;
	private String fatherName;
	private Long phone;
	private String email;
	private String imageUrl;
	private String motherName;
	private int id;
	private String userType;
	private String name;
	ArrayList<XMLBatchGroup> batchGroups;
	private ArrayList<XMLCoachStudent> coachStudents;
	private ArrayList<XMLNotification> notifications;
	private ArrayList<XMLInvite> invites;
	private ArrayList<XMLOffer> offers;
	private ArrayList<XMLInterview> interviews;
	private ArrayList<XMLTest> tests;
	private ArrayList<XMLEvents> events;
	private ArrayList<XMLNote> notes;
	private ArrayList<XMLNote> sharedNotes;
	private String mapKey;
	private XMLStudentProfile studentProfile;

	public XMLStudentProfile getStudentProfile() {
		return studentProfile;
	}

	@XmlElement(name = "student_profile")
	public void setStudentProfile(XMLStudentProfile studentProfile) {
		this.studentProfile = studentProfile;
	}

	public ArrayList<XMLNotification> getNotifications() {
		return notifications;
	}

	@XmlElement(name = "notification")
	@XmlElementWrapper(name = "notifications")
	public void setNotifications(ArrayList<XMLNotification> notifications) {
		this.notifications = notifications;
	}

	public String getUserType() {
		return userType;
	}

	@XmlAttribute
	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	@XmlAttribute
	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage_url() {
		if (imageUrl == null) {
			return "images/profile.jpg";
		} else {
			return imageUrl;
		}
	}

	@XmlAttribute
	public void setImage_url(String image_url) {
		this.imageUrl = image_url;
	}

	public ArrayList<XMLCoachStudent> getCoachStudents() {
		return coachStudents;
	}

	@XmlElement(name = "coach_student")
	@XmlElementWrapper(name = "coach_students")
	public void setCoachStudents(ArrayList<XMLCoachStudent> coachStudents) {
		this.coachStudents = coachStudents;
	}

	public XMLAddress getAddress() {
		return address;
	}

	@XmlElement(nillable = true)
	public void setAddress(XMLAddress address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}

	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public XMLOrganization getOrganization() {
		return organization;
	}

	@XmlElement
	public void setOrganization(XMLOrganization organization) {
		this.organization = organization;
	}

	public String getFatherName() {
		return fatherName;
	}

	@XmlAttribute
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Long getPhone() {
		return phone;
	}

	@XmlAttribute
	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getMotherName() {
		return motherName;
	}

	@XmlAttribute
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public ArrayList<XMLBatchGroup> getBatchGroups() {
		return batchGroups;
	}

	@XmlElement(name = "batch_group")
	@XmlElementWrapper(name = "batch_groups")
	public void setBatchGroups(ArrayList<XMLBatchGroup> batchGroups) {
		this.batchGroups = batchGroups;
	}

	public ArrayList<XMLInvite> getInvites() {
		return invites;
	}

	@XmlElement(name = "invite")
	@XmlElementWrapper(name = "invites")
	public void setInvites(ArrayList<XMLInvite> invites) {
		this.invites = invites;
	}

	public ArrayList<XMLOffer> getOffers() {
		return offers;
	}

	@XmlElement(name = "offer")
	@XmlElementWrapper(name = "offers")
	public void setOffers(ArrayList<XMLOffer> offers) {
		this.offers = offers;
	}

	public ArrayList<XMLInterview> getInterviews() {
		return interviews;
	}

	@XmlElement(name = "interview")
	@XmlElementWrapper(name = "interviews")
	public void setInterviews(ArrayList<XMLInterview> interviews) {
		this.interviews = interviews;
	}

	public ArrayList<XMLTest> getTests() {
		return tests;
	}

	@XmlElement(name = "test")
	@XmlElementWrapper(name = "tests")
	public void setTests(ArrayList<XMLTest> tests) {
		this.tests = tests;
	}

	public ArrayList<XMLEvents> getEvents() {
		return events;
	}

	@XmlElement(name = "event")
	@XmlElementWrapper(name = "events")
	public void setEvents(ArrayList<XMLEvents> events) {
		this.events = events;
	}

	public ArrayList<XMLNote> getNotes() {
		return notes;
	}

	@XmlElement(name = "note")
	@XmlElementWrapper(name = "mynotes")
	public void setNotes(ArrayList<XMLNote> notes) {
		this.notes = notes;
	}

	public ArrayList<XMLNote> getSharedNotes() {
		return sharedNotes;
	}

	@XmlElement(name = "note")
	@XmlElementWrapper(name = "shared_notes")
	public void setSharedNotes(ArrayList<XMLNote> sharedNotes) {
		this.sharedNotes = sharedNotes;
	}
}
