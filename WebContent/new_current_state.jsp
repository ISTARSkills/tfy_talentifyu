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
<!DOCTYPE html>
<html>
<%

	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";
	String sql = "SELECT 	bse.eventdate AS bse_event_date, 	bse.classroom_id AS class_id, bse.event_name AS event_name, 	CAST (bse. ID AS VARCHAR(50)) AS bse_event_id, 	CAST ( 		EQE.event_queue_id AS VARCHAR (50) 	) AS event_que, 	org. ID AS org_id, 	org. NAME AS org_name, 	s. NAME AS trainer_name, 	s. ID AS trainer_id, 	addr.address_geo_latitude AS geolat, 	addr.address_geo_longitude AS geolong FROM 	student s, 	address addr, 	batch_schedule_event bse, 	batch B, 	batch_group BG, 	college org, 	event_queue_events EQE WHERE 	EQE.event_id = bse. ID AND BG. ID = B.batch_group_id AND bse.batch_id = B. ID AND BG.college_id = org. ID AND bse.eventdate >= CURRENT_DATE + INTERVAL '0 hour' AND bse.eventdate <= CURRENT_DATE + INTERVAL '23 hour' AND bse.actor_id = s. ID AND s.user_type = 'TRAINER' AND event_name NOT LIKE '%TEST%' AND org.address_id = addr. ID AND addr.address_geo_latitude IS NOT NULL AND addr.address_geo_longitude IS NOT NULL AND bse. TYPE = 'BATCH_SCHEDULE_EVENT_TRAINER' ORDER BY 	bse.eventdate 	";
	com.istarindia.apps.dao.UUIUtils.printlog(sql);
	DBUTILS util = new DBUTILS();
	List<HashMap<String, Object>> data = util.executeQuery(sql);
	System.out.println("--------------------"+data.size());
	CustomEndPoint c = new CustomEndPoint();
	ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, javax.websocket.Session>> eq = CustomEndPoint.event_user_queue;
%>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>INSPINIA | Current Programs</title>

<link href="<%=baseURL%>org_admin_assets/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=baseURL%>org_admin_assets/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="<%=baseURL%>org_admin_assets/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="<%=baseURL%>org_admin_assets/css/animate.css" rel="stylesheet">
<link href="<%=baseURL%>org_admin_assets/css/style.css" rel="stylesheet">

</head>

<body>
	
	<div id="wrapper">
		<div id="page-wrapper" class="gray-bg" style="margin-left: auto!important;">
			<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-3">
						<div class="widget style1">
							<div class="row">
							 <div class="col-xs-4 text-center">
									<i class="fa fa-trophy fa-5x"></i>
								</div>
								<div class="col-xs-8 text-center">									
									<%if(data.size() > 0) {%>
									<span> Today Events</span>
									<h2 class="font-bold"><%=data.size() %></h2>
									
									<%}else{%>										
										<h2 class="font-bold">No Events</h2>
										
									<% } %>
								</div> 
							</div>
						</div>
					</div>
			</div>
				<div class="row">
					<div class="col-sm-2 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">Time</h3>
							</div>
						</div>
					<div class="col-sm-2 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">Name</h3>
						</div>
					</div>
					<div class="col-sm-2 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">College</h3>
						</div>
					</div>
					<div class="col-sm-1 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">Class Room</h3>
						</div>
					</div>
					<div class="col-sm-1 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">Trainer(ID)</h3>
						</div>
					</div>
					<div class="col-sm-2 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">Event Queue</h3>
						</div>
					</div>
					<div class="col-sm-2 event_details_row">
						<div class="widget style1 event_details_element">
									<h3 class="font-bold">Online</h3>
						</div>
					</div>
				</div>	
 					<%
					String event_name="";
					String bse_event_id = "";
					String event_que = "";
					int org_id = 0;
					String org_name="";
					int trainer_id=0;
					String trainer="";
					int class_id = 0;
					
					Timestamp event_date = new Timestamp(0);
					SimpleDateFormat dateFormat_new = new SimpleDateFormat();
					
			for (HashMap<String, Object> row : data) {
				event_date = (Timestamp) row.get("bse_event_date");
				dateFormat_new = new SimpleDateFormat("dd MMM yyyy hh:mm");

				event_name = (String) row.get("event_name");
				bse_event_id = (String) row.get("bse_event_id");
				
				event_que = (String) row.get("event_que");
				org_id = (int) row.get("org_id");
				org_name = (String) row.get("org_name");
				trainer_id = (int) row.get("trainer_id");
				trainer = (String) row.get("trainer_name");
				class_id = (int)row.get("class_id");
				
	

				
				if (eq.containsKey(UUID.fromString(event_que))) {
					
					ConcurrentHashMap<Integer, javax.websocket.Session> rows = eq.get(UUID.fromString(event_que));
					
					String members = "";
					String color = "WHITE";
					for (int id : rows.keySet()) {
						IstarUser u = new IstarUserDAO().findById(id);
						members = members + "," + u.getEmail();
					} 
					
					if (rows.size() == 1) {  %>
					
					<div class="row" id="red">
						<div class="widget style1 red-bg" >
							<div class="row show-grid">
                                 <div class="col-sm-2" style="background-color:#ed5565!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h2><%=dateFormat_new.format(event_date).toString()%></h2></div>
                                 <div class="col-sm-2" style="background-color:#ed5565!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=event_name%>(<%=bse_event_id %>)</h3></div>
                                <div class="col-sm-2" style="background-color:#ed5565!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=org_name%>(<%=org_id%>)</h3></div>
                                <div class="col-sm-1 text-center" style="background-color:#ed5565!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=class_id%></h3></div>
                                <div class="col-sm-1" style="background-color:#ed5565!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=trainer%>(<%=trainer_id%>)</h3></div>
                                <div class="col-sm-2" style="background-color:#ed5565!important;border: none!important;word-wrap:break-word;padding-right:0px;"><h3><%=event_que%></h3></div>
                                <div class="col-sm-2" style="background-color:#ed5565!important;border: none!important;word-wrap:break-word;padding-right:0px;"><h3><%=members%></h3></div>
								<a class="btn btn-info btn-rounded pull-right" id="detail" onclick="window.open('http://admin.talentify.in:8080/orgadmin/logs/trainer_logs.jsp?event_id=<%=bse_event_id %>')">Info</a>                      
                            </div>
						</div>
					</div>
					<%
						
					}
					else if (rows.size() >= 2) { %>
					
				<div class="row" id="green">
					<div class="widget style1 navy-bg" >
							<div class="row show-grid">
                                 <div class="col-sm-2" style="background-color:#1ab394!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h2><%=dateFormat_new.format(event_date).toString()%></h2></div>
                                 <div class="col-sm-2" style="background-color:#1ab394!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=event_name%>(<%=bse_event_id %>)</h3></div>
                                <div class="col-sm-2" style="background-color:#1ab394!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=org_name%>(<%=org_id%>)</h3></div>
                                <div class="col-sm-1 text-center" style="background-color:#1ab394!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=class_id%></h3></div>
                                <div class="col-sm-1" style="background-color:#1ab394!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3><%=trainer%>(<%=trainer_id%>)</h3></div>
                                <div class="col-sm-2" style="background-color:#1ab394!important;border: none!important;word-wrap:break-word;padding-right:0px;"><h3><%=event_que%></h3></div>
                                <div class="col-sm-2" style="background-color:#1ab394!important;border: none!important;word-wrap:break-word;padding-right:0px;"><h3><%=members%></h3></div>
								<a class="btn btn-info btn-rounded pull-right" id="detail" onclick="window.open('http://admin.talentify.in:8080/orgadmin/logs/trainer_logs.jsp?event_id=<%=bse_event_id %>')">Info</a>                      
                            </div>
						</div>
					</div>
					<%}
			}else{
				%>
				<div class="row" id="white ">
					<div class="widget style1" style="background-color:white;">
							<div class="row show-grid">
                                 <div class="col-sm-2" style="background-color:#ffffff!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h2 style="color: black!important;"><%=dateFormat_new.format(event_date).toString()%></h2></div>
                                 <div class="col-sm-2" style="background-color:#ffffff!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3 style="color: black!important;"><%=event_name%>(<%=bse_event_id %>)</h3></div>
                                <div class="col-sm-2" style="background-color:#ffffff!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3 style="color: black!important;"><%=org_name%>(<%=org_id%>)</h3></div>
                                <div class="col-sm-1 text-center" style="background-color:#ffffff!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3 style="color:black!important;"><%=class_id%></h3></div>
                                <div class="col-sm-1" style="background-color:#ffffff!important;border: none!important;padding-right:0px;word-wrap:break-word;"><h3 style="color: black!important;"><%=trainer%>(<%=trainer_id%>)</h3></div>
                                <div class="col-sm-2" style="background-color:#ffffff!important;border: none!important;word-wrap:break-word;padding-right:0px;"><h3 style="color: black!important;"><%=event_que%></h3></div>
                                <div class="col-sm-2" style="background-color:#ffffff!important;border: none!important;word-wrap:break-word;padding-right:0px;"><h3 style="color: black!important;"> Offline</h3></div>
								<a class="btn btn-info btn-rounded pull-right" id="detail" onclick="window.open('http://admin.talentify.in:8080/orgadmin/logs/trainer_logs.jsp?event_id=<%=bse_event_id %>')">Info</a>                      
                            </div>
						</div>
					</div>
				<% 
			}}
		%> 

			
			
</div>
		</div>
	</div>

	<!-- Mainly scripts -->  
	<script src="<%=baseURL%>org_admin_assets/js/jquery-2.1.1.js"></script>
	<script src="<%=baseURL%>org_admin_assets/js/jquery-ui-1.10.4.min.js"></script>
	<script src="<%=baseURL%>org_admin_assets/js/bootstrap.min.js"></script>
	<script src="<%=baseURL%>org_admin_assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script
		src="<%=baseURL%>org_admin_assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="<%=baseURL%>org_admin_assets/js/inspinia.js"></script>
	<script src="<%=baseURL%>org_admin_assets/js/plugins/pace/pace.min.js"></script>

	<!-- iCheck -->
	<script src="<%=baseURL%>org_admin_assets/js/plugins/iCheck/icheck.min.js"></script>

	<!-- Jvectormap -->
	<script
		src="<%=baseURL%>org_admin_assets/js/plugins/jvectormap/jquery-jvectormap-2.0.2.min.js"></script>
	<script
		src="<%=baseURL%>org_admin_assets/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>

	<!-- Flot -->
	<script src="<%=baseURL%>org_admin_assets/js/plugins/flot/jquery.flot.js"></script>
	<script src="<%=baseURL%>org_admin_assets/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
	<script src="<%=baseURL%>org_admin_assets/js/plugins/flot/jquery.flot.resize.js"></script>
	    <script src="<%=baseURL%>js/highcharts-custom.js"></script>
	<script>
        $(document).ready(function(){
        	
        //	var url1 = parent.document.getElementById("detail").href="http://localhost:8080/orgadmin/logs/trainer_logs.jsp?event_id=";
			
            
        });
    </script>


</body>

</html>
