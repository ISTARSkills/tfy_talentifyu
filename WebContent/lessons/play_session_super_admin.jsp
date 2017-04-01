<%@page import="com.sun.xml.internal.ws.api.pipe.NextAction"%>
<%@page import="com.istarindia.apps.dao.*"%><%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.dao.PresentaionDAO"%>
<%@page import="com.istarindia.apps.dao.Presentaion"%>	<%@page import="com.app.utils.AppUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!doctype html>
<% 
String wsProtocol = "ws";
if(request.getRequestURL().toString().startsWith("https")) {
	 wsProtocol = "wss";
} 


%>
<script type="text/javascript">
var wsocket;     
function connect() {      	 
	 var getUrl = window.location;
	  var baseUrl = getUrl.host + "/";	
	  console.log('conecting to server ');
		wsocket = new WebSocket("<%=wsProtocol%>://"+baseUrl+"/ratesrv");  
		<%System.err.println("21");%>
	    wsocket.onmessage = onMessage; 	
  } 
//console.log('socket state--------'+wsocket.readyState);

</script>	
<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

	HashMap<String, String> reqMAP = (new AppUtils()).getReqMap(request);
	PresentaionDAO dao = new PresentaionDAO();
	Presentaion ppt = new Presentaion();
	
	int lessonID = Integer.parseInt(request.getParameter("lesson_id").replaceAll("/", "").replaceAll("!", ""));
	//int user_id = Integer.parseInt(reqMAP.get("user_id"));

	String lesson_theme = "43";
	String nuetral = url.substring(0, url.length() - request.getRequestURI().length()) + "/";
	String style_body = "background-size: cover;";
	
	String course_id = reqMAP.get("course_id");
	String slide_id = reqMAP.get("current_slide_id");
	int actual_last_slide_id = Integer.parseInt(reqMAP.get("last_slide_id"));
	String module_id = reqMAP.get("module_id");
	String cmsession_id = reqMAP.get("cmsession_id");
	String session_list = reqMAP.get("session_list");
	String next_session_id = reqMAP.get("next_session_id");
	String previous_session_id = reqMAP.get("previous_session_id");
	String source = reqMAP.get("source"); 
	int user_id = Integer.parseInt(reqMAP.get("user_id"));
	String event_id =  reqMAP.get("event_id").toString();
	Lesson lesson = new LessonDAO().findById(lessonID);
	if(lessonID > 0) {
		ppt = lesson.getPresentaion();
		lesson_theme = ppt.getLesson().getLesson_theme_desktop();
	} 
	
	int ppt_id = lesson.getPresentaion().getId();
%>


<html lang="en">
	<head> <meta charset="utf-8">
	
	<title>TALENTIFY</title>
	
	<meta name="description" content="A framework for easily creating beautiful presentations using HTML">
	<meta name="author" content="Hakim El Hattab">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=baseURL%>assets/plugins/reveal/css/reveal.css">
	<link rel="stylesheet" href="<%=baseURL%>assets/css/animate.css" />
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.min.css">
	
	<%
		int themeID = 43;
		themeID = Integer.parseInt(lesson_theme);
		if ((new UiThemeDAO()).findById(themeID) != null) { 
	%>
	
		<jsp:include page="/themes/desktop/desktop_yellow.jsp"></jsp:include>
	
	<%
		}
	%>

	<script>
		var link = document.createElement( 'link' );
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.href = window.location.search.match( /print-pdf/gi ) ? '<%=baseURL%>assets/plugins/reveal/css/print/pdf.css' : '<%=baseURL%>assets/plugins/reveal/css/print/paper.css';
		document.getElementsByTagName( 'head' )[0].appendChild( link );
	</script>
</head>

<body style="<%=style_body%>;background-color:  ">
	
	<div class="reveal">
		
	 	<div class="slides"> 
	 	
	 		<%=ppt.getSessionListSlide(user_id, event_id, slide_id, "mobile") %>
	 		<%=ppt.outputSlidesForDesktop()%>
	 	
		</div>
	</div>
	
<!-- 	<button type="button" class="btn btn-primary btn-circle" id="session_list_button" data-slidenumber="0">i</button>
 -->	
	<script type="text/javascript" src="<%=baseURL%>assets/plugins/jquery/jquery.min.js"></script>
	<script src="<%=baseURL%>assets/plugins/reveal/js/reveal.js"></script>

	<script>
	
	function sendIstarMessage(message) {
		try{
	    	wsocket.send(message);
		}
		catch(err)
		{
			 
			/* console.log(err);
			connect();
			setTimeout(function() {
				sendIstarMessage(message);
				}, 2000); */
		}
	    	
	}
	
		Reveal.initialize({
			progress: false,
			center : false,
			controls : false,
		    slideNumber:  'c/t'
		});
		
		var orgBgColor = '#ffffff';	
		
		$("#session_list_button").click(function(){
			if($('.present').attr('id')!=0){
				$("#session_list_button").text('X');
				var slide_number = Reveal.getIndices().h;
				if(slide_number!=0) {
					$(this).data("slidenumber" , slide_number);
				}
	 			Reveal.slide(0);
				updateSlideBgColor();
			} else {
				$(this).text('i');
			    var slide_number = $(this).data("slidenumber");
		 		Reveal.slide(slide_number);
				updateSlideBgColor();
			}
		});
		
		$(document).ready(function(){
			
			orgBgColor = '<%=(new UiThemeDAO()).findById(themeID).getBackgroundColor()%>';
			updateSlideBgColor();	
			connect();
		});
		$("video").on("play", function (e) {
			console.log("video___" + $(".present").attr("id") + "___play");
			sendIstarMessage("video___" + $(".present").attr("id") + "___play");
		});
		
		

		$("video").on("pause", function (e) {
			console.log("video___" + $(".present").attr("id") + "___pause");
			sendIstarMessage("video___" + $(".present").attr("id") + "___pause");
		});
		
		
		function updateSlideBgColor() {
			try {
				if ($('.present').data("bgcolor") == "none") {
					document.body.style.background = orgBgColor;
				} else {
					document.body.style.background = $('.present').data("bgcolor");
					console.log( $('.present').data("bgcolor") + " ----------> " +  $('.present').attr("id"));
				}
			}
			catch(err) {
			    //console.log(err);
			}
		}

		if (($('.present').attr("style")).indexOf("background-color") < 0) {
			document.body.style.background = orgBgColor;
		}

		(document.getElementsByClassName('controls')[0]).style.display = 'none';

		Reveal.addEventListener('slidechanged', function(event) {
			console.log("slide_number : "+ Reveal.getIndices().h);
			console.log("send : "+ (Reveal.getIndices().h-1));
			
			$('.present').find('.show-all').each(function(index, value) {
				$(this).removeClass("show-all");
			});
			
			$('.slide-number-a').text('event.currentSlide.id');
			var currentURL = window.location.href; 
			var res = currentURL.split("#");
			currentURL = res[0] ;
			history.pushState({}, "URL Rewrite Example", currentURL + "#" + event.currentSlide.id);	
			
			var currentURL11 = window.location.href;
			var res11 = currentURL11.split("#");
			var curr_slide = res11[1] ;
			var last_slide= <%=actual_last_slide_id%>;
			
			if(Reveal.getIndices().h!=0){
				 if(Reveal.isLastSlide()) 
				 {
					var url;
					updateSessionLog(curr_slide);
					sendIstarMessage("slide_event___right___"+( Reveal.getIndices().h -1)+"___"+Reveal.getIndices().v+ "___"+ Reveal.getIndices().f );
						url = '<%=baseURL%>play_event_workflow?user_id=<%=request.getParameter("user_id")%>&event_id=<%=request.getParameter("event_id")%>';
						window.location.href = url;
			
				}
				else 
				{
					sendIstarMessage("slide_event___right___"+( Reveal.getIndices().h -1)+"___"+Reveal.getIndices().v+ "___"+ Reveal.getIndices().f );
					updateSessionLog(curr_slide);
				}
			}
			
			$('.present').find('.show-all').each(function(index, value) {
				$(this).removeClass("show-all");
			});
			
			updateSlideBgColor();
			
		});

		Reveal.addEventListener('fragmentshown', function(event) {
			if(Reveal.getIndices().h!=0){
				if ($(event.fragment).attr('id')=="737373") {
					$('.present').find('.fragment').each(function(index, value) {
						$(this).addClass("current-fragment show-all");
					});
					$(event.fragment).removeClass("current-fragment");
				}
				sendIstarMessage("slide_event___fragement_show___"+( Reveal.getIndices().h -1)+"___"+Reveal.getIndices().v+ "___"+ Reveal.getIndices().f );	//Reveal.nextFragment();
			}
		});

		Reveal.addEventListener( 'ready', function( event ) {
			var source ='<%=source%>';
			console.log("soursce -> "+ source);
			if(source!=null && source==='sessionList') {
					Reveal.slide(1);
					var slideIDD = $('.present').attr('id');
					console.log("slide-id ->" + slideIDD);
					updateSessionLog(slideIDD);
					sendIstarMessage("change_session");
			} 
			else  
			{
				    var slide_id = <%=slide_id%>;
					if(slide_id > 0 ) {
					    var slide_number = 0;
						var temp = -1;
						console.log("slide_id -> " + slide_id);

						$( ".slide" ).each(function( index ) {
							if($( this ).attr("id") == slide_id) {
								console.log("ignore " + "temp -> " + temp + " ; this_id -> "+ $( this ).attr("id"));
								console.log("");
								slide_number = temp;
							  } else if(! ($(this).hasClass( "stack_item" ))){
									console.log("ignore " + "temp -> " + temp + " ; sN -> "+ slide_number);
								  temp = temp + 1;
							  }
						});
						
						if(slide_number >= 0) {
							Reveal.slide(slide_number);
							updateSlideBgColor();
						}
					}
					else {
						Reveal.slide(1);
						updateSlideBgColor();
					}
			}
		} );
		
	</script>

	<script >
	
	var course_id = <%=course_id%>;
	var module_id = <%=module_id%>;
	var cmsession_id = <%=cmsession_id%>;
	var lesson_id = <%=lessonID%>;
	var ppt_id = <%=ppt_id%>;
	var userId = <%=user_id%>;
	var eventId = '<%=event_id%>';
	var cu_url = window.location.href;
	
	function updateSessionLog(slideID) {
		var slide_id11 = slideID;
	    $.ajax({
	            url: '/update_session_log',
	            data: {
	                course_id: course_id,
	                module_id: module_id,
	                cmsession_id: cmsession_id,
	                lesson_id: lesson_id,
	                ppt_id: ppt_id,
	                slide_id : slide_id11,
	                event_id : eventId,
	                user_id : userId,
	                curr_url : cu_url
	                
	            },
	            method: 'GET',
	            success: function(data, textStatus) {
	            	///console.log("success");
	            },
	            error: function(ts) {  }
	        });

	    return false;
	    // return true or false, depending on whether you want to allow the `href` property to follow through or not
	}
	
	</script>

</body>
</html>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-84086973-1', 'auto');
  ga('send', 'pageview');

</script>