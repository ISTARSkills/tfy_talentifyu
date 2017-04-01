<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!doctype html>
<%@page import="java.util.*"%>
<%@page import="in.talentifyU.auth.controllers.SkillHolder"%>

<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
	
	try {
		SkillHolder.createTree();
	} catch(Exception e) {
		System.err.println("Error occured while recreating skill tree after assessment!");
	}
%>


<html lang="en">
<head>
<meta charset="utf-8">

<title>TALENTIFY</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">
<link href="<%=baseURL%>assets/css/animate.css?<%=UUID.randomUUID() %>" type="text/css" rel="stylesheet" media="screen,projection">

<style type="text/css">

.already-attempted {
    background-image: url(/images/already_attempted_test.png);
    background-size: cover;
    height: 100vh;
    width: 100vw;
    margin: -8px;
}
/* style="background-repeat: no-repeat; background-image: url('/images/already_attempted_test.png'); background-size: contain; height: 200vh; zoom: 165%; margin-left: -6%; margin-top: -23%; margin-right: -6%;"  */
</style>
</head>

<body style="background-color: #fbc02d;">
	<div class="already-attempted animated bounceInDown"> </div>
	
	<script src="<%=baseURL%>new/js/jquery-2.1.1.js"></script>
	<script src="<%=baseURL%>assets/js/viewport-support.lesson.js"></script>
</body>
</html>
