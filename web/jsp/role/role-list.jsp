<%@page import="siddur.common.security.RoleInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<RoleInfo> list = (List<RoleInfo>)request.getAttribute("list");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Role List</title>
<style>
	.role-item{
		margin: 15px;
		float:left;
	}
	.body a:LINK {
		text-decoration: none;
	}
	.add{
		clear: both;
		padding-bottom: 20px;
		border-bottom: dotted 1px blue;
	}
}
</style>
</head>
<body>
<%@include file="/jsp/common/manage.jsp" %>
	<div class="crumb">
		<%= request.getAttribute("crumb")%>
	</div>
	<div class="body">
		<div class="add">
			<a class="btn1" href="/toolcloud/ctrl/role/detail">New Role</a>
		</div>
		<%if(list != null){
			for(RoleInfo r : list){
		%>
			<div class="role-item">
				<a class="txt" href="/toolcloud/ctrl/role/detail?roleId=<%=r.getRoleId()%>"><%=r.getRolename() %></a>
			</div>
		<%	}
		}
		%>
	</div>
</body>
</html>