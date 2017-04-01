package in.talentifyU.complexData.services;

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

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.ocpsoft.prettytime.PrettyTime;


import com.viksitpro.core.dao.entities.Assessment;
import com.viksitpro.core.dao.entities.AssessmentOption;
import com.viksitpro.core.dao.entities.AssessmentQuestion;
import com.viksitpro.core.dao.entities.Batch;
import com.viksitpro.core.dao.entities.BatchGroup;
import com.viksitpro.core.dao.entities.BatchStudents;
import com.viksitpro.core.dao.entities.Cmsession;
import com.viksitpro.core.dao.entities.EventSessionLog;
import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.dao.entities.Lesson;
import com.viksitpro.core.dao.entities.LessonDAO;
import com.viksitpro.core.dao.entities.Organization;
import com.viksitpro.core.dao.entities.Presentation;
import com.viksitpro.core.dao.entities.ProfessionalProfile;
import com.viksitpro.core.dao.entities.Question;
import com.viksitpro.core.dao.entities.Slide;
import com.viksitpro.core.dao.entities.TrainerBatch;
import com.viksitpro.core.dao.entities.UserProfile;
import com.viksitpro.core.dao.entities.UserSessionLog;
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

public class StudentInitializer {
	XMLStudent xs;
	IstarUser s;
	UserProfile userProfile;
	
	public XMLStudent initializeStudent(int student_id, HttpServletRequest request) {
		System.err.println("initializing " + student_id);
		IstarUserDAO dao = new IstarUserDAO();
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		s = dao.findById(student_id);
		userProfile = s.getUserProfile();
		session.evict(s);
		s = dao.findById(student_id);
		xs = new XMLStudent();
		if (s != null) {
			
			String fatherName ="NA";
			String motherName = "NA";
			String name="NA";
			Long mobile =s.getMobile()!=null ? s.getMobile():0L;
			String proImage=s.getEmail().toUpperCase().charAt(0)+".png";
			if(userProfile != null)
			{
				fatherName = userProfile.getFatherName()!=null && !userProfile.getFatherName().isEmpty() ?userProfile.getFatherName() :"NA";  
				motherName = userProfile.getMotherName()!=null && !userProfile.getMotherName().isEmpty() ?userProfile.getMotherName() :"NA";  
				name = userProfile.getFirstName()!=null && !userProfile.getFirstName().isEmpty() ?userProfile.getFirstName() :"NA";
				if(userProfile.getProfileImage()!=null)
				{
					proImage = userProfile.getProfileImage();
				}
				else if (name!=null)
				{
					proImage= "/android_images/"+name.toUpperCase().charAt(0)+".png";
				}
			}
			xs.setFatherName(fatherName);
			xs.setMotherName(motherName);
			xs.setId(s.getId());
			xs.setName(name);
			xs.setPhone(mobile);
			
			xs.setEmail(s.getEmail());
			xs.setImage_url(proImage);
			xs.setUserType(s.getUserRoles().iterator().next().getRole().getRoleName());
			xs.setStudentProfile(getStudentProfile(s));
			xs.setAddress(new XMLAddress(s.getUserProfile().getAddress().getId(), new XMLPincode(s.getUserProfile().getAddress().getPincode().getId(), s.getUserProfile().getAddress().getPincode().getState(), s.getUserProfile().getAddress().getPincode().getCity(), s.getUserProfile().getAddress().getPincode().getCountry(), s.getUserProfile().getAddress().getPincode().getPin()), s.getUserProfile().getAddress().getAddressline1(), s.getUserProfile().getAddress().getAddressline2(), s.getUserProfile().getAddress().getAddressGeoLongitude(), s.getUserProfile().getAddress().getAddressGeoLatitude()));
			ArrayList<XMLBatchGroup> batchGroups = new ArrayList<>();
			for (BatchStudents bg : s.getBatchStudentses()) {
				XMLBatchGroup e = new XMLBatchGroup();
				e.setId(bg.getBatchGroup().getId());
				e.setName(bg.getBatchGroup().getName());
				e.setOrg_id(bg.getBatchGroup().getOrganization().getId());
				e.setOrgname(bg.getBatchGroup().getOrganization().getName());
				ArrayList<XMLBatch> btaches = getBatches(bg.getBatchGroup());
				e.setBtaches(btaches);
				ArrayList<XMLBatchMember> classmates = getClassMates(bg.getBatchGroup());
				e.setClassmates(classmates);
				batchGroups.add(e);
			}
			xs.setBatchGroups(batchGroups);
			if (s.getUserRoles().contains("TRAINER")) {
				ArrayList<XMLCoachStudent> coach_students = getCoachStudents(s, request);
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
			ArrayList<XMLNote> notes = getAllMyNotes(s);;
			ArrayList<XMLNote> shared_notes = getAllMySharedNotes(s);
			xs.setNotes(notes);
			xs.setSharedNotes(shared_notes);
			ArrayList<XMLEvents> events = getAllEvents(s);
			xs.setEvents(events);
			Organization org = s.getUserOrgMappings().iterator().next().getOrganization();
			xs.setOrganization(new XMLOrganization(org.getName(), new XMLAddress(org.getAddress().getId(), new XMLPincode(s.getUserProfile().getAddress().getPincode().getId(), s.getUserProfile().getAddress().getPincode().getState(), s.getUserProfile().getAddress().getPincode().getCity(), s.getUserProfile().getAddress().getPincode().getCountry(), s.getUserProfile().getAddress().getPincode().getPin()), org.getAddress().getAddressline1(), org.getAddress().getAddressline2(), org.getAddress().getAddressGeoLongitude(), org.getAddress().getAddressGeoLatitude())));
			ArrayList<XMLNotification> notifications = getNotifications(s.getId());
			xs.setNotifications(notifications);
		}
		return xs;
	}

	private XMLStudentProfile getStudentProfile(IstarUser s2) {
		// TODO Auto-generated method stub
		UserProfile up = s2.getUserProfile();
		ProfessionalProfile pp = s2.getProfessionalProfile();
		
		
		String getStudentProfile = "SELECT 	IU.ID AS student_id, 	COALESCE(first_name, 'NA') as first_name , 	COALESCE(last_name, 'NA') as last_name, 	CAST (dob AS VARCHAR(50)) as dob, 	COALESCE(mobile,0) as mobile, 	COALESCE(gender,'NA') as gender, 	(case when profile_image is not null then profile_image 	else '/android_images/'||substring(upper(email) from 1 for 1)||'.png' 	end) as profile_image, 	COALESCE(aadhar_no,0) as aadhar_no, 	COALESCE(father_name,'NA') as father_name, 	COALESCE(mother_name,'NA') as mother_name, 	UP.user_id, UP.user_category, 	COALESCE(yop_10,0) as yop_10, 	COALESCE(marks_10,0) as marks_10, 	COALESCE(yop_12,0) as yop_12, 	COALESCE(marks_12,0) as marks_12, 	COALESCE(has_under_graduation,'f') as has_under_graduation, 	COALESCE(under_graduation_specialization_name,'NA') as under_graduation_specialization_name, 	COALESCE(under_gradution_marks,0) as under_gradution_marks, 	COALESCE(has_post_graduation,'f') as has_post_graduation, 	COALESCE(post_graduation_specialization_name,'NA') as post_graduation_specialization_name, 	COALESCE(post_gradution_marks,0) as post_gradution_marks, 	COALESCE(is_studying_further_after_degree,'f')as is_studying_further_after_degree, 	COALESCE(job_sector,'NA') as job_sector, 	COALESCE(preferred_location,'NA') as preferred_location, 	COALESCE(company_name,'NA') as company_name, 	COALESCE(POSITION,'NA') as position, 	COALESCE(duration,'NA') as duration, 	COALESCE(description,'NA') as description, 	COALESCE(interested_in_type_of_course,'NA') as interested_in_type_of_course, 	COALESCE(area_of_interest,'NA') as area_of_interest, 	COALESCE(marksheet_10,'NA') as marksheet_10, 	COALESCE(marksheet_12,'NA') as marksheet_12, 	COALESCE(under_graduate_degree_name,'NA') as under_graduate_degree_name, 	COALESCE(pg_degree_name,'NA') as pg_degree_name, 	COALESCE(resume_url,'NA') as resume_url, 	COALESCE(under_graduation_year,0) as under_graduation_year, 	COALESCE(post_graduation_year,0) as post_graduation_year, 	COALESCE(under_graduation_college,'NA') as under_graduation_college, 	COALESCE(post_graduation_college,'NA') as post_graduation_college FROM 	istar_user IU LEFT JOIN user_profile UP ON (UP.user_id = IU. ID) LEFT JOIN professional_profile PP ON (PP.user_id = IU. ID) WHERE 	IU. ID = " + s2.getId();
		List<HashMap<String, Object>> data = new DBUTILS().executeQuery(getStudentProfile);
		XMLStudentProfile prof = new XMLStudentProfile();
		if (data.size() > 0) {
			for (HashMap<String, Object> row : data) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				prof.setUser_id((Integer) row.get("student_id"));
				prof.setEmail((String) row.get("email"));
				prof.setAadharno(((BigInteger) row.get("aadhar_no")).longValue());
				prof.setFirstname((String) row.get("first_name"));
				prof.setLastname((String) row.get("last_name"));
				prof.setArea_of_interest((String) row.get("area_of_interest"));
				prof.setGender((String) row.get("gender"));
				prof.setMobileno(((BigInteger) row.get("mobile")).longValue());
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

		
				prof.setHas_post_graduation((Boolean)row.get("has_post_graduation"));
				prof.setPost_graduation_specialization_name((String)row.get("post_graduation_specialization_name"));
			
				prof.setIs_studying_further_after_degree((Boolean)row.get("is_studying_further_after_degree"));
				prof.setJob_sector((String)row.get("job_sector"));
				
				prof.setPreferred_location((String)row.get("preferred_location"));
				prof.setCompany_name((String)row.get("company_name"));
				prof.setDuration((String)row.get("duration"));
				prof.setDescription((String)row.get("description"));
				prof.setPosition((String)row.get("position"));
				
				prof.setInterested_in_type_of_course((String)row.get("interested_in_type_of_course"));
				prof.setImage_url_10((String)row.get("image_url_10"));
				prof.setImage_url_12((String)row.get("image_url_12"));
				prof.setProfile_image((String)row.get("profile_image"));
				prof.setUnder_graduate_degree_name((String)row.get("under_graduate_degree_name"));
				prof.setPg_degree_name((String)row.get("pg_degree_name"));
				prof.setResume_url((String)row.get("resume_url"));
				if(row.get("user_category")!=null)
				{
					prof.setUser_category((String)row.get("user_category"));
				}
				else
				{
					prof.setUser_category("COLLEGE");
				}	
			

			}
		}
		return prof;
	}

	private ArrayList<XMLTest> getTests(IstarUser s2) {
		ArrayList<XMLTest> tests = new ArrayList<>();
		/*DBUTILS util = new DBUTILS();
		String sql1 = "SELECT"
				+"	CAST (jobs_event.id AS VARCHAR(50)) AS event_id,"
				+"	jobs_event.eventhour as hours,"
				+"	jobs_event.eventminute as mins,"
				+"	jobs_event.eventdate as event_date,"
				+"	jobs_event.created_at as creation_time,"
				+"	jobs_event.type as event_type,"
				+"	jobs_event.vacancy_id as vacancy_id,"
				+"	jobs_event.status as status,"
				+"	vacancy.profile_title as profile_title,"
				+"	vacancy.location as location,"
				+"	vacancy.offer_letter as offer_letter,"
				+"	vacancy.description as description,"
				+"	company.name as company_name,"
				+"	company.image as image_url,"
				+"	recruiter.name as recruiter_name,"
				+"	assessment.id as assessment_id,"
				+"	assessment.assessmenttitle as assessment_title,"
				+"	assessment.number_of_questions as number_of_questions,"
				+"	assessment.assessmentdurationminutes as duration"
				+" FROM"
				+"	vacancy AS vacancy"
				+" INNER JOIN recruiter as recruiter on recruiter.id = vacancy.recruiter_id"
				+" INNER JOIN company as company on vacancy.company_id = company.ID"
				+" INNER JOIN istar_task_type AS istar_task_type ON vacancy.workflow_task = istar_task_type. ID"
				+" INNER JOIN istar_task_workflow AS istar_task_workflow ON istar_task_workflow.task_id = istar_task_type. ID"
				+" INNER JOIN jobs_event AS jobs_event on (jobs_event.task_id = istar_task_type. ID) AND (jobs_event.status = istar_task_workflow.stage)"
				+" INNER JOIN stage_action_mapping AS stage_action_mapping on stage_action_mapping.stage_id = istar_task_workflow. ID"
				+" INNER JOIN assessment AS assessment on CAST (assessment.ID as varchar(50)) = stage_action_mapping.stage_action"
				+" WHERE"
				+" stage_action_mapping.type='assessment'"
				+" AND jobs_event.actor_id="+s.getId();
		
		String sql1 = "SELECT distinct 	CAST(JE. ID as varchar(50)) AS event_id, 	JE.eventhour AS hours, 	JE.eventminute AS mins, 	" + "JE.eventdate AS event_date, 	JE.created_at as creation_time, 	JE. TYPE AS event_type, 	" + "V.profile_title AS profile_title, 	V.location AS location, 	V.offer_letter as offer_letter," + " 	JE.vacancy_id as vacancy_id, 	V.description as description, 	RC. NAME AS company_name, 	"
				+ "R.name as recruiter_name, 	RC.image as image_url, 	JE.status AS status, 	" + "A.id as assessment_id, 	A.assessmenttitle AS assessment_title, 	" + "A .number_of_questions AS number_of_questions, 	A .assessmentdurationminutes AS duration FROM 	jobs_event JE, 	vacancy V, 	company RC, 	recruiter R, 	assessment A, 	report RE 	 WHERE JE.actor_id = " + s.getId()
				+ " AND V. ID = JE.vacancy_id AND V.recruiter_id= R.id AND V.company_id = RC. ID AND JE.isactive = 't' AND JE.status LIKE '%ASSESSMENT%' AND A.id = (select CAST (split_part(task_action, '__', 2) as integer) as ass_id from istar_task_workflow_concrete where event_id=JE.id AND stage='ASSESSMENT' ) AND RE.assessment_id != A.id AND RE.user_id != " + s.getId();
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
		}*/
		return tests;
	}

	private ArrayList<XMLInterview> getInterviews(IstarUser s2) {
		DBUTILS util = new DBUTILS();
		ArrayList<XMLInterview> interviews = new ArrayList<>();

		/*String sql1 ="select distinct CAST(JE. ID as varchar(50)) AS event_id, JE.eventhour AS hours,"
				+ " 	JE.eventminute AS mins,  panelist_schedule.event_date,JE.created_at as creation_time, JE. TYPE AS event_typ,"
				+ " V.profile_title AS profile_title, 	V.location AS location, JE.vacancy_id as vacancy_id, 	V.description as description,"
				+ " 	RC. NAME AS company_name, R.name as recruiter_name, RC.image as image_url, string_agg(panelist.email, '!#')  as panelists,"
				+ " panelist_schedule.meeting_id, panelist_schedule.meeting_password, panelist_schedule.zoom_host_url, panelist_schedule.zoom_join_url "
				+ "from jobs_event JE,istar_task_type, istar_task_workflow, stage_action_mapping, panelist_schedule, panelist ,vacancy V, 	company RC, "
				+ "	recruiter R where JE.actor_id="+s2.getId()+" AND V. ID = JE.vacancy_id AND V.recruiter_id= R.id AND V.company_id = RC. ID "
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
				String description = "Your interview call scheduled with " + recruiter_name + " at " + interview_time + " on " + interview_date;
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
		}*/
		return interviews;
	}

	private ArrayList<XMLOffer> getOffers(IstarUser s2) {
		ArrayList<XMLOffer> offers = new ArrayList<>();
		/*DBUTILS util = new DBUTILS();
		String sql1 = "SELECT 	CAST(JE. ID as varchar(50)) AS event_id, 	JE.eventhour AS hours, 	JE.eventminute AS mins, 	" + "JE.eventdate AS event_date, 	JE.created_at as creation_time, 	JE. TYPE AS event_type, 	" + "V.profile_title AS profile_title, 	V.location AS location, je.action as offer_letter, " + "JE.vacancy_id as vacancy_id, 	V.description as description, 	RC. NAME AS company_name, "
				+ "R.name as recruiter_name, RC.image as image_url, 	JE.status AS status FROM 	jobs_event JE, 	vacancy V, company RC, 	recruiter R WHERE 	JE.actor_id = " + s.getId() + " AND V. ID = JE.vacancy_id AND V.recruiter_id= R.id AND V.company_id = RC. ID AND JE.isactive = 't' AND JE.status LIKE '%Offered%'";
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
				
				 * AndroidOffer androidoffer = new
				 * AndroidOffer(UUID.fromString(event_id), company_name,
				 * image_url, job_title, description, location, offer_letter,
				 * prettyTimeString);
				 
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
		}*/
		// TODO Auto-generated method stub
		return offers;
	}

	private ArrayList<XMLInvite> getInvites(IstarUser s2) {
		DBUTILS util = new DBUTILS();
		ArrayList<XMLInvite> invites = new ArrayList<>();
		/*String sql = "SELECT 	CAST(JE. ID as varchar(50)) AS event_id, 	JE.eventhour AS hours, 	JE.eventminute AS mins, 	" + "JE.eventdate AS event_date, 	JE.created_at as creation_time, 	JE. TYPE AS event_type," + " 	V.profile_title AS profile_title, 	JE. ACTION AS event_action, 	V.location AS location, " + "	V.description as description, 	RC. NAME AS company_name, RC.image as image_url, 	"
				+ "JE.status AS status FROM 	jobs_event JE, 	vacancy V, 	company RC WHERE 	JE.actor_id = " + s.getId() + " AND V. ID = JE.vacancy_id AND V.company_id = RC. ID AND JE.isactive = 't' AND JE.status LIKE '%TARGETED%'";
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
		}*/
		return invites;
	}

	private ArrayList<XMLNotification> getNotifications(Integer id) {
		ArrayList<XMLNotification> notifications = new ArrayList<>();
		// ArrayList<ArrayList<String>> table = new ArrayList<>();
		String sql1 = "SELECT CAST ( 		istar_notification.event_id AS VARCHAR 	) AS event_id,	CAST ( 		istar_notification. ID AS VARCHAR 	) AS notif_id, 	istar_notification.created_at AS notif_date, 	istar_notification. TYPE AS notif_type, 	istar_notification.title AS title, istar_notification.details as details, 	istar_notification.sender_id AS sender FROM 	istar_notification WHERE 	receiver_id = " + id
				+ " AND istar_notification.status = 'UNREAD' order by istar_notification.created_at desc LIMIT 20  ";
		DBUTILS db = new DBUTILS();
		List<HashMap<String, Object>> results = db.executeQuery(sql1);		
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
				String dddd = p.format(date).replaceAll("minute from now", "mins ago").replaceAll("minutes from now", "mins ago").replaceAll("minutes", "mins").replaceAll("moments", "mins");
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

	private ArrayList<XMLCoachStudent> getCoachStudents(IstarUser s2, HttpServletRequest request) {
		ArrayList<XMLCoachStudent> data = new ArrayList<>();
		/*ArrayList<Integer> batch_group_added = new ArrayList<>();
		ArrayList<Integer> student_added = new ArrayList<>();
		for (TrainerBatch tb : s2.getTrainerBatchs()) {
			BatchGroup bg = tb.getBatch().getBatchGroup();
			if (!batch_group_added.contains(bg.getId())) {
				batch_group_added.add(bg.getId());
				for (BatchStudents bs : bg.getBatchStudentses()) {
					if (bs.getIstarUser().getId() != s2.getId()) {
						XMLCoachStudent cs = new XMLCoachStudent();
						cs.setBatchGroup(bs.getBatchGroup().getName());
						String url = request.getRequestURL().toString();
						String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
						// System.out.println("222222222"+bs.getIstarUser().getImageUrl());
						if (bs.getIstarUser().getUserProfile().getProfileImage() == null || bs.getIstarUser().getUserProfile().getProfileImage().equalsIgnoreCase("null")) {
							cs.setImageUrl(baseURL + "" + "images/profile.jpg");
						} else {
							cs.setImageUrl(baseURL + "" + bs.getIstarUser().getUserProfile().getProfileImage());
						}
						cs.setBatchId(bg.getId());
						cs.setMobile(bs.getIstarUser().getMobile().toString());
						cs.setOrganizationName(bs.getIstarUser().getUserOrgMappings().iterator().next().getOrganization().getName());
						cs.setStudentId(bs.getIstarUser().getId());
						cs.setStudentName(bs.getIstarUser().getUserProfile().getFirstName());
						DBUTILS util = new DBUTILS();
						String query = "select * from trainer_favourite where student_id =" + bs.getIstarUser().getId() + " and trainer_id= " + s2.getId() + " and favourite_status='t'";
						if (util.executeQuery(query).size() > 0) {
							cs.setIsfavoutite(true);
						} else {
							cs.setIsfavoutite(false);
						}
						ArrayList<XMLCourseRating> crating = new ArrayList<>();
						String sql = "select * from (select  distinct C.id as course_id , C.course_name as name from batch_group, batch, batch_students, course C where batch_group.id = batch.batch_group_id and batch_students.batch_group_id= batch_group.id and batch.course_id =  C.id and batch_students.student_id= " + bs.getIstarUser().getId()
								+ ") t1 left join (SELECT 	( 		SUM ( 			R.score * 100 / A .number_of_questions 		) 	) / COUNT (*) AS score, 	C . ID AS course_id1 FROM 	report R, 	assessment A, 	cmsession CMM, 	MODULE MM, 	lesson LL, 	task TT, 	student S, 	course C WHERE 	R.user_id = S. ID AND A .lesson_id = LL. ID AND R.assessment_id = A . ID AND LL.dtype = 'ASSESSMENT' AND MM. ID = CMM.module_id AND MM.course_id = C . ID AND CMM. ID = LL.session_id AND LL. ID = TT.item_id AND TT.item_type = 'LESSON' AND TT.status = 'PUBLISHED' AND S. ID = "
								+ bs.getIstarUser().getId() + " GROUP BY 	C . ID ) t2 on t1.course_id = t2.course_id1 ";
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
		System.out.println("bg count" + batch_group_added.size());*/
		return data;
	}

	private ArrayList<XMLEvents> getAllEvents(IstarUser s2) {
		IstarUserDAO dao = new IstarUserDAO();
		ArrayList<XMLEvents> data = new ArrayList<>();
		DBUTILS util = new DBUTILS();
		String getBatchScheduleEvent ="SELECT	 	cast (eventdate as VARCHAR) as eventdate, 	eventhour, type,	eventminute,	 	batch_schedule_event.ID, 	status, 	ACTION, 	cmsession_id, 	batch_id, 	event_name, 	classroom_id, 	associate_trainee, 	batch_group_code, batch.id as batch_id, batch.name b_name,batch_group.id as bg_id, batch_group.name as bg_name, 	classroom_details.id as class_id, classroom_details.classroom_identifier as class_name FROM 	batch_schedule_event, batch_group, batch,classroom_details where batch_schedule_event.batch_id = batch.id and batch.batch_group_id = batch_group.id and batch_schedule_event.classroom_id = classroom_details.id and batch_schedule_event.actor_id ="+s2.getId();
		List<HashMap<String, Object>> bseData = util.executeQuery(getBatchScheduleEvent);
		for(HashMap<String, Object> bseRow : bseData)
		{
			XMLEvents event = new XMLEvents();
			event.setBgroupId(Integer.parseInt(bseRow.get("bg_id").toString()));
			event.setId(bseRow.get("id").toString());
			event.setBatchId(Integer.parseInt(bseRow.get("batch_id").toString()));
			event.setBatchName(bseRow.get("b_name").toString());
			event.setClassId(Integer.parseInt(bseRow.get("class_id").toString()));
			event.setClassName(bseRow.get("class_name").toString());
			event.setCmsessionId(Integer.parseInt(bseRow.get("cmsession_id").toString()));
			event.setCmsessionTitle("NOT_RELEVANT");
			// SimpleDateFormat simpleDateFormat = new
			// SimpleDateFormat("yyyy-MM-dd h:mm a");
			event.setEventdate(bseRow.get("eventdate").toString());
			event.setEventHour(Integer.parseInt(bseRow.get("eventhour").toString()));
			event.setEventMin(Integer.parseInt(bseRow.get("").toString()));
			event.setEventName(bseRow.get("eventminute").toString());
			String desc = "Batch: " + event.getBatchName() + "!#";
			desc = desc + "Classroom: " + event.getClassName() + "!#";
			desc = desc + "Classroom ID: " + event.getClassId();
			event.setDescription(desc);
			event.setEventType(bseRow.get("type").toString());
			event.setStatus(bseRow.get("status").toString());
			data.add(event);
		}
		
		String getIstarAssesmentEvent = "SELECT 	eventdate, 	eventhour, 	eventminute, 	isactive, 	TYPE, 	istar_assessment_event.ID, 	status, 	ACTION, 	assessment_id, 	batch_id, course.course_name, COALESCE(assessment.assessmenttitle,'NA') as title FROM 	istar_assessment_event, assessment, batch, course where assessment.id =istar_assessment_event.assessment_id and batch.id = istar_assessment_event.batch_id and course.id = batch.course_id and actor_id ="+s2.getId();
		List<HashMap<String, Object>> assessmentData = util.executeQuery(getIstarAssesmentEvent);
		for(HashMap<String, Object> assessRow : assessmentData)
		{
			XMLEvents event = new XMLEvents();
			event.setId(assessRow.get("id").toString());
			event.setBatchId(Integer.parseInt(assessRow.get("batch_id").toString()));
			event.setEventdate(assessRow.get("eventdate").toString());
			event.setEventHour(Integer.parseInt(assessRow.get("eventhour").toString()));
			if (assessRow.get("eventminute") != null) {
				event.setEventMin(Integer.parseInt(assessRow.get("eventminute").toString()));
			} else {
				event.setEventMin(30);
			}
			event.setStatus(assessRow.get("status").toString());
			event.setEventName(assessRow.get("title").toString());
			event.setEventType(assessRow.get("type").toString());
			event.setAssessmentId(Integer.parseInt(assessRow.get("assessment_id").toString()));
			event.setAssessmentTitle(assessRow.get("title").toString());
			event.setEventdate(assessRow.get("eventdate").toString());
			event.setCmsessionTitle("NON_RELEVANT");
			event.setCmsessionId(-1);
			String desc = "Assessment: " + assessRow.get("title").toString() + "!#";
			desc = desc + "Session: " + assessRow.get("title").toString() + "!#";
			desc = desc + "Course: " + assessRow.get("course_name").toString();
			event.setDescription(desc);
			data.add(event);
		}		
		
		return data;
	}

	private ArrayList<XMLNote> getAllMyNotes(IstarUser s2) {
		ArrayList<XMLNote> notes = new ArrayList<>();
		/*for (StudentNote iterable_element : s.getStudentnotes()) {
			try {
				Document doc = Jsoup.parse(iterable_element.getNoteText());
				String text = (doc.body().text());
				XMLNote note = new XMLNote(iterable_element.getId(), text, iterable_element.getSlideUrl(), iterable_element.getTimestamp(), true, null);
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
		}*/
		return notes;
	}

	private ArrayList<XMLNote> getAllMySharedNotes(IstarUser s2) {
		ArrayList<XMLNote> shared_notes = new ArrayList<>();
		/*for (StudentShare sh : s.getStudentshare()) {
			try {
				StudentNote sn = sh.getStudentNote();
				XMLNote note = new XMLNote(sn.getId(), sn.getNoteText(), sn.getSlideUrl(), sh.getTimestamp(), false, sh.getshared_by().getName());
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
		}*/
		return shared_notes;
	}

	private ArrayList<XMLBatch> getBatches(BatchGroup batchGroup) {
		ArrayList<XMLBatch> items = new ArrayList<>();
		IstarUserDAO dao = new IstarUserDAO();
		String hql = "from Batch isss where isss.batchGroup=:batch_group_id order by isss.orderId";
		Query query = dao.getSession().createQuery(hql);
		query.setInteger("batch_group_id", batchGroup.getId());
		List<Batch> batchess = query.list();
		for (Batch batch : batchess) {
			XMLBatch b = new XMLBatch();
			b.setId(batch.getId());
			b.setBatchName(batch.getName());
			b.setOrderId(batch.getOrderId());
			XMLCourse course = new XMLCourse();
			course.setCourseId(batch.getCourse().getId());
			
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
			boolean currentCourseHasPublishedLesson = false; 
			 outerloop:{
				for(XMLSession session: sessions )
				{
					for(XMLLesson l: session.getLessons())
					{
						
						if(l.getStatus()!=null && l.getStatus().equalsIgnoreCase("PUBLISHED"))
						{
							currentCourseHasPublishedLesson = true;
							break outerloop;
						}
					}
				}
			}
			
			if(currentCourseHasPublishedLesson)
			{
				b.setCourse(course);
				items.add(b);	
			}
			// }
		}
		return items;
	}

	private ArrayList<XMLSession> getCMSession(Batch batch) {
		int last_session_order = -1;
		int last_module_order = -1;
		IstarUserDAO dao = new IstarUserDAO();	
		String  hql1 ="select cast (cmsession_module.oid as integer) as cmsession_order,cast (module_course.oid as integer) as module_order  from slide_change_log, cmsession_module, module_course where cmsession_module.cmsession_id = slide_change_log.cmsession_id and cmsession_module.module_id = slide_change_log.module_id and module_course.module_id = slide_change_log.module_id and module_course.course_id = slide_change_log.course_id and slide_change_log.batch_id="+batch.getId()+"  order by slide_change_log.id desc limit 1";
		DBUTILS util = new DBUTILS();
		List<HashMap<String, Object>> data = util.executeQuery(hql1);
		if(data.size()>0)
		{
			if((data.get(0)).get("cmsession_order")!=null)
			{
				last_session_order =  (int)(data.get(0)).get("cmsession_order");
			}
			if((data.get(0)).get("module_order")!=null)
			{
				last_module_order = (int)(data.get(0)).get("module_order");
			}			 
		}
		
		ArrayList<XMLSession> cmsessions = new ArrayList<>();
		String moduleHql = "select cast (module_course.oid as integer) as module_order, module.id, module.module_name from module_course, module where module_course.course_id="+batch.getCourse().getId()+" and module_course.module_id = module.id and module.is_deleted = 'f' order by module_course.oid";
		List<HashMap<String, Object>> moduleData = util.executeQuery(moduleHql);		
		for (HashMap<String, Object> row: moduleData) {
			int moduleID = (int)row.get("id");
			int moduleORder = (int)row.get("module_order");
			String moduleName = (String)row.get("module_name");
				
			String hqlSession = "select cast (cmsession_module.oid as integer) as cmsession_order, cmsession.id, cmsession.title, COALESCE(cmsession.description,'NA') as description from cmsession_module, cmsession where cmsession_module.cmsession_id = cmsession.id and cmsession_module.module_id = "+moduleID+" and cmsession.is_deleted ='f' order by cmsession_module.oid ";
			List<HashMap<String, Object>> cmsessionData = util.executeQuery(hqlSession);			
			for (HashMap<String, Object> cmsRow: cmsessionData) {
				int cmsID = (int)cmsRow.get("id");
				int cmsORder = (int)cmsRow.get("cmsession_order");
				String cmsName = (String)cmsRow.get("title");
				String cmsDesc = (String)cmsRow.get("description");
				try {
					XMLSession cms = new XMLSession();
					cms.setLastOrderId(last_session_order);
					cms.setLastModuleOrderId(last_module_order);										
					cms.setCurrentmoduleOrderId(cmsORder);
					if(cmsORder> last_session_order)
					{
						cms.setReadable(false);
					}
					else
					{
						cms.setReadable(true);
					}	
					
					cms.setOrderId(cmsORder);
					cms.setCmsession_id(cmsID);
					cms.setCmsessionName(cmsName);
					if(cmsDesc.length()>100)
					{
						cms.setCmsessionDescription(cmsDesc.substring(0, 100));
					}
					else
					{
						cms.setCmsessionDescription(cmsDesc);	
					}
					
					ArrayList<XMLLesson> lessons = getLessons(cmsID, cmsORder, moduleORder);					
					cms.setLessons(lessons);
					cmsessions.add(cms);
					
				} catch (Exception se) {
					se.printStackTrace();
					System.err.println("Problem with Session ID ->" + cmsID);
				}
			}
		}
		return cmsessions;
	}

	private ArrayList<XMLLesson> getLessons(Integer cs, Integer cmsOrder, Integer moduleOrder) {
		ArrayList<XMLLesson> less = new ArrayList<>();
		DBUTILS util = new DBUTILS();
		String findLessonInCMSession = "SELECT 	CAST ( 		lesson_cmsession.oid AS INTEGER 	) AS lesson_order, 	lesson. ID, 	lesson. TYPE, 	lesson.title, task.state FROM 	lesson_cmsession, 	lesson, task WHERE  	lesson. ID = lesson_cmsession.lesson_id and task.item_id = lesson.id and task.item_type = 'LESSON'  AND lesson_cmsession.cmsession_id = "+cs+" ORDER BY 	lesson_cmsession.oid";
		List<HashMap<String, Object>> lessonsInCMSession = util.executeQuery(findLessonInCMSession);
		for(HashMap<String, Object> lessonRow: lessonsInCMSession)
		{
			int lessonOrderId = (int) lessonRow.get("lesson_order");
			int lessonId = (int)lessonRow.get("lesson_order");
			String status = lessonRow.get("state").toString();
			Lesson ll = new LessonDAO().findById(lessonId);
			for(Presentation ppt : ll.getPresentations())
			{
				XMLLesson l = new XMLLesson();			
				l.setAssessment(null);
				l.setId(lessonId);
				if(ppt.getType().equalsIgnoreCase("PRESENTATION"))
				{	
					
					l.setType("PRESENTATION");
					ArrayList<XMLSlide> slides = getSlides(ppt);
					l.setSlides(slides);
				}
				else if(ppt.getType().equalsIgnoreCase("INTERACTIVE"))
				{
					l.setType("INTERACTIVE");
				}
				
				l.setLast_slide_pointer(0);
				int session_order = cmsOrder;
				int lesson_order = lessonOrderId;
				int module_order_id  = moduleOrder;
	            int absolute_lesson_order = module_order_id*1000+session_order*100+lesson_order*10;								
				l.setLOrderId(absolute_lesson_order);								
				l.setConcreteId(ppt.getId());						
				if (status.equalsIgnoreCase("PUBLISHED")) {					
					l.setStatus("PUBLISHED");
				}
				else
				{
					l.setStatus("REQUEST_FOR_PUBLISH");
				}	
				less.add(l);
			}
			
			
		}
		
		
		return less;
	}

	private XMLAssessment getAssessment(Lesson ll) {
		XMLAssessment assess = new XMLAssessment();
		/*assess.setAssessmentId(ll.getAssessment().getId());
		assess.setAssessmentTitle(ll.getAssessment().getAssessmenttitle());
		assess.setAssessmentDurationMinutes(ll.getAssessment().getAssessmentdurationminutes());
		assess.setNumber_of_questions(ll.getAssessment().getNumber_of_questions());
		ArrayList<XMLQuestion> questions = getQuestions(ll.getAssessment());
		assess.setQuestions(questions);*/
		return assess;
	}

	private ArrayList<XMLQuestion> getQuestions(Assessment assessment) {
		ArrayList<XMLQuestion> question_set = new ArrayList<>();
		/*for (AssessmentQuestion aq : assessment.getAssessmentQuestions()) {
			XMLQuestion que = new XMLQuestion();
			que.setQueId(aq.getQuestion().getId());
			que.setQuestionText(aq.getQuestion().getQuestionText());
			que.setOrderId(aq.getOrderId());
			que.setQuestionType(aq.getQuestion().getQuestionType());
			que.setDurationInSec(aq.getQuestion().getDurationInSec());
			ArrayList<XMLOption> option = getOptions(aq.getQuestion());
			que.setOptions(option);
			question_set.add(que);
		}*/
		return question_set;
	}

	private ArrayList<XMLOption> getOptions(Question question) {
		ArrayList<XMLOption> data = new ArrayList<>();
		/*for (AssessmentOption aop : question.getAssessmentOptions()) {
			XMLOption op = new XMLOption();
			op.setOptionId(aop.getId());
			op.setOptionText(aop.getText());
			if (aop.getMarkingScheme() != null && aop.getMarkingScheme() == 1) {
				op.setCorrect(true);
			} else {
				op.setCorrect(false);
			}
			data.add(op);
		}*/
		return data;
	}

	private ArrayList<XMLSlide> getSlides(Presentation ppt) {
		ArrayList<XMLSlide> slides = new ArrayList<>();
		for (Slide slide : ppt.getSlides()) {
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
		/*StudentNote note = new StudentNote();
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
		}*/
		return data;
	}

	private ArrayList<XMLNote> getSharedNotes(Slide sl) {
		ArrayList<XMLNote> data = new ArrayList<>();
		/*StudentShareDAO sh = new StudentShareDAO();
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
		}*/
		return data;
	}

	private ArrayList<XMLBatchMember> getClassMates(BatchGroup batchGroup) {
		ArrayList<XMLBatchMember> items = new ArrayList<>();
		for (BatchStudents iterable_element : batchGroup.getBatchStudentses()) {
			XMLBatchMember xmb = new XMLBatchMember(iterable_element.getIstarUser().getId(), iterable_element.getIstarUser().getUserProfile().getFirstName(), "NULL");
			items.add(xmb);
		}
		return items;
	}

	/*public XMLStudent initializeStudent(int student_id) {
		XMLStudent xs;
		System.err.println("initializing " + student_id);
		IstarUserDAO dao = new IstarUserDAO();
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
			xs.setUserType(s.getUserRoles().iterator().next().getRole().getRoleName());
			xs.setAddress(new XMLAddress(s.getUserProfile().getAddress().getId(), new XMLPincode(s.getUserProfile().getAddress().getPincode().getId(), s.getUserProfile().getAddress().getPincode().getState(), s.getUserProfile().getAddress().getPincode().getCity(), s.getUserProfile().getAddress().getPincode().getCountry(), s.getUserProfile().getAddress().getPincode().getPin()), s.getUserProfile().getAddress().getAddressline1(), s.getUserProfile().getAddress().getAddressline2(), s.getUserProfile().getAddress().getAddressGeoLongitude(), s.getUserProfile().getAddress().getAddressGeoLatitude()));
			ArrayList<XMLBatchGroup> batchGroups = new ArrayList<>();
			for (BatchStudents bg : s.getBatchStudentses()) {
				XMLBatchGroup e = new XMLBatchGroup();
				e.setId(bg.getBatchGroup().getId());
				e.setName(bg.getBatchGroup().getName());
				e.setOrg_id(bg.getBatchGroup().getOrganization().getId());
				e.setOrgname(bg.getBatchGroup().getOrganization().getName());
				ArrayList<XMLBatch> btaches = getBatches(bg.getBatchGroup());
				e.setBtaches(btaches);
				ArrayList<XMLBatchMember> classmates = getClassMates(bg.getBatchGroup());
				e.setClassmates(classmates);
				batchGroups.add(e);
			}
			xs.setBatchGroups(batchGroups);
			if (s.getUserRoles().iterator().next().getRole().getRoleName().equalsIgnoreCase("TRAINER")) {
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
			Organization org = s.getUserOrgMappings().iterator().next().getOrganization();
			xs.setOrganization(new XMLOrganization(org.getName(), new XMLAddress(org.getAddress().getId(), new XMLPincode(s.getUserProfile().getAddress().getPincode().getId(), s.getUserProfile().getAddress().getPincode().getState(), s.getUserProfile().getAddress().getPincode().getCity(), s.getUserProfile().getAddress().getPincode().getCountry(), s.getUserProfile().getAddress().getPincode().getPin()), org.getAddress().getAddressline1(), org.getAddress().getAddressline2(), org.getAddress().getAddressGeoLongitude(), org.getAddress().getAddressGeoLatitude())));
			ArrayList<XMLNotification> notifications = getNotifications(s.getId());
			xs.setNotifications(notifications);
		}
		return xs;
	}*/

	private ArrayList<XMLCoachStudent> getCoachStudents(IstarUser s2) {
		ArrayList<XMLCoachStudent> data = new ArrayList<>();
		ArrayList<Integer> batch_group_added = new ArrayList<>();
		ArrayList<Integer> student_added = new ArrayList<>();
		for (TrainerBatch tb : s2.getTrainerBatchs()) {
			BatchGroup bg = tb.getBatch().getBatchGroup();
			if (!batch_group_added.contains(bg.getId())) {
				batch_group_added.add(bg.getId());
				for (BatchStudents bs : bg.getBatchStudentses()) {
					if (bs.getIstarUser().getId() != s2.getId()) {
						XMLCoachStudent cs = new XMLCoachStudent();
						cs.setBatchGroup(bs.getBatchGroup().getName());
						String baseURL = "http://api.talentify.in/";
						// System.out.println("222222222"+bs.getIstarUser().getImageUrl());
						if (bs.getIstarUser().getUserProfile().getProfileImage() == null || bs.getIstarUser().getUserProfile().getProfileImage().equalsIgnoreCase("null")) {
							cs.setImageUrl(baseURL + "" + "images/profile.jpg");
						} else {
							cs.setImageUrl(baseURL + "" + bs.getIstarUser().getUserProfile().getProfileImage());
						}
						cs.setBatchId(bg.getId());
						cs.setMobile(bs.getIstarUser().getMobile().toString());
						cs.setOrganizationName(bs.getIstarUser().getUserOrgMappings().iterator().next().getOrganization().getName());
						cs.setStudentId(bs.getIstarUser().getId());
						cs.setStudentName(bs.getIstarUser().getUserProfile().getFirstName());
						DBUTILS util = new DBUTILS();
						String query = "select * from trainer_favourite where student_id =" + bs.getIstarUser().getId() + " and trainer_id= " + s2.getId() + " and favourite_status='t'";
						if (util.executeQuery(query).size() > 0) {
							cs.setIsfavoutite(true);
						} else {
							cs.setIsfavoutite(false);
						}
						ArrayList<XMLCourseRating> crating = new ArrayList<>();
						String sql = "select * from (select  distinct C.id as course_id , C.course_name as name from batch_group, batch, batch_students, course C where batch_group.id = batch.batch_group_id and batch_students.batch_group_id= batch_group.id and batch.course_id =  C.id and batch_students.student_id= " + bs.getIstarUser().getId()
								+ ") t1 left join (SELECT 	( 		SUM ( 			R.score * 100 / A .number_of_questions 		) 	) / COUNT (*) AS score, 	C . ID AS course_id1 FROM 	report R, 	assessment A, 	cmsession CMM, 	MODULE MM, 	lesson LL, 	task TT, 	student S, 	course C WHERE 	R.user_id = S. ID AND A .lesson_id = LL. ID AND R.assessment_id = A . ID AND LL.dtype = 'ASSESSMENT' AND MM. ID = CMM.module_id AND MM.course_id = C . ID AND CMM. ID = LL.session_id AND LL. ID = TT.item_id AND TT.item_type = 'LESSON' AND TT.status = 'PUBLISHED' AND S. ID = "
								+ bs.getIstarUser().getId() + " GROUP BY 	C . ID ) t2 on t1.course_id = t2.course_id1 ";
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
