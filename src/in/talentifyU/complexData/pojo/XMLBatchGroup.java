/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author vaibhav
 *
 */
public class XMLBatchGroup {
	private Integer id;
	private String name;
	private Integer orgid;
	private String orgname;
	ArrayList<XMLBatchMember> classmates;
	ArrayList<XMLBatch> btaches;

	public Integer getId() {
		return id;
	}

	public String getOrgname() {
		return orgname;
	}

	@XmlAttribute
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	@XmlAttribute
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrg_id() {
		return orgid;
	}

	@XmlAttribute(name = "organization_id")
	public void setOrg_id(Integer org_id) {
		this.orgid = org_id;
	}

	@XmlElement(name = "batch")
	@XmlElementWrapper(name = "batches")
	public ArrayList<XMLBatch> getBtaches() {
		return btaches;
	}

	public void setBtaches(ArrayList<XMLBatch> btaches) {
		this.btaches = btaches;
	}

	public ArrayList<XMLBatchMember> getClassmates() {
		return classmates;
	}

	@XmlElement(name = "classmate")
	@XmlElementWrapper(name = "classmates")
	public void setClassmates(ArrayList<XMLBatchMember> classmates) {
		this.classmates = classmates;
	}

	public XMLBatchGroup(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public XMLBatchGroup() {
		super();
	}
}
