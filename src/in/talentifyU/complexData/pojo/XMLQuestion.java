package in.talentifyU.complexData.pojo;

import java.util.ArrayList;

public class XMLQuestion {
	private String questionText;
	private String questionType;
	private Integer durationInSec;
	private int queId;
	private int orderId;
	private ArrayList<XMLOption> options;

	public XMLQuestion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Integer getDurationInSec() {
		return durationInSec;
	}

	public void setDurationInSec(Integer durationInSec) {
		this.durationInSec = durationInSec;
	}

	public int getQueId() {
		return queId;
	}

	public void setQueId(int queId) {
		this.queId = queId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public ArrayList<XMLOption> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<XMLOption> options) {
		this.options = options;
	}
}
