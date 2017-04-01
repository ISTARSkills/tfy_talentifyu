package in.talentifyU.complexData.pojo;

import java.util.HashMap;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

public class CourseLevelLearningObj {
	@Attribute(name = "course_id")
	int course_id;
	@Element(name = "title")
	String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Element(name = "rookie")
	int rookie;
	@Element(name = "apprentice")
	int apprentice;
	@Element(name = "novice")
	int novice;
	@Element(name = "wizard")
	int wizard;

	public int getRookie() {
		return rookie;
	}

	public void setRookie(int rookie) {
		this.rookie = rookie;
	}

	public int getApprentice() {
		return apprentice;
	}

	public void setApprentice(int apprentice) {
		this.apprentice = apprentice;
	}

	public int getNovice() {
		return novice;
	}

	public void setNovice(int novice) {
		this.novice = novice;
	}

	public int getWizard() {
		return wizard;
	}

	public void setWizard(int wizard) {
		this.wizard = wizard;
	}

	@ElementMap(entry = "learn_obj", key = "key")
	HashMap<Integer, AndroidLearningObj> learn_obj;

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public HashMap<Integer, AndroidLearningObj> getLearn_obj() {
		return learn_obj;
	}

	public void setLearn_obj(HashMap<Integer, AndroidLearningObj> learn_obj) {
		this.learn_obj = learn_obj;
	}

	public CourseLevelLearningObj() {
		super();
		// TODO Auto-generated constructor stub
	}
}