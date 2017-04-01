<%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.IstarTaskStages"%>
	<%@page import="com.app.utils.AppUtils"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="com.istarindia.android.*"%>
<%@page import="com.istarindia.android.utils.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%><!doctype html>
<% 
String wsProtocol = "ws";
if(request.getRequestURL().toString().startsWith("https")) {
	 wsProtocol = "wss";
} 
%>	
	
<%

	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
	String lessonID = request.getParameter("lesson_id").split("!")[0];	
	Lesson ppt = (new LessonDAO()).findById(Integer.parseInt(lessonID));
	String event_id  = request.getParameter("event_id");

	String lesson_theme = ppt.getLesson_theme_desktop();
	IstarUser user = (IstarUser) request.getSession().getAttribute("user");

	BatchScheduleEventDAO dao = new BatchScheduleEventDAO();
	BatchScheduleEvent b = dao.findById(UUID.fromString(event_id));

String style_body = "background-size: cover;";
%><html lang="en">
<head>
<meta charset="utf-8">
<title>TALENTIFY</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="<%=baseURL%>student/css/bootstrap.min.css" >
<link rel="stylesheet" href="<%=baseURL%>student/css/reveal.css">
<%
int themeID = 100;

try {
themeID = Integer.parseInt(lesson_theme);

if ((new UiThemeDAO()).findById(themeID) != null) {
	

%>
	<jsp:include page="/themes/desktop/desktop_yellow.jsp"></jsp:include>
<% 
	
	} 
} catch (Exception e){
e.printStackTrace();
}
%>
	
<script>
var wsocket;     
function connect() {      	 
	 var getUrl = window.location;
	  var baseUrl = getUrl.host ;
	  console.log('conecting to server ');
		wsocket = new WebSocket("<%=wsProtocol%>://"+baseUrl+"/ratesrv");       
	    wsocket.onmessage = onMessage; 	
  }
  

</script>


</head>
<body style="background-size: cover;">
	<div class="reveal" id ="reveal_div">
		<div class="slides">
			<%=ppt.getPresentaion().outputSlidesForDesktop(themeID)%>
		</div>
		</div>
		  <!-- Modal -->
  
	
	
<script type="text/javascript" src="<%=baseURL%>assets/plugins/jquery/jquery.min.js"></script>
<%--  <script type="text/javascript" src="<%=baseURL%>js/bootstrap.min.js"></script>
 --%>
<script src="<%=baseURL%>student/lib/js/head.min.js"></script>
<script src="<%=baseURL%>student/js/reveal.js"></script>


<script>

	var orginalsize = <%=(new UiThemeDAO()).findById(themeID).getListitemFontSize()%> ;
	

	var window_width = $(window).width() ;
	var window_height = $(window).height() ;
	var body_width = $('body').width() ;
	var body_height = $('body').height() ;
	
	
	
		Reveal.initialize({
			center : true,
			controls : true, 
			width: window_width,
			minScale: 0.9,
		    maxScale: 0.9

		});
		
	var orgBgColor = '#ffffff';			
	
	$(document).ready(function(){
		orgBgColor = '<%=(new UiThemeDAO()).findById(themeID).getBackgroundColor()%>';
		updateSlideBgColor();
		connect();
	});

	(document.getElementsByClassName('controls')[0]).style.display = 'none';

	function updateSlideBgColor() {
		if ($('.present').data("bgcolor") == "none") {
			document.body.style.background = orgBgColor;
		} else {
			document.body.style.background = $('.present').data("bgcolor");
		}
	}
	
	Reveal.addEventListener( 'ready', function( event ) {
	    var currURL = window.location.href; 
		var res_ = currURL.split("!#");
		console.log("slide ID "+res_[1]);
		var id=0;
		var indexaa=0;
		$( ".slide" ).each(function( index ) {
		 if($( this ).attr("id") === res_[1]) {
		 indexaa = id;
		 }else {
		 id = id + 1;
		 }

		});
		if(indexaa >0)
		{
		Reveal.slide(indexaa-1,0, 0);
		}
		else
		{
		Reveal.slide(indexaa,0, 0);
		} 		
	} );
	Reveal.addEventListener('slidechanged', function(event) {
		$('.present').find('.show-all').each(function(index, value) {
			$(this).removeClass("show-all");
		});

		updateSlideBgColor();
		
		if (window_width > window_height) {
			$('.slides').css('left','50%');
			$('.slides').css('top','45%');
			$('.slides').css('height',window_height+"px");
		} else {
			$('.slides').css('top','40%');
		    $('.reveal .backgrounds .NO_CONTENT ').css('background-size','contain');
		}	
		
		var currentURL = window.location.href; 
		var res = currentURL.split("#");
		currentURL = res[0] ;
		//console.log(currentURL + "#/" + event.currentSlide.id);
		history.pushState({}, "URL Rewrite Example", currentURL + "#" + event.currentSlide.id);
		
		var url = window.location.href ;
		console.log("url"+url);
		var arr1 = url.split("?");
		var arr2= arr1[1].split("&");
		var last_slide_arr = arr2[1].split("=");
		var last_slide = last_slide_arr[1];
		var curr_slide_arr = arr2[3].split("#");
		var curr_slide = curr_slide_arr[1];
		var currr_slide_iddd = $(".present").attr("id");
		 if(currr_slide_iddd == 99999999)
		 {
			 console.log("presentor got last slide0");
		 	 window.location.href= '<%=baseURL%>start_presentor?classroom_id=<%=b.getClassroom().getId()%>';
		 }
		
		
		//console.log(curr_slide);
		//console.log(last_slide);
		
	});

	Reveal.addEventListener('fragmentshown', function(event) {			
		if ($(event.fragment).attr('id')=="737373") {
			$('.present').find('.fragment').each(function(index, value) {
				$(this).addClass("current-fragment show-all");
			});
			$(event.fragment).removeClass("current-fragment");
		}
		
	});
	
	function onMessage(msg) {
		  var msg_from_server = msg.data;
		  console.log("from presentor"+msg_from_server);
		   if(msg_from_server.startsWith('slide_event___right')) 
			{
			 try {				
				 console.log("recieved message from server to move to slide ->"+ msg_from_server.split('___')[2]);
				 Reveal.slide(msg_from_server.split('___')[2],msg_from_server.split('___')[3], msg_from_server.split('___')[4] );
				 updated = true;
			 } catch(err) {
				 console.log(err)
				 
			 }
			}
		  else if (msg_from_server.startsWith('slide_event___fragement_show'))
			  {
			  try {
					  console.log("came in slide_event___fragement_show ");
				Reveal.nextFragment();
					  // Reveal.slide(msg_from_server.split('___')[2],msg_from_server.split('___')[3], msg_from_server.split('___')[4] );
					  
			  }  catch(err) 
			 {    			 
				 console.log(err)
				}
			  }

		  else if (msg_from_server.startsWith("video") && msg_from_server.endsWith("play")) 
			{
				if(msg_from_server.split('___').indexOf($(".present").attr("id")) > -1) {
					$('.present video')[0].play();
				}
			}
			else if (msg_from_server.startsWith("video") && msg_from_server.endsWith("pause")) 
			{
				if(msg_from_server.split('___').indexOf($(".present").attr("id")) > -1) {
					$('.present  video')[0].pause();
				}
			}
			else if (msg_from_server.startsWith("change_session") ) 
			{
				console.log("calling change session");
				window.location.href= '<%=baseURL%>start_presentor?classroom_id=<%=b.getClassroom().getId()%>';
			}
		
		  
		             
	}
	
	
	</script>
	<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-84086973-1', 'auto');
  ga('send', 'pageview');

</script>

</body>
</html>
