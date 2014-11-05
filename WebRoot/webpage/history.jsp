<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>History</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta name="viewport" content="
		width = device-width,
		initial-scale = 1
		">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="css/historyCss.css" rel="stylesheet" type="text/css">
	<script language=javascript src="js/jscharts.js"></script>
	<script language=javascript src="js/popcalendar.js" charset=”utf-8″></script>
	<script language=javascript src="js/jspost.js"></script>
	<script language=javascript src="js/My97DatePicker/WdatePicker.js"></script>
  </head>
  
  <body style="text-align: center;">
	<script type="text/javascript">
	
		function createXmlHttp() {
		    var xmlHttp = null;
		     
		    try {
		        //Firefox, Opera 8.0+, Safari
		        xmlHttp = new XMLHttpRequest();
		    } catch (e) {
		        //IE
		        try {
		            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		        } catch (e) {
		            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		        }
		    }
		     
		    return xmlHttp;
		}
		 
		function submitForm(formId) {
		    var xmlHttp = createXmlHttp();
		    if(!xmlHttp) {
		        alert("您的浏览器不支持AJAX！");
		        return 0;
		    }
		    
		    var postBody = serializeForm(formId);  
    		var url = document.getElementById('dateForm').action; 
		    
		    //var url = 'www.100healths.com/health/thermometer/Oauth2HistoryServlet';
		    //var postData = "";
		    //postData = "date=" + document.getElementById('searchDate').value;
		    //postData += "t=" + Math.random();
		    
		    xmlHttp.open("POST", url, true);
		    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		    xmlHttp.onreadystatechange = function() {
		        if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
		        	var myData = eval('('+xmlHttp.responseText+')');
		        	var arr = new Array();
		        	for (i = 0; i < myData[0].time.length; i++) {
		        		arr[i] = new Array(myData[0].time[i], myData[0].temperature[i] / 10.0);
		        	}
		            var myChart = new JSChart('graph', 'line');
					myChart.setDataArray(arr);
					myChart.setTitle('History');
					myChart.setTitleColor('#8E8E8E');
					myChart.setTitleFontSize(11);
					myChart.setAxisNameX('Time');
					myChart.setAxisNameY('Temperature');
					myChart.setAxisColor('#C4C4C4');
					myChart.setAxisValuesColor('#343434');
					myChart.setAxisPaddingLeft(50);
					myChart.setAxisPaddingRight(120);
					myChart.setAxisPaddingTop(50);
					myChart.setAxisPaddingBottom(40);
					myChart.setGraphExtend(true);
					myChart.setGridColor('#c2c2c2');
					myChart.setLineWidth(1);
					myChart.setLineColor('#9F0505');
					//myChart.setSize(616, 321);
					myChart.setSize(500, 250);
					myChart.setBackgroundImage('chart_bg.jpg');
					myChart.draw();
		        }
		    }
		    xmlHttp.send(postBody);
		}
	</script>
	<form action="../Thermometer/thermometer/Oauth2HistoryServlet" method="post" name="dateForm" id="dateForm" style="text-align:center;" >
		<input style="text-align:center;" name="searchDate" type="text" id="searchDate" value="2008-10-24" size="20" onClick="WdatePicker()"/>
		<input name="openid" type="hidden" value=<%= request.getAttribute("openid")%> />
		<img onclick="popUpCalendar(this, dateForm.searchDate, ' yyyy-mm-dd')" src="js/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle" />
		<div align="center" style="margin:30;">
			<input  type="button" onclick="submitForm('dateForm')" value="Confirm" />
		</div>
		
	</form>
	<div id="graph" align="center" >Loading graph...</div>
  </body>
  <!-- //popUpCalendar(this, dateForm.searchDate, ' yyyy-mm-dd') <a href="#" onClick="setday(this,document.all.searchDate); return false;">
		<img src="images/date_selector.gif" border="0" align="absMiddle">
		</a>-->
</html>
