<%@page import="org.apache.commons.lang.exception.ExceptionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<% 




try {

			Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
			Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
			String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");

			
			System.err.println("JSP Exception Page "+(new ExceptionUtils()).getFullStackTrace(throwable));
} catch(Exception ee) {
	System.err.println("JSP Exception Page ");

}
			
			%>

</body>
</html><script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-84086973-1', 'auto');
  ga('send', 'pageview');

</script>