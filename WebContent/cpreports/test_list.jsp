<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="in.talentifyU.auth.controllers.SkillHolder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.istarindia.apps.dao.StatsStudentAssessment"%>
<%@page import="com.istarindia.apps.dao.Assessment"%>
<%@page import="com.istarindia.apps.dao.SkillDAO"%>
<%@page import="com.istarindia.apps.dao.Skill"%>
<%@page import="in.talentifyU.utils.services.NewReportUtils"%>

<%@page import="java.util.*"%>


<%	
	String url = request.getRequestURL().toString();			
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())	+ request.getContextPath() + "/";


	int user_id = Integer.parseInt(request.getParameter("user_id"));
	
	int skill_id = 0;
	if(request.getParameterMap().containsKey("skill_id"))
	{
	skill_id = Integer.parseInt(request.getParameter("skill_id"));	
	}	
%>
<div class="tab_2_holder">		
<%
ArrayList<Assessment> assessments = SkillHolder.getAssessmentForSkill( skill_id, user_id);
String className = "";
for(Assessment row : assessments)
{

	
	new NewReportUtils().getRepoDetailForAssessment(user_id, row);
HashMap <String, String>  repdata = new NewReportUtils().getRepoDetailForAssessment(user_id, row);
	
	String accuracy=repdata.get("accuracy");
	String time_taken =repdata.get("time_taken");
	String percentile =repdata.get("percentile");
	String created_at=repdata.get("created_at");
	int id = row.getId();
	String assessment_title= repdata.get("assessment_title");
	String wizard_image=repdata.get("wizard_image");
	
	%>	
	 <div class="test-card-holder" style="background-image: url('<%=baseURL%>assets/images/reports/<%=wizard_image %>.png?a=1');" onClick="window.location ='<%=baseURL%>cpreports/test_skill_list.jsp?user_id=<%=user_id%>&assessment_id=<%=row.getId()%>&skill_id=<%=skill_id %>'" >
	 <div class="header-span <%=className%>">
			<span><%=assessment_title %></span>
			<span class="timestamp"><i><%=created_at %></i></span>
		</div>
		<div class="gcard percentiles test"  >
			<div class="item-3">
				<span class="left-bottom "><%=accuracy%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/accuracy.png?a=1" />
			</div>
			
			<div class="item-3 bleft">
				<span class="left-bottom "> <%=percentile%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/ranking.png?a=1" />
			</div>
			
			<div class="item-3 bleft">
				<span class="left-bottom "> <%=time_taken %></span>
				<img class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/time.png?a=1" />
			</div>
			
			
		</div>
		</div>
	
<%

className = "gdivider";
}
%>
</div>



