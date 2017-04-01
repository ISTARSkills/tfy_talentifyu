<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!doctype html>

<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
%>

<html lang="en">
<head>
<meta charset="utf-8">

<title>TALENTIFY</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">
<link rel="stylesheet" href="<%=baseURL%>assets/css/animate.css" />

<style type="text/css">

.end-of-course {
    background-image: url(/images/endofsession.png);
    background-size: cover;
    height: 100vh;
    width: 100vw;
    margin: -8px;
}

/* style="background-image: url('/images/endofsession.png'); background-size: contain; height: 200vh; zoom: 165%; margin-left: -6%; margin-top: -23%; margin-right: -6%;" */

</style>

</head>
<body style="background-color: #e5e5e56b">

	<div class="end-of-course animated bounceInDown"> </div>
		
	<script src="<%=baseURL%>new/js/jquery-2.1.1.js"></script>
	<script src="<%=baseURL%>assets/js/viewport-support.lesson.js"></script>

</body>
</html>