<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>找回密码</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
div#queren {
	text-align: center;
	color: green;
	size: 200px;
}

div#cc {
	text-align: left;
	color: green;
	size: 200px;
}
div#xiugai {
	text-align: center;
	color: green;
	size: 200px;
}

div#dd {
	text-align: left;
	color: green;
	size: 200px;
}

body {
	margin: 300px;
	background-image: url(image/qq.jpg);
	background-repeat: no-repeat;
}
<
</style>
<script type="text/javascript" src="jq/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
		$("#xiugai").hide();
		setInterval(getmessage, 2000);
	});
	
	function xx() {
		var us = $("#user").val();
		var tn = $("#truename").val();
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "zhaohui",
				id : us,
				tn : tn
			},
			success : function(data) {
					if (data != us) {
					$("#user").val("");
					$("#truename").val("");
					alert(data);
				}

				else {
					$("#xiugai").show();
					$("#queren").slideUp();
				}

			}
		});
	}
	
	function pp(){
	var us = $("#user").val();
	var newpwd = $("#newpwd").val();
	$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "xiugai",
				id : us,
				pwd : newpwd
			},
			success : function(data) {
			if(data==1){
			alert("修改成功！");
			}
			else { alert("修改失败！");}
			}
		});
	}
	
</script>

</head>

<body style="height: 696px; width: 791px; ">
	<div id="queren" style="height: 156px; width: 739px; ">
		<div id="cc">
			请输入用户名：<input id="user" type="text"> <br> <br> 
			请输入真实姓名：<input id="truename" type="text"> <br> <br> 
			<input type="button" value="确认" onclick="xx()" style="width: 143px; ">
			<input type="button" value="返回主界面"
				onclick="location.href='index.jsp'" style="width: 143px; ">
		</div>
	</div>
	
	<div id="xiugai" style="height: 156px; width: 739px; ">
	<div id="dd">
			请输入新密码：<input id="newpwd" type="password"> <br> <br> 
			<input type="button" value="确认修改" onclick="pp()" style="width: 143px; ">
			<input type="button" value="返回主界面"
				onclick="location.href='index.jsp'" style="width: 143px; ">
	
</div>
	</div>
</body>
</html>