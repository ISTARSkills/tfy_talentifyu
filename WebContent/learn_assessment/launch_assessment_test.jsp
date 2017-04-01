<%@page import="org.ocpsoft.prettytime.*"%>
<%@page import="java.util.Random"%>
<%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="com.istarindia.android.*"%>
<%@page import="com.app.utils.AppUtils"%>
<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";
	HashMap<String, String> reqMAP = (new AppUtils()).getReqMap(request);

	IstarUser user = (IstarUser)request.getSession().getAttribute("user");
	StringBuffer launch_test = (StringBuffer)request.getSession().getAttribute("launch_test_learn");
	int assessmentId = Integer.parseInt(request.getSession().getAttribute("assessmentId").toString());
	/* String course_id = reqMAP.get("course_id");
	//System.out.println()
	String slide_id = reqMAP.get("slide_id");
	String module_id = reqMAP.get("module_id");
	String cmsession_id = reqMAP.get("cm_session_id");
	//String session_list = reqMAP.get("session_list");
	String next_session_id = reqMAP.get("next_session_id");
	String previous_session_id = reqMAP.get("previous_session_id");
	String source = reqMAP.get("source");  */
	
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<meta name="description" content="Materialize is a Material Design Admin Template,It's modern, responsive and based on Material Design by Google. ">
<meta name="keywords" content="materialize, admin template, dashboard template, flat admin template, responsive admin template,">
<title>TALENTIFY</title>
<link href='https://fonts.googleapis.com/css?family=Righteous' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Roboto:400,400italic,100,100italic,300,300italic,500,500italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>

<!-- Favicons-->
<link rel="icon" href="images/favicon/favicon-32x32.png" sizes="32x32">
<!-- Favicons-->
<link rel="apple-touch-icon-precomposed" href="images/favicon/apple-touch-icon-152x152.png">
<!-- For iPhone -->
<meta name="msapplication-TileColor" content="#00bcd4">
<meta name="msapplication-TileImage" content="images/favicon/mstile-144x144.png">
<!-- For Windows Phone -->
<!-- CORE CSS-->
<link href="<%=baseURL%>assets/css/materialize.min.css?<%=UUID.randomUUID() %>" type="text/css" rel="stylesheet" media="screen,projection">
<link href="<%=baseURL%>assets/css/style.min.css?<%=UUID.randomUUID() %>" type="text/css" rel="stylesheet" media="screen,projection">
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
		<div id="bokehs" ></div>
	
		<!-- START WRAPPER -->
		<div class="wrapper">
			<!-- START CONTENT -->
			<section id="content">
				<%-- <input type="hidden" id="course_id" name="course_id" value="<%=course_id%>">
				<input type="hidden" id="color" name="color" value="<%=color%>"> --%>
				<%=launch_test %>
			</section>
		</div>
	</div>

	<script type="text/javascript" src="<%=baseURL%>assets/plugins/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="<%=baseURL%>assets/js/materialize.min.js?<%=UUID.randomUUID() %>"></script>
	<script type="text/javascript" src="<%=baseURL%>assets/js/bokeh.js"></script>
	<script type="text/javascript" src="<%=baseURL%>assets/js/viewport-support.lesson.js"></script>
	
	<script>
	
		$( document ).ready(function() {
			createBokeh(10);
		});
		
		//submitbtn_test
		$('#submitbtn_test').click(function() {
			console.log(document.getElementById("next_session_id").value);
			updateStartTest();
		});
		
		function updateStartTest() {
		    $.ajax({
	            url: '/start_learn_assessment',
	            data: {
	            	assessment_id: $("#assessment_id").val(),
                    duration: $("duration").val(),
                    course_id: $("#course_id").val(),
                    slide_id: $("#slide_id").val(),
                    module_id: $("#module_id").val(),
                    cmsession_id: $("#cmsession_id").val(),
                    next_session_id: $("#next_session_id").val(),
                    previous_session_id: $("#previous_session_id").val(),
                    source: $("#source").val()
	            },
	            success: function(data, textStatus) {
	               	console.log(data);
	            }
		    });
		
		    return true;
		    // return true or false, depending on whether you want to allow the `href` property to follow through or not
		}
		
	</script>
