<%@ page language="java" contentType="text/html; charset=UTF-8TF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sign Up</title>
<style>
	.add_user div{
		padding-bottom:10px;
	}
</style>
<script>
	function preSubmit(){
		var username = document.getElementsByName("username")[1].value;
		var passwords = document.getElementsByName("password");

		if(!username){
			alert("username is required");	
			return false;
		}

		if(!passwords[1].value){
			alert("password is required");
			return false;
		}

		if(passwords[1].value != passwords[2].value){
			alert("passwords are not the same");
			return false;
		}
	}
</script>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<div class="add_user">
			<form method="post" action="/toolcloud/ctrl/user/register">
				<div>
					Username<span class="asterisk">*</span>:
					<input name="username">
				</div>
				<div>
					Password<span class="asterisk">*</span>:
					<input type="password" name="password">
				</div>
				<div>
					Password again<span class="asterisk">*</span>:
					<input type="password" name="password">
				</div>
				<div>
					Email:
					<input name="email">
				</div>
				<div>
					Realname:
					<input name="realname">
				</div>
				<div>
					Nickname:
					<input name="nickname">
				</div>
				<input class="btn" type="submit" value="sign up" onclick="return preSubmit();">
			</form>
		</div>
	</div>
</body>
</html>