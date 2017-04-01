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
public class XMLLesson {
	private Integer id;
	private String title;
	private ArrayList<XMLLO> los;
	private ArrayList<XMLSlide> slides;
	private String type;
	private XMLAssessment assessment;
	private Integer last_slide_pointer;
	private String status;
	private Integer concreteId;
	private Integer LOrderId;
	
	
	

	public Integer getLOrderId() {
		return LOrderId;
	}
	@XmlAttribute
	public void setLOrderId(Integer lOrderId) {
		LOrderId = lOrderId;
	}

	public String getStatus() {
		return status;
	}

	@XmlAttribute
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getConcreteId() {
		return concreteId;
	}

	@XmlAttribute
	public void setConcreteId(Integer concreteId) {
		this.concreteId = concreteId;
	}

	public XMLLesson() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLLesson(Integer id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public XMLAssessment getAssessment() {
		return assessment;
	}

	@XmlElement
	public void setAssessment(XMLAssessment assessment) {
		this.assessment = assessment;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	@XmlAttribute
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<XMLSlide> getSlides() {
		return slides;
	}

	@XmlElement(name = "slide")
	@XmlElementWrapper(name = "slides")
	public void setSlides(ArrayList<XMLSlide> slides) {
		this.slides = slides;
	}

	@XmlAttribute
	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<XMLLO> getLos() {
		return los;
	}

	@XmlElement(name = "llo")
	@XmlElementWrapper(name = "llos")
	public void setLos(ArrayList<XMLLO> los) {
		this.los = los;
	}

	@XmlAttribute
	public Integer getLast_slide_pointer() {
		return last_slide_pointer;
	}

	public void setLast_slide_pointer(Integer last_slide_pointer) {
		this.last_slide_pointer = last_slide_pointer;
	}
	
	
	
}
