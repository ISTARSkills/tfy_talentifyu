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
public class XMLAddress {
	private Integer id;
	private XMLPincode pincode;
	private String addressline1;
	private String addressline2;
	private Double addressGeoLongitude;
	private Double addressGeoLatitude;

	public XMLAddress() {
		super();
	}

	public XMLAddress(Integer id, XMLPincode pincode, String addressline1, String addressline2, Double addressGeoLongitude, Double addressGeoLatitude) {
		super();
		this.id = id;
		this.pincode = pincode;
		this.addressline1 = addressline1;
		this.addressline2 = addressline2;
		this.addressGeoLongitude = addressGeoLongitude;
		this.addressGeoLatitude = addressGeoLatitude;
	}

	public Integer getId() {
		return id;
	}

	@XmlAttribute
	public void setId(Integer id) {
		this.id = id;
	}

	public XMLPincode getPincode() {
		return pincode;
	}

	@XmlElement
	public void setPincode(XMLPincode pincode) {
		this.pincode = pincode;
	}

	public String getAddressline1() {
		return addressline1;
	}

	@XmlAttribute
	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getAddressline2() {
		return addressline2;
	}

	@XmlAttribute
	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public Double getAddressGeoLongitude() {
		return addressGeoLongitude;
	}

	@XmlAttribute
	public void setAddressGeoLongitude(Double addressGeoLongitude) {
		this.addressGeoLongitude = addressGeoLongitude;
	}

	public Double getAddressGeoLatitude() {
		return addressGeoLatitude;
	}

	@XmlAttribute
	public void setAddressGeoLatitude(Double addressGeoLatitude) {
		this.addressGeoLatitude = addressGeoLatitude;
	}
}
