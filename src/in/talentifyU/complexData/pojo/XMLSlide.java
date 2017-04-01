/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author vaibhav
 *
 */
public class XMLSlide {
	private Integer id;
	private String title;
	private ArrayList<XMLNote> myNotes;
	private ArrayList<XMLNote> sharedNotes;

	public XMLSlide() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLSlide(Integer id, String title, ArrayList<XMLNote> slideNotes) {
		super();
		this.id = id;
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	@XmlAttribute
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	@XmlAttribute
	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<XMLNote> getMyNotes() {
		return myNotes;
	}

	public void setMyNotes(ArrayList<XMLNote> myNotes) {
		this.myNotes = myNotes;
	}

	public ArrayList<XMLNote> getSharedNotes() {
		return sharedNotes;
	}

	public void setSharedNotes(ArrayList<XMLNote> sharedNotes) {
		this.sharedNotes = sharedNotes;
	}
}
