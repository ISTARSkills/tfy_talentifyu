/**
 * 
 */
package in.talentifyU.offline.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.viksitpro.core.dao.entities.UiTheme;

import in.talentifyU.complexData.pojo.AdapterCDATA;

/**
 * @author ComplexObject
 *
 */
@XmlRootElement(name = "question")
public class CMSQuestion {

	
	private Integer id;
	private String questionText;
	private Integer durationInSec;
	private int orderId;
	private String template;
	private Integer difficultyLevel;
	private String comprehensive_passage;
	private UiTheme theme;
	private List<CMSOption> options;

	public CMSQuestion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	@XmlAttribute(name = "id")
	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "questionText")
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Integer getDurationInSec() {
		return durationInSec;
	}

	@XmlAttribute(name = "durationInSec", required=false)
	public void setDurationInSec(Integer durationInSec) {
		this.durationInSec = durationInSec;
	}

	public int getOrderId() {
		return orderId;
	}

	@XmlAttribute(name = "orderId", required=false)
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	

	public String getTemplate() {
		return template;
	}
	@XmlAttribute(name = "template", required=false)
	public void setTemplate(String template) {
		this.template = template;
	}

	public Integer getDifficultyLevel() {
		return difficultyLevel;
	}

	@XmlAttribute(name = "difficultyLevel", required=false)
	public void setDifficultyLevel(Integer difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	

	public String getComprehensive_passage() {
		return comprehensive_passage;
	}

	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "comprehensive_passage", required=false)
	public void setComprehensive_passage(String comprehensive_passage) {
		this.comprehensive_passage = comprehensive_passage;
	}

	

	public List<CMSOption> getOptions() {
		return options;
	}
	@XmlElementWrapper(name = "options")
	@XmlElement(name = "option")
	public void setOptions(List<CMSOption> options) {
		this.options = options;
	}

	public UiTheme getTheme() {
		return theme;
	}

	@XmlElement(name = "theme")
	public void setTheme(UiTheme theme) {
		this.theme = theme;
	}
	
	
	
	
	
	
	
	
}
