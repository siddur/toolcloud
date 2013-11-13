<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:manage>
<jsp:attribute name="headPart">
<style>
	.perm-item{
		padding: 2px;
		border-bottom:dotted 1px grey;
		margin: 5px;
	}
</style>
</jsp:attribute>
<jsp:body>
	<div class="crumb">
		${crumb}
	</div>
	<div class="body">
		<div class="left_float" style="width:500px;">
		<c:choose>
			<c:when test="${not empty role}">
				<form method="post" action="${root}/ctrl/role/update">
				    <input type="hidden" name="roleId" value="${role.roleId}">
				    <c:forEach var="perm" items="${allPerms}" varStatus="status">
				    	<div class="perm-item">
					    	<input type="checkbox" value="${status.index}" name="perm" <c:if test="${perms[status.index]}">checked="checked"</c:if>>
					    	<label>${perm.fullName}</label>
					    </div>
					</c:forEach>
					<input class="btn" type="submit" value="save">
				</form>
			</c:when>
			<c:otherwise>
				<form method="post" action="${root}/ctrl/role/add">
					<label>rolename:</label><input type="text" name="rolename">
					<c:forEach var="perm" items="${allPerms}" varStatus="status">
					    <div class="perm-item">
					    	<input type="checkbox"  value="${status.index}" name="perm">
					    	<label>${perm.fullName}</label>
					    </div>
					</c:forEach>
				  	<input class="btn" type="submit" value="save">
				</form>
			</c:otherwise>
		</c:choose>
		</div>
	</div>
</jsp:body>
</s:manage>