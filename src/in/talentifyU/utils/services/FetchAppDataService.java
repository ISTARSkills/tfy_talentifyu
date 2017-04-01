package in.talentifyU.utils.services;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.ocpsoft.prettytime.PrettyTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.istarindia.apps.dao.BatchScheduleEvent;
import com.istarindia.apps.dao.College;
import com.istarindia.apps.dao.IstarAssessmentEvent;
import com.istarindia.apps.dao.IstarEvent;
import com.istarindia.apps.dao.Student;
import com.istarindia.apps.dao.StudentDAO;
import com.istarindia.apps.dao.StudentNote;
import com.istarindia.apps.dao.StudentNoteDAO;
import com.istarindia.apps.dao.StudentShare;
import com.istarindia.apps.dao.StudentShareDAO;
import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentOption;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.BatchGroup;
import com.viksitpro.core.dao.entities.BatchStudents;
import com.viksitpro.core.dao.entities.Cmsession;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.dao.entities.TrainerBatch;
import com.viksitpro.core.dao.utils.HibernateSessionFactory;
import com.viksitpro.core.utilities.DBUTILS;

import in.talentifyU.complexData.pojo.XMLAddress;
import in.talentifyU.complexData.pojo.XMLAssessment;
import in.talentifyU.complexData.pojo.XMLBatch;
import in.talentifyU.complexData.pojo.XMLBatchGroup;
import in.talentifyU.complexData.pojo.XMLBatchMember;
import in.talentifyU.complexData.pojo.XMLCoachStudent;
import in.talentifyU.complexData.pojo.XMLCourse;
import in.talentifyU.complexData.pojo.XMLCourseRating;
import in.talentifyU.complexData.pojo.XMLEvents;
import in.talentifyU.complexData.pojo.XMLInterview;
import in.talentifyU.complexData.pojo.XMLInvite;
import in.talentifyU.complexData.pojo.XMLLesson;
import in.talentifyU.complexData.pojo.XMLNote;
import in.talentifyU.complexData.pojo.XMLNotification;
import in.talentifyU.complexData.pojo.XMLOffer;
import in.talentifyU.complexData.pojo.XMLOption;
import in.talentifyU.complexData.pojo.XMLOrganization;
import in.talentifyU.complexData.pojo.XMLPincode;
import in.talentifyU.complexData.pojo.XMLQuestion;
import in.talentifyU.complexData.pojo.XMLSession;
import in.talentifyU.complexData.pojo.XMLSlide;
import in.talentifyU.complexData.pojo.XMLStudent;
import in.talentifyU.complexData.pojo.XMLStudentProfile;
import in.talentifyU.complexData.pojo.XMLTest;

public class FetchAppDataService {
	XMLStudent xs;
	Student s;
	int lCounter = 0;

	public void sendData(String type, int student_id, HttpServletResponse response, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();

		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(introspector);
		StudentDAO dao = new StudentDAO();
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		s = dao.findById(student_id);
		session.evict(s);
		s = dao.findById(student_id);
		xs = new XMLStudent();
		Object object = null;
		switch (type) {
		case "COMPLEXOBJECT":
			xs = new XMLStudent();
			xs.setFatherName(s.getFatherName());
			xs.setMotherName(s.getMotherName());
			xs.setId(s.getId());
			xs.setName(s.getName());
			xs.setEmail(s.getEmail());
			xs.setImage_url(s.getImageUrl());
			xs.setUserType(s.getUserType());
			xs.setAddress(new XMLAddress(s.getAddress().getId(), new XMLPincode(s.getAddress().getPincode().getId(), s.getAddress().getPincode().getState(), s.getAddress().getPincode().getCity(), s.getAddress().getPincode().getCountry(), s.getAddress().getPincode().getPin()), s.getAddress().getAddressline1(), s.getAddress().getAddressline2(), s.getAddress().getAddressGeoLongitude(), s.getAddress().getAddressGeoLatitude()));
			College org = s.getCollege();
			xs.setOrganization(new XMLOrganization(org.getName(), new XMLAddress(org.getAddress().getId(), new XMLPincode(s.getAddress().getPincode().getId(), s.getAddress().getPincode().getState(), s.getAddress().getPincode().getCity(), s.getAddress().getPincode().getCountry(), s.getAddress().getPincode().getPin()), org.getAddress().getAddressline1(), org.getAddress().getAddressline2(), org.getAddress().getAddressGeoLongitude(), org.getAddress().getAddressGeoLatitude())));
			object = xs;
			break;
		case "Events":
			object = getAllEvents(s);
			break;
		case "XMLNote":
			object = getAllMyNotes(s);
			break;
		case "SHAREXMLNote":
			object = getAllMySharedNotes(s);
			break;
		case "XMLBatchGroup":
			ArrayList<XMLBatchGroup> batchGroups = new ArrayList<>();
			for (BatchStudents bg : s.getBatchStudentses()) {
				XMLBatchGroup e = new XMLBatchGroup();
				e.setId(bg.getBatchGroup().getId());
				e.setName(bg.getBatchGroup().getName());
				e.setOrg_id(bg.getBatchGroup().getCollege().getId());
				e.setOrgname(bg.getBatchGroup().getCollege().getName());
				ArrayList<XMLBatch> btaches = getBatches(bg.getBatchGroup());
				e.setBtaches(btaches);
				ArrayList<XMLBatchMember> classmates = getClassMates(bg.getBatchGroup());
				e.setClassmates(classmates);
				batchGroups.add(e);
			}
			object = batchGroups;
			break;
		case "XMLCoachStudent":
			object= getCoachStudents(s, request);
			break;
		case "XMLInterview":
			object= getInterviews(s);

			break;
		case "XMLInvite":
			object = getInvites(s);
			break;
		case "XMLNotification":
			object = getNotifications(s.getId());
			break;
		case "XMLOffer":
			object = getOffers(s);
			break;
		case "XMLStudentProfile":
			object = getStudentProfile(s);
			break;
		case "XMLTest":
			object = getTests(s);
			break;
		}
		
		String result;
		try {
			result = mapper.writeValueAsString(object);
		response.getWriter().println(result);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private XMLStudentProfile getStudentProfile(Student s2) {
		// TODO Auto-generated method stub
		String getStudentProfile = "SELECT student_id, firstname, lastname, cast (dob as varchar(50)), mobileno, gender, pincode,        aadharno, email,"
				+ " yop_10, marks_10, yop_12, marks_12, has_under_graduation,      "
				+ " under_graduation_specialization_name, under_gradution_marks,        has_post_graduation, post_graduation_specialization_name,"
				+ " post_gradution_marks,        is_studying_further_after_degree, job_sector, preferred_location,        company_name, position, "
				+ "duration, description, interested_in_type_of_course,        area_of_interest, image_url_10, image_url_12, profile_image,      "
				+ "  id, under_graduate_degree_name, pg_degree_name, resume_url, user_category   FROM public.student_profile_data where student_id="
				+ s2.getId();
		List<HashMap<String, Object>> data = new DBUTILS().executeQuery(getStudentProfile);
		XMLStudentProfile prof = new XMLStudentProfile();
		if (data.size() > 0) {
			for (HashMap<String, Object> row : data) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				prof.setUser_id((Integer) row.get("student_id"));
				prof.setEmail((String) row.get("email"));
				prof.setAadharno(((BigInteger) row.get("aadharno")).longValue());
				prof.setFirstname((String) row.get("firstname"));
				prof.setLastname((String) row.get("lastname"));
				prof.setArea_of_interest((String) row.get("area_of_interest"));
				prof.setGender((String) row.get("gender"));
				prof.setMobileno(((BigInteger) row.get("mobileno")).longValue());
				prof.setPincode((Integer) row.get("pincode"));
				prof.setYop_10((Integer) row.get("yop_10"));
				prof.setYop_12((Integer) row.get("yop_12"));
				if (row.get("marks_10") != null) {
					prof.setMarks_10(((Float) row.get("marks_10")).floatValue());
				}
				if (row.get("marks_12") != null) {
					prof.setMarks_12(((Float) row.get("marks_12")).floatValue());
				}
				if (row.get("dob") != null) {
					prof.setDob((String) row.get("dob"));
				}
				prof.setHas_under_graduation((Boolean) row.get("has_under_graduation"));
				prof.setUnder_graduation_specialization_name((String) row.get("under_graduation_specialization_name"));
				if (row.get("under_gradution_marks") != null) {
					prof.setUnder_gradution_marks(((Float) row.get("under_gradution_marks")).floatValue());
				}
				if (row.get("post_gradution_marks") != null) {
					prof.setPost_gradution_marks(((Float) row.get("post_gradution_marks")).floatValue());
				}

				prof.setHas_post_graduation((Boolean) row.get("has_post_graduation"));
				prof.setPost_graduation_specialization_name((String) row.get("post_graduation_specialization_name"));

				prof.setIs_studying_further_after_degree((Boolean) row.get("is_studying_further_after_degree"));
				prof.setJob_sector((String) row.get("job_sector"));

				prof.setPreferred_location((String) row.get("preferred_location"));
				prof.setCompany_name((String) row.get("company_name"));
				prof.setDuration((String) row.get("duration"));
				prof.setDescription((String) row.get("description"));
				prof.setPosition((String) row.get("position"));

				prof.setInterested_in_type_of_course((String) row.get("interested_in_type_of_course"));
				prof.setImage_url_10((String) row.get("image_url_10"));
				prof.setImage_url_12((String) row.get("image_url_12"));
				prof.setProfile_image((String) row.get("profile_image"));
				prof.setUnder_graduate_degree_name((String) row.get("under_graduate_degree_name"));
				prof.setPg_degree_name((String) row.get("pg_degree_name"));
				prof.setResume_url((String) row.get("resume_url"));
				if (row.get("user_category") != null) {
					prof.setUser_category((String) row.get("user_category"));
				} else {
					prof.setUser_category("COLLEGE");
				}

			}
		}
		return prof;
	}

	private ArrayList<XMLTest> getTests(Student s2) {
		ArrayList<XMLTest> tests = new ArrayList<>();
		DBUTILS util = new DBUTILS();
		String sql1 = "SELECT distinct 	CAST(JE. ID as varchar(50)) AS event_id, 	JE.eventhour AS hours, 	JE.eventminute AS mins, 	"
				+ "JE.eventdate AS event_date, 	JE.created_at as creation_time, 	JE. TYPE AS event_type, 	"
				+ "V.profile_title AS profile_title, 	V.location AS location, 	V.offer_letter as offer_letter,"
				+ " 	JE.vacancy_id as vacancy_id, 	V.description as description, 	RC. NAME AS company_name, 	"
				+ "R.name as recruiter_name, 	RC.image as image_url, 	JE.status AS status, 	"
				+ "A.id as assessment_id, 	A.assessmenttitle AS assessment_title, 	"
				+ "A .number_of_questions AS number_of_questions, 	A .assessmentdurationminutes AS duration FROM 	jobs_event JE, 	vacancy V, 	company RC, 	recruiter R, 	assessment A, 	report RE 	 WHERE JE.actor_id = "
				+ s.getId()
				+ " AND V. ID = JE.vacancy_id AND V.recruiter_id= R.id AND V.company_id = RC. ID AND JE.isactive = 't' AND JE.status LIKE '%ASSESSMENT%' AND A.id = (select CAST (split_part(task_action, '__', 2) as integer) as ass_id from istar_task_workflow_concrete where event_id=JE.id AND stage='ASSESSMENT' ) AND RE.assessment_id != A.id AND RE.user_id != "
				+ s.getId();
		List<HashMap<String, Object>> results = util.executeQuery(sql1);
		for (HashMap<String, Object> test : results) {
			try {
				String company_name = (String) test.get("company_name");
				String image_url = (String) test.get("image_url");
				String vacancy_job_title = (String) test.get("profile_title");
				String vacancy_location = (String) test.get("location");
				String event_id = (String) test.get("event_id");
				String assessment_title = (String) test.get("assessment_title");
				Timestamp posted_time = (Timestamp) test.get("creation_time");
				int duration = 30;
				if (test.get("duration") != null) {
					duration = (Integer) test.get("duration");
				}
				// int duration = (Integer) test.get("duration");
				int number_of_questions = (Integer) test.get("number_of_questions");
				// String date_time =
				// ((Timestamp)test.get("eventdate")).toString();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Timestamp dt = (Timestamp) test.get("event_date");
				SimpleDateFormat formatter_day = new SimpleDateFormat("EEE");
				String test_day = formatter_day.format(formatter.parse(dt.toString()));
				SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");
				String test_time = format_time.format(formatter.parse(dt.toString()));
				SimpleDateFormat format_date = new SimpleDateFormat("EEE, MMM d");
				String test_date = format_date.format(formatter.parse(dt.toString()));
				String prettyTimeString = new PrettyTime().format(formatter.parse(posted_time.toString()));
				int assessment_id = (int) test.get("assessment_id");
				int vacancy_id = (int) test.get("vacancy_id");
				XMLTest test_row = new XMLTest();
				test_row.setAssessmentId(assessment_id);
				test_row.setAssessmentTitle(assessment_title);
				test_row.setCompanyName(company_name);
				test_row.setDuration(duration);
				test_row.setImageUrl(image_url);
				test_row.setJobTitle(vacancy_job_title);
				test_row.setLocation(vacancy_location);
				test_row.setNumberOfQuestions(number_of_questions);
				test_row.setPostedHoursAgo(prettyTimeString);
				test_row.setStudentInviteId(event_id);
				test_row.setTestDate(test_date);
				test_row.setTestTime(test_time);
				test_row.setVacancyId(vacancy_id);
				tests.add(test_row);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tests;
	}

	private ArrayList<XMLInterview> getInterviews(Student s2) {
		DBUTILS util = new DBUTILS();
		ArrayList<XMLInterview> interviews = new ArrayList<>();

		String sql1 = "select distinct CAST(JE. ID as varchar(50)) AS event_id, JE.eventhour AS hours,"
				+ " 	JE.eventminute AS mins,  panelist_schedule.event_date,JE.created_at as creation_time, JE. TYPE AS event_typ,"
				+ " V.profile_title AS profile_title, 	V.location AS location, JE.vacancy_id as vacancy_id, 	V.description as description,"
				+ " 	RC. NAME AS company_name, R.name as recruiter_name, RC.image as image_url, string_agg(panelist.email, '!#')  as panelists,"
				+ " panelist_schedule.meeting_id, panelist_schedule.meeting_password, panelist_schedule.zoom_host_url, panelist_schedule.zoom_join_url "
				+ "from jobs_event JE,istar_task_type, istar_task_workflow, stage_action_mapping, panelist_schedule, panelist ,vacancy V, 	company RC, "
				+ "	recruiter R where JE.actor_id=" + s2.getId()
				+ " AND V. ID = JE.vacancy_id AND V.recruiter_id= R.id AND V.company_id = RC. ID "
				+ "and cast(istar_task_type.id as varchar(50))  = cast (JE.task_id as varchar(50)) and cast(istar_task_type.id as varchar(50)) = cast(istar_task_workflow.task_id as varchar(50)) "
				+ "and istar_task_workflow.id = stage_action_mapping.stage_id and lower(stage_action_mapping.type)='interview' "
				+ "and cast(panelist_schedule.jobs_event_id as varchar(50)) = cast(JE.id as varchar(50)) and panelist_schedule.status ='SCHEDULED' "
				+ "and panelist_schedule.panelist_id = panelist.id "
				+ "GROUP BY event_id, je.eventhour,je.created_at,je.type,je.vacancy_id, v.profile_title,je.action,v.location,v.description,rc.name,"
				+ "r.name,rc.image, je.eventminute,panelist_schedule.event_date,panelist_schedule.meeting_id, panelist_schedule.meeting_password,"
				+ "panelist_schedule.zoom_host_url, panelist_schedule.zoom_join_url";
		System.out.println(sql1);

		List<HashMap<String, Object>> results = util.executeQuery(sql1);
		for (HashMap<String, Object> interview : results) {
			try {
				String panelists = (String) interview.get("panelists");
				String meeting_id = (String) interview.get("meeting_id");
				String meeting_password = (String) interview.get("meeting_password");
				String zoom_host_url = (String) interview.get("zoom_host_url");
				String zoom_join_url = (String) interview.get("zoom_join_url");
				String company_name = (String) interview.get("company_name");
				String image_url = (String) interview.get("image_url");
				String job_title = (String) interview.get("profile_title");
				String location = (String) interview.get("location");
				int hours = (int) interview.get("hours");
				int mins = (int) interview.get("mins");
				int duration_in_minutes = hours * 60 + mins;
				String recruiter_name = (String) interview.get("recruiter_name");
				SimpleDateFormat formatter = formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Timestamp dt = (Timestamp) interview.get("event_date");
				SimpleDateFormat formatter2 = new SimpleDateFormat("EEE");
				String interview_day = formatter2.format(formatter.parse(dt.toString()));
				SimpleDateFormat format_time = new SimpleDateFormat("h:mm a");
				String interview_time = format_time.format(formatter.parse(dt.toString()));
				SimpleDateFormat format_date = new SimpleDateFormat("EEE, MMM d");
				String interview_date = format_date.format(formatter.parse(dt.toString()));
				int vacancy_id = (int) interview.get("vacancy_id");
				String event_id = (String) interview.get("event_id");
				Timestamp posted_time = (Timestamp) interview.get("creation_time");
				String prettyTimeString = new PrettyTime().format(formatter.parse(posted_time.toString()));
				String description = "Your interview call scheduled with " + recruiter_name + " at " + interview_time
						+ " on " + interview_date;
				XMLInterview interview_row = new XMLInterview();
				interview_row.setDescription(description);
				interview_row.setCompanyName(company_name);
				interview_row.setDurationInMinutes(duration_in_minutes);
				interview_row.setEventId(event_id);
				interview_row.setImageUrl(image_url);
				interview_row.setInterviewDate(interview_date);
				interview_row.setInterviewDay(interview_day);
				interview_row.setInterviewTime(interview_time);
				interview_row.setJobTitle(job_title);
				interview_row.setLocation(location);
				interview_row.setRecruiterName(recruiter_name);
				interview_row.setVacancyId(vacancy_id);
				interview_row.setPostedHoursAgo(prettyTimeString);
				interview_row.setInterviwType("TECHNICAL");
				interview_row.setMeetingId(meeting_id);
				interview_row.setMeetingPasword(meeting_password);
				interview_row.setJoinUrl(zoom_join_url);
				interview_row.setHostUrl(zoom_host_url);
				interview_row.setPanelist(panelists);
				interviews.add(interview_row);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return interviews;
	}

	private ArrayList<XMLOffer> getOffers(Student s2) {
		ArrayList<XMLOffer> offers = new ArrayList<>();
		DBUTILS util = new DBUTILS();
		String sql1 = "SELECT 	CAST(JE. ID as varchar(50)) AS event_id, 	JE.eventhour AS hours, 	JE.eventminute AS mins, 	"
				+ "JE.eventdate AS event_date, 	JE.created_at as creation_time, 	JE. TYPE AS event_type, 	"
				+ "V.profile_title AS profile_title, 	V.location AS location, V.offer_letter as offer_letter, "
				+ "JE.vacancy_id as vacancy_id, 	V.description as description, 	RC. NAME AS company_name, "
				+ "R.name as recruiter_name, RC.image as image_url, 	JE.status AS status FROM 	jobs_event JE, 	vacancy V, company RC, 	recruiter R WHERE 	JE.actor_id = "
				+ s.getId()
				+ " AND V. ID = JE.vacancy_id AND V.recruiter_id= R.id AND V.company_id = RC. ID AND JE.isactive = 't' AND JE.status LIKE '%OFFER%'";
		List<HashMap<String, Object>> results = util.executeQuery(sql1);
		for (HashMap<String, Object> offer : results) {
			try {
				String company_name = (String) offer.get("company_name");
				String image_url = (String) offer.get("image_url");
				String job_title = (String) offer.get("profile_title");
				String location = (String) offer.get("location");
				// com.istarindia.apps.dao.UUIUtils.printlog("--------------------"+location);
				String description = "Congratulation, you are selected. Kindly accept the offer letter.";
				String offer_letter = (String) offer.get("offer_letter");
				String event_id = (String) offer.get("event_id");
				Timestamp posted_time = (Timestamp) offer.get("creation_time");
				SimpleDateFormat formatter = formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String prettyTimeString;
				prettyTimeString = new PrettyTime().format(formatter.parse(posted_time.toString()));
				/*
				 * AndroidOffer androidoffer = new
				 * AndroidOffer(UUID.fromString(event_id), company_name,
				 * image_url, job_title, description, location, offer_letter,
				 * prettyTimeString);
				 */
				XMLOffer offer_row = new XMLOffer();
				offer_row.setCompanyName(company_name);
				offer_row.setDescription(description);
				offer_row.setEventId(event_id);
				offer_row.setImageUrl(image_url);
				offer_row.setJobTitle(job_title);
				offer_row.setLocation(location);
				offer_row.setOfferLetterUrl(offer_letter);
				offer_row.setPostedHoursAgo(prettyTimeString);
				offers.add(offer_row);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		return offers;
	}

	private ArrayList<XMLInvite> getInvites(Student s2) {
		DBUTILS util = new DBUTILS();
		ArrayList<XMLInvite> invites = new ArrayList<>();
		String sql = "SELECT 	CAST(JE. ID as varchar(50)) AS event_id, 	JE.eventhour AS hours, 	JE.eventminute AS mins, 	"
				+ "JE.eventdate AS event_date, 	JE.created_at as creation_time, 	JE. TYPE AS event_type,"
				+ " 	V.profile_title AS profile_title, 	JE. ACTION AS event_action, 	V.location AS location, "
				+ "	V.description as description, 	RC. NAME AS company_name, RC.image as image_url, 	"
				+ "JE.status AS status FROM 	jobs_event JE, 	vacancy V, 	company RC WHERE 	JE.actor_id = "
				+ s.getId()
				+ " AND V. ID = JE.vacancy_id AND V.company_id = RC. ID AND JE.isactive = 't' AND JE.status LIKE '%INVITE%'";
		List<HashMap<String, Object>> results = util.executeQuery(sql);
		for (HashMap<String, Object> invite : results) {
			try {
				String company_name = (String) invite.get("company_name");
				String image_url = (String) invite.get("image_url");
				String vacancy_job_title = (String) invite.get("profile_title");
				String vacancy_location = (String) invite.get("location");
				String vacancy_description = (String) invite.get("description");
				String event_id = (String) invite.get("event_id");
				String event_action = (String) invite.get("event_action");
				// com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(),
				// company_name);
				Timestamp posted_time = (Timestamp) invite.get("creation_time");
				SimpleDateFormat formatter = formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String prettyTimeString;
				prettyTimeString = new PrettyTime().format(formatter.parse(posted_time.toString()));
				XMLInvite in = new XMLInvite();
				in.setCompanyName(company_name);
				in.setEventAction(event_action);
				in.setEventId(event_id);
				in.setImageUrl(image_url);
				in.setPostedHoursAgo(prettyTimeString);
				in.setVacancyDescription(vacancy_description);
				in.setVacancyJobTitle(vacancy_job_title);
				in.setVacancyLocation(vacancy_location);
				invites.add(in);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return invites;
	}

	private ArrayList<XMLNotification> getNotifications(Integer id) {
		ArrayList<XMLNotification> notifications = new ArrayList<>();
		// ArrayList<ArrayList<String>> table = new ArrayList<>();
		String sql1 = "SELECT CAST ( 		istar_notification.event_id AS VARCHAR 	) AS event_id,	CAST ( 		istar_notification. ID AS VARCHAR 	) AS notif_id, 	istar_notification.created_at AS notif_date, 	istar_notification. TYPE AS notif_type, 	istar_notification.title AS title, istar_notification.details as details, 	istar_notification.sender_id AS sender FROM 	istar_notification WHERE 	receiver_id = "
				+ id
				+ " AND istar_notification.status = 'UNREAD' order by istar_notification.created_at desc LIMIT 20  ";
		DBUTILS db = new DBUTILS();
		List<HashMap<String, Object>> results = db.executeQuery(sql1);
		com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "sql1 --> " + sql1);
		com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "sql1 --> " + sql1);
		for (HashMap<String, Object> notification : results) {
			try {
				String notif_id = (String) notification.get("notif_id");
				String event_id = (String) notification.get("event_id");
				String title = (String) notification.get("title");
				String notif_type = (String) notification.get("notif_type");
				int sender = (int) notification.get("sender");
				String details = (String) notification.get("details");
				Calendar cal = Calendar.getInstance();
				Timestamp notif_date = new Timestamp(cal.getTime().getTime());
				if (notification.get("notif_date") != null) {
					notif_date = (Timestamp) notification.get("notif_date");
				}
				// ArrayList<String> row = new ArrayList<>();
				// row.add(notif_id + "");
				// row.add(notif_type + "");
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
				SimpleDateFormat dtdate = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat dttime = new SimpleDateFormat("HH:mm");
				// 2016-06-19 16:08:51.447
				XMLNotification n = new XMLNotification();
				Date date;
				date = (Date) dt.parse(notif_date.toString());
				n.setDate(dtdate.format(date));
				n.setTime(dttime.format(date));
				n.setEventID(event_id);
				n.setId(notif_id);
				n.setTitle(title);
				n.setType(notif_type);
				n.setDescription(details);
				n.setSender(sender + "");
				PrettyTime p = new PrettyTime();
				String dddd = p.format(date).replaceAll("minute from now", "mins ago")
						.replaceAll("minutes from now", "mins ago").replaceAll("minutes", "mins")
						.replaceAll("moments", "mins");
				n.setReceivedAt(dddd);
				notifications.add(n);
				// row.add(dddd + "");
				// row.add((String) notification.get("title"));
				// com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(),
				// notif_date);
				// table.add(row);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		// return table;
		return notifications;
	}

	private ArrayList<XMLCoachStudent> getCoachStudents(Student s2, HttpServletRequest request) {
		ArrayList<XMLCoachStudent> data = new ArrayList<>();
		ArrayList<Integer> batch_group_added = new ArrayList<>();
		ArrayList<Integer> student_added = new ArrayList<>();
		for (TrainerBatch tb : s2.getTrainerBatches()) {
			BatchGroup bg = tb.getBatch().getBatchGroup();
			if (!batch_group_added.contains(bg.getId())) {
				batch_group_added.add(bg.getId());
				for (BatchStudents bs : bg.getBatchStudentses()) {
					if (bs.getStudent().getId() != s2.getId()) {
						XMLCoachStudent cs = new XMLCoachStudent();
						cs.setBatchGroup(bs.getBatchGroup().getName());
						String url = request.getRequestURL().toString();
						String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
								+ request.getContextPath() + "/";
						// System.out.println("222222222"+bs.getStudent().getImageUrl());
						if (bs.getStudent().getImageUrl() == null
								|| bs.getStudent().getImageUrl().equalsIgnoreCase("null")) {
							cs.setImageUrl(baseURL + "" + "images/profile.jpg");
						} else {
							cs.setImageUrl(baseURL + "" + bs.getStudent().getImageUrl());
						}
						cs.setBatchId(bg.getId());
						cs.setMobile(bs.getStudent().getMobile().toString());
						cs.setOrganizationName(bs.getStudent().getCollege().getName());
						cs.setStudentId(bs.getStudent().getId());
						cs.setStudentName(bs.getStudent().getName());
						DBUTILS util = new DBUTILS();
						String query = "select * from trainer_favourite where student_id =" + bs.getStudent().getId()
								+ " and trainer_id= " + s2.getId() + " and favourite_status='t'";
						if (util.executeQuery(query).size() > 0) {
							cs.setIsfavoutite(true);
						} else {
							cs.setIsfavoutite(false);
						}
						ArrayList<XMLCourseRating> crating = new ArrayList<>();
						String sql = "select * from (select  distinct C.id as course_id , C.course_name as name from batch_group, batch, batch_students, course C where batch_group.id = batch.batch_group_id and batch_students.batch_group_id= batch_group.id and batch.course_id =  C.id and batch_students.student_id= "
								+ bs.getStudent().getId()
								+ ") t1 left join (SELECT 	( 		SUM ( 			R.score * 100 / A .number_of_questions 		) 	) / COUNT (*) AS score, 	C . ID AS course_id1 FROM 	report R, 	assessment A, 	cmsession CMM, 	MODULE MM, 	lesson LL, 	task TT, 	student S, 	course C WHERE 	R.user_id = S. ID AND A .lesson_id = LL. ID AND R.assessment_id = A . ID AND LL.dtype = 'ASSESSMENT' AND MM. ID = CMM.module_id AND MM.course_id = C . ID AND CMM. ID = LL.session_id AND LL. ID = TT.item_id AND TT.item_type = 'LESSON' AND TT.status = 'PUBLISHED' AND S. ID = "
								+ bs.getStudent().getId() + " GROUP BY 	C . ID ) t2 on t1.course_id = t2.course_id1 ";
						List<HashMap<String, Object>> courseWithRating = util.executeQuery(sql);
						for (HashMap<String, Object> row : courseWithRating) {
							BigInteger rating = (BigInteger) row.get("score");
							int course_id = (int) row.get("course_id");
							String course_name = (String) row.get("name");
							XMLCourseRating cr = new XMLCourseRating();
							cr.setCourseName(course_name);
							cr.setCourse_id(course_id);
							if ((BigInteger) row.get("score") == null) {
								cr.setRating(3);
							} else {
								cr.setRating((rating.intValue() / 20) % 5);
							}
							crating.add(cr);
						}
						cs.setCourseRating(crating);
						data.add(cs);
					}
				}
			}
		}
		System.out.println("bg count" + batch_group_added.size());
		return data;
	}

	private ArrayList<XMLEvents> getAllEvents(Student s2) {
		IstarUserDAO dao = new IstarUserDAO();
		ArrayList<XMLEvents> data = new ArrayList<>();
		String hql = "from IstarEvent isss where isss.actor=:actor order by isss.eventdate";
		Query query = dao.getSession().createQuery(hql);
		query.setInteger("actor", s2.getId());
		System.err.println(query.getQueryString());
		List<IstarEvent> results = query.list();
		for (IstarEvent ev : results) {
			if (s2.getUserType().equalsIgnoreCase("TRAINER") && ev.getType().contains("BATCH_SCHEDULE_EVENT")) {
				BatchScheduleEvent e = (BatchScheduleEvent) ev;
				XMLEvents event = new XMLEvents();
				event.setBgroupId(e.getBatch().getBatchGroup().getId());
				event.setId(e.getId().toString());
				event.setBatchId(e.getBatch().getId());
				event.setBatchName(e.getBatch().getName());
				event.setClassId(e.getClassroom().getId());
				event.setClassName(e.getClassroom().getClassroomIdentifier());
				event.setCmsessionId(e.getCmsession().getId());
				event.setCmsessionTitle(e.getCmsession().getTitle());
				// SimpleDateFormat simpleDateFormat = new
				// SimpleDateFormat("yyyy-MM-dd h:mm a");
				event.setEventdate(e.getEventdate().toString());
				event.setEventHour(e.getEventhour());
				event.setEventMin(e.getEventminute());
				event.setEventName(e.getEventName());
				String desc = "Batch: " + event.getBatchName() + "!#";
				desc = desc + "Classroom: " + event.getClassName() + "!#";
				desc = desc + "Classroom ID: " + event.getClassId();
				event.setDescription(desc);
				event.setEventType(e.getType());
				event.setStatus(e.getStatus());
				data.add(event);
			} else if (ev.getType().equalsIgnoreCase("ASSESSMENT_EVENT")) {
				try {
					IstarAssessmentEvent e = (IstarAssessmentEvent) ev;
					XMLEvents event = new XMLEvents();
					event.setId(e.getId().toString());
					event.setBatchId(0);
					event.setEventdate(e.getEventdate().toString());
					event.setEventHour(e.getEventhour());
					if (e.getEventminute() != null) {
						event.setEventMin(e.getEventminute());
					} else {
						event.setEventMin(30);
					}
					event.setStatus(e.getStatus());
					event.setEventName(e.getAssessment().getAssessmenttitle());
					event.setEventType(e.getType());
					event.setAssessmentId(e.getAssessment().getId());
					event.setAssessmentTitle(e.getAssessment().getAssessmenttitle());
					event.setEventdate(e.getEventdate().toString());
					event.setCmsessionTitle(e.getAssessment().getLesson().getCmsession().getTitle());
					event.setCmsessionId(e.getAssessment().getLesson().getCmsession().getId());
					String desc = "Assessment: " + e.getAssessment().getAssessmenttitle() + "!#";
					desc = desc + "Session: " + e.getAssessment().getLesson().getCmsession().getTitle() + "!#";
					desc = desc + "Course: "
							+ e.getAssessment().getLesson().getCmsession().getModule().getCourse().getCourseName();
					event.setDescription(desc);
					data.add(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * else
			 * if(ev.getType().equalsIgnoreCase(IstarEventTypes.JOBS_EVENT)) {
			 * JobsEvent e = (JobsEvent)ev; XMLEvents event = new XMLEvents();
			 * event.setId(e.getId().toString());
			 * event.setEventType(e.getType());
			 * event.setEventdate(e.getEventdate().toString());
			 * event.setEventHour(e.getEventhour());
			 * event.setEventMin(e.getEventminute());
			 * event.setStatus(e.getStatus());
			 * event.setLocation(e.getVacancy().getLocation());
			 * event.setRecruiter(e.getVacancy().getRecruiter().getName());
			 * event.setVacancyDescription(e.getVacancy().getDescription());
			 * event.setVacancyTitle(e.getVacancy().getProfileTitle());
			 * event.setCompany(e.getVacancy().getRecruiterCompany().getName());
			 * data.add(event);
			 * 
			 * }
			 */
		}
		return data;
	}

	private ArrayList<XMLNote> getAllMyNotes(Student s2) {
		ArrayList<XMLNote> notes = new ArrayList<>();
		for (StudentNote iterable_element : s.getStudentnotes()) {
			try {
				Document doc = Jsoup.parse(iterable_element.getNoteText());
				String text = (doc.body().text());
				XMLNote note = new XMLNote(iterable_element.getId(), text, iterable_element.getSlideUrl(),
						iterable_element.getTimestamp(), true, null);
				note.setCMSession(iterable_element.getCmsessionId().getTitle());
				note.setCourse(iterable_element.getCourse().getCourseName());
				note.setLesson(iterable_element.getLesson().getTitle());
				note.setSlideId(iterable_element.getSlideID());
				note.setSessionId(iterable_element.getCmsessionId().getId());
				note.setCourseId(iterable_element.getCourse().getId());
				note.setLessonId(iterable_element.getLesson().getId());
				notes.add(note);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// System.out.println(iterable_element.getId());
				// e.printStackTrace();
			}
		}
		return notes;
	}

	private ArrayList<XMLNote> getAllMySharedNotes(Student s2) {
		ArrayList<XMLNote> shared_notes = new ArrayList<>();
		for (StudentShare sh : s.getStudentshare()) {
			try {
				StudentNote sn = sh.getStudentNote();
				XMLNote note = new XMLNote(sn.getId(), sn.getNoteText(), sn.getSlideUrl(), sh.getTimestamp(), false,
						sh.getshared_by().getName());
				note.setCourse(sn.getCourse().getCourseName());
				note.setLesson(sn.getLesson().getTitle());
				note.setSlideId(sn.getSlideID());
				note.setSessionId(sn.getCmsessionId().getId());
				note.setCourseId(sn.getCourse().getId());
				note.setLessonId(sn.getLesson().getId());
				shared_notes.add(note);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		return shared_notes;
	}

	private ArrayList<XMLBatch> getBatches(BatchGroup batchGroup) {
		ArrayList<XMLBatch> items = new ArrayList<>();
		IstarUserDAO dao = new IstarUserDAO();
		String hql = "from Batch isss where isss.batchGroup=:batch_group_id order by isss.order_id";
		Query query = dao.getSession().createQuery(hql);
		query.setInteger("batch_group_id", batchGroup.getId());
		List<Batch> batchess = query.list();
		for (Batch batch : batchess) {
			XMLBatch b = new XMLBatch();
			b.setId(batch.getId());
			b.setBatchName(batch.getName());
			b.setOrderId(batch.getOrder_id());
			XMLCourse course = new XMLCourse();
			course.setCourseId(batch.getCourse().getId());
			lCounter = 0;
			course.setCourseName(batch.getCourse().getCourseName());
			String desc = "Description not provided";
			try {
				if (batch.getCourse().getCourseDescription().length() > 180) {
					desc = batch.getCourse().getCourseDescription().substring(0, 180);
				} else {
					desc = batch.getCourse().getCourseDescription();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			course.setCoursedescription(desc);
			ArrayList<XMLSession> sessions = getCMSession(batch);
			course.setCmsessions(sessions);
			// if (sessions.size() > 0) {
			b.setCourse(course);
			items.add(b);
			// }
		}
		return items;
	}

	private ArrayList<XMLSession> getCMSession(Batch batch) {
		int last_session_order = -1;
		int last_module_order = -1;
		String get_sesion_id = "SELECT 	cmsession.order_id as session_order_id, 	event_session_log.cmsession_id as session_id, 	module.order_id as module_order_id, 	module.id as module_id FROM 	event_session_log, cmsession, module WHERE event_session_log.cmsession_id = cmsession.id and	event_session_log.batch_id = "
				+ batch.getId() + " and module.id = cmsession.module_id ORDER BY 	event_session_log.ID DESC LIMIT 1;";
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> ss = util.executeQuery(get_sesion_id);
		if (ss.size() > 0) {
			if (ss.get(0).get("session_order_id") != null) {
				last_session_order = (int) ss.get(0).get("session_order_id");
			}
			if (ss.get(0).get("module_order_id") != null) {
				last_module_order = (int) ss.get(0).get("module_order_id");
			}
		}
		ArrayList<XMLSession> cmsessions = new ArrayList<>();
		IstarUserDAO dao = new IstarUserDAO();
		String hql = "from Module isss where isss.course=:course_id order by isss.order_id";
		Query query = dao.getSession().createQuery(hql);
		query.setInteger("course_id", batch.getCourse().getId());
		List<Module> modules = query.list();
		for (Module m : modules) {
			String hql_session = "from Cmsession isss where isss.module=:module_id order by isss.order_id";
			Query query_session = dao.getSession().createQuery(hql_session);
			query_session.setInteger("module_id", m.getId());
			List<Cmsession> sessions = query_session.list();
			for (Cmsession cs : sessions) {
				try {
					XMLSession cms = new XMLSession();
					cms.setLastOrderId(last_session_order);
					cms.setLastModuleOrderId(last_module_order);
					if (cs.getModule().getOrder_id() != null && cs.getOrder_id() != null) {
						cms.setCurrentmoduleOrderId(cs.getModule().getOrder_id());
						if (cs.getOrder_id() > last_session_order) {
							cms.setReadable(false);
						} else {
							cms.setReadable(true);
						}
						cms.setOrderId(cs.getOrder_id());
						cms.setCmsession_id(cs.getId());
						cms.setCmsessionName(cs.getTitle());
						String desc = "";
						if (cs.getSession_description().length() > 100) {
							desc = cs.getSession_description().substring(0, 100);
						} else {
							desc = cs.getSession_description();
						}
						cms.setCmsessionDescription(desc);
						ArrayList<XMLLesson> lessons = getLessons(cs);
						// if (lessons.size() > 0) {
						cms.setLessons(lessons);
						cmsessions.add(cms);
					}
					// }
				} catch (Exception se) {
					se.printStackTrace();
					System.err.println("Problem with Session ID ->" + cs.getId());
				}
			}
		}
		return cmsessions;
	}

	private ArrayList<XMLLesson> getLessons(Cmsession cs) {
		ArrayList<XMLLesson> less = new ArrayList<>();
		try {
			try {
				for (Lesson ll : cs.getLessons()) {

					// System.out.println(ll.getId()+"
					// PPT"+ll.getPresentaion()+" ASSESS"+ll.getAssessment());
					try {
						XMLLesson l = new XMLLesson();
						if (ll.getPresentaion() != null) {
							l.setType("PRESENTATION");
							l.setTitle(ll.getTitle());
							ArrayList<XMLSlide> slides = getSlides(ll);
							l.setSlides(slides);
							l.setAssessment(null);

							String getLastPointer = "SELECT 	slide.order_id FROM 	user_session_log, slide WHERE 	 	user_session_log.lesson_id = "
									+ ll.getId() + " and slide.id = user_session_log.slide_id AND user_id = "
									+ s.getId() + " ORDER BY 	user_session_log.ID DESC LIMIT 1";
							DBUTILS util = new DBUTILS();
							List<HashMap<String, Object>> data = util.executeQuery(getLastPointer);
							if (data.size() > 0) {
								int last_pointer = (int) data.get(0).get("order_id");
								l.setLast_slide_pointer(last_pointer);
							} else {
								l.setLast_slide_pointer(0);
							}

							l.setLOrderId(lCounter++);
							l.setConcreteId(ll.getPresentaion().getId());

							if (ll.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
								l.setStatus("PUBLISHED");
							} else {
								l.setStatus("REQUEST_FOR_PUBLISH");
							}
						} else if (ll.getAssessment() != null) {
							l.setType("ASSESSMENT");
							l.setTitle(ll.getAssessment().getAssessmenttitle());
							l.setAssessment(null);
							l.setLOrderId(lCounter++);
							l.setConcreteId(ll.getAssessment().getId());
							if (ll.getTask().getStatus().equalsIgnoreCase("PUBLISHED")) {
								l.setStatus("PUBLISHED");
							} else {
								l.setStatus("REQUEST_FOR_PUBLISH");
							}
							l.setLast_slide_pointer(0);
						} else {
							/*
							 * l.setType("NONE"); l.setTitle("NONE");
							 * l.setAssessment(null); l.setSlides(null);
							 */
						}
						less.add(l);

					} catch (Exception e) {
						// e.printStackTrace();
						// System.err.println(ll.getId());
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return less;
	}

	private XMLAssessment getAssessment(Lesson ll) {
		XMLAssessment assess = new XMLAssessment();
		assess.setAssessmentId(ll.getAssessment().getId());
		assess.setAssessmentTitle(ll.getAssessment().getAssessmenttitle());
		assess.setAssessmentDurationMinutes(ll.getAssessment().getAssessmentdurationminutes());
		assess.setNumber_of_questions(ll.getAssessment().getNumber_of_questions());
		ArrayList<XMLQuestion> questions = getQuestions(ll.getAssessment());
		assess.setQuestions(questions);
		return assess;
	}

	private ArrayList<XMLQuestion> getQuestions(Assessment assessment) {
		ArrayList<XMLQuestion> question_set = new ArrayList<>();
		for (AssessmentQuestion aq : assessment.getAssessmentQuestions()) {
			XMLQuestion que = new XMLQuestion();
			que.setQueId(aq.getQuestion().getId());
			que.setQuestionText(aq.getQuestion().getQuestionText());
			que.setOrderId(aq.getOrderId());
			que.setQuestionType(aq.getQuestion().getQuestionType());
			que.setDurationInSec(aq.getQuestion().getDurationInSec());
			ArrayList<XMLOption> option = getOptions(aq.getQuestion());
			que.setOptions(option);
			question_set.add(que);
		}
		return question_set;
	}

	private ArrayList<XMLOption> getOptions(Question question) {
		ArrayList<XMLOption> data = new ArrayList<>();
		for (AssessmentOption aop : question.getAssessmentOptions()) {
			XMLOption op = new XMLOption();
			op.setOptionId(aop.getId());
			op.setOptionText(aop.getText());
			if (aop.getMarkingScheme() != null && aop.getMarkingScheme() == 1) {
				op.setCorrect(true);
			} else {
				op.setCorrect(false);
			}
			data.add(op);
		}
		return data;
	}

	private ArrayList<XMLSlide> getSlides(Lesson ll) {
		ArrayList<XMLSlide> slides = new ArrayList<>();
		for (Slide slide : ll.getPresentaion().getSlides()) {
			XMLSlide s = new XMLSlide();
			s.setId(slide.getId());
			Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(slide.getTitle()));
			s.setTitle(doc.body().text());
			// ArrayList<XMLNote> myNotes = getMyNotes(slide);
			// ArrayList<XMLNote> sharedNotes = getSharedNotes(slide);
			// s.setMyNotes(myNotes);
			// s.setSharedNotes(sharedNotes);
			slides.add(s);
		}
		return slides;
	}

	private ArrayList<XMLNote> getMyNotes(Slide sl) {
		ArrayList<XMLNote> data = new ArrayList<>();
		StudentNote note = new StudentNote();
		note.setStudent(s);
		note.setSlideID(sl.getId());
		StudentNoteDAO dao = new StudentNoteDAO();
		for (StudentNote sn : dao.findByExample(note)) {
			XMLNote n = new XMLNote();
			n.setCreated_at(sn.getTimestamp().toString());
			n.setMyNote(true);
			n.setNote_id(sn.getId());
			Document doc = Jsoup.parse(sn.getNoteText());
			n.setNoteText(sn.getNoteText());
			n.setSlideUrl(sn.getSlideUrl());
			n.setSharedBy("none");
			data.add(n);
			if (sn.getId() == 4709) {
				System.out.println(n.getNoteText());
			}
		}
		return data;
	}

	private ArrayList<XMLNote> getSharedNotes(Slide sl) {
		ArrayList<XMLNote> data = new ArrayList<>();
		StudentShareDAO sh = new StudentShareDAO();
		List<StudentShare> shared = sh.findBySharedWith(s.getId());
		for (StudentShare s : shared) {
			StudentNote sn = s.getStudentNote();
			XMLNote n = new XMLNote();
			n.setCreated_at(sn.getTimestamp().toString());
			n.setMyNote(false);
			n.setNote_id(sn.getId());
			n.setNoteText(sn.getNoteText());
			n.setSlideUrl(sn.getSlideUrl());
			n.setSharedBy(s.getshared_by().getName());
			data.add(n);
		}
		return data;
	}

	private ArrayList<XMLBatchMember> getClassMates(BatchGroup batchGroup) {
		ArrayList<XMLBatchMember> items = new ArrayList<>();
		for (BatchStudents iterable_element : batchGroup.getBatchStudentses()) {
			XMLBatchMember xmb = new XMLBatchMember(iterable_element.getStudent().getId(),
					iterable_element.getStudent().getName(), "NULL");
			items.add(xmb);
		}
		return items;
	}

	public XMLStudent initializeStudent(int student_id) {
		XMLStudent xs;
		System.err.println("initializing " + student_id);
		StudentDAO dao = new StudentDAO();
		s = dao.findById(student_id);
		xs = new XMLStudent();
		if (s != null) {
			xs.setFatherName(s.getFatherName());
			xs.setMotherName(s.getMotherName());
			xs.setId(s.getId());
			xs.setName(s.getName());
			xs.setPhone(s.getPhone());
			xs.setEmail(s.getEmail());
			xs.setImage_url(s.getImageUrl());
			xs.setUserType(s.getUserType());
			xs.setAddress(new XMLAddress(s.getAddress().getId(),
					new XMLPincode(s.getAddress().getPincode().getId(), s.getAddress().getPincode().getState(),
							s.getAddress().getPincode().getCity(), s.getAddress().getPincode().getCountry(),
							s.getAddress().getPincode().getPin()),
					s.getAddress().getAddressline1(), s.getAddress().getAddressline2(),
					s.getAddress().getAddressGeoLongitude(), s.getAddress().getAddressGeoLatitude()));
			ArrayList<XMLBatchGroup> batchGroups = new ArrayList<>();
			for (BatchStudents bg : s.getBatchStudentses()) {
				XMLBatchGroup e = new XMLBatchGroup();
				e.setId(bg.getBatchGroup().getId());
				e.setName(bg.getBatchGroup().getName());
				e.setOrg_id(bg.getBatchGroup().getCollege().getId());
				e.setOrgname(bg.getBatchGroup().getCollege().getName());
				ArrayList<XMLBatch> btaches = getBatches(bg.getBatchGroup());
				e.setBtaches(btaches);
				ArrayList<XMLBatchMember> classmates = getClassMates(bg.getBatchGroup());
				e.setClassmates(classmates);
				batchGroups.add(e);
			}
			xs.setBatchGroups(batchGroups);
			if (s.getUserType().equalsIgnoreCase("TRAINER")) {
				ArrayList<XMLCoachStudent> coach_students = getCoachStudents(s);
				xs.setCoachStudents(coach_students);
			} else {
				xs.setCoachStudents(new ArrayList<XMLCoachStudent>());
				ArrayList<XMLInvite> invites = getInvites(s);
				xs.setInvites(invites);
				ArrayList<XMLOffer> offers = getOffers(s);
				xs.setOffers(offers);
				ArrayList<XMLInterview> interviews = getInterviews(s);
				xs.setInterviews(interviews);
				ArrayList<XMLTest> tests = getTests(s);
				xs.setTests(tests);
			}
			ArrayList<XMLNote> notes = getAllMyNotes(s);
			;
			ArrayList<XMLNote> shared_notes = getAllMySharedNotes(s);
			xs.setNotes(notes);
			xs.setSharedNotes(shared_notes);
			ArrayList<XMLEvents> events = getAllEvents(s);
			xs.setEvents(events);
			College org = s.getCollege();
			xs.setOrganization(new XMLOrganization(org.getName(),
					new XMLAddress(org.getAddress().getId(),
							new XMLPincode(s.getAddress().getPincode().getId(), s.getAddress().getPincode().getState(),
									s.getAddress().getPincode().getCity(), s.getAddress().getPincode().getCountry(),
									s.getAddress().getPincode().getPin()),
							org.getAddress().getAddressline1(), org.getAddress().getAddressline2(),
							org.getAddress().getAddressGeoLongitude(), org.getAddress().getAddressGeoLatitude())));
			ArrayList<XMLNotification> notifications = getNotifications(s.getId());
			xs.setNotifications(notifications);
		}
		return xs;
	}

	private ArrayList<XMLCoachStudent> getCoachStudents(Student s2) {
		ArrayList<XMLCoachStudent> data = new ArrayList<>();
		ArrayList<Integer> batch_group_added = new ArrayList<>();
		ArrayList<Integer> student_added = new ArrayList<>();
		for (TrainerBatch tb : s2.getTrainerBatches()) {
			BatchGroup bg = tb.getBatch().getBatchGroup();
			if (!batch_group_added.contains(bg.getId())) {
				batch_group_added.add(bg.getId());
				for (BatchStudents bs : bg.getBatchStudentses()) {
					if (bs.getStudent().getId() != s2.getId()) {
						XMLCoachStudent cs = new XMLCoachStudent();
						cs.setBatchGroup(bs.getBatchGroup().getName());
						String baseURL = "http://api.talentify.in/";
						// System.out.println("222222222"+bs.getStudent().getImageUrl());
						if (bs.getStudent().getImageUrl() == null
								|| bs.getStudent().getImageUrl().equalsIgnoreCase("null")) {
							cs.setImageUrl(baseURL + "" + "images/profile.jpg");
						} else {
							cs.setImageUrl(baseURL + "" + bs.getStudent().getImageUrl());
						}
						cs.setBatchId(bg.getId());
						cs.setMobile(bs.getStudent().getMobile().toString());
						cs.setOrganizationName(bs.getStudent().getCollege().getName());
						cs.setStudentId(bs.getStudent().getId());
						cs.setStudentName(bs.getStudent().getName());
						DBUTILS util = new DBUTILS();
						String query = "select * from trainer_favourite where student_id =" + bs.getStudent().getId()
								+ " and trainer_id= " + s2.getId() + " and favourite_status='t'";
						if (util.executeQuery(query).size() > 0) {
							cs.setIsfavoutite(true);
						} else {
							cs.setIsfavoutite(false);
						}
						ArrayList<XMLCourseRating> crating = new ArrayList<>();
						String sql = "select * from (select  distinct C.id as course_id , C.course_name as name from batch_group, batch, batch_students, course C where batch_group.id = batch.batch_group_id and batch_students.batch_group_id= batch_group.id and batch.course_id =  C.id and batch_students.student_id= "
								+ bs.getStudent().getId()
								+ ") t1 left join (SELECT 	( 		SUM ( 			R.score * 100 / A .number_of_questions 		) 	) / COUNT (*) AS score, 	C . ID AS course_id1 FROM 	report R, 	assessment A, 	cmsession CMM, 	MODULE MM, 	lesson LL, 	task TT, 	student S, 	course C WHERE 	R.user_id = S. ID AND A .lesson_id = LL. ID AND R.assessment_id = A . ID AND LL.dtype = 'ASSESSMENT' AND MM. ID = CMM.module_id AND MM.course_id = C . ID AND CMM. ID = LL.session_id AND LL. ID = TT.item_id AND TT.item_type = 'LESSON' AND TT.status = 'PUBLISHED' AND S. ID = "
								+ bs.getStudent().getId() + " GROUP BY 	C . ID ) t2 on t1.course_id = t2.course_id1 ";
						List<HashMap<String, Object>> courseWithRating = util.executeQuery(sql);
						for (HashMap<String, Object> row : courseWithRating) {
							BigInteger rating = (BigInteger) row.get("score");
							int course_id = (int) row.get("course_id");
							String course_name = (String) row.get("name");
							XMLCourseRating cr = new XMLCourseRating();
							cr.setCourseName(course_name);
							if ((BigInteger) row.get("score") == null) {
								cr.setRating(3);
							} else {
								cr.setRating((rating.intValue() / 20) % 5);
							}
							crating.add(cr);
						}
						cs.setCourseRating(crating);
						data.add(cs);
					}
				}
			}
		}
		System.out.println("bg count" + batch_group_added.size());
		return data;
	}
}