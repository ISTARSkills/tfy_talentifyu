/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author vaibhav
 *
 */
public class XMLBatch {
	private int id;
	private XMLCourse course;
	private String batchName;
	private int orderId;

	public XMLBatch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLBatch(XMLCourse course, String batchName, XMLBatchGroup batchGroup) {
		super();
		this.course = course;
		this.batchName = batchName;
	}

	public int getOrderId() {
		return orderId;
	}

	@XmlAttribute
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getId() {
		return id;
	}

	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}

	public XMLCourse getCourse() {
		return course;
	}

	@XmlElement(name = "course")
	public void setCourse(XMLCourse course) {
		this.course = course;
	}

	public String getBatchName() {
		return batchName;
	}

	@XmlAttribute
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
}
