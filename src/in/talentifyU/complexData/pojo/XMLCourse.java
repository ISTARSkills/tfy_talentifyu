/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author vaibhav
 *
 */
public class XMLCourse {
	private int courseId;
	private String courseName;
	private ArrayList<XMLSession> cmsessions;
	private String coursedescription;

	public XMLCourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLCourse(int courseId, String courseName, ArrayList<XMLSession> cmsessions) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.cmsessions = cmsessions;
	}

	public String getCoursedescription() {
		return coursedescription;
	}

	@XmlAttribute
	public void setCoursedescription(String coursedescription) {
		this.coursedescription = coursedescription;
	}

	public int getCourseId() {
		return courseId;
	}

	@XmlAttribute
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	@XmlAttribute
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public ArrayList<XMLSession> getCmsessions() {
		return cmsessions;
	}

	@XmlElement(name = "cmsession")
	@XmlElementWrapper(name = "cmsessions")
	public void setCmsessions(ArrayList<XMLSession> cmsessions) {
		this.cmsessions = cmsessions;
	}
}
