package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class XMLCourseRating {
	private String courseName;
	private int rating;
	private int course_id;

	public XMLCourseRating() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCourseName() {
		return courseName;
	}

	@XmlAttribute
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getRating() {
		return rating;
	}

	@XmlAttribute
	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getCourse_id() {
		return course_id;
	}

	@XmlAttribute
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
}
