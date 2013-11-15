<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="menu_item" value="Manage" scope="page"></c:set>
<s:site>
<jsp:attribute name="headPart">
<script>
function checkText() {
	var password = $("#oldPassword").val();
	var paswd = $("#password").val();
	var confirmpwd = $("#cPassword").val();
		if(!password){
			$("#oldPwdSpan").html("The original password can not be empty");
			return false;
		}else if(!paswd){
			$("#npasswordSpan").html("Password can not be empty");
		}else if (paswd != confirmpwd) {
			$("#password").value = "";
			$("#passwordSpan").html("The two passwords you enter is inconsistent");
			return false;
		} else {
			$("#passwordSpan").html("");
				return true;
		}
	
}
function clearSpan(){
	$("#oldPwdSpan").html("");
	$("#npasswordSpan").html("");
	$("#passwordSpan").html("");
}
function submitButton(selectButton) {
	if ('Save' == selectButton) {
		if (checkText() ) {
			pwdForm.action = "${root}/ctrl/user/updatepwd";
			pwdForm.submit();
		}
	}
	else{
		location.href = "${root}/ctrl/user/me";
	}
}
</script>
<style>
	.pac_login_span{
		color:red;
	}
</style>
</jsp:attribute>
<jsp:body>
<div class="body">
	<form id="pwdForm" method="post">
		<div class="pac_query_table">
		 	<table class="table_location" >
		 		<tr>
		 			<td class="pac_query_td_left">Old password:</td>
		 			<td class="pac_query_td_right">
		 				<input type="password" name="oldPassword" id="oldPassword"  onfocus="clearSpan()"/>
		 				<span class="pac_login_span" id="oldPwdSpan">${noMatch }</span>
		 			</td>
		 	    </tr>
		 		<tr>
		 			<td class="pac_query_td_left">New password:</td>
		 			<td class="pac_query_td_right">
		 				<input type="password"  name="newPassword" id="password" onfocus="clearSpan()" />
		 				<span class="pac_login_span" id="npasswordSpan"></span>
		 			</td>
		 	    </tr>
		 		<tr>
		 			<td class="pac_query_td_left">Confirm Password:</td>
		 			<td class="pac_query_td_right"><input type="password" id="cPassword" onfocus="clearSpan()"/>
		 				<span class="pac_login_span" id="passwordSpan"></span>
		 			</td>
		 	    </tr>
		 	    
			   <tr>
					<td class="pac_query_td_left" ><input type="button" value="Save" onclick="submitButton('Save')"/></td>
					<td class="pac_query_td_right"><input type="button" value="Cancel" onClick="submitButton('Cancel')"/></td>
			  </tr>
		 	</table>
		</div>
	 </form>
</div>
</jsp:body>
</s:site>
