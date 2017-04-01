/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ComplexObject
 *
 */
public class XMLSkillReportLAData {
	Integer skillId;
	String skillName;
	Integer pointsEarned;
	Integer totalPoints;
	Integer rating;
	String imageUrl;
	Integer percentileCountry;
	Integer percentileglobe;
	Integer percentileBatch;
	Integer percentileOrganozation;
	Integer rank;
	ArrayList<XMLSkillReportLAData> subSkills;
	XMLSkillGraph graphData;

	public XMLSkillReportLAData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getSkillId() {
		return skillId;
	}

	@XmlAttribute
	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	@XmlAttribute
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public Integer getPointsEarned() {
		return pointsEarned;
	}

	@XmlAttribute
	public void setPointsEarned(Integer pointsEarned) {
		this.pointsEarned = pointsEarned;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	@XmlAttribute
	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Integer getRating() {
		return rating;
	}

	@XmlAttribute
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@XmlAttribute
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ArrayList<XMLSkillReportLAData> getSubSkills() {
		return subSkills;
	}

	@XmlElement(name = "subskill")
	@XmlElementWrapper(name = "subskills")
	public void setSubSkills(ArrayList<XMLSkillReportLAData> subSkills) {
		this.subSkills = subSkills;
	}

	public XMLSkillGraph getGraphData() {
		return graphData;
	}

	@XmlElement(name = "skill_graph")
	public void setGraphData(XMLSkillGraph graphData) {
		this.graphData = graphData;
	}

	public Integer getPercentileCountry() {
		return percentileCountry;
	}

	@XmlAttribute
	public void setPercentileCountry(Integer percentileCountry) {
		this.percentileCountry = percentileCountry;
	}

	public Integer getPercentileglobe() {
		return percentileglobe;
	}

	@XmlAttribute
	public void setPercentileglobe(Integer percentileglobe) {
		this.percentileglobe = percentileglobe;
	}

	public Integer getPercentileBatch() {
		return percentileBatch;
	}

	@XmlAttribute
	public void setPercentileBatch(Integer percentileBatch) {
		this.percentileBatch = percentileBatch;
	}

	public Integer getPercentileOrganozation() {
		return percentileOrganozation;
	}

	@XmlAttribute
	public void setPercentileOrganozation(Integer percentileOrganozation) {
		this.percentileOrganozation = percentileOrganozation;
	}

	public Integer getRank() {
		return rank;
	}

	@XmlAttribute
	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
