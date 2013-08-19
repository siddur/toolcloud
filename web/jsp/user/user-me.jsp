<%@page import="java.util.List"%>
<%@page import="siddur.common.security.RequestUtil"%>
<%@page import="siddur.common.security.UserInfo"%>
<%@page import="siddur.common.security.RoleInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	UserInfo user = (UserInfo)session.getAttribute("user");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Me</title>
<style>
	.user_item{
		padding: 20px;
	}
	
	span.txt{
		padding: 1px 30px;
		color:blue;
	}
</style>
</head>
<body>
<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<form action="/toolcloud/ctrl/user/selfupdate">
			<div class="user_item"><span>USERNAME</span><span class="txt"><%= user.getUsername()%></span></div>
			<div class="user_item"><span>PASSWORD</span><span class="txt"><%= user.getPassword()%></span></div>
			
			<div class="user_item">
				<span>ROLE</span>
				<span class="txt"><%= user.getRole().getRolename()%></span>
			</div>
			
			<div class="user_item">
				<span>EMAIL</span>
				<input value="<%= user.getEmail() == null ? "" : user.getEmail()%>" name="email">
			</div>
			<div class="user_item">
				<span>REALNAME</span>
				<input value="<%= user.getRealname() == null ? "" : user.getRealname()%>" name="realname">
			</div>
			<div class="user_item">
				<span>NICKNAME</span>
				<input value="<%= user.getNickname() == null ? "" : user.getNickname()%>" name="nickname">
			</div>
			<input type="submit" value="update">
		</form>
	</div>
</body>
</html>