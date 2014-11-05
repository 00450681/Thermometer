<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Interval</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="
		width = device-width,
		initial-scale = 1
		">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="text-align:center;">
  Set the measure interval<p>
    <form  method="post" action="../Thermometer/thermometer/Oauth2IntervalServlet">
    	<input name="interval" type="text" style="text-align:center;"/>
    	<input name="openid" type="hidden" value=<%= request.getAttribute("openid")%> />
    	<div style="margin:30;">
    		<input type="submit" value="Change"/>
    	</div>
    </form>
  </body>
</html>
