<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.istarindia.apps.dao.DBUTILS"%>
<%@page import="com.istarindia.apps.dao.CourseDAO"%>
<%@page import="com.istarindia.apps.dao.Course"%>
<%@page import="in.talentifyU.auth.controllers.SkillHolder"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.istarindia.apps.dao.SkillDAO"%>
<%@page import="com.istarindia.apps.dao.Skill"%>
<%@page import="in.talentifyU.utils.services.NewReportUtils"%>
<html>
<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";
	int user_id = Integer.parseInt(request.getParameter("user_id"));
	int skill_id = 0;
	if (request.getParameterMap().containsKey("skill_id")) {
		skill_id = Integer.parseInt(request.getParameter("skill_id"));
	}
	if (request.getParameterMap().containsKey("course_id")) {
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		System.out.println("course_id" + course_id);
		Course c = new CourseDAO().findById(course_id);
		DBUTILS ut = new DBUTILS();
		String sqllll = "select id from skill where skill_title like '%" + c.getCourseName().trim()
				+ "%' limit 1";
		System.out.println(sqllll);
		List<HashMap<String, Object>> iddd = ut.executeQuery(sqllll);
		if (iddd.size() > 0) {
			skill_id = (int) iddd.get(0).get("id");
		}

	}
	boolean should_go = SkillHolder.hasData(skill_id, user_id);
	if (!should_go) {
		System.out.println("going to no report available");
		request.getRequestDispatcher("no_report_available.jsp").forward(request, response);
		//response.sendRedirect("/no_report_available.jsp?skill_id=" + skill_id + "&user_id=" + user_id);
	}
	else
	{
		
		
	System.out.println("dfsd sfds sfdsd able");
	HashMap<String, String> percentiles = new NewReportUtils().getPercentileForSkill(skill_id, user_id);
	Skill skill = (new SkillDAO()).findById(skill_id);
%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>INSPINIA | Report</title>
<link href="<%=baseURL%>new/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=baseURL%>new/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="<%=baseURL%>student/css/inspinia/slick.css" rel="stylesheet">
<link href="<%=baseURL%>student/css/inspinia/slick-theme.css"
	rel="stylesheet">
<link href="<%=baseURL%>new/css/animate.css" rel="stylesheet">
<link href="<%=baseURL%>new/css/style.css" rel="stylesheet">
<link href="<%=baseURL%>new/css/custom.css" rel="stylesheet">
<link href="<%=baseURL%>new/css/gcard.css" rel="stylesheet">
<link href="<%=baseURL%>assets/plugins/jQueryAddUI/dist/addDropMenu.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="<%=baseURL%>assets/plugins/swiper/dist/css/swiper.min.css">
<link rel="stylesheet"
	href="<%=baseURL%>assets/plugins/card-flip/card-flip.css">

</head>
<body class="">
	<div id="wrapper" class="gray-bg" style="padding: 0%; min-height: 100vh">
		
		<div id='mimic-header-gap' class="mimic-header" style="margin-top: 2vh;">
			
		</div>
		
		<div class="gcard gpanel-header mb0 page-header" >
			
			<img id="window-back" class=" z3 i-arrow left"  src="<%=baseURL%>assets/images/reports/window-back.png?a=1" />
			<span style="font-size: 18px;"> <%=skill.getSkillTitle() %> </span>
			<img class="toggle-visibility z14 i-arrow right"  src="<%=baseURL%>assets/images/reports/down.png?a=1" />
			
			<div class="mydropdown" style="display: none;">
				<% if(skill_id!=0) { %>
				<a class="hide-options" style="padding-top: 0%; " href='<%=baseURL%>cpreports/report.jsp?user_id=<%=user_id%>'>Overall</a> <p></p><hr style="margin: 5px;">
				<% 
					}
					int temp = 0;
					for (Skill item : SkillHolder.getChilSkills(skill_id, user_id)) {
						if(SkillHolder.getChilSkills(item.getId(), user_id).size() >0 && SkillHolder.hasData(item.getId(), user_id))
						{
						temp = temp + 6;
				%>
				<a class="hide-options" style="padding-top: <%=temp%>%; " href='<%=baseURL%>cpreports/report.jsp?user_id=<%=user_id%>&skill_id=<%=item.getId()%>'><%=item.getSkillTitle()%></a><p></p><hr style="margin: 5px;">
				
				<%
						}
					}
				%>
			</div>
		</div>
		
		<div class="gcard mt0" id='create-graph-parent-div' style="min-height: 42vh;">
			<jsp:include page="create_graph.jsp">
				<jsp:param name="user_id" value="<%=user_id%>" />
				<jsp:param name="skill_id" value="<%=skill_id%>" />
			</jsp:include>
		</div>

		<div class="gcard percentiles ">
			<div class="item-4">
				<span class="left-bottom "><%=percentiles.get("percentile_batch")%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/batch.png?a=1" />
			</div>
			
			<div class="item-4 bleft">
				<span class="left-bottom "><%=percentiles.get("percentile_organization")%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/org.png?a=1" />
			</div>
			
			<div class="item-4 bleft bright">
				<span class="left-bottom ">	<%=percentiles.get("percentile_country")%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/country.png?a=1" />
			</div>
			
			<div class="item-4">
				<span class="left-bottom "><%=percentiles.get("percentile_globe")%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/global.png?a=1" />
			</div>
		</div>
		
		<div class="gcard gpanel-tab-header  mb0">
			<div class="item-2 active" style="width: 56%;"><a class="" data-toggle="tab" href="#tab-1"><span style="padding: 5px;font-size: 14px">Learning Analysis</span></a></div>
			<div class="item-2 bleft"  style="width: 43%;"><a class="" data-toggle="tab" href="#tab-2"><span style="padding: 5px;font-size: 14px">Test Analysis</span></a></div>
		</div>
		
		<div class="gcard mt0">
			<div class="tab-content">
				<div id="tab-1" class="tab-pane active">
					<jsp:include page="learning_analysis.jsp">
						<jsp:param name="skill_id" value="<%=skill_id%>" />
						<jsp:param name="user_id" value="<%=user_id%>" />
					</jsp:include>

				</div>
				<div id="tab-2" class="tab-pane ">
					<jsp:include page="test_list.jsp">
						<jsp:param name="skill_id" value="<%=skill_id%>" />
						<jsp:param name="user_id" value="<%=user_id%>" />
					</jsp:include>
				</div>
			</div>
		</div>
		
	</div>

	<!-- Mainly scripts -->
	<script src="<%=baseURL%>new/js/jquery-2.1.1.js"></script>
	<script src="<%=baseURL%>new/js/bootstrap.min.js"></script>
	<script src="<%=baseURL%>new/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script
		src="<%=baseURL%>new/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="<%=baseURL%>student/js/inspinia/slick.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="<%=baseURL%>new/js/inspinia.js"></script>
	<script src="<%=baseURL%>new/js/plugins/pace/pace.min.js"></script>
	<script src="<%=baseURL%>student/js/highcharts/highcharts.js"></script>
	<script src="<%=baseURL%>student/js/highcharts/data.js"></script>
	<script src="<%=baseURL%>assets/plugins/swiper/dist/js/swiper.min.js"></script>
	<script src="<%=baseURL%>assets/plugins/card-flip/card-flip.js"></script>
	<script src="<%=baseURL%>assets/js/viewport-support.report.js"></script>



	<script type="text/javascript">
		$(document).ready(function() {
			handleGraphs();
			$('.panel-collapse.collapse.in').each(function(index) {
				$(this).removeClass('in');
			});
		});

		$("#window-back").click(function() {
			window.history.back();
		});
		
		$("a").click(function() {
			$( ".item-2" ).each(function( index ) {
				$(this).removeClass("active");
			});

			$(this).parent( ".item-2" ).addClass("active");			
		});
		
		$('.toggle-visibility').click(function() {
			if ($('.mydropdown').css('display') == 'none') {
				$('.mydropdown').fadeIn().delay(2000).fadeOut();
			} else {
				$('.mydropdown').css('display', 'none');
			}
		});

		var swiper = new Swiper('.swiper-container', {
			nextButton : '.swiper-button-next',
			prevButton : '.swiper-button-prev',
			slidesPerView : 1,
			paginationClickable : true,
			spaceBetween : 30,
			loop : true
		});
	</script>

	<script type="text/javascript">
		function handleGraphs() {
			try {
				var graph_title = $(this).data('graph_title');

				$('#report_container_222').highcharts({
					data : {
						table : 'datatable_report_222'
					},
					title : {
						text : ''
					},
					yAxis : {
						title : {
							text : ''
						},
						gridLineColor : 'transparent',
						labels : {
							enabled : false
						},

					},
					chart : {

						zoomType : 'x',

						// Edit chart spacing
						spacingBottom : 0,
						spacingTop : 10,
						spacingLeft : 0,
						spacingRight : 0,

						// Explicitly tell the width and height of a chart
						width : null,
						height : null,
						backgroundColor : 'transparent'

					},
					legend : {
						enabled : false
					},
					plotOptions : {
						line : {
							fillColor : {
								linearGradient : {
									x1 : 0,
									y1 : 0,
									x2 : 0,
									y2 : 1
								}
							},
							marker : {
								radius : 2
							},
							lineWidth : 4,
							states : {
								hover : {
									lineWidth : 1
								}
							},
							threshold : null
						}
					},
					xAxis : {
						lineWidth : 1,
						minorGridLineWidth : 1,
						lineColor : '#149098',
						labels : {
							enabled : true
						},
						gridLineColor : '#149098',

					},
					series : [ {
						type : 'spline',

					} ]

				});

				//Hide Table
				//$('.dataTables_wrapper').hide();

			} catch (err) {
				console.log(err);
			}
		}
	</script>


</body>

</html>
<%
	}
%>