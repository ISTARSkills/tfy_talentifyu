
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%

String url = request.getRequestURL().toString();			
String baseURL = url.substring(0, url.length() - request.getRequestURI().length())	+ request.getContextPath() + "/";

	int child_skill_id = Integer.parseInt(request.getParameter("child_skill_id"));
	int parent_skill_id = Integer.parseInt(request.getParameter("parent_skill_id"));
	int que_id = Integer.parseInt(request.getParameter("que_id"));
	int que_count = Integer.parseInt(request.getParameter("que_count"));
	HashMap<Integer, HashSet<LearningObjective>> que_lobj = (HashMap<Integer, HashSet<LearningObjective>>) request
			.getAttribute("que_lobj");
	HashMap<Integer, HashMap<String, String>> que_deatils = (HashMap<Integer, HashMap<String, String>>) request
			.getAttribute("que_details");
	HashMap<String, String> qqq = (HashMap<String, String>) que_deatils.get(que_id);
	String selected_ans = qqq.get("selected_ans");
	String difficulty_level = qqq.get("difficulty_level");
	String time_taken = qqq.get("time_taken");
	String percentile = qqq.get("percentile");
	String correct_ans = qqq.get("correct_ans");
	String is_corect = qqq.get("is_corect").toLowerCase();
	String question_text = qqq.get("question_text");
	String prof_level = qqq.get("prof_level");
	String explanation = qqq.get("explanation").replaceAll("<p>", "").replaceAll("</p>", "");
%>	
		<div class="flip-card-container ">
			<div class="card " id="qCard-<%=que_id%>">
			   <div class="front">
			    <div class="inner">
					<div class="card-header" >
						<h3 class="text-center question-card-header ">Question <%=que_count%>/<%=que_deatils.size()%> </h3>
					</div>
					<div class="card-body" >
						<h3 class="text-left">
							<strong>Question</strong> 
						</h3>
						<div class="qustion-text">	<%=question_text%></div>
					</div>
				</div>
			</div>
			   <div class="back">
			   	<div class="inner ">
					<div class="card-header" >
						<h3 class="text-center question-card-header ">Question <%=que_count%>/<%=que_deatils.size()%> </h3>
					</div>
					<div class="card-body" >
						<h3 class="text-left">
							<strong>Explanation</strong> 
						</h3>
							<div class="explanation-text"><%=explanation%></div>
					</div>
				</div>
			   </div>
			</div>
			
			<img class="question-result" src="<%=baseURL%>assets/images/reports/<%=is_corect %>.png">
			
			<div class="gcard percentiles question"  >
				<div class="item-4">
					<span class="left-top "><%=prof_level%></span>
					<img class="right-bottom  z14"  src="<%=baseURL%>assets/images/reports/difficulty.png?a=1" />
				</div>
				
				<div class="item-4 bleft white">
					<span class="left-top "> <%=percentile%></span>
					<img class=" right-bottom  z14"  src="<%=baseURL%>assets/images/reports/percentile.png?a=1" />
				</div>
				
				<div class="item-4 bleft bright white">
					<span class="left-top "><%=time_taken %></span>
					<img class="right-bottom  z14"  src="<%=baseURL%>assets/images/reports/time_white.png?a=1" />
				</div>
				
				<div class="item-4 card-flipper" id="<%=que_id%>">
					<img  class="   z25 center"  src="<%=baseURL%>assets/images/reports/explain.png?a=1" />
				</div>
			</div>  
				
			
		</div>
		
		
