package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class XMLNotification {
	private String id;
	private String date;
	private String time;
	private String description;
	private String title;
	private String type;
	private String sender;
	private String receivedAt;
	private String eventID;

	public String getEventID() {
		return eventID;
	}

	@XmlAttribute
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getReceivedAt() {
		return receivedAt;
	}

	@XmlAttribute
	public void setReceivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
	}

	public XMLNotification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	@XmlAttribute
	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	@XmlAttribute
	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	@XmlAttribute
	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	@XmlAttribute
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	@XmlAttribute
	public void setSender(String sender) {
		this.sender = sender;
	}
}
