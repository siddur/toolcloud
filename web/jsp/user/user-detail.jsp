<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:manage>
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
	<div class="crumb">
		${crumb}
	</div>
	<div class="body">
		<form action="/toolcloud/ctrl/user/update">
			<input type="hidden" name="userId" value="${user.userId}">
			<div class="user_item"><span>USERNAME</span><span class="txt">${user.username}</span></div>
			<div class="user_item"><span>PASSWORD</span><span class="txt">${user.password}</span></div>
			
			<div class="user_item">
				<span>ROLE</span>
				<c:choose>
				<c:when test="${!updatable}">
					<span class="txt">${user.role.rolename}</span>
				</c:when>
				<c:otherwise>
					<select name="role">
						<c:forEach var="r" items="${roles}">
							<option value="${r.roleId}" <c:if test="${u.role.roleId == r.roleId}">selected="selected"</c:if>>${r.rolename}</option>
						</c:forEach>
					</select>
				</c:otherwise>
				</c:choose>
			</div>
			
			<div class="user_item">
				<span>EMAIL</span>
				<c:choose>
				<c:when test="${!updatable}">
					<span class="txt">${user.email}</span>
				</c:when>
				<c:otherwise>
					<input value="${user.email}" name="email">
				</c:otherwise>
				</c:choose>
			</div>
			
			<div class="user_item">
				<span>REALNAME</span>
				<c:choose>
				<c:when test="${!updatable}">
					<span class="txt">${user.realname}</span>
				</c:when>
				<c:otherwise>
					<input value="${user.realname}" name="realname">
				</c:otherwise>
				</c:choose>
			</div>
			<div class="user_item">
				<span>NICKNAME</span>
				<c:choose>
				<c:when test="${!updatable}">
					<span class="txt">${user.nickname}</span>
				</c:when>
				<c:otherwise>
					<input value="${user.nickname}" name="nickname">
				</c:otherwise>
				</c:choose>
			</div>
			<c:if test="${updatable}">
				<input type="submit" value="update">
			</c:if>
		</form>
	</div>
</jsp:body>
</s:manage>