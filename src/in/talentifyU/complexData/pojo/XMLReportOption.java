/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ComplexObject
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLReportOption {
	@XmlAttribute
	Integer optionId;
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	String optionText;
	@XmlAttribute
	Integer markingScheme;

	public XMLReportOption() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public Integer getMarkingScheme() {
		return markingScheme;
	}

	public void setMarkingScheme(Integer markingScheme) {
		this.markingScheme = markingScheme;
	}
}
