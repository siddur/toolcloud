<%@page import="siddur.common.security.Permission"%>
<%@page import="siddur.common.security.RequestUtil"%>
<%@page import="siddur.common.security.UserInfo"%>
<%@page import="siddur.common.security.RoleInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	List<UserInfo> list = (List<UserInfo>)request.getAttribute("list");
	List<RoleInfo> roles = (List<RoleInfo>)request.getAttribute("roles");
	boolean canDelete = RequestUtil.hasPerm(request, Permission.USER_DEL);
	boolean canAdd = RequestUtil.hasPerm(request, Permission.USER_ADD);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users</title>
<style>

	table{
		border-spacing: 0;
		width:600px;
	}
	td{
		border-bottom: 1px solid #DDDDDD;
		padding:8px;
	}
	
	tr:first-child{
		background-color:#DDDDDD;
		font-weight: bolder;
		font-size: smaller;
	}
	
	tr.item{
		cursor:pointer;
	}
	
	.add_user{
		margin:20px;
	}
	.add_user div{
		padding-bottom:10px;
	}

</style>
<script type="text/javascript" src="/toolcloud/jquery/js/jquery-1.9.1.js"></script>
<script>
	$(document).ready(function(){
			$(".item").click(function(){
				var userId = this.children[6].children[0].children[0].value;
				location.href = "/toolcloud/ctrl/user/detail?userId=" + userId;
			});
		});
</script>
</head>
<body>

<%@include file="/jsp/common/manage.jsp" %>
	<div class="crumb">
		<%= request.getAttribute("crumb")%>
	</div>
	<div class="body">
	<%if(list != null){%>
		<table>
			<tr>
				<td width="100">USERNAME</td>
				<td width="100">PASSWORD</td>
				<td width="100">ROLE</td>
				<td width="100">EMAIL</td>
				<td width="100">REALNAME</td>
				<td width="100">NICKNAME</td>
				<td width="100">&nbsp;</td>
			</tr>
			<%for(UserInfo u : list){ %>
			<tr class="item">
				<td><%=u.getUsername()%></td>
				<td><%=u.getPassword()%></td>
				<td><%=u.getRole().getRolename()%></td>
				<td><%=u.getEmail()==null ? "" : u.getEmail()%></td>
				<td><%=u.getRealname()==null ? "" : u.getRealname()%></td>
				<td><%=u.getNickname()==null ? "" : u.getNickname()%></td>
				<td>
					<form action="/toolcloud/ctrl/user/delete">
						<input type="hidden" name="userId" value="<%=u.getUserId()%>">
						<%if(canDelete){ %>
							<input class="btn" type="submit" value="delete">
						<%} %>
					</form>
				</td>
				
			</tr>
			<%} %>
		</table>
	<%}%>
	<%if(canAdd){ %>
		<div class="add_user">
			<form method="post" action="/toolcloud/ctrl/user/add">
				<div>
					Username<span class="asterisk">*</span>:
					<input name="username">
				</div>
				<div>
					Password<span class="asterisk">*</span>:
					<input name="password">
				</div>
				<div>
					Role<span class="asterisk">*</span>:
					<select name="role">
						<%
							for(RoleInfo r : roles){
						%>
								<option value="<%= r.getRoleId()%>"><%= r.getRolename()%></option>
						<%
							}
						%>
					</select>
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
				<input class="btn" type="submit" value="save&add">
			</form>
		</div>
	<%}%>
	</div>
</body>
</html>