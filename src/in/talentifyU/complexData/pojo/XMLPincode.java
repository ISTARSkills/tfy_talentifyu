/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author vaibhav
 *
 */
public class XMLPincode {
	private int id;
	private String state;
	private String city;
	private String country;
	private int pin;

	public XMLPincode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLPincode(int id, String state, String city, String country, int pin) {
		super();
		this.id = id;
		this.state = state;
		this.city = city;
		this.country = country;
		this.pin = pin;
	}

	public int getId() {
		return id;
	}

	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	@XmlAttribute
	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	@XmlAttribute
	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	@XmlAttribute
	public void setCountry(String country) {
		this.country = country;
	}

	public int getPin() {
		return pin;
	}

	@XmlAttribute
	public void setPin(int pin) {
		this.pin = pin;
	}
}
