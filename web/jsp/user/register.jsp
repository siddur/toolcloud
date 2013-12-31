<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.register-panel{
		margin: 100px auto;
		width: 400px;
		-moz-box-shadow: 5px 5px 2px rgba(68, 68, 68, 0.5);
		-webkit-box-shadow: 5px 5px 2px rgba(68, 68, 68, 0.5);
		box-shadow: 5px 5px 2px rgba(68, 68, 68, 0.5);
	}
	.register-head{
		border-radius: 4px 4px 0 0;
		background-color: #515151;
		color:white;
		font-weight: bolder;
		padding:10px;
	}
	.register-body{
		border-radius: 0 0 4px 4px;
		background-color:#D0E9C6;
		padding:25px;	
	}
	.register-panel .item{
		margin-bottom: 10px;
		width:98%;
		height:21px;
	}
	
	.register-panel .item span{
		display: inline-block;
		text-align: right;
		width: 80px;
	}
</style>
<script>
	var correctCode = false;

	function checkCode(value){
		correctCode = TC.Captcha.verify(value);
	}
	
	function preSubmit(){
		var username = document.getElementsByName("username")[0].value;
		var passwords = document.getElementsByName("password");

		if(!username){
			showErr("用户名不能为空");	
			return false;
		}

		if(!passwords[0].value){
			showErr("密码不能为空");
			return false;
		}

		if(passwords[0].value != passwords[1].value){
			showErr("两次密码输入不一致");
			return false;
		}

		if(correctCode == false){
			showErr("验证码不正确");
			return false;
		}

		return true;
	}


	function showErr(msg){
		TC.tip.err(msg);
	};
</script>
</jsp:attribute>
<jsp:body>
	<div class="register-panel">
		<div class="register-head">注册</div>
		<div class="register-body">
		<form method="post" action="${root}/ctrl/user/register">
			<div class="item">
				<span>验证码</span>
				<input name="authenticode"  onchange="checkCode(this.value)"/>
				<img id="captcha" align="top" border="1" onclick="TC.Captcha.change()">
			</div>
			<div class="item">
				<span>昵称</span>
				<input name="nickname">
			</div>
			<div class="item">
				<span>用户名<sup class="asterisk">*</sup></span>
				<input name="username">
			</div>
			<div class="item">
				<span>登录密码<sup class="asterisk">*</sup></span>
				<input type="password" name="password">
			</div>
			<div class="item">
				<span>密码确认<sup class="asterisk">*</sup></span>
				<input type="password" name="password">
			</div>
			<div class="item">
				<span>邮箱</span>
				<input name="email">
			</div>
			<div class="item">
				<span>真实姓名</span>
				<input name="realname">
			</div>
			<div style="padding-left: 250px">
				<input type="submit" value="提交注册" onclick="return preSubmit();">
			</div>
		</form>
		</div>
	</div>
</jsp:body>
</s:site>