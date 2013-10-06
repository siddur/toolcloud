<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.login-panel{
		width: 400px;
		margin: 50px auto;
	}
	.login-head{
		border-radius: 4px 4px 0 0;
		background-color: #515151;
		color:white;
		font-size: 20px;
		font-weight: bolder;
		padding:10px;
	}
	.login-body{
		border-radius: 0 0 4px 4px;
		background-color:#D0E9C6;
		font-weight: bolder;
		padding:25px;	
	}
	.input-item{
		margin-bottom: 20px;
		width:98%;
		height:21px;
	}
	.login-btn{
		height:30px;
		font-color:#333333;
		box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
		border-width: 1px;
		font-weight:bold;
		background-color: #EAEAEA;
	    background-image: linear-gradient(#FAFAFA, #EAEAEA);
	    background-repeat: repeat-x;
	    border-color: #DDDDDD #DDDDDD #C5C5C5;
	}
	.login-btn:HOVER {
		cursor: pointer;
	}
</style>
</jsp:attribute>
<jsp:body>
	<div class="login-panel">
		<div class="login-head">
			Login
		</div>
		<div class="login-body">
			<form method="post" action="/toolcloud/ctrl/user/login">
				<div>Username</div>
				<div><input class="input-item" name="username"></div>
				<div>Password</div>
				<div><input class="input-item" name="password" type="password"></div>
				<div><input class="login-btn" type="submit" value="submit"></div>
			</form>
		</div>
	</div>
</jsp:body>
</s:site>