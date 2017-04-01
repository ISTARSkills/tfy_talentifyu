<%@page import="in.talentifyU.auth.controllers.SkillHolder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.istarindia.apps.dao.SkillDAO"%>
<%@page import="com.istarindia.apps.dao.Skill"%>
<%@page import="java.util.List"%>
<%@page import="in.talentifyU.utils.services.NewReportUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
			+ request.getContextPath() + "/";

	int user_id = Integer.parseInt(request.getParameter("user_id"));

	int skill_id = 0;
	if (request.getParameterMap().containsKey("skill_id")) {
		skill_id = Integer.parseInt(request.getParameter("skill_id"));
	}

%>

<div class="tab_1_holder" >

	<%
	
		String className = "";
		int temp = 0;
		
		for (Skill skill : SkillHolder.getChilSkills(skill_id, user_id)) {

				HashMap<String, String> percentiles = new NewReportUtils().getPercentileForChild(skill.getId(),user_id);
				String college_avg_diff = percentiles.get("college_avg_diff");
				String percentile_batch = percentiles.get("percentile_batch");
			 	String accuracy =percentiles.get("accuracy");
			 	String industry_benchmark =percentiles.get("industry_benchmark");
			 
				String image_name = college_avg_diff.split("##")[0];
				String percentile_value = college_avg_diff.split("##")[1];
				boolean isChildSkill = SkillHolder.isChildSkill(skill.getId());
				System.out.println("is child"+isChildSkill);
				System.out.println("is cpercentile_batch.equalsIgnoreCase"+percentile_batch.equalsIgnoreCase("N/A"));
				System.out.println("is SkillHolder.hasData(skill.getId(), user_id)"+SkillHolder.hasData(skill.getId(), user_id));
				System.out.println("is child"+isChildSkill);
	%>
	
<%

if(!isChildSkill && !percentile_batch.equalsIgnoreCase("N/A") && SkillHolder.hasData(skill.getId(), user_id) && SkillHolder.getChilSkills(skill.getId(), user_id)!= null && SkillHolder.getChilSkills(skill.getId(), user_id).size()>0 )
{
%>	

		<div class="header-span <%=className%>" onClick="window.location ='<%=baseURL%>cpreports/report.jsp?user_id=<%=user_id%>&skill_id=<%=skill.getId()%>'">
			<span ><%=skill.getSkillTitle()%></span>
		</div>
		<div class="gcard percentiles "  onClick="window.location ='<%=baseURL%>cpreports/report.jsp?user_id=<%=user_id%>&skill_id=<%=skill.getId()%>'">
			<div class="item-4">
				<span class="left-bottom "><%=accuracy%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/accuracy.png?a=1" />
			</div>
			
			<div class="item-4 bleft">
				<span class="left-bottom "> <%=percentile_batch%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/ranking.png?a=1" />
			</div>
			
			<div class="item-4 bleft bright">
				<span class="left-top small">College Average</span>
				<span class="left-bottom "><%=percentile_value%></span>
				<%
				if(!percentile_value.equalsIgnoreCase("N/A"))
				{
				%>
				<img class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/<%=image_name%>.png?a=1" />
				<%
				}
				%>
			</div>
			
			<div class="item-4">
				<span class="left-top small ">Industry Benchmark</span>
				<span class="left-bottom "><%=industry_benchmark.replace("-", "")%></span>
				<%if(industry_benchmark.equalsIgnoreCase("N/A"))
					{
					
					}
				else if(!industry_benchmark.startsWith("-")) { %>
					<img  class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/positive.png?a=1" />
				<% } else { 
					
				%>
					<img  class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/negetive.png?a=1" /> 
				<% } %>
			</div>
		</div>
		
	<%
	temp++;
		}
else if(isChildSkill && SkillHolder.hasData(skill.getId(), user_id) && new NewReportUtils().hasQuestionData(skill.getId(), user_id))
{
%>
		<div class="header-span <%=className%>" onclick="jQuery('#child_skill_<%=skill_id%>_<%=skill.getId()%>').collapse('toggle');">
			<span><%=skill.getSkillTitle()%></span>
		</div>
		<div class="gcard percentiles "  onclick="jQuery('#child_skill_<%=skill_id%>_<%=skill.getId()%>').collapse('toggle');">
			<div class="item-4">
				<span class="left-bottom "><%=accuracy%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/accuracy.png?a=1" />
			</div>
			
			<div class="item-4 bleft">
				<span class="left-bottom "> <%=percentile_batch%></span>
				<img class="right-top z14"  src="<%=baseURL%>assets/images/reports/ranking.png?a=1" />
			</div>
			
			<div class="item-4 bleft bright">
				<span class="left-top small">College Average</span>
				<span class="left-bottom "><%=percentile_value%></span>
				
				<%
				if(!percentile_value.equalsIgnoreCase("N/A"))
				{
				%>
				<img class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/<%=image_name%>.png?a=1" />
				<%
				}
				%>
			</div>
			
			<div class="item-4">
				<span class="left-top small ">Industry Benchmark</span>
				<span class="left-bottom "> <%=industry_benchmark.replace("-", "")%></span>
				<%if(industry_benchmark.equalsIgnoreCase("N/A"))
				{
					
				}
			else if(!industry_benchmark.startsWith("-")) { %>
					<img  class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/positive.png?a=1" />
				<% } else { %>
					<img  class="right-top z14 r0"  src="<%=baseURL%>assets/images/reports/negetive.png?a=1" /> 
				<% } %>
			</div>
		</div>
		<div id="child_skill_<%=skill_id%>_<%=skill.getId()%>" class="learning-analysis-child-skill panel-collapse collapse in" style=" min-height: 55vh;">
			<div class="panel-body" >
				<jsp:include page="skill_question_slider.jsp">
					<jsp:param name="child_skill_id" value="<%=skill.getId()%>" />
					<jsp:param name="parent_skill_id" value="<%=skill_id%>" />
					<jsp:param name="user_id" value="<%=user_id%>" />
				</jsp:include> 
			</div>
		</div>

<%
temp++;
		}
	if(temp>0) {
		className = "gdivider";
	}
	
	}		
%>	


</div>