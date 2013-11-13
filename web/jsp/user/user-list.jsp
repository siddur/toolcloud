<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:manage>
<jsp:attribute name="headPart">
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
<script type="text/javascript" src="${root}/jquery/js/jquery-1.9.1.js"></script>
<script>
	$(document).ready(function(){
		$(".item").click(function(){
			var userId = this.children[6].children[0].children[0].value;
			location.href = "${root}/ctrl/user/detail?userId=" + userId;
		});
	});
</script>
</jsp:attribute>
<jsp:body>
<div class="crumb">
	${crumb}
</div>
<div class="body">
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
	<c:forEach var='u' items='${list}'>
		<tr class="item">
			<td>${u.username}</td>
			<td>${u.password}</td>
			<td>${u.role.rolename }</td>
			<td>${u.email }</td>
			<td>${u.realname }</td>
			<td>${u.nickname }</td>
			<td>
				<form action="${root}/ctrl/user/delete">
					<input type="hidden" name="userId" value="${u.userId }">
					<c:if test="${canDelete}">
						<input class="btn" type="submit" value="delete">
					</c:if>
				</form>
			</td>
		</tr>
	</c:forEach>
	</table>
	<c:if test="${canAdd}">
		<div class="add_user">
			<form method="post" action="${root}/ctrl/user/add">
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
					<c:forEach var="r" items="${roles}">
						<option value="${r.roleId }">
							${r.rolename }
						</option>
					</c:forEach>
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
	</c:if>
</div>
</jsp:body>
</s:manage>