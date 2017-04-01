package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class AppResponseObject {
	String responseCode;
	String responseMessage;
	XMLStudent student;

	public String getResponseCode() {
		return responseCode;
	}

	@XmlAttribute(required = false)
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	@XmlAttribute(required = false)
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public XMLStudent getStudent() {
		return student;
	}

	@XmlElement
	public void setStudent(XMLStudent student) {
		this.student = student;
	}

	public AppResponseObject() {
		super();
		// TODO Auto-generated constructor stub
	}
}
