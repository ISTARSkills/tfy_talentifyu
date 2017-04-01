package in.talentifyU.utils.services;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.viksitpro.core.utilities.DBUTILS;

public class CoachReportUtils {

	public HashMap<Integer, ArrayList<HashMap<String, String>>> getSubSkills() {
		HashMap<Integer, ArrayList<HashMap<String, String>>> data = new HashMap<>();
		String sql = "SELECT 	child.skill_title AS child_title, 	child. ID AS child_id, 	parent. ID AS parent_id FROM 	skill parent, 	skill child WHERE 	child.parent_skill = parent.ID";
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		for (HashMap<String, Object> row : res) {
			int parent_id = (int) row.get("parent_id");
			int child_id = (int) row.get("child_id");
			String child_title = (String) row.get("child_title");
			if (data.containsKey(parent_id)) {
				ArrayList<HashMap<String, String>> xx = data.get(parent_id);
				HashMap<String, String> sub_skill = new HashMap<>();
				sub_skill.put("sub_skill_id", child_id + "");
				sub_skill.put("sub_skill_title", child_title);
				xx.add(sub_skill);
				data.put(parent_id, xx);
			} else {
				ArrayList<HashMap<String, String>> xx = new ArrayList<>();
				HashMap<String, String> sub_skill = new HashMap<>();
				sub_skill.put("sub_skill_id", child_id + "");
				sub_skill.put("sub_skill_title", child_title);
				xx.add(sub_skill);
				data.put(parent_id, xx);
			}

		}
		return data;
	}

	public HashMap<String, String> getOverAllPercentileForGraph(int user_id) {
		HashMap<String, String> data = new HashMap<>();
		DBUTILS util = new DBUTILS();
		String xaxis = "";
		String yaxis = "";
		Calendar cal = Calendar.getInstance();
		int average_batch_percentile = 0;
		// int batch_percentile=0;
		int i = 1;
		String sql = "SELECT 	CAST ( 		SUM (VALUE) / COUNT (*) AS INTEGER 	) AS percentile, 	skill_precentile. TYPE AS stype, to_date(	to_char( 		skill_precentile. TIMESTAMP, 		'MON-YY' 	),'MON-YY') created_at FROM 	skill_precentile WHERE 	student_id = "
				+ user_id
				+ " AND skill_precentile. TYPE = 'BATCH' AND skill_id IN ( 	SELECT DISTINCT 		skill_learning_obj_mapping.skill_id 	FROM 		assessment_question, 		learning_objective_question, 		skill_learning_obj_mapping 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_question.learning_objectiveid 	AND learning_objective_question.questionid = assessment_question.questionid 	AND assessment_question.assessmentid IN ( 		SELECT 			assessment. ID 		FROM 			lesson, 			cmsession, 			MODULE, 			course, 			assessment 		WHERE 			lesson.session_id = cmsession. ID 		AND cmsession.module_id = MODULE . ID 		AND course. ID = MODULE .course_id 		AND lesson.dtype = 'ASSESSMENT' 		AND assessment.lesson_id = lesson. ID 	) ) GROUP BY 	created_at, 	stype order by created_at ";
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		for (HashMap<String, Object> row : res) {
			i++;
			int batch_percentile = (int) row.get("percentile");
			average_batch_percentile = average_batch_percentile + batch_percentile;
			Date created_at = (Date) row.get("created_at");
			cal.setTime(created_at);
			xaxis = xaxis + "'" + new SimpleDateFormat("MMM").format(cal.getTime()) + "-"
					+ new SimpleDateFormat("YY").format(cal.getTime()) + "',";
			yaxis = yaxis + batch_percentile + ",";
		}
		average_batch_percentile = average_batch_percentile / 5;
		if (xaxis.length() >= 1 && xaxis.charAt(xaxis.length() - 1) == ',') {
			xaxis = xaxis.substring(0, xaxis.length() - 1);
			data.put("xaxis", xaxis);
		}
		if (yaxis.length() >= 1 && yaxis.charAt(yaxis.length() - 1) == ',') {
			yaxis = yaxis.substring(0, yaxis.length() - 1);
			data.put("yaxis", yaxis);
		}
		data.put("average_percentile", average_batch_percentile + "");
		return data;
	}

	public HashMap<String, String> getOverAllPercentile(int user_id) {
		DBUTILS util = new DBUTILS();
		int state_percentile = 0;
		int college_percentile = 0;
		int country_percentile = 0;
		int batch_percentile = 0;
		HashMap<String, String> data = new HashMap<>();
		String sql = "select cast(sum(value)/count(*) as INTEGER) as percentile, skill_precentile.type as stype  from skill_precentile where student_id="
				+ user_id
				+ " and  skill_id in (select distinct skill_learning_obj_mapping.skill_id from assessment_question, learning_objective_question, skill_learning_obj_mapping where skill_learning_obj_mapping.learning_objective_id = learning_objective_question.learning_objectiveid and learning_objective_question.questionid =assessment_question.questionid and assessment_question.assessmentid in (select assessment.id from lesson, cmsession, module, course, assessment where lesson.session_id = cmsession.id and cmsession.module_id = module.id and course.id = module.course_id and  lesson.dtype = 'ASSESSMENT' and assessment.lesson_id = lesson.id) ) group by stype";
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		for (HashMap<String, Object> row : res) {
			String type = (String) row.get("stype");
			if (type.equalsIgnoreCase("STATE")) {
				state_percentile = (int) row.get("percentile");
				data.put(type, state_percentile + "");
			} else if (type.equalsIgnoreCase("COLLEGE")) {
				college_percentile = (int) row.get("percentile");
				data.put(type, college_percentile + "");
			} else if (type.equalsIgnoreCase("BATCH")) {
				batch_percentile = (int) row.get("percentile");
				data.put(type, batch_percentile + "");
			} else if (type.equalsIgnoreCase("COUNTRY")) {
				country_percentile = (int) row.get("percentile");
				data.put(type, country_percentile + "");
			}

		}
		return data;
	}

	public ArrayList<ArrayList<Object>> getCourseWithRatings(int student_id) {
		ArrayList<ArrayList<Object>> table = new ArrayList<>();

		String sql1 = "select * from (select  distinct C.id as course_id , C.course_name as name from batch_group, batch, batch_students, course C where batch_group.id = batch.batch_group_id and batch_students.batch_group_id= batch_group.id and batch.course_id =  C.id and batch_students.student_id= "
				+ student_id
				+ ") t1 left join (SELECT 	( 		SUM ( 			R.score * 100 / A .number_of_questions 		) 	) / COUNT (*) AS score, 	C . ID AS course_id1 FROM 	report R, 	assessment A, 	cmsession CMM, 	MODULE MM, 	lesson LL, 	task TT, 	student S, 	course C WHERE 	R.user_id = S. ID AND A .lesson_id = LL. ID AND R.assessment_id = A . ID AND LL.dtype = 'ASSESSMENT' AND MM. ID = CMM.module_id AND MM.course_id = C . ID AND CMM. ID = LL.session_id AND LL. ID = TT.item_id AND TT.item_type = 'LESSON' AND TT.status = 'PUBLISHED' AND S. ID = "
				+ student_id + " GROUP BY 	C . ID ) t2 on t1.course_id = t2.course_id1 ";

		DBUTILS db = new DBUTILS();

		List<HashMap<String, Object>> results = db.executeQuery(sql1);

		for (HashMap<String, Object> courses : results) {
			BigInteger score = (BigInteger) courses.get("score");
			int sc = 0;
			if (courses.get("score") == null) {
				sc = 60;
			} else {
				sc = score.intValue();
			}
			int course_id = (int) courses.get("course_id");
			String course_name = (String) courses.get("name");
			ArrayList<Object> row = new ArrayList<>();
			row.add(course_id + "");
			row.add(course_name + "");
			row.add(sc + "");
			table.add(row);
		}

		return table;
	}

	public HashMap<String, String> getPercentileForCourse(int course_id, int user_id) {
		DBUTILS util = new DBUTILS();
		int state_percentile = 0;
		int college_percentile = 0;
		int country_percentile = 0;
		int batch_percentile = 0;
		HashMap<String, String> data = new HashMap<>();
		String sql = "select cast(sum(value)/count(*) as INTEGER) as percentile, skill_precentile.type as stype  from skill_precentile where student_id="
				+ user_id
				+ " and  skill_id in (select distinct skill_learning_obj_mapping.skill_id from assessment_question, learning_objective_question, skill_learning_obj_mapping where skill_learning_obj_mapping.learning_objective_id = learning_objective_question.learning_objectiveid and learning_objective_question.questionid =assessment_question.questionid and assessment_question.assessmentid in (select assessment.id from lesson, cmsession, module, course, assessment where lesson.session_id = cmsession.id and cmsession.module_id = module.id and course.id = module.course_id and course.id = "
				+ course_id + " and lesson.dtype = 'ASSESSMENT' and assessment.lesson_id = lesson.id) ) group by stype";
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		for (HashMap<String, Object> row : res) {
			String type = (String) row.get("stype");
			if (type.equalsIgnoreCase("STATE")) {
				state_percentile = (int) row.get("percentile");
				data.put(type, state_percentile + "");
			} else if (type.equalsIgnoreCase("COLLEGE")) {
				college_percentile = (int) row.get("percentile");
				data.put(type, college_percentile + "");
			} else if (type.equalsIgnoreCase("BATCH")) {
				batch_percentile = (int) row.get("percentile");
				data.put(type, batch_percentile + "");
			} else if (type.equalsIgnoreCase("COUNTRY")) {
				country_percentile = (int) row.get("percentile");
				data.put(type, country_percentile + "");
			}

		}
		return data;
	}

	public List<HashMap<String, Object>> getAllSkillsForCourse(int course_id) {
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT DISTINCT 	skill.id , skill.skill_title FROM 	assessment_question, 	learning_objective_question, 	skill_learning_obj_mapping, skill WHERE 	skill_learning_obj_mapping.learning_objective_id = learning_objective_question.learning_objectiveid and skill.id =   skill_learning_obj_mapping.skill_id AND learning_objective_question.questionid = assessment_question.questionid AND assessment_question.assessmentid IN ( 	SELECT 		assessment. ID 	FROM 		lesson, 		cmsession, 		MODULE, 		course, 		assessment 	WHERE 		lesson.session_id = cmsession. ID 	AND cmsession.module_id = MODULE . ID 	AND course. ID = MODULE .course_id 	AND course. ID = "
				+ course_id + " 	AND lesson.dtype = 'ASSESSMENT' 	AND assessment.lesson_id = lesson. ID )";
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		if (res.size() > 0) {
			data = res;
		}
		return data;
	}

	public List<HashMap<String, Object>> getQuestionPerSkill(int skill_id, int user_id) {
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT DISTINCT 	Q. ID, 	Q.duration_in_sec, 	Q.question_text, 	Q.question_type, 	SA.correct is_correct, 	 	( CASE SA.option1 WHEN 't' THEN ((SELECT AO.text||', ' as text FROM assessment_option AO WHERE AO.question_id=SA.question_id ORDER BY AO.id LIMIT 1 OFFSET 0)) ELSE '' END ) || ( CASE SA.option2 WHEN 't' THEN ((SELECT AO.text||', ' as text FROM assessment_option AO WHERE AO.question_id=SA.question_id ORDER BY AO.id LIMIT 1 OFFSET 1)) ELSE '' END ) || ( CASE SA.option3 WHEN 't' THEN ((SELECT AO.text||', ' as text FROM assessment_option AO WHERE AO.question_id=SA.question_id ORDER BY AO.id LIMIT 1 OFFSET 2)) ELSE '' END ) || ( CASE SA.option4 WHEN 't' THEN ((SELECT AO.text||', ' as text FROM assessment_option AO WHERE AO.question_id=SA.question_id ORDER BY AO.id LIMIT 1 OFFSET 3)) ELSE '' END ) || ( CASE SA.option5 WHEN 't' THEN ((SELECT AO.text as text FROM assessment_option AO WHERE AO.question_id=SA.question_id ORDER BY AO.id LIMIT 1 OFFSET 4)) ELSE '' END )as selected_options, (SELECT string_agg(AOO.text,', ')  FROM  assessment_option AOO  WHERE AOO.marking_scheme=1 AND AOO.question_id=SA.question_id  ) as correct_options FROM 	skill_learning_obj_mapping SLOM, 	learning_objective_question LOQ, 	question Q, 	student_assessment SA, 	assessment_option AO WHERE 	SLOM.learning_objective_id = LOQ.learning_objectiveid AND LOQ.questionid = Q. ID AND SLOM.skill_id = "
				+ skill_id + " AND SA.student_id = " + user_id + " AND SA.question_id = Q. ID";
		List<HashMap<String, Object>> res = util.executeQuery(sql);
		if (res.size() > 0) {
			data = res;
		}
		return data;
	}
	

}
