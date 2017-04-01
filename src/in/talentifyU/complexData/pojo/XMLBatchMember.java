/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author vaibhav
 *
 */
public class XMLBatchMember {
	private int studentId;
	private String StudentName;
	private String imageUrl;

	public int getStudentId() {
		return studentId;
	}

	@XmlAttribute
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return StudentName;
	}

	@XmlAttribute
	public void setStudentName(String studentName) {
		StudentName = studentName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@XmlAttribute
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public XMLBatchMember() {
		super();
	}

	public XMLBatchMember(int studentId, String studentName, String imageUrl) {
		super();
		this.studentId = studentId;
		StudentName = studentName;
		this.imageUrl = imageUrl;
	}
}
