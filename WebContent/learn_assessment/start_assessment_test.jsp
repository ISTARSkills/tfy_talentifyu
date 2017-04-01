<%@page import="com.istarindia.complexobject.XMLOption"%>
<%@page import="com.istarindia.complexobject.XMLQuestion"%>
<%@page import="com.istarindia.complexobject.XMLAssessment"%>
<%@page import="org.ocpsoft.prettytime.*"%>
<%@page import="java.util.*"%>
<%@page import="in.talentifyU.utils.services.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="com.istarindia.android.*"%>
<%@page import="com.app.utils.AppUtils"%>
<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";
	IstarUser user = (IstarUser)request.getSession().getAttribute("user");
	HashMap<String, String> reqMAP = (new AppUtils()).getReqMap(request);
	String course_id = reqMAP.get("course_id");
	String slide_id = reqMAP.get("slide_id");
	String module_id = reqMAP.get("module_id");
	String cmsession_id = reqMAP.get("cm_session_id");
	String next_session_id = reqMAP.get("next_session_id");
	String previous_session_id = reqMAP.get("previous_session_id");
	String previous_lesson_id = reqMAP.get("previous_lesson_id");
	String source = reqMAP.get("source");
	XMLAssessment single_assessment = AssessmentRegistry.getInstance().getAssessment();
	int assessmentId = single_assessment.getAssessmentId();
	long start_time = Long.parseLong(request.getParameter("start_time"));
	int duration = Integer.parseInt(request.getParameter("duration"));
	int total_que = AssessmentRegistry.getInstance().getAssessment().getNumber_of_questions();
	Calendar c = Calendar.getInstance();
	long sec = c.getTimeInMillis();
	System.out.println("start time"+start_time);
	System.out.println("sec time"+sec);
	int remaining_min = (int) (((duration * 60000) - (sec - start_time)) / 60000);
	System.out.println("remaining_min time"+remaining_min);
	int remaining_sec = (int) ((((duration * 60000) - (sec - start_time)) % 60000) / 1000);
	System.out.println("remaining_sec time"+remaining_sec);
	%>
	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<meta name="description" content="Materialize is a Material Design Admin Template,It's modern, responsive and based on Material Design by Google. ">
<meta name="keywords" content="materialize, admin template, dashboard template, flat admin template, responsive admin template,">
<title>TALENTIFY</title>

<!-- Favicons-->
<link rel="icon" href="images/favicon/favicon-32x32.png" sizes="32x32">
<!-- Favicons-->
<link rel="apple-touch-icon-precomposed" href="images/favicon/apple-touch-icon-152x152.png">
<!-- For iPhone -->
<meta name="msapplication-TileColor" content="#00bcd4">
<meta name="msapplication-TileImage" content="images/favicon/mstile-144x144.png">
<!-- For Windows Phone -->
<!-- CORE CSS-->
<link href="<%=baseURL%>assets/css/style.min.css?<%=UUID.randomUUID() %>" type="text/css" rel="stylesheet" media="screen,projection">
<link href="<%=baseURL%>assets/css/materialize.min.css?<%=UUID.randomUUID() %>" type="text/css" rel="stylesheet" media="screen,projection">
<link href="<%=baseURL%>assets/css/animate.css?<%=UUID.randomUUID() %>" type="text/css" rel="stylesheet" media="screen,projection">

<!-- Custome CSS-->

<%
		if( assessmentId > 0) {
			AssessmentDAO assessmentDAO = new AssessmentDAO();
			Assessment assessment = assessmentDAO.findById(assessmentId);
			int lesson_id = assessment.getLesson().getId();
			String lesson_theme = assessment.getLesson().getLesson_theme();

			int themeID = 43;
			themeID = Integer.parseInt(lesson_theme);
			if ((new UiThemeDAO()).findById(themeID) != null) {
	%>

<jsp:include page="/themes/mobile/yellow.jsp">
	<jsp:param name="lesson_theme" value="<%=lesson_theme %>" />
</jsp:include>


<% } } %>

</head>	
	<!-- START MAIN -->
	<div id="main" style="overflow-x: hidden; overflow-y: scroll;">
		<!-- START WRAPPER -->
		<div class="wrapper">
			<!-- START CONTENT -->
			<section id="content">
			<div class="col s12 m8 l9">
				<div class="card  gainsboro assessment-page" >
					<div class="card-content black-text timer-div" >
						<div class="col s4" style="padding-left: 45px;">
							<i class="mdi-image-timer" ></i>
							<p id="minutes" style="display: inline;"><%=remaining_min %></p>
							<p id="minutes" style="display: inline;">:</p>
							<p id="seconds" style="display: inline;"><%=remaining_sec %></p>
							<p id="current_que" style="display: inline; float: right; margin-right: 10px">1/5</p>
						</div>
					</div>
					<form id="save_answer_form" action="<%=baseURL%>save_answer" method="GET" style="text-align: center">
						<%
						ArrayList<XMLQuestion> questions = single_assessment.getQuestions();
							for (int que_id =0 ; que_id<questions.size(); que_id++) {
								XMLQuestion q = questions.get(que_id);
								String questionGroup="question_group_"+q.getQueId();
						%>
						<div class='istar_question' id='<%=q.getQueId()%>' data-optgrp='<%=questionGroup %>'>
							<input type="hidden" name="next_question_id" value="<%=q.getQueId()%>">
							<div class="card-content black-text question-div" >
								<div class="question-text"><%=q.getQuestionText()%>
								</div>
							</div>
							<div class="card-action options-div">
								<table>
									<%
										ArrayList<XMLOption> hmap= single_assessment.getQuestions().get(que_id).getOptions();
										ArrayList<XMLOption> new_list = new ArrayList<>();	
										for(XMLOption op : hmap)
										{
											if(!op.getOptionText().equalsIgnoreCase("") && !op.getOptionText().equalsIgnoreCase(" ") && !op.getOptionText().trim().isEmpty())
											{
												new_list.add(op);
											}
										}
											int count = 1;
											
											
											for (int j=0; j < new_list.size(); j++) {
												if (count == 1) {
													count++;
									%>
									<tr class="option-text" >
										<td>A.</td>
										<td><%=new_list.get(j).getOptionText()%></td>
										<td><input class="with-gap" name="<%=questionGroup %>" type="radio" id="<%=new_list.get(j).getOptionId()%>"><label for="<%=new_list.get(j).getOptionId()%>"></label></td>
									</tr>


									<%
										} else if (count == 2) {
													count++;
									%>
									<tr class="option-text" >
										<td>B.</td>
										<td><%=new_list.get(j).getOptionText()%></td>
										<td><input class="with-gap" name="<%=questionGroup %>" type="radio" id="<%=new_list.get(j).getOptionId()%>"><label for="<%=new_list.get(j).getOptionId()%>"></label></td>
									</tr>

									<%
										} else if (count == 3) {
													count++;
									%>
									<tr class="option-text" >
										<td>C.</td>
										<td><%=new_list.get(j).getOptionText()%></td>
										<td><input class="with-gap" name="<%=questionGroup %>" type="radio" id="<%=new_list.get(j).getOptionId()%>" ><label for="<%=new_list.get(j).getOptionId()%>"></label></td>
									</tr>

									<%
										} else if (count == 4) {
													count++;
									%>
									<tr class="option-text" >
										<td>D.</td>
										<td><%=new_list.get(j).getOptionText()%></td>
										<td><input class="with-gap" name="<%=questionGroup %>" type="radio" id="<%=new_list.get(j).getOptionId()%>" ><label for="<%=new_list.get(j).getOptionId()%>"></label></td>
									</tr>
									<%
										}
												
									%>

									<%
										}
									%>
								</table>
							</div>
						</div>
						
						<%
							}
						%>

						<a href="#" class="c-btn button-shadow waves-effect waves-light  center warning-toast animated fadeInUpBig" ><label>Hurry, less than one minute is remaining!</label></a>
						<a name="action" type="submit" class="c-btn button-shadow waves-effect waves-light  center submitbtn_q" ><label>Submit</label></a>

					</form>
				</div>
			</div>
		</section>
		</div>
	</div>
	
	<script type="text/javascript" src="<%=baseURL%>student/js/inspinia/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="<%=baseURL%>assets/js/materialize.min.js"></script>
	<script type="text/javascript" src="<%=baseURL%>assets/plugins/js-toast/toast.js"></script>
	<script src="<%=baseURL%>assets/js/viewport-support.lesson.js"></script>
	
	<script>
		var qindex=0;	
		var total_que = <%=single_assessment.getNumber_of_questions()%>;
		
		$(document).ready(function() {
			console.log("INIT!");
			setTimeout('Decrement()', 1000);
			
			// Iterate all question and when u reach the last question just submit the form 
			if(qindex == 0 ) {
				$( ".istar_question" ).first().css( "display", "block" );
				 document.getElementById("current_que").innerHTML = (1)+'/'+total_que; 
			}
			
			//submitbtn_q			
			$(".submitbtn_q").click(function(){
				 var currentQuid = 0;
				 var currentQopName = 'NONE';
				 console.log("loop ->"+ $('.istar_question').length);
				 $('.istar_question').each(function (index, value) {
					var d = new Date();
					var n = d.getTime();
					var q_time= <%=sec%>
					n = n -q_time;
					var id = $(this).attr('id');
					$('#'+id).hide();
			    	if(qindex == index) {
			    		currentQuid = id;
			    		currentQopName = $('#'+currentQuid).data('optgrp');
			    		 $.ajax({
			                url : '/submit_assessment_new',
			                data : {
			                    option_id : $('#'+id + ' input[name='+currentQopName+']:checked').attr('id'),
			                    question_id :  id,
			                    student_id : <%=user.getId() %>,
			                    start_time : <%=start_time%>,
			    		 		question_start_time: n,
			    		 		quest_index : qindex
			                },
			            }); 
			    	}
			    	
			    	if(qindex + 1 == index) {
			    		$('#'+id).show();
					    $('#'+id).addClass('animated slideInRight');	// $('#'+id).addClass('animated fadeInUp');
						document.getElementById("current_que").innerHTML = (index+1)+'/'+total_que; 
			    	}
			    });
				 
				qindex = qindex + 1;
				console.log(qindex)
	    		if(qindex===total_que) {	
	   				window.location.href = '<%=baseURL%>/play_session?cm_session_id=<%=next_session_id%>&user_id=<%=user.getId()%>&source=<%=source%>&previous_lesson_id=<%=previous_lesson_id%>';
	   			}
				return false;
			});
		});
	
		function Decrement() {
			var minutes = document.getElementById("minutes");
			var seconds = document.getElementById("seconds");
			time_msg = document.getElementById("timer_msg");
	
			if (seconds.innerHTML <= 0 && minutes.innerHTML <= 0) {
				//if time runs out: Submit the form
				//document.getElementById("save_answer_form").submit();
				console.log('end of assessment');
				window.location.href = '<%=baseURL%>/play_session?cm_session_id=<%=next_session_id%>&user_id=<%=user.getId()%>&source=<%=source%>&previous_lesson_id=<%=previous_lesson_id%>';
			} else if (seconds.innerHTML > 0 && seconds.innerHTML <= 59 && minutes.innerHTML <= 0) {	
				// if less than one minutes is remaining: Show the warning
				seconds.innerHTML = getseconds(minutes.innerHTML, seconds.innerHTML);
				$(".warning-toast").show();
			} else {
				// default case - Update the remaining time and keep going
				var tempCurrMin = minutes.innerHTML;
				minutes.innerHTML = getminutes(minutes.innerHTML, seconds.innerHTML);
				seconds.innerHTML = getseconds(tempCurrMin, seconds.innerHTML);
			}
			//
			setTimeout('Decrement()', 1000);
		}
	
		function getminutes(minutes, seconds) {
			if (seconds == 0) {
				return (minutes - 1);
			} else {
				return minutes;
			}
		}
	
		function getseconds(minutes, seconds) {
			if (seconds == 0) {
				return 59;
			} else {
				return (seconds - 1);
			}
		}
		
	</script>
