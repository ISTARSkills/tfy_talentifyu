<%@page import="java.sql.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.istarindia.apps.dao.DBUTILS"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	int user_id = Integer.parseInt(request.getParameter("user_id"));
	int skill_id = Integer.parseInt(request.getParameter("skill_id"));
	String sql = "select percentile_batch, cast(timestamp as date) as date from skill_precentile where student_id ="
			+ user_id + " and skill_id=" + skill_id +" order by date";
	
	/**batch level changes*/
	//sql ="select FinalTable.tt as date ,FinalTable.max_score, batch_group.name, cast (100 * FinalTable.total_score / sum(FinalTable.total_score) over () as integer) as perc from (select cast (timestamp as date) as tt, batch_students.batch_group_id as batch_gp_id, max(score) as max_score ,  sum(report.score) as total_score from skill_precentile, report, batch_students where skill_precentile.student_id =report.user_id and report.user_id = batch_students.student_id and batch_students.batch_group_id=98 and cast(report.created_at as date) = cast (skill_precentile.timestamp as date) and skill_precentile.skill_id = 0 and report.assessment_id in ( SELECT DISTINCT 				assessment. ID 			FROM 				skill_learning_obj_mapping, 				lesson, 				learning_objective_lesson, 				assessment 			WHERE 				skill_learning_obj_mapping.learning_objective_id = learning_objective_lesson.learning_objectiveid 			AND learning_objective_lesson.lessonid = lesson. ID 			AND lesson. ID = assessment.lesson_id 			AND skill_learning_obj_mapping.skill_id IN ( 				SELECT 					0 AS ID 				UNION 					( 						WITH RECURSIVE supplytree AS ( 							SELECT 								ID, 								skill_title, 								parent_skill 							FROM 								skill 							WHERE 								parent_skill = 0 							UNION ALL 								SELECT 									si. ID, 									si.skill_title, 									si.parent_skill 								FROM 									skill AS si 								INNER JOIN supplytree AS sp ON (si.parent_skill = sp. ID) 						) SELECT 							ID 						FROM 							supplytree 					) 			) ) group by batch_gp_id,tt order by batch_group_id, tt ) FinalTable , batch_group  where FinalTable.batch_gp_id = batch_group.id  	 order by batch_group.name      ";
	
	/***/
	
	DBUTILS util = new DBUTILS();

	List<HashMap<String, Object>> res = util.executeQuery(sql);
%>

<div class='row pie-progress-charts margin-bottom-60'>
	<div class='panel panel-sea margin-bottom-40'
		style=' margin: 10px; border: 3px solid #1ABC9C; margin: 0 10px;'>
		<div id='datatable_report_panel_body' class='panel-body' style="padding: 0px;"></div>
		<table
			class='table table-striped table-bordered display responsive dt-responsive  dataTable datatable_report'
			id='datatable_report_222' data-graph_type='datetime'
			data-graph_title='' data-graph_containter='report_container_222' style="display: none">
			<thead>
				<tr>
					<th style="max-width: 100px !important; word-wrap: break-word;">Date</th>
					<!-- <th style="max-width: 100px !important; word-wrap: break-word;">Max Score</th> -->
					<th style="max-width: 100px !important; word-wrap: break-word;">Batch Percentage</th>
				</tr>
			</thead>
			<tbody id='datatable_report_222_body'>
				<%
					for (HashMap<String, Object> row : res) {
						Date timestamp = (Date) row.get("date");
						int batch_percentile = (int) row.get("percentile_batch");
						System.out.println(timestamp);
						/**batch level changes*/
						//
						//int max_score = (int) row.get("max_score");
						//int perc = (int) row.get("perc");
						/**batch level changes*/
				%>

				<tr id=534>
					<td style='max-width: 100px !important; word-wrap: break-word;'><%=timestamp%></td>
					<td style='max-width: 100px !important; word-wrap: break-word;'><%=batch_percentile%></td>
					<%-- <td style='max-width: 100px !important; word-wrap: break-word;'><%=perc%></td> --%>
				</tr>

				<%
					}
				%>
			</tbody>
		</table>
		
	</div>
</div>
<div class="graph_container" id='report_container_222' ></div>