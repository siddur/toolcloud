<%@page import="siddur.common.security.PermissionManager"%>
<%@page import="siddur.common.security.Permission"%>
<%@page import="siddur.common.security.RoleInfo"%>
<%@page import="siddur.common.security.PermissionManager.PermissionGroup"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	RoleInfo role = (RoleInfo)request.getAttribute("role");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Role List</title>
<style>
	.perm-item{
		padding: 2px;
		border-bottom:dotted 1px grey;
		margin: 5px;
	}
</style>
</head>
<body>
<%@include file="/jsp/common/manage.jsp" %>
	<div class="crumb">
		<%= request.getAttribute("crumb")%>
	</div>
	<div class="body">
		<div class="left_float" style="width:500px;">
			<%if(role != null) {%>
				<form method="post" action="/toolcloud/ctrl/role/update">
				    <input type="hidden" name="roleId" value="<%=role.getRoleId()%>">
				    <%
				    	PermissionGroup pg = PermissionManager.createPermissionGroup(role.getPermission());
				    	for(int i=0; i < PermissionManager.getAllPermissions().size(); i++){
				    		Permission p = PermissionManager.getAllPermissions().get(i);
				    %>
					    <div class="perm-item">
					    	<input type="checkbox" value="<%=i %>" name="perm" <%if(pg.hasPerm(p)){%>checked="checked"<%}%>>
					    	<label><%=p.getFullName() %></label>
					    </div>
				    <%} %>
			<%}else{ %>
				<form method="post" action="/toolcloud/ctrl/role/add">
					<label>rolename:</label><input type="text" name="rolename">
					 <%for(int i=0; i < PermissionManager.getAllPermissions().size(); i++){
				    		Permission p = PermissionManager.getAllPermissions().get(i);
				     %>
					    <div class="perm-item">
					    	<input type="checkbox" value="<%=i %>" name="perm">
					    	<label><%=p.getFullName() %></label>
					    </div>
				    <%}%>
			<%} %>
			  	<input class="btn" type="submit" value="save">
			</form>
		</div>
	</div>
</body>
</html>