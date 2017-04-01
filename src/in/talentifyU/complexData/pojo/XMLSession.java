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
public class XMLSession {
	private int cmsessionId;
	private String cmsessionName;
	private String cmsessionDescription;
	private ArrayList<XMLLesson> lessons;
	private boolean readable;
	private int orderId;
	private int lastOrderId;
	private int currentmoduleOrderId;
	private int lastModuleOrderId;

	public int getCurrentmoduleOrderId() {
		return currentmoduleOrderId;
	}

	@XmlAttribute
	public void setCurrentmoduleOrderId(int currentmoduleOrderId) {
		this.currentmoduleOrderId = currentmoduleOrderId;
	}

	public int getLastModuleOrderId() {
		return lastModuleOrderId;
	}

	@XmlAttribute
	public void setLastModuleOrderId(int lastModuleOrderId) {
		this.lastModuleOrderId = lastModuleOrderId;
	}

	public int getLastOrderId() {
		return lastOrderId;
	}

	@XmlAttribute
	public void setLastOrderId(int lastOrderId) {
		this.lastOrderId = lastOrderId;
	}

	public boolean isReadable() {
		return readable;
	}

	@XmlAttribute
	public void setReadable(boolean readable) {
		this.readable = readable;
	}

	public XMLSession() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLSession(int cmsession_id, String cmsessionName, ArrayList<XMLLesson> lessons) {
		super();
		this.cmsessionId = cmsession_id;
		this.cmsessionName = cmsessionName;
		this.lessons = lessons;
	}

	public int getCmsession_id() {
		return cmsessionId;
	}

	public int getOrderId() {
		return orderId;
	}

	@XmlAttribute
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getCmsessionDescription() {
		return cmsessionDescription;
	}

	@XmlAttribute
	public void setCmsessionDescription(String cmsessionDescription) {
		this.cmsessionDescription = cmsessionDescription;
	}

	@XmlAttribute
	public void setCmsession_id(int cmsession_id) {
		this.cmsessionId = cmsession_id;
	}

	public String getCmsessionName() {
		return cmsessionName;
	}

	@XmlAttribute
	public void setCmsessionName(String cmsessionName) {
		this.cmsessionName = cmsessionName;
	}

	public ArrayList<XMLLesson> getLessons() {
		return lessons;
	}

	@XmlElement(name = "lesson")
	@XmlElementWrapper(name = "lessons")
	public void setLessons(ArrayList<XMLLesson> lessons) {
		this.lessons = lessons;
	}
}
