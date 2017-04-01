<%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.IstarTaskStages"%>
<%@page import="com.app.utils.AppUtils"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="com.istarindia.android.*"%>
<%@page import="com.istarindia.android.utils.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%><!doctype html>

	
<%
	
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";
	
	IstarUser user = (IstarUser) request.getSession().getAttribute("user");

String style_body = "background-size: cover;";
%><html lang="en">
<head>
<meta charset="utf-8">
<title>TALENTIFY</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" >
<link rel="stylesheet" href="<%=baseURL%>student/css/reveal.css">

	



</head>
<body style="background-size: cover;">
	<div class="container" style="padding: 0px !important; margin-top: 0px; %">
					<div class="col s12 m8 l9">
						<div class="row" style="overflow: hidden;">
					
 		
 		 		<div class="s12 center" style="
    background-size: cover;
    min-height: 100vh;">
    
<h1 style="font-size:4em ; margin :0;font-family: Roboto sans-serif !important;">No Scheduled Events Found</h1>
   
<h2 style="font-size:40px;font-family: Roboto sans-serif !important;">Causes may be following </h2>

<h2 style="color:red; font-size:25px;font-family: Roboto sans-serif !important;">1) Your session time is over.</h2>


<h2 style="color:red; font-size:25px;font-family: Roboto sans-serif !important;">2) Classroom Id mentioned in "Start Class" button is different than that mentioned in compute stick.</h2>
<h2 style="color:green; font-size:25px;font-family: Roboto sans-serif !important;">How to resolve: Click on Edit description button, Enter Classroom ID mentioned in "Start Class" button and click on update.   </h2>

<h2 style="color:red; font-size:25px;font-family: Roboto sans-serif !important;">3) Class was started late than the scheduled time.</h2>
<h2 style="color:green; font-size:25px;font-family: Roboto sans-serif !important;">How to resolve: Login with trainer id on compute stick and take the session.</h2>
  
<h2 style="color:red; font-size:25px;font-family: Roboto sans-serif !important;">4) There are no event scheduled for today. </h2>
<h2 style="color:green; font-size:25px;font-family: Roboto sans-serif !important;">How to resolve: Contact Operation team representative.</h2>     
          					</div>         					
						</div>
					</div>
				</div>
	
<script type="text/javascript" src="<%=baseURL%>js/plugins/jquery-1.11.2.min.js"></script>
<script src="<%=baseURL%>student/lib/js/head.min.js"></script>

	

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