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
	int lessonID = 0 ;
	int pptID =  0;
	if ((request.getParameterMap().containsKey("ppt_id"))){
		pptID = Integer.parseInt(request.getParameter("ppt_id").replaceAll("/", ""));
		ppt =  dao.findById(pptID);
	} else if ((request.getParameterMap().containsKey("lesson_id"))) {
		 lessonID = Integer.parseInt(request.getParameter("lesson_id").toString().split("!")[0]);
		 Lesson lesson = (new LessonDAO()).findById(lessonID);
		 ppt = lesson.getPresentaion();
	} else {
		ppt = (Presentaion) request.getAttribute("ppt");
	}
	
	String lesson_theme = "0";
	try {
		lesson_theme = ppt.getLesson().getLesson_theme_desktop();
		System.err.println("debuggggggggg "+lesson_theme);
	} catch(NullPointerException npe) {
		System.out.println( ppt.getLesson().getId());
	}
	
	String sql = "select * from ui_theme as T where T.id="+lesson_theme;
	UiThemeDAO uiDao = new UiThemeDAO();
	Session uiSession = uiDao.getSession();
	SQLQuery query = uiSession.createSQLQuery(sql);
	query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	DBUTILS util = new DBUTILS();
	List<HashMap<String, Object>> rr = util.executeQuery(sql);
	HashMap<String, Object> temp1 = rr.get(0);
	HashMap<String, String> temp2= new HashMap<String, String>();
	for(String str: temp1.keySet())
	{
		temp2.put(str, temp1.get(str).toString());
	}
	HashMap<String, String> theme = temp2;
	%>
<style>

body {
	background-color: <%=theme.get("background_color")%>;
}

.reveal  .backgrounds  .slide-background {
	background-size: contain !important;
}

@font-face {
    font-family: 'Alice';
    src: url('/content/themes/fonts/Alice/Alice-Regular.ttf');
}

@font-face {
    font-family: 'Asap-Bold';
    src: url('/content/themes/fonts/Asap/Asap-Bold.ttf');
}

@font-face {
    font-family: 'Asap-BoldItalic';
    src: url('/content/themes/fonts/Asap/Asap-BoldItalic.ttf');
}

@font-face {
    font-family: 'Biryani';
    src: url('/content/themes/fonts/Biryani/Biryani-Regular.ttf');
}

@font-face {
    font-family: 'ComingSoon';
    src: url('/content/themes/fonts/Coming_Soon/ComingSoon.ttf');
}

@font-face {
    font-family: 'Coockie';
    src: url('/content/themes/fonts/Coockie/Coockie-Regular.ttf');
}

@font-face {
    font-family: 'Domine';
    src: url('/content/themes/fonts/Domine/Domine-Regular.ttf');
}

@font-face {
    font-family: 'DroidSerif';
    src: url('/content/themes/fonts/Droid_Serif/DroidSerif.ttf');
}

@font-face {
    font-family: 'Exo';
    src: url('/content/themes/fonts/Exo/Exo-Regular.ttf');
}

@font-face {
    font-family: 'Lato';
    src: url('/content/themes/fonts/Lato/Lato-Regular.ttf');
}

@font-face {
    font-family: 'LeagueScript';
    src: url('/content/themes/fonts/League_Script/LeagueScript.ttf');
}

@font-face {
    font-family: 'LibreBaskerville';
    src: url('/content/themes/fonts/Libre_Baskerville/LibreBaskerville-Regular.ttf');
}

@font-face {
    font-family: 'NatoSerif';
    src: url('/content/themes/fonts/Nato_Serif/NatoSerif-Regular.ttf');
}

@font-face {
    font-family: 'OpenSans';
    src: url('/content/themes/fonts/Open_Sans/OpenSans-Regular.ttf');
}

@font-face {
    font-family: 'Prata';
    src: url('/content/themes/fonts/Prata/Prata-Regular.ttf');
}

@font-face {
    font-family: 'Raleway';
    src: url('/content/themes/fonts/Raleway/Raleway-Regular.ttf');
}

@font-face {
    font-family: 'Quicksand';
    src: url('/content/themes/fonts/Quicksand/Quicksand-Regular.ttf');
}

@font-face {
    font-family: 'Roboto';
    src: url('/content/themes/fonts/Roboto/Roboto-Regular.ttf');
}

#session_list_button {
	position: absolute; 
	top: 5%; 
	right: 5%; 
	z-index: 999; 
	background-color: antiquewhite; 
	color: black;
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

.reveal .SESSION_LIST h2 {
    text-align: left;
	margin-left: -4%;
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
   
.reveal .SESSION_LIST ul li a{
    padding-bottom: 48px;
    padding-right: 52px;
    color: #fee0c0;
    font-weight: 100;
    font-size: 42px;
    line-height: 1.5;
    font-family: Roboto;
	text-decoration: none;
}
.reveal .SESSION_LIST ul li.current_session  a{
    font-size: 48px;
	font-weight: bold;
    color: #f9bc7a;
}

.reveal  .ONLY_TITLE h2 {
	margin-left: -4%;
	color: <%=theme.get("title_____font_color")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family: '<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
	
}

.reveal  .ONLY_2TITLE h2 {
	margin-left: -4%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>; 
	font-family: <%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
	
	overflow-wrap: break-word;
  word-wrap: break-word;
  -webkit-hyphens: auto;
  -ms-hyphens: auto;
  -moz-hyphens: auto;
  hyphens: auto;
  p  adding-right: 3%
}

.reveal  .ONLY_2TITLE_IMAGE h2 {
    padding-bottom: 3%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  table  {
	margin: auto;
    border-collapse: collapse;
    vertical-align: baseline;
    display: table;
    font-size: 2.5vh;
    color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
    width: 100%;
    font-weight: 400;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  thead  {
	display: table-header-group;
	border-collapse: collapse;
    margin: 0;
    padding: 0;
    border: 3px solid black;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE   td  {
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

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  tr  {
    display: table-row;
    margin: 0;
    padding: 0;
    border: 3px solid black;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}
.reveal .ONLY_TITLE_PARAGRAPH_IMAGE h2 {
    margin-left: -4%;
    padding-right: 3%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
	width: 100%;
}

.reveal .ONLY_TITLE_PARAGRAPH  h2  , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  h2  {
    padding-bottom: 3%;
    color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}
.reveal .ONLY_TITLE_TABLE h2 {
	margin-left: -4%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_IMAGE h2 {    vertical-align: middle;
 	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=(Integer.parseInt(theme.get("title_____font_size")))*0.9%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}


.reveal .ONLY_TITLE_VIDEO h2 {
	position: absolute;
    margin-left: -4%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}


.reveal .ONLY_PARAGRAPH_TITLE h2{
    margin-left: -4%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
	
	}

.reveal .SIMPLE_LIST___ONLY_TITLE_LIST h2 {
	margin-left: -4%;
	padding-bottom: 3%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_LIST_NUMBERED h2 {
	margin-left: -4%;
	  padding-bottom: 3%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_TREE h2 {
	margin-left: -4%;
	padding-bottom: 40px;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal  .ONLY_2TITLE h3 {
	padding-top: 8%
	color: <%=theme.get("subtitle_____font_color")%>;
	font-weight: <%=theme.get("subtitle_____font_weight")%>;
	font-size: <%=theme.get("subtitle_____font_size")%>px;
	line-height: <%=theme.get("subtitle_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family: <%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
	
}

.reveal  .ONLY_2TITLE_IMAGE h3 {
	color: <%=theme.get("subtitle_____font_color")%>;
	font-weight: <%=theme.get("subtitle_____font_weight")%>;
	font-size: <%=theme.get("subtitle_____font_size")%>px;
	line-height: <%=theme.get("subtitle_____line_height")%>;
	text-align: <%=theme.get("subtitle_____text_alignment")%>;
	font-family: <%=theme.get("subtitle_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE .paragraph p {
	list-style: none;
    z-index: 9999;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
}

.reveal .ONLY_paragraph  p {
	 
}

.reveal .ONLY_PARAGRAPH  table  {
	margin: auto;
    border-collapse: collapse;
    vertical-align: baseline;
    display: table;
    font-size: 2.5vh;
    color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
    width: 100%;
    font-weight: 400;
}

.reveal .ONLY_PARAGRAPH  thead  {
	display: table-header-group;
	border-collapse: collapse;
    margin: 0;
    padding: 0;
    border: 3px solid black;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_PARAGRAPH   td  {
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

.reveal .ONLY_PARAGRAPH  tr  {
    display: table-row;
    margin: 0;
    padding: 0;
    border: 3px solid black;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_TITLE_PARAGRAPH  table , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  table  {
	margin: auto;
    border-collapse: collapse;
    vertical-align: baseline;
    display: table;
    font-size: 2.5vh;
    color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
    width: 100%;
    font-weight: 400;
}

.reveal .ONLY_TITLE_PARAGRAPH  thead , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  thead  {
	display: table-header-group;
	border-collapse: collapse;
    margin: 0;
    padding: 0;
     font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_TITLE_PARAGRAPH   td , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented   td  {
	text-align: left;
    padding: 8px;
    border: 2px solid rgba(214, 201, 201, 0.3);
    font-size: 120%;
}

.reveal .ONLY_TITLE_PARAGRAPH  th , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  th {
	text-align: left;
    padding: 8px;
    font-weight: bold;
    background: rgba(224,224,224,0.67);
    color: black;
    padding: 20px;
    font-size: 120%;
    border: 2px solid rgba(0,0,0,0.3);
}
.reveal .ONLY_TITLE_PARAGRAPH .paragraph  td p , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented .paragraph  td p {
     margin-bottom: 0%; 
}
.reveal .ONLY_TITLE_PARAGRAPH .paragraph  td, .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented .paragraph  td {
     margin-bottom: 2%; 
     padding: 0.6%;
}
.reveal .ONLY_TITLE_PARAGRAPH  tr , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  tr  {
    display: table-row;
    margin: 0;
    padding: 0;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_TITLE_TABLE  table  {
	margin: auto;
    border-collapse: collapse;
    vertical-align: baseline;
    display: table;
    font-size:2.5vh;
    color: <%=theme.get("paragraph_____font_color")%>;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
    width: 100%;
    font-weight: 400;
}

.reveal .ONLY_TITLE_TABLE  thead  {
	display: table-header-group;
	border-collapse: collapse;
    margin: 0;
    padding: 0;
    border: 3px solid black;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_TITLE_TABLE   td  {
	text-align: left;
    padding: 8px;
    border: 3px solid black;
}

.reveal .ONLY_TITLE_TABLE  th {
	text-align: left;
    padding: 8px;
    border: 3px solid black;
    font-weight: bold;
    background: #e0e0e0;
}

.reveal .ONLY_TITLE_TABLE  tr  {
    display: table-row;
    margin: 0;
    padding: 0;
    border: 3px solid black;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

.reveal .ONLY_TITLE_TABLE th p {
	text-align: left;
    padding: 8px;
    font-weight: bold;
    background: #e0e0e0;
}


.reveal .ONLY_TITLE_TABLE .paragraph>p {
	padding-bottom: 4%;
    list-style: none;
	padding-right: 5%;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
}

.reveal .ONLY_PARAGRAPH_TITLE .paragraph {
	list-style: none;
	margin-left: 2%;
	margin-bottom: 5%;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;

}

.reveal .ONLY_PARAGRAPH .paragraph  {
	list-style: none;
	z-index: 9999;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
}

.reveal .ONLY_PARAGRAPH_IMAGE  .paragraph  {
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px !important;;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
}

.reveal .ONLY_TITLE_PARAGRAPH_IMAGE .paragraph  {
    margin-top: 1%;
	margin-left: 2%;
    list-style: none;
    z-index: 9999;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
}

.reveal .ONLY_TITLE_PARAGRAPH  .paragraph , .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  .paragraph {
	margin-top: 2%;
    padding-left: 0%;
    list-style: none;
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family: '<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>', serif;
}
.reveal .ONLY_TITLE_PARAGRAPH  .paragraph p, .reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  .paragraph p{
	margin-bottom: 2%;
}

.reveal .ONLY_PARAGRAPH  table ul{
	list-style-type: none !important;
}
.reveal .ONLY_TITLE_PARAGRAPH  table ul{
	list-style-type: none !important;
}

.reveal .ONLY_TABLE  table ul{
	list-style-type: none !important;
}
.reveal .ONLY_TITLE_TABLE  table ul{
	list-style-type: none !important;
}

.reveal .SIMPLE_LIST___ONLY_TITLE_LIST ul li  {
/*	list-style-image: url('https://cdn3.iconfinder.com/data/icons/price-tags/512/price-sale-cart-shopping-list-ecommerce-tag-18-64.png');*/
list-style: none !important;
	padding-bottom: 20px;
	padding-right: 30px;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .SIMPLE_LIST___ONLY_TITLE_LIST ul  {
    margin-left: 5%;
    color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_LIST_NUMBERED ul li  {
	padding-bottom: 48px;
	padding-right: 52px;
	list-style-type: decimal-leading-zero;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_LIST_NUMBERED ul  {
    margin-left: 2%;
    color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_TREE ul li.parent_tree_item  {
    margin-bottom: 20px;
	padding-right: 40px;
    list-style-type: square !important;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}
.reveal .ONLY_TITLE_TREE ul li.child_tree_item  {
    margin-bottom: 20px;
	padding-right: 40px;
    list-style-type: disc !important;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_TITLE_TREE ul  {
    margin-left: 6%;
    padding-bottom: 6px;
    list-style-type: none;
}

.reveal .ONLY_LIST ul li  {
	list-style: none !important;
	padding-bottom: 48px;
	padding-right: 52px;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_LIST ul  {
    margin-left: 2%;
   
}

.reveal .ONLY_LIST_NUMBERED ul li  {
	list-style: none !important;
	padding-bottom: 48px;
	padding-right: 52px;
	list-style-type: decimal-leading-zero;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: <%=theme.get("listitem_____font_size")%>px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family:<%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .ONLY_LIST_NUMBERED ul  {
    margin-left: 2%;
   
}

.ONLY_2TITLE_TREE h1 {
	
	margin-left: 2%;
	color: <%=theme.get("title_____font_color")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-size: <%=theme.get("title_____font_size")%>px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-family:<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.ONLY_2TITLE_TREE h2 {
	text-align: left;
	margin-left: -4%;
	color: #7E4B0E;
	font-weight: 400;
	position: absolute;

}

.ONLY_2TITLE_TREE h3 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
}

.ONLY_2TITLE_TREE h4 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
}

.ONLY_2TITLE_TREE h5 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
	margin-left: 55%;
}

.ONLY_2TITLE_TREE h6 {
	text-align: left;
	margin-left: 2%;
	color: #FDC530;
	font-weight: 400;
	position: absolute;
}

.reveal img.current-fragment {
    animation-name: pulse;
    -webkit-animation-name: pulse;
    animation-duration: 1s;
    -webkit-animation-duration: 1s;
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




/* Infographics */
.reveal .IN_OUT_1___ONLY_TITLE_LIST .first-set {
	position: fixed;
}
.reveal .IN_OUT_1___ONLY_TITLE_LIST .second-set {
	position: fixed;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST #element_2 {
	position: fixed;
    margin-top: -2%;
    margin-left: 75px;
    zoom: 60%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST #element_3 {
	position: fixed;
    margin-left: 5%;
    margin-top: 41%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST #element_5 {
    position: fixed;
	zoom: 75%;
	margin-top: -2%;
    margin-left: 2%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST h2 {
	position: fixed;
    margin-top: -5%;
    margin-left: 20%;
	color: <%=theme.get("title_____font_color")%>;
	font-size: 60px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST .in {
    margin-left: 18%;
    margin-top: 10%;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST .out {
    margin-left: 12%;
    margin-top: 10%;
	color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}


.reveal .IN_OUT_1___ONLY_TITLE_LIST  ul {
	list-style: none !important;
	padding-bottom: 5%;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST  ul li.parent {zoom: 120%;
	background: url("/content/assets/img/coin.png") no-repeat top left !important;
    zoom: 70%;
    padding: 5%;
    padding-left: 11%;
	font-size: 55px;
}

.reveal .IN_OUT_1___ONLY_TITLE_LIST  ul li.child {
    list-style-image: url("/content/assets/img/box.png") !important;
    padding-left: 3%;
    zoom: 75%;
    margin-left: 40%;
}


/* IN_OUT_2 infographic */
.reveal .IN_OUT_2___ONLY_TITLE_LIST .first-set {
	position: fixed;
}
.reveal .IN_OUT_2___ONLY_TITLE_LIST .second-set {
	position: fixed;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST #element_2 {
	position: fixed;
    margin-top: 40%;
    zoom: 50%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST #element_5 {
	position: fixed;
    margin-top: 40%;
    zoom: 50%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST h2 {
	position: fixed;
    margin-left: 20%;
	color: <%=theme.get("title_____font_color")%>;
	font-size: 40px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .in {
    margin-left: 25%;
    margin-top: 20%;
     color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .out {
    margin-left: 30%;
    margin-top: 20%;
     color: <%=theme.get("listitem_____font_color")%>;
	font-weight: <%=theme.get("listitem_____font_weight")%>;
	font-size: 45px;
	line-height: <%=theme.get("listitem_____line_height")%>;
	text-align: <%=theme.get("listitem_____text_alignment")%>;
	font-family: <%=theme.get("listitem_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>;
}


.reveal .IN_OUT_2___ONLY_TITLE_LIST  ul {
	list-style: none;
	padding-bottom: 5%;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .in ul li.parent {zoom: 70%;
	background: url("/content/assets/img/in_out_2_infographic/top_drop_big.png") no-repeat top left
		!important;
	padding: 5%;
	padding-left: 11%;
	font-size: 55px;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .in ul li.child {
list-style-image: url("/content/assets/img/in_out_2_infographic/top_drop_small.png") !important;
        padding-left: 3%;
    margin-left: 46%;
    margin-top: -2%;
    zoom: 75%;
}
.reveal .IN_OUT_2___ONLY_TITLE_LIST .out ul li.parent {zoom: 70%;
	background: url("/content/assets/img/in_out_2_infographic/bottom_smoke_1.png") no-repeat top left
		!important;
	padding: 5%;
	padding-left: 11%;
	font-size: 55px;
}

.reveal .IN_OUT_2___ONLY_TITLE_LIST .out ul li.child {
list-style-image: url("/content/assets/img/in_out_2_infographic/bottom_smoke_2.png") !important;
   padding-left: 3%;
    margin-left: 48%;
    margin-top: -2%;
    zoom: 75%;
}

.reveal .ONLY_2BOX   h2 {
	color: <%=theme.get("title_____font_color")%>;
	font-size: 70px;
	line-height: <%=theme.get("title_____line_height")%>;
	text-align: <%=theme.get("title_____text_alignment")%>;
	font-weight: <%=theme.get("title_____font_weight")%>;
	font-family:
		'<%=theme.get("title_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	padding-bottom: 5%;
	margin-left: 5%;
}
.reveal .ONLY_2BOX li {
	color: <%=theme.get("paragraph_____font_color")%>;
	font-weight: <%=theme.get("paragraph_____font_weight")%>;
	font-size: <%=theme.get("paragraph_____font_size")%>px;
	line-height: <%=theme.get("paragraph_____line_height")%>;
	text-align: <%=theme.get("paragraph_____text_alignment")%>;
	font-family:
		'<%=theme.get("paragraph_____font_family").replaceAll(".ttf", "").replaceAll("-Regular", "")%>',
		serif;
	list-style: none;
	margin-left: 5%;
}


.reveal .ONLY_2BOX p {
	list-style: none;
	padding-bottom: 5%
}



/* Below are of no use.. just for the demo */


#top {
	height: 900px;
	padding-top: 300px;
	margin-top: -110%;
	width: 129%;
	margin-left: -10%;
}

#bottom {
	height: 1000px;
	padding-top: 41px;
	margin-left: -10%;
	width: 120%;
}

#top h2 {
	color: rgb(102,197,187);
	text-align: left;
	font-size: 90px;
	margin-left: 5%;
	font-weight: bolder;
	padding-top: 26%;
	margin-left: 11%;
	font-weight: 900;
}
#bottom h3 {
	color: white;
	text-align: left;
	font-size: 90px;
	margin-left: 5%;
	font-weight: bolder;
	padding-top: 26%;
	margin-left: 11%;
	font-weight: 900;
}
#top li {
	color: rgb(93,93,93);
	font-size: 55px;
	list-style: none;
	margin-left: 12%;
	padding-right: 31%;
	text-align: left;
	line-height: 1.1;
}

#bottom li {
	color: white;
	font-size: 55px;
	list-style: none;
	padding-top: 23px;
	margin-left: 12%;
	padding-right: 31%;
	text-align: left;
	line-height: 1.1;
}


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



.reveal .slides section .fragment.grow.visible {
    -webkit-transform: scale(1.2) !important;
    transform: scale(1.1) !important;
    margin-left: 4% !important;
}


.css-typing
{
    overflow:hidden;
   -webkit-animation: type 5s steps(50, end);
    animation: type 5s steps(50, end);
    line-height: 1.2 !important;
}

@keyframes type{
    from { width: 0; }
}

@-webkit-keyframes type{
    from { width: 0; }
}


/* 
.list {
     list-style:disc outside none;
     display:list-item; 
     }
      */
     
     
#data_slide_paragraph ul li{
margin-bottom: 22px;}

.row img {
	-webkit-border-radius: 22px;
    -moz-border-radius: 22px;
    -o-border-radius: 22px;
    border-radius: 22px;
 }

.p-xl {
    padding: 40px;
}
.lazur-bg {
    background-color: #23c6c8;
    color: #ffffff;
}
.widget {
    border-radius: 5px;
    padding: 15px 20px;
    margin-bottom: 10px;
    margin-top: 10px;
        padding: 20px !important;
}

.grid {
  background: #EEE;
  max-width: 1200px;
}

/* clearfix */
.grid:after {
  content: '';
  display: block;
  clear: both;
}

/* ---- grid-item ---- */

.grid-item {
  float: left;
  background: #D26;
  border: 2px solid #333;
  border-color: hsla(0, 0%, 0%, 0.5);
  border-radius: 5px;
}

.grid-item--width2 { width: 320px; }
.grid-item--width3 { width: 480px; }
.grid-item--width4 { width: 640px; }

.grid-item--height2 { height: 200px; }
.grid-item--height3 { height: 260px; }
.grid-item--height4 { height: 360px; }  

.fragment.current-visible.visible:not(.current-fragment) {
   display: none;
   height:0px;
   line-height: 0px;
   font-size: 0px;
}

.final {
	font-size : 100% !important;
}

.interim {
	font-size : 150% !important;
	width: 90% !important;
}

.show-all {
	position: static;
	font-size : 100% !important;
}




@media screen and (max-width: 1024px) and (min-width: 1024px) {
    .reveal .SIMPLE_LIST___ONLY_TITLE_LIST h2 {
		padding-left: 1% !important;
		padding-right: 1% !important;
		font-size: <%=(Integer.parseInt(theme.get("title_____font_size")))*0.75%>px !important;
	}
	
	.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  h2  {
				font-size: <%=(Integer.parseInt(theme.get("title_____font_size")))*0.70%>px !important;
				padding-top: 10px;

	}
}


</style>
