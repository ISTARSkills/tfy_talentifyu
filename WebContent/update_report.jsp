<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.istarindia.assessment.v2.AssessmentV2Services"%>
<%@page import="com.istarindia.apps.dao.DBUTILS"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%

System.out.println(" started");

String sql="select distinct user_id, assessment_id from report;";
System.out.println(sql);
DBUTILS util =new DBUTILS();
AssessmentV2Services serv = new AssessmentV2Services();
List<HashMap<String, Object	>> data = util.executeQuery(sql);
for(HashMap<String, Object	> row: data)
{
	int student_id =(int)row.get("user_id");
	int assessment_id =(int)row.get("assessment_id");
	System.out.println(" executing user_id="+student_id+ " and assess id ="+assessment_id);
	
	    	
	    	serv.updateReport(student_id, assessment_id);
	 		serv.reportdataNew(student_id, assessment_id);
	 		serv.updateLOBAggregate(student_id, assessment_id);
	 	//	serv.updateSkillPErcentile(assessment_id);		
	 	//	serv.updateSkillAggreaget(); 
	 		
	     }
		
	System.out.println(" ends");

	     
	
%>
</body>
</html>