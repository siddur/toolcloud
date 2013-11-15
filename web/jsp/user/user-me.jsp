<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.user_item{
		padding: 20px;
	}
	
	span.txt{
		padding: 1px 30px;
		color:blue;
	}
</style>
</jsp:attribute>
<jsp:body>
	<div class="body">
		<form action="${root}/ctrl/user/selfupdate">
			<div class="user_item">
				<span>USERNAME</span>
				<span class="txt">${user.username }</span>
				<a href="${root}/ctrl/user/updatepwd">修改密码</a>
			</div>
			
			<div class="user_item">
				<span>ROLE</span>
				<span class="txt">${user.role.rolename }</span>
			</div>
			
			<div class="user_item">
				<span>EMAIL</span>
				<input value="${user.email }" name="email">
			</div>
			<div class="user_item">
				<span>REALNAME</span>
				<input value="${user.realname }" name="realname">
			</div>
			<div class="user_item">
				<span>NICKNAME</span>
				<input value="${user.nickname }" name="nickname">
			</div>
			<input type="submit" value="update">
		</form>
	</div>
</jsp:body>
</s:site>