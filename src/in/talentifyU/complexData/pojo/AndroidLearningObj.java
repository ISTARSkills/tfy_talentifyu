package in.talentifyU.complexData.pojo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class AndroidLearningObj {
	@Attribute(name = "objId")
	int objId;
	@Element(name = "objName")
	String objName;

	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public AndroidLearningObj() {
		super();
		// TODO Auto-generated constructor stub
	}
}