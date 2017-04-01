/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ComplexObject
 *
 */
public class XMLSkillGraph {
	ArrayList<XMLGraphPoints> graphPoints;

	public XMLSkillGraph() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<XMLGraphPoints> getGraphPoints() {
		return graphPoints;
	}

	@XmlElement(name = "graph_point")
	@XmlElementWrapper(name = "graph_points")
	public void setGraphPoints(ArrayList<XMLGraphPoints> graphPoints) {
		this.graphPoints = graphPoints;
	}
}
