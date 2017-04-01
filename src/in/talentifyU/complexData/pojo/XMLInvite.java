/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author vaibhav
 *
 */
public class XMLInvite {
	private String companyName;
	private String imageUrl;
	private String vacancyJobTitle;
	private String vacancyLocation;
	private String vacancyDescription;
	private String eventId;
	private String postedHoursAgo;
	private String eventAction;

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

	public String getVacancyJobTitle() {
		return vacancyJobTitle;
	}

	@XmlAttribute
	public void setVacancyJobTitle(String vacancyJobTitle) {
		this.vacancyJobTitle = vacancyJobTitle;
	}

	public String getVacancyLocation() {
		return vacancyLocation;
	}

	@XmlAttribute
	public void setVacancyLocation(String vacancyLocation) {
		this.vacancyLocation = vacancyLocation;
	}

	public String getVacancyDescription() {
		return vacancyDescription;
	}

	@XmlAttribute
	public void setVacancyDescription(String vacancyDescription) {
		this.vacancyDescription = vacancyDescription;
	}

	public String getEventId() {
		return eventId;
	}

	@XmlAttribute
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPostedHoursAgo() {
		return postedHoursAgo;
	}

	@XmlAttribute
	public void setPostedHoursAgo(String postedHoursAgo) {
		this.postedHoursAgo = postedHoursAgo;
	}

	public String getEventAction() {
		return eventAction;
	}

	@XmlAttribute
	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public XMLInvite() {
		super();
		// TODO Auto-generated constructor stub
	}
}
