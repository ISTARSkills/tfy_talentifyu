<%@page import="com.istarindia.apps.dao.*"%><%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.dao.PresentaionDAO"%>
<%@page import="com.istarindia.apps.dao.Presentaion"%>
<%@page import="com.app.utils.AppUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!doctype html>

<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
%>


<html lang="en">
<head>
<meta charset="utf-8">

<title>TALENTIFY</title>

<meta name="description"
	content="A framework for easily creating beautiful presentations using HTML">
<meta name="author" content="Hakim El Hattab">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">

<link rel="stylesheet"
	href="<%=baseURL%>assets/plugins/reveal/css/reveal.css">
<link rel="stylesheet" href="<%=baseURL%>assets/css/animate.css" />

</head>

<body style="background-color: #25282f;">

	<div class="reveal">
		<div class="slides">
			<section id="content">
				<div id='no-report-img-div' style=" background-repeat: no-repeat; background-image: url('/images/no_data_found.png'); background-size: contain; height: 200vh; zoom: 165%; margin-left: -6%; margin-top: -23%; margin-right: -6%;"> </div>
			</section>
		</div>
	</div>

	<script type="text/javascript" src="<%=baseURL%>assets/plugins/jquery/jquery.min.js"></script>
	<script src="<%=baseURL%>assets/plugins/reveal/js/reveal.js"></script>
	<script src="<%=baseURL%>assets/js/viewport-support.report.js"></script>

	<script>
		Reveal.initialize({
			center : true,
			controls : false,
			minscale:1,
			maxscale:1,
			width:900,
			height:1600,
		});
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