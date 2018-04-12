<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>注册用户</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
div#login {
	text-align: center;
	color: green;
	size: 200px;
}

div#cc {
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
	function xx() {
		var us = $("#user").val();
		var pwd = $("#pwd").val();
		var name = $("#name").val();
		var tn = $("#truename").val();
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "zc",
				id : us,
				pwd : pwd,
				name : name,
				truename : tn
			},
			success : function(data) {
				if (data == 5) {
					alert("注册成功！");
				} else if (data == "不能为空!") {
					alert(data);
				} else if (data == 1) {
					alert("注册失败！");
				} else if (data == 0) {
					alert("用户已存在！");
				} else {
					alert(data);
				}
			}
		});
	}
</script>

</head>

<body style="height: 696px; width: 791px; ">
	<div id="login" style="height: 156px; width: 739px; ">
		<div id="cc">
			用户名：<input id="user" type="text"> <br> <br> 密码：<input
				id="pwd" type="password"> <br> <br> 昵称：<input
				id="name" type="text"> <br> <br> 真实姓名：<input
				id="truename" type="text"> <br> <br> <input
				type="button" value="注册" onclick="xx()" style="width: 143px; ">
			<input type="button" value="返回主界面"
				onclick="location.href='index.jsp'" style="width: 143px; ">
		</div>
	</div>
</body>
</html>
