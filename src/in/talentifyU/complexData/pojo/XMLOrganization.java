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
public class XMLOrganization {
	private String name;
	private XMLAddress address;

	public XMLOrganization() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLOrganization(String name, XMLAddress address) {
		super();
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public XMLAddress getAddress() {
		return address;
	}

	@XmlElement
	public void setAddress(XMLAddress address) {
		this.address = address;
	}
}
