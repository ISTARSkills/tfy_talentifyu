/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ComplexObject
 *
 */
public class XMLGraphPoints {
	String date;
	Integer percentileCountry;
	Integer percentileglobe;
	Integer percentileBatch;
	Integer percentileOrganozation;

	public XMLGraphPoints() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDate() {
		return date;
	}

	@XmlAttribute
	public void setDate(String date) {
		this.date = date;
	}

	public Integer getPercentileCountry() {
		return percentileCountry;
	}

	@XmlAttribute
	public void setPercentileCountry(Integer percentileCountry) {
		this.percentileCountry = percentileCountry;
	}

	public Integer getPercentileglobe() {
		return percentileglobe;
	}

	@XmlAttribute
	public void setPercentileglobe(Integer percentileglobe) {
		this.percentileglobe = percentileglobe;
	}

	public Integer getPercentileBatch() {
		return percentileBatch;
	}

	@XmlAttribute
	public void setPercentileBatch(Integer percentileBatch) {
		this.percentileBatch = percentileBatch;
	}

	public Integer getPercentileOrganozation() {
		return percentileOrganozation;
	}

	@XmlAttribute
	public void setPercentileOrganozation(Integer percentileOrganozation) {
		this.percentileOrganozation = percentileOrganozation;
	}
}
