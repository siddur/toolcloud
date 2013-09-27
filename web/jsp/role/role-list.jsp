<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:manage>
<jsp:attribute name="headPart">
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
</jsp:attribute>
<jsp:body>
	<div class="crumb">
		${crumb}
	</div>
	<div class="body">
		<div class="add">
			<a class="btn1" href="/toolcloud/ctrl/role/detail">New Role</a>
		</div>
		<c:forEach var="r" items="${list}">
			<div class="role-item">
				<a class="txt" href="/toolcloud/ctrl/role/detail?roleId=${r.roleId }">${r.rolename }</a>
			</div>
		</c:forEach>
	</div>
</jsp:body>
</s:manage>