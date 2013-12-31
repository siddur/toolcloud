<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.login-panel{
		width: 400px;
		margin: 100px auto;
		-moz-box-shadow: 5px 5px 2px rgba(68, 68, 68, 0.5);
		-webkit-box-shadow: 5px 5px 2px rgba(68, 68, 68, 0.5);
		box-shadow: 5px 5px 2px rgba(68, 68, 68, 0.5);
	}
	.login-head{
		border-radius: 4px 4px 0 0;
		background-color: #515151;
		color:white;
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
		margin-bottom: 18px;
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
		<div class="login-head">登录</div>
		<div class="login-body">
			<form method="post" action="${root}/ctrl/user/login">
				<div style="margin-bottom: 20px;">
					验证码
					<input style="height:21px;" name="authenticode" onchange="TC.Captcha.verify(this.value)"/>
					<img id="captcha" align="top" border="1" onclick="TC.Captcha.change()">
				</div>
				<div>用户名</div>
				<div><input class="input-item" name="username"></div>
				<div>密码</div>
				<div><input class="input-item" name="password" type="password"></div>
				<div><input class="login-btn" type="submit" value="submit" onclick="return TC.Captcha.verify(this.form.authenticode.value)"></div>
			</form>
		</div>
	</div>
</jsp:body>
</s:site>