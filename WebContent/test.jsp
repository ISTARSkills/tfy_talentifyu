<%@page import="com.istarindia.apps.dao.IstarUserDAO"%>
<%@page import="com.istarindia.apps.dao.IstarUser"%>
<%@page import="websocket.server.CustomEndPoint"%>
<%@page import="java.util.*"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<body  >
<table  >
<tr ><td style="width:100px">User Id</td><td style="width:300px">EventQueue ID</td><td style="width:200px">Email</td><td style="width:180px">Socket Session</td></tr>
<%
CustomEndPoint c = new CustomEndPoint();
ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, javax.websocket.Session>> eq = CustomEndPoint.event_user_queue;

for(UUID event_queue : eq.keySet())
{
	ConcurrentHashMap<Integer, javax.websocket.Session> rows = eq.get(event_queue);
	for(int id : rows.keySet())
	{
		IstarUser u = new IstarUserDAO().findById(id);
		%>
		<tr><td><%=id %></td><td><%=event_queue %></td><td><%=u.getEmail() %></td><td><%=rows.get(id).getId() %></td></tr>
		<% 
	}
}
%>

</table>
</body>
</html><script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-84086973-1', 'auto');
  ga('send', 'pageview');

</script>