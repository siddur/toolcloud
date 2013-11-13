<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
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
</jsp:attribute>
<jsp:body>
	<div class="body">
		<div class="add_user">
			<form method="post" action="${root}/ctrl/user/register">
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
</jsp:body>
</s:site>