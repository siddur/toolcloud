<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.add_user .item{
		padding-bottom:10px;
	}
	
	#checkCodeMsg1, #checkCodeMsg2{
		display: none;
	}
	#img{
		position: relative;
		top:9px;
	}
</style>
<script>
	var correctCode = false;

	function preSubmit(){
		var username = document.getElementsByName("username")[0].value;
		var passwords = document.getElementsByName("password");

		if(!username){
			alert("Username is required");	
			return false;
		}

		if(!passwords[0].value){
			alert("Password is required");
			return false;
		}

		if(passwords[0].value != passwords[1].value){
			alert("Passwords are not the same");
			return false;
		}

		if(correctCode == false){
			alert("Captcha is not correct");
			return false;
		}
	}

	function getVerificationCode() {
		ToolCloud.Captcha.change();
		$("#checkCodeMsg1").hide();
		$("#checkCodeMsg2").hide();
		correctCode = false;
	}
	
	function verify(code){
		ToolCloud.Captcha.verify(code, function(isOk){
			if(isOk){
				$("#checkCodeMsg1").css("display", "inline-block");
				$("#checkCodeMsg2").hide();
				correctCode = true;
			}else{
				$("#checkCodeMsg2").css("display", "inline-block");
				$("#checkCodeMsg1").hide();
				correctCode = false;
			}
		})
	}
</script>
</jsp:attribute>
<jsp:body>
	<div class="body">
		<div class="add_user">
			<form method="post" action="${root}/ctrl/user/register">
				<div class="item">
					Captcha
					<input name="authenticode"  onchange="verify(this.value)"/>
					<div id="checkCodeMsg1" class="ui-state-highlight">
						<div class="ui-icon ui-icon-circle-check"></div>
					</div>
					<div id="checkCodeMsg2" class="ui-state-error">
						<div class="ui-icon ui-icon-circle-close"></div>
					</div>
					&nbsp;&nbsp;
					<img height="27px" id="captcha">
					<a href="javascript:getVerificationCode()">换一张</a>
				</div>
				<div class="item">
					Username<span class="asterisk">*</span>:
					<input name="username">
				</div>
				<div class="item">
					Password<span class="asterisk">*</span>:
					<input type="password" name="password">
				</div>
				<div class="item">
					Password again<span class="asterisk">*</span>:
					<input type="password" name="password">
				</div>
				<div class="item">
					Email
					<input name="email">
				</div>
				<div class="item">
					Realname
					<input name="realname">
				</div>
				<div class="item">
					Nickname
					<input name="nickname">
				</div>
				
				<input class="btn" type="submit" value="sign up" onclick="return preSubmit();">
			</form>
		</div>
	</div>
</jsp:body>
</s:site>