<%@page import="siddur.common.security.Permission"%>
<%@page import="siddur.common.security.RequestUtil"%>
<%@page import="siddur.common.security.UserInfo"%>
<%@page import="java.util.List"%>
<%@page import="siddur.common.security.RoleInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	UserInfo user = (UserInfo)request.getAttribute("user");
	boolean updateMsg = RequestUtil.hasPerm(request, Permission.USER_EDIT);
	boolean updateRole = RequestUtil.hasPerm(request, Permission.ROLE_EDIT);
	boolean canUpdate = updateMsg || updateRole;
	List<RoleInfo> roles = (List<RoleInfo>)request.getAttribute("roles");
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
<%@include file="/jsp/common/manage.jsp" %>
	<div class="crumb">
		<%= request.getAttribute("crumb")%>
	</div>
	<div class="body">
		<form action="/toolcloud/ctrl/user/update">
			<input type="hidden" name="userId" value="<%= user.getUserId()%>">
			<div class="user_item"><span>USERNAME</span><span class="txt"><%= user.getUsername()%></span></div>
			<div class="user_item"><span>PASSWORD</span><span class="txt"><%= user.getPassword()%></span></div>
			
			<div class="user_item">
				<span>ROLE</span>
				<% if(!updateRole){ %>
					<span class="txt"><%= user.getRole().getRolename()%></span>
				<%}else{ %>
					<select name="role">
						<%
							for(RoleInfo r : roles){
						%>
								<option value="<%= r.getRoleId()%>" <%if(r.getRoleId() == user.getRole().getRoleId()){ %>selected="selected"<%} %>><%= r.getRolename()%></option>
						<%
							}
						%>
					</select>
				<%} %>
			</div>
			
			<div class="user_item">
				<span>EMAIL</span>
				<% if(!updateMsg){ %>
					<span class="txt"><%= user.getEmail() == null ? "" : user.getEmail()%></span>
				<%}else{ %>
					<input value="<%= user.getEmail() == null ? "" : user.getEmail()%>" name="email">
				<%} %>
			</div>
			
			<div class="user_item">
				<span>REALNAME</span>
				<% if(!updateMsg){ %>
					<span class="txt"><%= user.getRealname() == null ? "" : user.getRealname()%></span>
				<%}else{ %>
					<input value="<%= user.getRealname() == null ? "" : user.getRealname()%>" name="realname">
				<%} %>
			</div>
			<div class="user_item">
				<span>NICKNAME</span>
				<% if(!updateMsg){ %>
					<span class="txt"><%= user.getNickname() == null ? "" : user.getNickname()%></span>
				<%}else{ %>
					<input value="<%= user.getNickname() == null ? "" : user.getNickname()%>" name="nickname">
				<%} %>
			</div>
			<%if(canUpdate){ %>
				<input type="submit" value="update">
			<%} %>
		</form>
	</div>
</body>
</html>