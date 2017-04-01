/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author vaibhav
 *
 */
public class XMLOffer {
	private String eventId;
	private String companyName;
	private String imageUrl;
	private String jobTitle;
	private String description;
	private String location;
	private String offerLetterUrl;
	private String postedHoursAgo;

	public XMLOffer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEventId() {
		return eventId;
	}

	@XmlAttribute
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCompanyName() {
		return companyName;
	}

	@XmlAttribute
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@XmlAttribute
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	@XmlAttribute
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getDescription() {
		return description;
	}

	@XmlAttribute
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	@XmlAttribute
	public void setLocation(String location) {
		this.location = location;
	}

	public String getOfferLetterUrl() {
		return offerLetterUrl;
	}

	@XmlAttribute
	public void setOfferLetterUrl(String offerLetterUrl) {
		this.offerLetterUrl = offerLetterUrl;
	}

	public String getPostedHoursAgo() {
		return postedHoursAgo;
	}

	@XmlAttribute
	public void setPostedHoursAgo(String postedHoursAgo) {
		this.postedHoursAgo = postedHoursAgo;
	}
}
