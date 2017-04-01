/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author vaibhav
 *
 */
public class XMLNote {
	private int note_id;
	private String noteText;
	private String slideUrl;
	private String created_at;
	private boolean isMyNote;
	private String sharedBy;
	private String CMSession;
	private int sessionId;
	private String Lesson;
	private int lessonId;
	private String Course;
	private int courseId;
	private int slideId;

	public int getSessionId() {
		return sessionId;
	}

	@XmlAttribute
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getLessonId() {
		return lessonId;
	}

	@XmlAttribute
	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public int getCourseId() {
		return courseId;
	}

	@XmlAttribute
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCMSession() {
		return CMSession;
	}

	@XmlAttribute
	public void setCMSession(String cMSession) {
		CMSession = cMSession;
	}

	public String getLesson() {
		return Lesson;
	}

	@XmlAttribute
	public void setLesson(String lesson) {
		Lesson = lesson;
	}

	public String getCourse() {
		return Course;
	}

	@XmlAttribute
	public void setCourse(String course) {
		Course = course;
	}

	public int getSlideId() {
		return slideId;
	}

	@XmlAttribute
	public void setSlideId(int slideId) {
		this.slideId = slideId;
	}

	public int getNote_id() {
		return note_id;
	}

	@XmlAttribute
	public void setNote_id(int note_id) {
		this.note_id = note_id;
	}

	public String getNoteText() {
		return noteText;
	}

	@XmlElement
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public String getSlideUrl() {
		return slideUrl;
	}

	@XmlAttribute
	public void setSlideUrl(String slideUrl) {
		this.slideUrl = slideUrl;
	}

	public String getCreated_at() {
		return created_at;
	}

	@XmlAttribute
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isMyNote() {
		return isMyNote;
	}

	@XmlAttribute
	public void setMyNote(boolean isMyNote) {
		this.isMyNote = isMyNote;
	}

	public String getSharedBy() {
		return sharedBy;
	}

	@XmlAttribute
	public void setSharedBy(String sharedBy) {
		this.sharedBy = sharedBy;
	}

	public XMLNote(int note_id, String noteText, String slideUrl, Date created_at, boolean isMyNote, String sharedBy) {
		super();
		this.note_id = note_id;
		this.noteText = noteText;
		this.slideUrl = slideUrl;
		this.isMyNote = isMyNote;
		this.sharedBy = sharedBy;
	}

	public XMLNote() {
		super();
		// TODO Auto-generated constructor stub
	}
}
