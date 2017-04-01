<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.istarindia.apps.dao.DBUTILS"%>
<%@page import="in.talentifyU.auth.controllers.SkillHolder"%>
<%@page import="com.istarindia.apps.dao.StatsStudentQuestion"%>
<%@page import="com.istarindia.apps.dao.Question"%>
<%@page import="com.istarindia.apps.dao.LearningObjective"%>
<%@page import="com.istarindia.apps.dao.SkillLearningObjMapping"%>
<%@page import="com.istarindia.apps.dao.StudentDAO"%>
<%@page import="com.istarindia.apps.dao.Student"%>
<%@page import="com.istarindia.apps.dao.SkillPrecentile"%>
<%@page import="com.istarindia.apps.dao.AssessmentDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.istarindia.apps.dao.StatsStudentAssessment"%>
<%@page import="com.istarindia.apps.dao.Assessment"%>
<%@page import="com.istarindia.apps.dao.SkillDAO"%>
<%@page import="com.istarindia.apps.dao.Skill"%>
<%@page import="in.talentifyU.utils.services.NewReportUtils"%>
<%@page import="java.util.*"%>
<%@page import="in.talentifyU.auth.controllers.SkillHolder"%>

<html>

<%	
	String url = request.getRequestURL().toString();			
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())	+ request.getContextPath() + "/";
	int user_id = Integer.parseInt(request.getParameter("user_id"));
	Student student = new StudentDAO().findById(user_id);
	int skill_id = 5;
	if (request.getParameterMap().containsKey("skill_id")) {
		skill_id = Integer.parseInt(request.getParameter("skill_id"));
	}
	Skill skill0 = new SkillDAO().findById(skill_id);
	int parent_test_id = 0;
	if(request.getParameterMap().containsKey("assessment_id"))
	{
		parent_test_id = Integer.parseInt(request.getParameter("assessment_id"));	
	}
	
	Assessment row= new AssessmentDAO().findById(parent_test_id);
	HashMap <String, String>  repdata = new NewReportUtils().getRepoDetailForAssessment(user_id, row);
	
	String accuracy=repdata.get("accuracy");
	String time_taken =repdata.get("time_taken");
	String percentile =repdata.get("percentile");
	String created_at=repdata.get("created_at");
	int id = row.getId();
	String assessment_title= repdata.get("assessment_title");
	String wizard_image=repdata.get("wizard_image");
	
	
	
	
%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>INSPINIA | Empty Page</title>
<link href="<%=baseURL%>new/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=baseURL%>new/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="<%=baseURL%>new/css/animate.css" rel="stylesheet">
<link href="<%=baseURL%>new/css/style.css" rel="stylesheet">
<link href="<%=baseURL%>new/css/gcard.css" rel="stylesheet">
<link href="<%=baseURL%>assets/plugins/jQueryAddUI/dist/addDropMenu.css" rel="stylesheet" type="text/css">
 <link rel="stylesheet" href="<%=baseURL%>assets/plugins/swiper/dist/css/swiper.min.css">
 <link rel="stylesheet" href="<%=baseURL%>assets/plugins/card-flip/card-flip.css">
<link href="<%=baseURL%>new/css/custom.css" rel="stylesheet">
</head>
<body class="">
	<div id="wrapper" class="gray-bg" style="min-height: 100vh">
	
		<div id='mimic-header-gap' class="mimic-header" style="margin-top: 2vh;">
			
		</div>
		<div class="gcard gpanel-header mb0 page-header">
			
			<img id="window-back" class=" z3 i-arrow left"  src="<%=baseURL%>assets/images/reports/window-back.png?a=1" />
			<span> <%=skill0.getSkillTitle() %> </span>
			<img class="toggle-visibility z14 i-arrow right"  src="<%=baseURL%>assets/images/reports/down.png?a=1" />
			
			<div class="mydropdown" style="display: none;">
				<% if(skill_id!=0) { %>
				<a class="hide-options" style="padding-top: 0%; " href='<%=baseURL%>cpreports/report.jsp?user_id=<%=user_id%>'>Overall</a> <p></p><hr style="margin: 5px;">
				<% 
					}
					int temp = 0;
					for (Skill item : SkillHolder.getChilSkills(skill_id, user_id)) {
						if(SkillHolder.getChilSkills(item.getId(), user_id).size() >0 && SkillHolder.hasData(item.getId(), user_id))
						{
						temp = temp + 6;
				%>
				<a class="hide-options" style="padding-top: <%=temp%>%; " href='<%=baseURL%>cpreports/report.jsp?user_id=<%=user_id%>&skill_id=<%=item.getId()%>'><%=item.getSkillTitle()%></a><p></p><hr style="margin: 5px;">
				
				<%
						}
					}
				%>
			</div>
		</div>
		<div class="gcard single">
		<div class="test-card-holder" style="background-image: url('<%=baseURL%>assets/images/reports/<%=wizard_image %>.png?a=1');" onClick="window.location ='<%=baseURL%>cpreports/test_skill_list.jsp?user_id=<%=user_id%>&assessment_id=<%=row.getId()%>&skill_id=<%=skill_id %>'" >
	 <div class="header-span ">
			<span><%=assessment_title %></span>
			<span class="timestamp"><i><%=created_at %></i></span>
		</div>
		<div class="gcard percentiles test"  >
			<div class="item-3">
				<span class="left-bottom "><%=accuracy%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/accuracy.png?a=1" />
			</div>
			
			<div class="item-3 bleft">
				<span class="left-bottom "> <%=percentile%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/ranking.png?a=1" />
			</div>
			
			<div class="item-3 bleft">
				<span class="left-bottom "> <%=time_taken %></span>
				<img class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/time.png?a=1" />
			</div>
			
			
		</div>
		</div>
	</div>
			
			
			 <div class=" gcard-child" >
					
<%
ArrayList<Skill> data =new NewReportUtils().getAllSkillsForAssessment(parent_test_id);
for(Skill skill : data)
	{
	
	String child_skill_title= skill.getSkillTitle();
	int child_id = skill.getId();
	String accuracy_skill ="N/A";
	String percentile_skill = "N/A";
	String time_taken_skill="";
	int time =0;
	ArrayList<Integer> alreadyadded = new ArrayList<>();
	
	String qqq = "select distinct student_assessment.question_id, student_assessment.correct, student_assessment.time_taken  from student_assessment, skill_learning_obj_mapping, learning_objective_question where skill_learning_obj_mapping.learning_objective_id = learning_objective_question.learning_objectiveid and learning_objective_question.questionid = student_assessment.question_id and skill_id in ( "+new NewReportUtils().queryForChildSkill(child_id)+" ) and student_assessment.student_id="+user_id+" ";
	DBUTILS util = new DBUTILS();
	List<HashMap<String, Object>> qqdata = util.executeQuery(qqq);
	int total_que = 0;
	int ans_corr= 0;
	for(HashMap<String, Object> rows: qqdata)
	{
		int question_id = (int) rows.get("question_id");
		if(!alreadyadded.contains(question_id))
		{
			alreadyadded.add(question_id);
			boolean corr  = (boolean) rows.get("correct");
			int t_taken = (int) rows.get("time_taken");
			if(corr)
			{
				ans_corr++;
			}
			total_que++;
			time = time+t_taken;			
		}		
	}

	time_taken_skill = time+"m"; 
	if(total_que!=0)
	{
		accuracy_skill = ((ans_corr*100)/total_que)+"%";
	}
	
	String batch_percentile_query = "select MT.stud_id, cast (MT.batch_percentile as integer) from (SELECT 	report.user_id as stud_id, (cume_dist() OVER (ORDER BY sum (report.score))*100) as batch_percentile FROM 	report WHERE report.user_id IN ( SELECT DISTINCT 		BC.student_id 	FROM 		batch_students BC 	WHERE 		BC.batch_group_id IN ( 			SELECT 				B.batch_group_id 			FROM 				batch_students B 			WHERE 				B.student_id = "+user_id+"  		)  order by 	BC.student_id ) AND report.assessment_id IN ( 	SELECT DISTINCT 		assessment. ID 	FROM 		skill_learning_obj_mapping, 		lesson, 		learning_objective_lesson, 		assessment 	WHERE 		skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 	AND learning_objective_lesson.lessonid = lesson. ID 	AND lesson. ID = assessment.lesson_id 	AND skill_learning_obj_mapping.skill_id in ("+new NewReportUtils().queryForChildSkill(child_id)+") )  group by stud_id ) MT  where MT.stud_id = "+user_id+"  ";
	List<HashMap<String , Object>> data_batch_percentile = util.executeQuery(batch_percentile_query);
	for(HashMap<String , Object> rows : data_batch_percentile)
	{
		if(rows.get("batch_percentile")!=null)
		{
			percentile_skill= (int)rows.get("batch_percentile")+"%";
		}
	}
	

%>
				<div class="item-holder ">
					<div id="<%=parent_test_id%>_<%=child_id%>" class="skill-list-container"   data-toggle="collapse" data-parent="#test_accordion_skill" href="#test_child_skill_<%=parent_test_id%>_<%=child_id%>">
						<div class="header-span gdivider">
							<span  ><%=skill.getSkillTitle()%></span>
						</div>
						<div class="gcard percentiles "  >
							<div class="item-4">
								<span class="bottom-middle  middle"><%=accuracy_skill%></span>
								<img class="top-middle  z18"  src="<%=baseURL%>assets/images/reports/accuracy.png?a=1" />
							</div>
							
							<div class="item-4 bleft">
								<span class=" bottom-middle middle"> <%=percentile_skill%></span>
								<img class=" top-middle  z18"  src="<%=baseURL%>assets/images/reports/ranking.png?a=1" />
							</div>
							
							<div class="item-4 bleft bright">
								<span class=" bottom-middle middle"><%=time_taken_skill %></span>
								<img class="top-middle  z18"  src="<%=baseURL%>assets/images/reports/time.png?a=1" />
							</div>
							
							<div class="item-4">
								<span class=" bottom-middle  middle " style="right:15%">Questions</span>
								<img  class=" top-middle  z18 "  src="<%=baseURL%>assets/images/reports/play_questions.png?a=1" />
							</div>
						</div>
					</div>
					
					<div id="test_child_skill_<%=parent_test_id%>_<%=child_id%>" class="test-skill-list-test-child-div panel-collapse collapse in" style=" height: 55vh; background-color: white;  box-shadow: 1px 1px 4px black;">
						<div class="panel-body" style="padding: 10px 10px;  border: 1px solid whitesmoke;">
							 <jsp:include page="skill_question_slider.jsp">
								<jsp:param name="child_skill_id" value="<%=child_id%>" />
								<jsp:param name="parent_skill_id" value="<%=parent_test_id%>" />
								<jsp:param name="user_id" value="<%=user_id%>" />
							</jsp:include>
						</div>
					</div>
					
				</div>
				
				
<%
}
%>
			
			</div>
		</div>

	<!-- Mainly scripts -->
	<script src="<%=baseURL%>new/js/jquery-2.1.1.js"></script>
	<script src="<%=baseURL%>new/js/bootstrap.min.js"></script>
	<script src="<%=baseURL%>new/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="<%=baseURL%>new/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	
	<!-- Custom and plugin javascript -->
	<script src="<%=baseURL%>new/js/inspinia.js"></script>
	<script src="<%=baseURL%>new/js/plugins/pace/pace.min.js"></script>
	<script src="<%=baseURL%>assets/plugins/swiper/dist/js/swiper.min.js"></script>
	<script src="<%=baseURL%>assets/plugins/card-flip/card-flip.js"></script>
	<script src="<%=baseURL%>assets/js/viewport-support.report.js"></script>

	<script type="text/javascript">

			var swiper = new Swiper('.swiper-container', {
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev',
		        slidesPerView: 1,
		        paginationClickable: true,
		        spaceBetween: 30,
		        loop: true
		    });
			
			$(document).ready(function() {
				
				$('.panel-collapse.collapse.in').each( function(index){
					$(this).removeClass('in');
				});
				
			});


			$("#window-back").click(function() {
				window.history.back();
			});
			
			$('.toggle-visibility').click(function() {
				if ($('.mydropdown').css('display') == 'none') {
					$('.mydropdown').fadeIn().delay(2000).fadeOut();
				} else {
					$('.mydropdown').css('display', 'none');
				}
			});

	</script>


</body>

</html>
