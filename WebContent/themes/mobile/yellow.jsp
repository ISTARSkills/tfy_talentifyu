<%@page import="java.awt.Color"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.istarindia.apps.dao.*"%>
<%@page import="com.istarindia.apps.dao.PresentaionDAO"%>
<%@page import="com.istarindia.apps.dao.*"%>

<%@page import="org.hibernate.Criteria"%>
<%@page import="org.hibernate.HibernateException"%>
<%@page import="org.hibernate.SQLQuery"%>
<%@page import="org.hibernate.Session"%>
<%@page import="org.hibernate.Transaction"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<% 
	PresentaionDAO dao = new PresentaionDAO();
	Presentaion ppt = new Presentaion();
	AssessmentDAO assessmentDAO = new AssessmentDAO();
	Assessment assessment = new Assessment();
	
	int lessonID = 0 ;
	int pptID =  0;
	
	String lesson_theme = "43";
	try {
		lesson_theme = request.getParameter("lesson_theme").toString();
	} catch(Exception npe) {
		npe.printStackTrace();
	}
	
	String sql = "select * from ui_theme as T where T.id="+lesson_theme;
	DBUTILS util = new DBUTILS();
	List<HashMap<String, Object>> results = util.executeQuery(sql);
	HashMap<String, Object> temp = results.get(0);
	HashMap<String, String> temp2 = new HashMap<String, String>();
	for(String str : temp.keySet()) {
		temp2.put(str, temp.get(str).toString());
	}
	
	HashMap<String, String> theme = temp2;
	
%>

<style>

<% 	
	String red = Color.decode(theme.get("title_____font_color")).getRed()+""; 
	String green = Color.decode(theme.get("title_____font_color")).getGreen()+"";
	String blue = Color.decode(theme.get("title_____font_color")).getBlue()+"";
	
	String bg_red = Color.decode(theme.get("background_color")).getRed()+""; 	
	String bg_green = Color.decode(theme.get("background_color")).getGreen()+"";
	String bg_blue = Color.decode(theme.get("background_color")).getBlue()+""; 
%>
	
.submitbtn_q, .launch-button , .warning-toast {
	background-color: rgba(<%= red %>, <%= green %>, <%= blue %>, 0.8);
	position: absolute !important;
	bottom: 35px ;
    left: 5% !important;
	margin-top: 10% !important;
    width: 90% !important;
	height: 48px;
}

.warning-toast {
	background-color: rgba(<%= red %>, <%= green %>, <%= blue %>, 0.5);
    display: none;
	bottom: 90px !important;
    padding: 0.3rem 1rem;
}

.submitbtn_q label, .launch-button label { 
	font-size: 16px;
    line-height: 45px;
	color: <%=theme.get("background_color")%> !important;
}

.warning-toast label{ 
	font-size: 13px;
	color: rgba(<%= red %>, <%= green %>, <%= blue %>, 0.8);
}

.istar_question {
    display: none;
}

.istar_question .question-div, .launch-assessment-page {
	margin-top: -38%;
	color: <%=theme.get("title_____font_color")%> !important;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll (
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.istar_question .question-div .question-text {
    font-size: 20%;
    margin-top: 45%;
}

.options-div {
	margin-bottom: 40%;
	padding-top: 0 !important;
}

.istar_question .options-div  .option-text {
	padding-bottom: 48px;
	padding-right: 52px;
	color: <%=theme.get("listitem_____font_color")%> !important;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll (
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.istar_question .options-div  .option-text td:nth-child(1) {
    width: 5%;
}

.istar_question .options-div  .option-text td:nth-child(2) {
    width: 90%;
    padding-left: 10px;
}

.istar_question .options-div  .option-text td:nth-child(3) {
    width: 5%;
}

.istar_question .options-div  .option-text  td {
	font-size: 24%;
}

.delay-1 {
    -webkit-animation-delay: 1s; /* Safari 4.0 - 8.0 */
    animation-delay: 1s;
}
.delay-2 {
    -webkit-animation-delay: 2s; /* Safari 4.0 - 8.0 */
    animation-delay: 2s;
}

.assessment-page, .launch-assessment-page {
    min-height: 97vh;
    margin: 4px;
	background-color: <%=theme.get("background_color")%>;
}

.assessment-page .timer-div {
	background-color: rgba(<%= red %>, <%= green %>, <%= blue %>, 0.8);
	height: 50px;
	padding: 12px;
	font-size: 18px;
}

.assessment-page .timer-div p , [class^="mdi-"], [class*="mdi-"] {
	color : <%=theme.get("background_color")%>;
}

.launch-assessment-page .session-title {
	font-size: 10vw;
	padding-bottom: 5%;
    margin-top: 25% !important;
    text-shadow: 0 0 10px #fff, 0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>, 
    			0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>;
}

.launch-assessment-page .lesson-title {
	font-size: 8vw;
	border-top: 1px solid rgba(<%= red %>, <%= green %>, <%= blue %>, 0.5);
	padding-top: 5%;
	padding-bottom: 10%;
    text-shadow: 0 0 10px #fff, 0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>, 
    			0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>;
}

.launch-assessment-page .instructions {
	font-size: 4.5vw;
	font-weight: 400; 
	line-height: 2 !important;
    text-shadow: 0 0 10px #fff, 0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>, 
    			0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>, 0 0 10px <%=theme.get("background_color")%>;
}

.bokeh { position:absolute; z-index: 1; opacity: 0.5; }

[type="radio"]:not(:checked)+label:before {
	border: 2px solid rgba(<%= red %>, <%= green %>, <%= blue %>, 0.5);
}

[type="radio"].with-gap:checked+label:before {
    border-radius: 50%;
    border: 2px solid <%=theme.get("listitem_____font_color")%>;
}

[type="radio"]:checked+label:after {
	background-color: rgba(<%= red %>, <%= green %>, <%= blue %>, 0.5) !important;
    border: 2px solid rgba(<%= red %>, <%= green %>, <%= blue %>, 0.5) !important;
}


/* play-session styling below */

body {
	background-color: <%=theme.get("background_color")%>;
}

.reveal  .backgrounds  .slide-background {
	background-size: cover !important;
}

@font-face {
	font-family: 'Alice';
	src: url('/themes/fonts/Alice/Alice-Regular.ttf');
}

@font-face {
	font-family: 'Asap-Bold';
	src: url('/themes/fonts/Asap/Asap-Bold.ttf');
}

@font-face {
	font-family: 'Asap-BoldItalic';
	src: url('/themes/fonts/Asap/Asap-BoldItalic.ttf');
}

@font-face {
	font-family: 'Biryani';
	src: url('/themes/fonts/Biryani/Biryani-Regular.ttf');
}

@font-face {
	font-family: 'ComingSoon';
	src: url('/themes/fonts/Coming_Soon/ComingSoon.ttf');
}

@font-face {
	font-family: 'Coockie';
	src: url('/themes/fonts/Coockie/Coockie-Regular.ttf');
}

@font-face {
	font-family: 'Domine';
	src: url('/themes/fonts/Domine/Domine-Regular.ttf');
}

@font-face {
	font-family: 'DroidSerif';
	src: url('/themes/fonts/Droid_Serif/DroidSerif.ttf');
}

@font-face {
	font-family: 'Exo';
	src: url('/themes/fonts/Exo/Exo-Regular.ttf');
}

@font-face {
	font-family: 'Lato';
	src: url('/themes/fonts/Lato/Lato-Regular.ttf');
}

@font-face {
	font-family: 'LeagueScript';
	src: url('/themes/fonts/League_Script/LeagueScript.ttf');
}

@font-face {
	font-family: 'LibreBaskerville';
	src: url('/themes/fonts/Libre_Baskerville/LibreBaskerville-Regular.ttf');
}

@font-face {
	font-family: 'NatoSerif';
	src: url('/themes/fonts/Nato_Serif/NatoSerif-Regular.ttf');
}

@font-face {
	font-family: 'OpenSans';
	src: url('/themes/fonts/Open_Sans/OpenSans-Regular.ttf');
}

@font-face {
	font-family: 'Prata';
	src: url('/themes/fonts/Prata/Prata-Regular.ttf');
}

@font-face {
	font-family: 'Raleway';
	src: url('/themes/fonts/Raleway/Raleway-Regular.ttf');
}

@font-face {
	font-family: 'Quicksand';
	src: url('/themes/fonts/Quicksand/Quicksand-Regular.ttf');
}

@font-face {
	font-family: 'Roboto';
	src: url('/themes/fonts/Roboto/Roboto-Regular.ttf');
}

#cover-div {
	width: 100%;
	height: 100%;
	top: 0%;
	z-index: 1000;
	left: 0%;
	position: absolute;
}

.btn-circle {
	width: 30px;
	height: 30px;
	text-align: center;
	padding: 6px 0;
	font-size: 12px;
	line-height: 1.428571429;
	border-radius: 15px;
	background-color: #ffffff;
	border: none;
}

#left-nav-div {
	padding-top: 50vh;
	position: absolute;
	top: 0%;
	left: 0%;
	z-index: 999;
	font-weight: bold;
	height: 75vh;
	width: 20%;
	color: black;
	opacity: 0.4;
}

#right-nav-div {
	padding-top: 50vh;
	position: absolute;
	color: black;
	top: 0%;
	right: 0%;
	z-index: 999;
	font-weight: bold;
	height: 90vh;
	width: 20%;
	opacity: 0.4;
}

.hideDiv {
	height: 0px !important;
	visibility: hidden;
}

#session_list_button {
	position: absolute;
	top: 5%;
	right: 5%;
	z-index: 999;
	background-color: #08e3ff;
	color: #131313;
}

.reveal .SESSION_LIST h2 {
	text-align: left;
	margin-left: 2%;
	margin-top: -40%;
	color: #d87e1f;
	font-weight: 100;
	font-size: 110px;
	line-height: 1;
	font-family: Raleway;
}

.reveal .SESSION_LIST h3 {
	text-align: left;
	margin-left: 6%;
	color: #d4cfcf;
	font-weight: 100;
	margin-top: 15%;
	font-size: 65px;
	line-height: 1;
	font-family: Raleway;
}

.reveal .SESSION_LIST  button {
	font-size: 50px;
	font-family: monospace;
	position: absolute;
	margin-top: 5%;
	margin-left: -10%;
}

.reveal .SESSION_LIST ul {
	list-style: none;
	margin-left: 5%;
	padding-top: 5%;
	text-align: left;
}

.reveal .SESSION_LIST ul li a {
	padding-bottom: 48px;
	padding-right: 52px;
	color: #fee0c0;
	font-weight: 100;
	font-size: 60px;
	line-height: 1.5;
	font-family: Roboto;
	text-decoration: none;
}

.reveal .SESSION_LIST ul li.current_session  a {
	font-size: 65px;
	font-weight: bold;
	color: #f9bc7a;
}

.reveal  .ONLY_TITLE h2 {
	margin-top: -37%;
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal  .ONLY_2TITLE h2 {
	margin-top: -37%;
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal  .ONLY_2TITLE_IMAGE h2 {
	margin-left: 2%;
	padding-right: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE h2 {
	margin-left: 2%;
	padding-right: 3%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
	width: 95%;
}

.reveal .ONLY_TITLE_PARAGRAPH  h2 {
	margin-top: -37%;
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented   h2 {
	margin-top: -37%;
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented thead tr th span {
	font-size: 40px;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented tbody tr th span {
	font-size: 35px;
}

.reveal .ONLY_TITLE_IMAGE h2 {
	margin-top: 4%;
	margin-left: 2%;
	margin-right: -2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: 100px !important;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_VIDEO {
	padding: 0px !important;
}

.reveal .ONLY_PARAGRAPH_TITLE h2 {
	margin-top: 5%;
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .SIMPLE_LIST___ONLY_TITLE_LIST h2 {
	margin-left: 2%;
	margin-top: -38%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_LIST_NUMBERED h2 {
	margin-left: 2%;
	margin-top: -38%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_TREE h2 {
	margin-left: 2%;
	margin-top: -35% !important;
	padding-bottom: 40px;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal  .ONLY_2TITLE h3 {
	margin-left: 2%;
	margin-top: 8%;
	color: <%=theme.get("subtitle_____font_color")%>;
	font-weight: <%=theme.get("subtitle_____font_weight")%>;
	font-size: <%=theme.get("subtitle_____font_size")%>px;
	line-height: <%=theme.get("subtitle_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal  .ONLY_2TITLE_IMAGE h3 {
	margin-left: 2%;
	padding-top: 4%;
	color: <%=theme.get("subtitle_____font_color")%>;
	font-weight: <%=theme.get("subtitle_____font_weight")%>;
	font-size: <%=theme.get("subtitle_____font_size")%>px;
	line-height: <%=theme.get("subtitle_____line_height")%>;
	text-align: <%=theme.get("subtitle_____text_alignment")%>;
	font-family: <%= theme.get ( "subtitle_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE .paragraph p {
	list-style: none !important;
	z-index: 9999;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .ONLY_PARAGRAPH  p {
	margin-top: 4%;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  table {
	list-style: none !important;
	margin: auto;
	border-collapse: collapse;
	vertical-align: baseline;
	display: table;
	margin-top: 5%;
	font-size: 4vh;
	color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	width: 100%;
	font-weight: 400;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  thead {
	display: table-header-group;
	border-collapse: collapse;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
	color: #333333;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE   td {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  th {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
	font-weight: bold;
	background: #e0e0e0;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  tr {
	display: table-row;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}

.reveal .ONLY_PARAGRAPH  table {
	list-style: none !important;
	margin: auto;
	border-collapse: collapse;
	vertical-align: baseline;
	display: table;
	margin-top: 5%;
	font-size: 4vh;
	color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	width: 100%;
	font-weight: 400;
}

.reveal .ONLY_PARAGRAPH  thead {
	display: table-header-group;
	border-collapse: collapse;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
	color: #333333;
}

.reveal .ONLY_PARAGRAPH   td {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
}

.reveal .ONLY_PARAGRAPH  th {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
	font-weight: bold;
	background: #e0e0e0;
}

.reveal .ONLY_PARAGRAPH  tr {
	display: table-row;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}

.reveal .ONLY_TITLE_PARAGRAPH  table {
	list-style: none !important;
	margin: auto;
	border-collapse: collapse;
	vertical-align: baseline;
	display: table;
	margin-top: 5%;
	font-size: 4vh;
	color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	width: 100%;
	font-weight: 400;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented   table {
	list-style: none !important;
	margin: auto;
	border-collapse: collapse;
	vertical-align: baseline;
	display: table;
	margin-top: 5%;
	font-size: 4vh;
	color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	width: 100%;
	font-weight: 400;
}

.reveal .ONLY_TITLE_PARAGRAPH  thead {
	display: table-header-group;
	border-collapse: collapse;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
	color: #333333;
}

.reveal .ONLY_TITLE_PARAGRAPH   td {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
}

.reveal .ONLY_TITLE_PARAGRAPH  th {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
	font-weight: bold;
	background: #e0e0e0;
}

.reveal .ONLY_TITLE_PARAGRAPH  tr {
	display: table-row;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  thead {
	display: table-header-group;
	border-collapse: collapse;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
	color: #333333;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented   td {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  th {
	text-align: left;
	padding: 8px;
	border: 3px solid black;
	font-weight: bold;
	background: #e0e0e0;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  tr {
	display: table-row;
	margin: 0;
	padding: 0;
	border: 3px solid black;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}

.reveal .ONLY_PARAGRAPH_TITLE .paragraph {
	list-style: none !important;
	margin-top: -45%;
	margin-left: 2%;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .ONLY_PARAGRAPH .paragraph {
	margin-top: -41%;
	list-style: none;
	z-index: 9999;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .ONLY_PARAGRAPH_IMAGE  .paragraph {
	margin-top: -45%;
	margin-left: 2%;
	max-width: 99%;
	width: 130%;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .ONLY_PARAGRAPH_IMAGE  .paragraph p {
	margin-top: 5%;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE .paragraph {
	margin-left: 2%;
	margin-top: 3%;
	list-style: none !important;
	z-index: 9999;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .ONLY_TITLE_PARAGRAPH  .paragraph {
	margin-left: 2%;
	list-style: none !important;
	margin-top: 3%;
	padding-right: 2%;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .ONLY_PARAGRAPH  table ul {
	list-style-type: none !important;
}

.reveal .ONLY_TITLE_PARAGRAPH  table ul {
	list-style-type: none !important;
}

.reveal .ONLY_TABLE  table ul {
	list-style-type: none !important;
}

.reveal .ONLY_TITLE_TABLE  table ul {
	list-style-type: none !important;
}

.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented   table ul {
	list-style-type: none !important;
}

.reveal .SIMPLE_LIST___ONLY_TITLE_LIST ul .in_progress li {
	font-size: 80px !important;
}

.reveal .SIMPLE_LIST___ONLY_TITLE_LIST ul {
	margin-left: 5%;
	padding-top: 10%;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_LIST_NUMBERED ol li {
	padding-bottom: 48px;
	padding-right: 52px;
	list-style-type: decimal-leading-zero;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_LIST_NUMBERED ol {
	margin-left: 5%;
	padding-top: 10%;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_TREE ul li.child_tree_item {
	margin-bottom: 20px;
	padding-right: 40px;
	list-style-type: disc;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_TREE ul li.parent_tree_item {
	margin-bottom: 20px;
	padding-right: 40px;
	list-style-type: square;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_TITLE_TREE ul {
	margin-top: 45px;
	margin-left: 6%;
	padding-bottom: 56px;
	list-style-type: none;
}

.reveal .ONLY_LIST ul li {
	list-style: square !important;
	padding-bottom: 48px;
	padding-right: 52px;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_LIST ul {
	margin-left: 5%;
	margin-top: -41%;
}

.reveal .ONLY_LIST_NUMBERED ol li {
	padding-bottom: 48px;
	padding-right: 52px;
	list-style-type: decimal-leading-zero;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.reveal .ONLY_LIST_NUMBERED ol {
	margin-left: 5%;
	margin-top: -41%;
}

.reveal .ONLY_PARAGRAPH_IMAGE img {
	width: 180%;
}

.reveal .ONLY_TITLE_IMAGE img {
	width: 180%;
}

.reveal .ONLY_2TITLE_IMAGE img {
	width: 180%;
}

.reveal .ONLY_IMAGE img {
	width: 180%;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE img {
	width: 180%;
}

.reveal .ONLY_PARAGRAPH_TITLE #data_slide_paragraph {
	
}

.reveal .ONLY_2BOX #top {
	height: 900px;
	padding-top: 300px;
	margin-top: -110%;
	width: 129%;
	margin-left: -10%;
}

.reveal .ONLY_2BOX #bottom {
	height: 1000px;
	padding-top: 41px;
	margin-left: -10%;
	width: 120%;
}

.reveal .ONLY_2BOX #top h2 {
	color: <%=theme.get("title_____font_color")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	padding-top: 26%;
	padding-bottom: 5%;
	margin-left: 11%;
	margin-right: 15%;
}

.reveal .ONLY_2BOX #bottom h3 {
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
	padding-top: 26%;
	margin-left: 11%;
	padding-bottom: 4%;
	margin-right: 10%;
}

.reveal .ONLY_2BOX #top li {
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	list-style: none;
	margin-left: 12%;
	padding-right: 31%;
}

.reveal .ONLY_2BOX #bottom li {
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	list-style: none;
	margin-left: 12%;
	padding-right: 31%;
}

/* Infographics */
.reveal .IN_OUT_1___ONLY_TITLE_LIST .first-set {
	position: fixed;
	margin-top: -50%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST .second-set {
	position: fixed;
	padding-top: 21%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST #element_2 {
	position: fixed;
	margin-left: -5%;
	zoom: 117%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST #element_3 {
	position: fixed;
	paddin-top: 2%;
	margin-left: -38%;
	zoom: 120%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST #element_5 {
	position: fixed;
	margin-left: 4%;
	zoom: 120%;
	margin-top: -2%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST h2 {
	position: fixed;
	margin-left: 40%;
	color: <%=theme.get("title_____font_color")%>;
	font-size: 70px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST .in {
	margin-left: 18%;
	margin-top: -26%;
	margin-bottom: 5%;
	color: <%=theme.get("title_____font_color")%>;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( ".ttf",
		"") .replaceAll ( "-Regular", "") %>;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST .out {
	margin-left: 18%;
	margin-top: 48%;
	color: <%=theme.get("title_____font_color")%>;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( ".ttf",
		"") .replaceAll ( "-Regular", "") %>;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST  ul {
	list-style: none;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST  ul li.parent {
	zoom: 120%;
	background: url("/content/assets/img/coin.png") no-repeat top left
		!important;
	padding: 5%;
	padding-left: 11%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST  ul li.child {
	list-style-image: url("/content/assets/img/box.png") !important;
	padding-left: 3%;
	margin-left: 35%;
	margin-top: -2%;
	zoom: 125%;
}

/* IN_OUT_2 infographic */
.reveal .IN_OUT_2___ONLY_TITLE_LIST .first-set {
	position: fixed;
	margin-top: -57%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .second-set {
	position: fixed;
	padding-top: 26%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST #element_2 {
	position: fixed;
	zoom: 118%;
	margin-top: -11%;
	margin-left: -51%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST #element_3 {
	position: fixed;
	paddin-top: 2%;
	margin-left: -38%;
	zoom: 120%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST #element_5 {
	position: fixed;
	margin-left: -6%;
	zoom: 120%;
	margin-top: 64%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST h2 {
	position: fixed;
	color: <%=theme.get("title_____font_color")%>;
	font-size: 70px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .in {
	margin-left: 40%;
	margin-top: -46%;
	margin-bottom: 5%;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( ".ttf",
		"") .replaceAll ( "-Regular", "") %>;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .out {
	margin-left: 40%;
	margin-top: 47%;
	color: <%=theme.get("title_____font_color")%>;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%= theme.get ( "listitem_____font_family") .replaceAll ( ".ttf",
		"") .replaceAll ( "-Regular", "") %>;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST  ul {
	list-style: none;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .in ul li.parent {
	zoom: 120%;
	background:
		url("/content/assets/img/in_out_2_infographic/top_drop_big.png")
		no-repeat top left !important;
	padding: 5%;
	padding-left: 11%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .out ul li.parent {
	zoom: 120%;
	background:
		url("/content/assets/img/in_out_2_infographic/bottom_smoke_1.png")
		no-repeat top left !important;
	padding: 5%;
	padding-left: 11%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .in ul li.child {
	list-style-image:
		url("/content/assets/img/in_out_2_infographic/top_drop_small.png")
		!important;
	padding-left: 3%;
	margin-left: 46%;
	margin-top: -2%;
	zoom: 125%;
	padding-bottom: 6%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .out ul li.child {
	list-style-image:
		url("/content/assets/img/in_out_2_infographic/bottom_smoke_2.png")
		!important;
	padding-left: 3%;
	margin-left: 48%;
	padding-bottom: 5%;
	margin-top: -2%;
	zoom: 125%;
}

/* Working templates but never-used */
.ONLY_2TITLE_TREE h1 {
	margin-top: -26%;
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%= theme.get ( "title_____font_family") .replaceAll ( 
		".ttf", "") .replaceAll ( "-Regular", "") %>;
}

.ONLY_2TITLE_TREE h2 {
	text-align: left;
	margin-left: 2%;
	color: #7E4B0E;
	font-weight: 400;
	margin-top: 13%;
	position: absolute;
}

.ONLY_2TITLE_TREE h3 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
	margin-top: 49%;
}

.ONLY_2TITLE_TREE h4 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
	margin-top: 75%;
}

.ONLY_2TITLE_TREE h5 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
	margin-top: 49%;
	margin-left: 55%;
}

.ONLY_2TITLE_TREE h6 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
	margin-top: 104%;
}

.reveal .ONLY_2TITLE_TABLE h2 {
	font-weight: bolder;
	font-size: 133px;
	padding-top: 130px;
	color: #e89d81;
	line-height: 2.7;
	width: 100%;
	margin-top: -64%;
	text-align: left;
	margin-left: 0%;
}

.reveal .ONLY_2TITLE_TABLE table tr td {
	font-weight: 400;
	font-size: 50px;
	padding-top: 39px;
	padding: 14px;
	text-align: left;
}

.reveal .ONLY_2TITLE_TABLE table tr td input {
	width: 121px;
	height: 101px;
	padding-top: 44px;
}

.reveal .ONLY_2TITLE_TABLE {
	background: white;;
}

.reveal .ONLY_2TITLE_TABLE h3 {
	font-weight: bold;
	font-size: 67px;
	color: #e89d81;
	line-height: 2.7;
	width: 100%;
	margin-top: -17%;
	text-align: left;
	margin-left: 0%;
}

/* Below are of no use.. just for the demo */
.reveal .ONLY_TITLE_ASSESSMENT_2COLUMNS {
	background: white;;
}

.reveal .ONLY_TITLE_ASSESSMENT_2COLUMNS h2 {
	font-weight: 700;
	font-size: 90px;
	color: white;
	background-color: #9cc4a6;
	line-height: 2.7;
	width: 116%;
	padding-top: 130px;
	margin-top: -63%;
	margin-left: -10%;
	text-align: left;
	margin-left: 7%;
}

.reveal .ONLY_TITLE_ASSESSMENT_2COLUMNS table tr td {
	font-weight: 700;
	font-size: 44px;
	line-height: 1.2;
	text-align: left;
	padding-top: 83px !important;
}

.reveal .ONLY_TITLE_ASSESSMENT_2COLUMNS table tr td input {
	width: 121px;
	height: 101px;
	padding-top: 44px;
}
</style>