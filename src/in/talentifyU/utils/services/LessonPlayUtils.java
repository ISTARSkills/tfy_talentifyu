package in.talentifyU.utils.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.istarindia.android.utils.PlayHolder;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.LessonDAO;

public class LessonPlayUtils {

	public ArrayList<PlayHolder> findPresFromCMsession(int cmsession_id) {
		com.istarindia.apps.dao.UUIUtils.printlog("finding in sessioon");
		ArrayList<PlayHolder> list_of_lessons = new ArrayList<>();
		IstarUserDAO dao = new IstarUserDAO();
		Session session = dao.getSession();
		String sql1 = "select * from ((SELECT DISTINCT 	L. ID AS lesson_id, 	L.dtype AS TYPE, 	L.order_id FROM 	lesson L, 	task T, 	cmsession CM, presentaion P, slide S WHERE 	L. ID = T.item_id AND T.status = 'PUBLISHED' AND CM. ID = L.session_id AND T.item_type = 'LESSON' AND CM. ID = "
				+ cmsession_id
				+ " AND P.lesson_id = L.id AND S.presentation_id = P.id ORDER BY 	L.order_id ) UNION (SELECT DISTINCT 	L. ID AS lesson_id, 	L.dtype AS TYPE, 	L.order_id FROM 	lesson L, 	task T, 	cmsession CM, assessment A, assessment_question AQ WHERE 	L. ID = T.item_id AND T.status = 'PUBLISHED' AND CM. ID = L.session_id AND T.item_type = 'LESSON' AND CM. ID = "
				+ cmsession_id
				+ " AND L.dtype = 'ASSESSMENT' AND A.lesson_id = L.id AND AQ.assessmentid = A.id ORDER BY 	L.order_id )) as FINAL_TABLE order by FINAL_TABLE.order_id";
		SQLQuery query = session.createSQLQuery(sql1);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> results = query.list();

		com.istarindia.apps.dao.UUIUtils.printlog("-----" + results.size());
		for (HashMap<String, Object> key_val : results) {
			int lesson_id = (int) key_val.get("lesson_id");
			String type = (String) key_val.get("type");

			try {
				PlayHolder p = getPlayHolder(lesson_id, type);
				if (p != null) {
					list_of_lessons.add(p);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "bad lessson " + lesson_id);
				e.printStackTrace();
			}

		}
		com.istarindia.apps.dao.UUIUtils.printlog("total lessons" + list_of_lessons.size());
		return list_of_lessons;
	}

	private PlayHolder getPlayHolder(int lesson_id, String type) {
		IstarUserDAO dao = new IstarUserDAO();
		Session session = dao.getSession();

		if (!type.equalsIgnoreCase("ASSESSMENT")) {
			PlayHolder p = new PlayHolder();
			p.setNextLessonType("xyz");
			p.setCurrentLessonID(lesson_id);

			p.setCurrentLessonType(type);

			String sql11 = "(select S.id as start_slide_id from slide S, lesson L, presentaion P where P.lesson_id= L.id AND L.id ="
					+ lesson_id
					+ " AND S.presentation_id = P.id order by S.order_id asc limit 1  ) UNION ALL ( select S.id as last_slide_id  from slide S, lesson L, presentaion P where P.lesson_id= L.id AND L.id ="
					+ lesson_id + " AND S.presentation_id = P.id order by S.order_id desc limit 1 )";

			SQLQuery query11 = session.createSQLQuery(sql11);
			query11.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<HashMap<String, Object>> results11 = query11.list();
			if (results11.size() > 0) {
				p.setFirstSlideID(results11.get(0).get("start_slide_id").toString());
				// results11.remove(0);
				p.setLastSlideID(results11.get(1).get("start_slide_id").toString());
				return p;
			} else {
				return null;
			}
		} else {
			PlayHolder p = new PlayHolder();
			p.setNextLessonType("xyz");
			p.setCurrentLessonID(lesson_id);

			p.setCurrentLessonType(type);
			return p;
		}

	}

	public ArrayList<PlayHolder> findPresFromCourse(int course_id) {
		ArrayList<PlayHolder> lesson_list = new ArrayList<>();
		IstarUserDAO dao = new IstarUserDAO();
		Session session = dao.getSession();
		String sql1 = "SELECT DISTINCT 	lesson. ID as lesson_id, 	lesson.dtype as type, lesson.order_id FROM 	lesson, 	task, 	course, 	module, cmsession WHERE lesson. ID = task.item_id AND task.status = 'PUBLISHED' AND module.course_id ="
				+ course_id
				+ " AND module.id = cmsession.module_id AND cmsession.ID = lesson.session_id and task.item_type='LESSON' order by lesson.order_id";
		com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), sql1);
		SQLQuery query = session.createSQLQuery(sql1);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> results = query.list();

		for (HashMap<String, Object> key_val : results) {
			int lesson_id = (int) key_val.get("lesson_id");
			String type = (String) key_val.get("type");
			try {

				PlayHolder p = getPlayHolder(lesson_id, type);

				if (p != null) {
					lesson_list.add(p);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "bad lessson " + lesson_id);
				e.printStackTrace();
			}

		}
		return lesson_list;
	}

	public StringBuffer getSlides(int lessonID, String user_type) {
		Lesson ppt = (new LessonDAO()).findById(lessonID);
		String lesson_theme = ppt.getLesson_theme();
		String str = "";
		if (user_type.equalsIgnoreCase("TRAINER")) {
			str = ppt.getPresentaion().outputSlidesForTrainer();
		} else if (user_type.equalsIgnoreCase("PRESENTOR")) {
			str = ppt.getPresentaion().outputSlidesForDesktop();
		} else if (user_type.equalsIgnoreCase("STUDENT")) {
			str = ppt.getPresentaion().outputSlides();
		}

		return new StringBuffer(str);

	}

	public StringBuffer getLaunchTestForLearn(String assessment_id, String duration, String session_title, String title,
			String ppt_id, String course_id, String slide_id, String module_id, String cmsession_id,
			String session_list, String next_session_id, String previous_session_id, String source, String previous_lesson_id) {

		System.out.println(
				"--------------------------------------------------play util next_session_id" + next_session_id);

		StringBuffer bf = new StringBuffer();
		bf.append("<input type='hidden' id='assessment_id' name='assessment_id' value='" + assessment_id + "'>");
		bf.append("<input type='hidden' id='duration' name='duration' value='" + duration + "'>");
		bf.append("<div class='col s12 m8 l9'>");
		bf.append("<div class='card launch-assessment-page'>");
		
		bf.append("		<div class='card-content'>");
		bf.append("			<p class='session-title animated bounceInDown'>" + session_title.toUpperCase() + "</p>");
		bf.append("			<p class='lesson-title animated zoomIn delay-1'>" + title + "</p>");
		bf.append("			<p class='instructions animated bounceIn delay-2'>");
		bf.append("				All questions are compulsory.<br>");
		bf.append("				All questions are OBJECTIVE multiple choice.<br>");
		bf.append("				Each question will have at least 4 choices.");
		bf.append("			</p>");
		bf.append("		</div>");
		
		bf.append("		<div class='action'>");
		bf.append("			<a href='/start_learn_assessment?assessment_id=" + assessment_id + "&duration=" + duration + ""
								+ "&course_id=" + course_id + "&slide_id=" + slide_id + "&module_id=" + module_id + "&cmsession_id="
								+ cmsession_id + "&next_session_id=" + next_session_id + "&previous_session_id=" + previous_session_id
								+ "&source=" + source + "&previous_lesson_id="+previous_lesson_id+"' ");
		bf.append("				class='btn button-shadow waves-effect waves-light center launch-button animated fadeInUpBig delay-2'><label>Launch Test</label>");
		bf.append("			</a>");
		bf.append("		</div>");
		
		bf.append("</div></div>");
		
		return bf;
	}

	public StringBuffer getLaunchTest(String assessment_id, String duration, String session_title, String title) {

		StringBuffer bf = new StringBuffer();
		bf.append("<input type='hidden' id='assessment_id' name='assessment_id' value='" + assessment_id + "'>");
		bf.append("<input type='hidden' id='duration' name='duration' value='" + duration + "'>");
		bf.append("<div class='col s12 m8 l9'>");
		bf.append("<div class='card launch-assessment-page'>");
		
		bf.append("		<div class='card-content'>");
		bf.append("			<p class='session-title animated bounceInDown'>" + session_title.toUpperCase() + "</p>");
		bf.append("			<p class='lesson-title animated zoomIn delay-1'>" + title + "</p>");
		bf.append("			<p class='instructions animated bounceIn delay-2'>");
		bf.append("				All questions are compulsory.<br>");
		bf.append("				All questions are OBJECTIVE multiple choice.<br>");
		bf.append("				Each question will have at least 4 choices.");
		bf.append("			</p>");
		bf.append("		</div>");
		
		bf.append("		<div class='action'>");
		bf.append("			<a href='/start_assessment_event?assessment_id=" + assessment_id + "&duration=" + duration + "' ");
		bf.append("				class='btn button-shadow waves-effect waves-light center launch-button animated fadeInUpBig delay-2'><label>Launch Test</label>");
		bf.append("			</a>");
		bf.append("		</div>");
		
		bf.append("</div></div>");
		
		return bf;
	}
	
	
	/*
	 * 
	public StringBuffer getStartTest() {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<input type='hidden' name='next_question_id' 			value'<%=next_question_id%>'> <input type='hidden' 			name=\"start_time\" value=\"<%=start_time%>\"> <input 			type=\"hidden\" name=\"lesson_id\" value=\"<%=lesson_id%>\"> <input 			type=\"hidden\" name=\"assessment_id\" value=\"<%=assessment_id%>\"> 		<input type=\"hidden\" name=\"duration\" value=\"<%=duration%>\"> <input 			type=\"hidden\" name=\"student_id\" 			value=\"<%=user.getId()%>\"> 		<div class=\"col s12 m8 l9\"> 			<div class=\"row\" style=\"margin: 0px;\"> 				<div class=\"col s12 m8 l9\" style=\"padding: 0px;\"> 					<div class=\"card  gainsboro\" 						style=\"height: 586px; margin: 0px; background: #e4e3e2\"> 						<div class=\"card-content black-text\" 							style=\"background: white; height: 45px; padding: 10px; font-size: 18px; font-family: Roboto;\"> 							<div class=\"col s4\" 								style=\"padding-left: 6px !important; border: none;\"> 								<p style=\"display: inline\"> 									<i class=\"mdi-image-timer\"></i> 								</p> 								<p id=\"minutes\" style=\"display: inline;\"><%=remaining_min %></p> 								<p style=\"display: inline\">:</p> 								<p id=\"seconds\" style=\"display: inline;\"><%=remaining_sec %></p> 							</div> 							<div class=\"divider\" 								style=\"position: absolute; right: 249px; right: 249px; width: 1px; height: 28px; margin-right: 22px; background-color: black !important;\"></div> 							<p 								style=\"display: inline; color: #e56f60; font-style: italic; font-size: 14px; visibility: hidden\" 								id=\"timer_msg\">QUICK - Less than one minute is remaining</p> 							<p style=\"display: inline; float: right; margin-right: 9px\"><%=que_display%></p> 						</div> 						<div class=\"card-content black-text\" 							style=\"font-size: 15px; font-family: Roboto;\"> 							<p><%=single_assessment.getQuestions().get(next_question_id).getQuestion_text()%></p> 						</div> 						<div class=\"card-action\">  							<table> 								<% 									HashMap<Integer, AndroidCMSOption> hmap = single_assessment.getQuestions().get(next_question_id) 											.getOptions(); 									Map<Integer, AndroidCMSOption> map = new TreeMap<Integer, AndroidCMSOption>(hmap); 									int count = 1; 									for (int j : map.keySet()) { 								%>  								<% 									if (count == 1) { 											count++; 								%> 								<tr style=\"font-size: 15px; font-family: Roboto;\"> 									<td>A.</td> 									<td><label for=\"test1\" 										style=\"font-size: 15px; font-family: Roboto; color: #3B3C36 !important;\"><%=(map.get(j).getOptionValue())%></label> 									</td> 									<td><input name=\"group1\" type=\"radio\" id=\"option1\" 										style=\"visibility: visible !important; position: static !important; font-size: 15px; font-family: Roboto; color: #3B3C36;\"></td> 								</tr>   								<% 									} else if (count == 2) { 											count++; 								%> 								<tr> 									<td>B.</td> 									<td><label for=\"test1\" 										style=\"font-size: 15px; font-family: Roboto; color: #3B3C36 !important;\"><%=(map.get(j).getOptionValue())%></label> 									</td> 									<td><input name=\"group2\" type=\"radio\" id=\"option2\" 										style=\"visibility: visible !important; position: static !important; color: #3B3C36 !important;\"></td> 								</tr>  								<% 									} else if (count == 3) { 											count++; 								%> 								<tr> 									<td>C.</td> 									<td><label for=\"test1\" 										style=\"font-size: 15px; font-family: Roboto; color: #3B3C36 !important;\"><%=(map.get(j).getOptionValue())%></label> 									</td> 									<td><input name=\"group3\" type=\"radio\" id=\"option3\" 										style=\"visibility: visible !important; position: static !important; color: #3B3C36 !important;\"></td> 								</tr>  								<% 									} else if (count == 4) { 											count++; 								%> 								<tr> 									<td>D.</td> 									<td><label for=\"test1\" 										style=\"font-size: 15px; font-family: Roboto; color: #3B3C36 !important;\"><%=(map.get(j).getOptionValue())%></label> 									</td> 									<td><input name=\"group4\" type=\"radio\" id=\"option4\" 										style=\"visibility: visible !important; position: static !important; color: #3B3C36 !important;\"></td> 								</tr> 								<% 									} 								%>  								<% 									} 								%> 							</table> 							<button name=\"action\" type=\"submit\" id=\"submitbtn\" 								class=\"btn waves-effect waves-light center\" 								style=\"margin: auto 0; margin-top: 10px; text-align: center; display: inline-block; border-radius: 80px; background-color: #ffffff; color: #000000;\">Submit</button> 						</div> 					</div> 				</div> 			</div> 		</div>");
		return sb;
	} */
}
