<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="in.talentifyU.utils.services.NewReportUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!-- <div class="slick_learn"> -->

<%
	int child_skill_id = Integer.parseInt(request.getParameter("child_skill_id"));    
	int parent_skill_id = Integer.parseInt(request.getParameter("parent_skill_id"));
	int user_id = Integer.parseInt(request.getParameter("user_id"));
%>

 <div class="swiper-container" id="swiper-container-<%=parent_skill_id%>_<%=child_skill_id%>">
        <div class="swiper-wrapper">
            
<% 
	
	NewReportUtils util = new NewReportUtils();
	HashMap<String, Object> data = util.getQuestionWithDetailsPerSkill(parent_skill_id, child_skill_id, user_id);
	HashMap<Integer, HashSet<LearningObjective>> que_lobj = (HashMap<Integer, HashSet<LearningObjective>>)data.get("learning_obj_details");
	HashMap<Integer, HashMap<String, String>> que_deatils = (HashMap<Integer, HashMap<String, String>>)data.get("question_details");
	request.setAttribute("que_details", que_deatils);
	request.setAttribute("que_lobj", que_lobj);
	int que_count = 0;
	for(int que_idd: que_deatils.keySet())
	{
		que_count++;
%>

	<div class="swiper-slide ">
		<jsp:include page="skill_question_card.jsp">
			<jsp:param name="child_skill_id" value="<%=child_skill_id%>" />
			<jsp:param name="parent_skill_id" value="<%=parent_skill_id%>" />
			<jsp:param name="que_id" value="<%=que_idd %>" />
			<jsp:param name="que_count" value="<%=que_count %>" />
		</jsp:include>
	</div>
	<%} %>
	
	 </div>
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
    </div>