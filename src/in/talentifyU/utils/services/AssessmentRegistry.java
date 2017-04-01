package in.talentifyU.utils.services;

import in.talentifyU.complexData.pojo.XMLAssessment;

public class AssessmentRegistry {
	XMLAssessment assessment;
	int count_of_questions;
	int count_of_correct;

	public int getCount_of_questions() {
		return count_of_questions;
	}

	public void setCount_of_questions(int count_of_questions) {
		this.count_of_questions = count_of_questions;
	}

	public int getCount_of_correct() {
		return count_of_correct;
	}

	public void setCount_of_correct(int count_of_correct) {
		this.count_of_correct = count_of_correct;
	}

	public XMLAssessment getAssessment() {
		return assessment;
	}

	public void setAssessment(XMLAssessment assessment) {
		this.assessment = assessment;
	}

	private AssessmentRegistry() {
	}

	// private static class SingletonHolder {
	private static AssessmentRegistry INSTANCE = new AssessmentRegistry();
	// }

	public static AssessmentRegistry getInstance() {

		return INSTANCE;
	}
}
