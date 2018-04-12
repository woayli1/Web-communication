<%@ page language="java" pageEncoding="UTF-8"%>
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

<title>主界面</title>
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

div#main {
	position: absolute;
	left: 200px;
	top: 50px;
}

div#userl {
	position: absolute;
	left: 20px;
	top: 50px;
	background-color: blue;
}

div#sendmsg {
	position: absolute;
	left: 400px;
	top: 350px;
}

body {
	margin: 300px;
	background-image: url(image/qq.jpg);
	background-repeat: no-repeat;
}
</style>
<script type="text/javascript" src="jq/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#main").hide();
		setInterval(getmessage, 2000);
	});

	function xx() {
		var us = $("#user").val();
		var pw = $("#pwd").val();
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "userexit",
				username : us,
				password : pw

			},

			success : function(data) {
				if (data != us) {
					$("#user").val("");
					$("#pwd").val("");
					alert(data);
				}

				else {
					//$("#username").html(data);
					getUser();
					getUserList();
					$("#main").show();
					$("#login").slideUp();
				}
			}
		});
	}

	function getUserList() {
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "UserList"
			},
			success : function(data) {
				var res = $.parseJSON(data);
				var al = "";
				$.each(res, function(index, element) {
					al += "<li>" + element + "</li>";
				});
				$("#userl").html(al);
			}
		});
	}

	function getUser() {
		var us = $("#user").val();
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "getUser",
				id2 : us
			},
			success : function(data) {
				var aaa = $.parseJSON(data);
				$("#username").html(aaa);
			}
		});

	}

	function send() {
		var msg = $("#sendmsg").val();
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "sendmsgs",
				send : msg
			},
			success : function(ss) {
				$("#sendmsg").val("");
				getmessage();

			}
		});
	}

	function tui() {
		$("#user").val("");
		$("#pwd").val("");
		$("#main").hide();
		$("#login").show();
	}

	function getmessage() {
		$.ajax({
			type : "POST",
			url : "servlet/UserServlet",
			data : {
				method : "getmessage"
			},
			success : function(data) {
				var arr = $.parseJSON(data);
				var mess = "";
				$.each(arr, function(element, index) {
					mess += index + "\r\n";
					$("#messageList").val(mess);
				});

			}
		});

	}
</script>
</head>

<body style="height: 696px; width: 791px; ">
	<div id="login" style="height: 156px; width: 739px; ">
		用户名：<input id="user" type="text"> <br> <br> 密码：<input
			id="pwd" type="password"> <br> <br> <input
			type="button" value="登录" onclick="xx()" style="width: 143px; "><br>
		<br> <input type="button" value="注册"
			onclick="location.href='zc.jsp'" style="width: 143px; "> <input
			type="button" value="修改密码" onclick="location.href='xg.jsp'"
			style="width: 143px; "> <input type="button" value="找回密码"
			onclick="location.href='zh.jsp'" style="width: 143px; ">
	</div>
	<div id="main">
		欢迎您！<span id="username"></span><br>

		<div id="username1" style="float:left">

			<ul id="userl">

			</ul>
		</div>
		<br> <br>
		<div style="center">
			<br>
			<div>
				<textarea rows="10" cols="20" id="messageList"
					style="width: 320px; height: 188px"></textarea>
				<br>
			</div>
			<div>
				<textarea rows="2" cols="20" id="sendmsg"
					style="width: 320px; height: 100px"></textarea>
				<button id="sendmsgs" onclick="send()">发送</button>
				<br>
				<!--<input type="button" value="个人信息" onclick="location.href='geren。jsp'"style="width: 143px; ">-->
				<input type="button" value="退出登陆" onclick="tui()"
					style="width: 143px; ">
			</div>

		</div>
	</div>

</body>
</html>