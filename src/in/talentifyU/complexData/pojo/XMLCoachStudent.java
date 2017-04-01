package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class XMLCoachStudent {
	private int studentId;
	private String batchGroup;
	private int batchId;
	private String StudentName;
	private String imageUrl;
	private String mobile;
	private String organizationName;
	private boolean isfavoutite;
	private ArrayList<XMLCourseRating> courseRating;

	public int getBatchId() {
		return batchId;
	}

	@XmlAttribute
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public ArrayList<XMLCourseRating> getCourseRating() {
		return courseRating;
	}

	@XmlElement(name = "course_rating")
	@XmlElementWrapper(name = "course_ratings")
	public void setCourseRating(ArrayList<XMLCourseRating> courseRating) {
		this.courseRating = courseRating;
	}

	public boolean isIsfavoutite() {
		return isfavoutite;
	}

	@XmlAttribute
	public void setIsfavoutite(boolean isfavoutite) {
		this.isfavoutite = isfavoutite;
	}

	public int getStudentId() {
		return studentId;
	}

	@XmlAttribute
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getBatchGroup() {
		return batchGroup;
	}

	@XmlAttribute
	public void setBatchGroup(String batchGroup) {
		this.batchGroup = batchGroup;
	}

	public String getStudentName() {
		return StudentName;
	}

	@XmlAttribute
	public void setStudentName(String studentName) {
		StudentName = studentName;
	}

	public String getImageUrl() {
		if (imageUrl == null || imageUrl.equalsIgnoreCase("null")) {
			return "images/profile.jpg";
		} else {
			return imageUrl;
		}
	}

	@XmlAttribute
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getMobile() {
		return mobile;
	}

	@XmlAttribute
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	@XmlAttribute
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public XMLCoachStudent(int studentId, String batchGroup, String studentName, String imageUrl, String mobile, String organizationName) {
		super();
		this.studentId = studentId;
		this.batchGroup = batchGroup;
		StudentName = studentName;
		this.imageUrl = imageUrl;
		this.mobile = mobile;
		this.organizationName = organizationName;
	}

	public XMLCoachStudent() {
		super();
		// TODO Auto-generated constructor stub
	}
}
