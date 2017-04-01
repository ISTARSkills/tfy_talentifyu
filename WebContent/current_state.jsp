<%@page import="java.util.*"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.istarindia.apps.IstarTaskStages"%>
<%@page import="com.app.utils.AppUtils"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="com.istarindia.android.*"%>
<%@page import="com.istarindia.android.utils.*"%>
<%@page import="java.text.*"%>
<%@page import="websocket.server.CustomEndPoint"%>
<%@page import="java.util.*"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%><!doctype html>
<%

	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";
	String sql = "SELECT 	bse.eventdate AS bse_event_date, bse.classroom_id as class_id,	CAST (bse. ID AS VARCHAR(50)) AS bse_event_id, cast(EQE.event_queue_id as varchar(50)) as event_que, 	org. ID AS org_id, 	org. NAME AS org_name, 	s. NAME AS trainer_name, 	s. ID AS trainer_id, 	addr.address_geo_latitude AS geolat, 	addr.address_geo_longitude AS geolong FROM 	student s, 	address addr, 	batch_schedule_event bse, 	batch B, 	batch_group BG, 	college org, event_queue_events EQE WHERE EQE.event_id = bse.id and 	BG. ID = B.batch_group_id AND bse.batch_id = B. ID AND BG.college_id = org. ID AND bse.eventdate >= CURRENT_DATE + INTERVAL '0 hour' AND bse.eventdate <= CURRENT_DATE + INTERVAL '23 hour' AND bse.actor_id = s. ID AND s.user_type = 'TRAINER' AND event_name NOT LIKE '%TEST%' AND org.address_id = addr. ID AND addr.address_geo_latitude IS NOT NULL AND addr.address_geo_longitude IS NOT NULL AND bse. TYPE = 'BATCH_SCHEDULE_EVENT_TRAINER' ORDER BY 	bse.eventdate ";
	com.istarindia.apps.dao.UUIUtils.printlog(sql);
	DBUTILS util = new DBUTILS();
	List<HashMap<String, Object>> data = util.executeQuery(sql);
	CustomEndPoint c = new CustomEndPoint();
	ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, javax.websocket.Session>> eq = CustomEndPoint.event_user_queue;
%><html lang="en">
<head>
<meta charset="utf-8">
<title>TALENTIFY</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="<%=baseURL%>css/bootstrap.min.css">
</head>
<body style="background-size: cover;">

	<div class="container-fluid" style="width: 100%">
		
		<div class="bs-example" data-example-id="simple-table">
			<table class="table">
				<caption>Current Status</caption>
				<thead>
					<tr>
						<th>#</th>
						<th>Event Date</th>
						<th>Org Name(ID)</th>
						<th>ClassID</th>
						<th>Trainer(ID)</th>
						<th>Event Queue</th>
						<th>Members</th>
						
					</tr>
				</thead>
				<tbody>
		<%
			for (HashMap<String, Object> row : data) {
				Timestamp event_date = (Timestamp) row.get("bse_event_date");
				SimpleDateFormat dateFormat_new = new SimpleDateFormat("dd MMM yyyy hh:mm");

				String event_que = (String) row.get("event_que");
				int org_id = (int) row.get("org_id");
				String org_name = (String) row.get("org_name");
				int trainer_id = (int) row.get("trainer_id");
				String trainer = (String) row.get("trainer_name");
				int class_id = (int)row.get("class_id");
		%>
		<%
			String members = "";
				String color = "WHITE";
				if (eq.containsKey(UUID.fromString(event_que))) {
					ConcurrentHashMap<Integer, javax.websocket.Session> rows = eq.get(UUID.fromString(event_que));
					if (rows.size() == 1) {
						color = "RED";
					}
					if (rows.size() >= 2) {
						color = "GREEN";
					}
					for (int id : rows.keySet()) {
						IstarUser u = new IstarUserDAO().findById(id);
						members = members + "," + u.getEmail();
					}
				}
		%>
		<tr style="background: <%=color%>">
						<th scope="row">1</th>
						<td><%=dateFormat_new.format(event_date).toString()%></td>
						<td><%=org_name%>
				(<%=org_id%>)</td>
				<td><%=class_id%>
						<td><%=trainer%>
				(<%=trainer_id%>)</td>
						<td><%=event_que%></td>
						<td><%=members%></td>
					</tr>
		
		<%
			}
		%>
</tbody>
</table>
	</div>

</div>




	<script type="text/javascript"
		src="<%=baseURL%>js/plugins/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="<%=baseURL%>js/bootstrap.min.js"></script>





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
