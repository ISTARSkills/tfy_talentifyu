/**
 * 
 */
package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ComplexObject
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLReportQuestion {
	@XmlAttribute
	Integer questionId;
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	String questionText;
	@XmlElement(name = "options")
	@XmlElementWrapper(name = "option")
	ArrayList<XMLReportOption> options;
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	String correctAnswer;
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	String selectedAnswer;
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	String explanation;
	@XmlAttribute
	String correctness;
	@XmlAttribute
	String dificultyLevel;
	@XmlAttribute
	String timeTaken;

	public XMLReportQuestion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public ArrayList<XMLReportOption> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<XMLReportOption> options) {
		this.options = options;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(String selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getCorrectness() {
		return correctness;
	}

	public void setCorrectness(String correctness) {
		this.correctness = correctness;
	}

	public String getDificultyLevel() {
		return dificultyLevel;
	}

	public void setDificultyLevel(String dificultyLevel) {
		this.dificultyLevel = dificultyLevel;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}
}
