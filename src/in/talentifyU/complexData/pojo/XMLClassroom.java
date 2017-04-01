/**
 * 
 */
package in.talentifyU.complexData.pojo;

/**
 * @author vaibhav
 *
 */
public class XMLClassroom {
	private int classId;
	private String className;
	private String ipAddress;

	public XMLClassroom(int classId, String className, String ipAddress) {
		super();
		this.classId = classId;
		this.className = className;
		this.ipAddress = ipAddress;
	}

	public XMLClassroom() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
