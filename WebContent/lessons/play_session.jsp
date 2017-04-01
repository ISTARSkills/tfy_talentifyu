<%@page import="com.sun.xml.internal.ws.api.pipe.NextAction"%>
<%@page import="com.istarindia.apps.dao.*"%><%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.dao.PresentaionDAO"%>
<%@page import="com.istarindia.apps.dao.Presentaion"%>	<%@page import="com.app.utils.AppUtils"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!doctype html>

<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

	HashMap<String, String> reqMAP = (new AppUtils()).getReqMap(request);
	PresentaionDAO dao = new PresentaionDAO();
	Presentaion ppt = new Presentaion();
	
	int ppt_id = Integer.parseInt(request.getParameter("ppt_id").replaceAll("/", ""));
	int lesson_id = -1;
	int user_id = Integer.parseInt(reqMAP.get("user_id"));

	String lesson_theme = "43";
	String nuetral = url.substring(0, url.length() - request.getRequestURI().length()) + "/";
	String style_body = "background-size: cover;";
	
	String course_id = reqMAP.get("course_id");
	String slide_id = reqMAP.get("slide_id");
	String module_id = reqMAP.get("module_id");
	String cmsession_id = reqMAP.get("cm_session_id");
	String session_list = reqMAP.get("session_list");
	String next_session_id = reqMAP.get("next_session_id");
	String previous_session_id = reqMAP.get("previous_session_id");
	String source = reqMAP.get("source"); 
	String previous_lesson_id = reqMAP.get("previous_lesson_id");
	if(ppt_id > 0) {
		ppt = dao.findById(ppt_id);
		lesson_id = ppt.getLesson().getId();
		lesson_theme = ppt.getLesson().getLesson_theme();
	} 
	
	IstarUser user = new IstarUser();
	String user_type = "STUDENT" ;
	try {
		user =  (new IstarUserDAO()).findById(user_id);
		user_type = user.getUserType();
	} catch(Exception e) { 	}
%>


<html lang="en">
	<head> <meta charset="utf-8">
	
	<title>TALENTIFY</title>
	
	<meta name="description" content="A framework for easily creating beautiful presentations using HTML">
	<meta name="author" content="Hakim El Hattab">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">
	<link rel="stylesheet" href="<%=baseURL%>assets/plugins/reveal/css/reveal.css">
	<link rel="stylesheet" href="<%=baseURL%>assets/css/animate.css" />
	<link href="<%=baseURL%>assets/css/font-awesome.css" rel="stylesheet">
	<link href="<%=baseURL%>assets/css/font-awesome.min.css" rel="stylesheet">
	
	<%
		int themeID = 43;
		themeID = Integer.parseInt(lesson_theme);
		if ((new UiThemeDAO()).findById(themeID) != null) { 
	%>
	
		<jsp:include page="/themes/mobile/yellow.jsp">
			<jsp:param name="lesson_theme" value="<%=lesson_theme %>" />
		</jsp:include>
	
	<%
		}
	%>

</head>

<body style="<%=style_body%>;background-color:  ">
	
	<div class="reveal">
		
	 	<div class="slides"> 
	 	
			<% if(ppt_id > 0) { %>
			 		<%=ppt.outputSlidesNew(cmsession_id, session_list, source, user_id).replace("fragment","")%>
		 	<% } 
				if(next_session_id.equalsIgnoreCase("-1")) { %>
			 	<section id="99999999" style="display: none;" data-background="none" data-transition="zoom" data-bgcolor="null" data-background-transition="zoom" data-notes="Not Available" class="ONLY_TITLE step slide future" hidden="" aria-hidden="true">
	        		<h2 id="data_slide_title" style="">END OF COURSE</h2>
				</section> 
			 	<section id="99999998" style="display: none;" data-background="none" data-transition="zoom" data-bgcolor="null" data-background-transition="zoom" data-notes="Not Available" class="ONLY_TITLE step slide future" hidden="" aria-hidden="true">
	        		<h2 id="data_slide_title" style="">END OF COURSE</h2>
				</section> 
		 	<% } %>
	 	
		</div>
	</div>
	
	<div id="left-nav-div"   onClick="Reveal.left()"> <span style="padding-left: 18%; position: absolute; zoom: 50%;"><img id="left-nav-img" alt="" src="/images/left_arrow.png" style="visibility"></span></div>
	<div id="right-nav-div"  onClick="Reveal.right()"> <span style="padding-left: 54%; position: absolute; zoom: 50%;"> <img id="right-nav-img" alt="" src="/images/right_arrow.png" style="visibility"> </span></div>
	<div id="cover-div" ></div>
	<button type="button" class="btn btn-primary btn-circle" id="session_list_button" data-slidenumber="0">i</button>
	
	<script type="text/javascript"   src="<%=baseURL%>assets/plugins/jquery/jquery.min.js?a=1"></script>
	<script type="text/javascript"   src="<%=baseURL%>assets/plugins/reveal/js/reveal.js?a=1"></script>
	<script type="text/javascript"   src="<%=baseURL%>assets/plugins/reveal/js/extra-wheel.js?a=1"></script>

	<script>
		var link = document.createElement( 'link' );
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.href = window.location.search.match( /print-pdf/gi ) ? '<%=baseURL%>assets/plugins/reveal/css/print/pdf.css' : '<%=baseURL%>assets/plugins/reveal/css/print/paper.css';
		document.getElementsByTagName( 'head' )[0].appendChild( link );
	</script>
	
<%
		int auto_slide_duration = 5000;

		try {
			Properties properties = new Properties();
			String propertyFileName = "app.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName);
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propertyFileName + "' not found in the classpath");
			}
			
			auto_slide_duration = Integer.parseInt(properties.getProperty("auto_slide_duration"));
		} catch (Exception e ) {
			e.printStackTrace();
		}
		 
%>
	
	<script>
	
		<% if (user_type.equalsIgnoreCase("TRAINER")) { %>
	
			Reveal.initialize({
				progress: false,
				center : false,
				controls : false,
			    slideNumber:  'c/t', 
			    showNotes: true,
			    autoSlide: <%=auto_slide_duration%>
			});
 	
 		<% } else { %>
 	
			Reveal.initialize({
				progress: false,
				center : false,
				controls : false,
			    slideNumber:  'c/t',
			    autoSlide: <%=auto_slide_duration%>
			});
		
	 	<% } %>
		
	 	var orgBgColor = '#ffffff';			
		
		$(document).ready(function(){
			//console.log("event --> document.ready");
			orgBgColor = '<%=(new UiThemeDAO()).findById(themeID).getBackgroundColor()%>';
			updateSlideBgColor();
			//console.log("-----------------------------------------------");
		});
		
		$("#session_list_button").click(function(){
			//console.log("event --> session_list_button.click");
			handleAutoSlideShow('session_list_button');
			handleTouch('session_list_button');
			handleToggleSessionListJump(Reveal.getIndices().h);
			handlePlaybackButton('session_list_button');
			//console.log("-----------------------------------------------");
		});

		if (($('.present').attr("style")).indexOf("background-color") < 0) {
			document.body.style.background = orgBgColor;
		}

		Reveal.addEventListener('autoslidepaused', function(event) {
			//console.log("event --> Reveal.autoslidepaused");
			handleMedia("autoslidepaused");
			handleTouch('autoslidepaused');
			handleButtonVisibility('autoslidepaused');
			//console.log("-----------------------------------------------");
		});
		
		Reveal.addEventListener('autoslideresumed', function(event) {
			//console.log("event --> Reveal.autoslideresumed");
			handleMedia("autoslideresumed");
			handleTouch('autoslideresumed');
			handleButtonVisibility('autoslideresumed');
			resetCurrentSlideFragments('autoslidepaused');
			//console.log("-----------------------------------------------");
		});
		
		Reveal.addEventListener('slidechanged', function(event) {
			//console.log("event --> Reveal.slidechanged");
			handleTouch('slidechanged');
			handleNavigationButtons("slidechanged");
			handleLastSlide();
			handleFragmentVisibility('slidechanged');
			updateSlideBgColor();
			handleMedia('slidechanged');
			updateURL(event);
			//console.log("-----------------------------------------------");
		});

		Reveal.addEventListener('fragmentshown', function(event) {
			//console.log("event --> Reveal.fragmentshown");
			handleFragmentVisibility('fragmentshown');
			//console.log("-----------------------------------------------");
		});

		Reveal.addEventListener( 'ready', function( event ) {
			//console.log("event --> Reveal.ready");
			handleNavigationButtons("ready");
			handleButtonVisibility('ready');
			handleTouch('ready');
			restoreHistory(<%=slide_id%>);
			handleMedia("ready");
			handleVideoFrameSize("ready");
			//console.log("-----------------------------------------------");
		} );
		
	</script>

	<script type="text/javascript"   src="<%=baseURL%>assets/js/viewport-support.lesson.js?a=1"></script>
	
	<script >
	
	var course_id = <%=course_id%>;
	var module_id = <%=module_id%>;
	var cmsession_id = <%=cmsession_id%>;
	var lesson_id = <%=lesson_id%>;
	var ppt_id = <%=ppt_id%>;
	var user_id = <%=user_id%>;
	var cu_url = window.location.href;
	
	function updateSessionLog(slideID) {
		var slide_id = slideID;
	    $.ajax({
	            url: '/update_user_session_log',
	            data: {
	                course_id: course_id,
	                module_id: module_id,
	                cmsession_id: cmsession_id,
	                lesson_id: lesson_id,
	                ppt_id: ppt_id,
	                slide_id : slide_id,
	                user_id : user_id,
	                curr_url : cu_url
	                
	            },
	            method: 'GET',
	            success: function(data, textStatus) {
	            },
	            error: function(ts) {  }
	        });

	    return false;
	}

	function handleLastSlide() {
		if(Reveal.isLastSlide()) {
			var url;
			if(<%=next_session_id%> > 0) {
				url = '<%=baseURL%>/play_session?cm_session_id=<%=next_session_id%>&user_id=<%=user_id%>&source=<%=source%>&previous_lesson_id=<%=previous_lesson_id%>';
				window.location.href = url;
			} else {
				//Reveal.slide(0);
				//updateSlideBgColor();
			}
		} else if(<%=source.equalsIgnoreCase("course_play")%> && Reveal.getIndices().h!=0 && $('.present').attr('id')<99999998) {
			updateSessionLog($('.present').attr('id'));
		}
	}
	</script>

</body>
</html>
<!-- <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-84086973-1', 'auto');
  ga('send', 'pageview');

</script> -->