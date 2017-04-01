/**
 * 
 */
package in.talentifyU.offline.pojo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import in.talentifyU.complexData.pojo.AdapterCDATA;

/**
 * @author ComplexObject
 *
 */
@XmlRootElement(name = "option")
public class CMSOption {

	
	private Integer id;
	private String optionText;
	private Integer markingScheme;
	private Integer optionOrder;
	
	
	public Integer getId() {
		return id;
	}
	
	@XmlAttribute(name = "id")
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOptionText() {
		return optionText;
	}
	
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "optionText")
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public Integer getMarkingScheme() {
		return markingScheme;
	}
	
	@XmlAttribute(name = "markingScheme", required=false)
	public void setMarkingScheme(Integer markingScheme) {
		this.markingScheme = markingScheme;
	}
	public Integer getOptionOrder() {
		return optionOrder;
	}
	
	@XmlAttribute(name = "optionOrder", required=false)
	public void setOptionOrder(Integer optionOrder) {
		this.optionOrder = optionOrder;
	}
	public CMSOption() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
