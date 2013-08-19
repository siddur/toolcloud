<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="fieldname" required="true"%>
<%@ attribute name="displayname" required="true"%>
<%@ attribute name="isImg" required="false" type="java.lang.Boolean"%>
<%@ attribute name="url" required="false"%>
<c:choose>
	<c:when test="${url == null }">
		<c:set var="url" value=""></c:set>
	</c:when>
</c:choose>

<div class='upload_item'>
	<button class="btn1" onclick='upload(this<c:if test="${isImg==true}">, true</c:if>); return false;'>
		<span class="left_float ui-icon ui-icon-folder-open"></span>
		<span>${displayname}</span>
	</button>
	<input type='hidden' name='${fieldname}' value="${url}">
	<c:choose>
		<c:when test="${true == isImg}">
			<img src='${url}' style='display:block'>
		</c:when>
		<c:otherwise>
			<span style='margin-left:20px;padding: 0 5px;background-color:#FCF8E3;'>uploaded file</span>
		</c:otherwise>
	</c:choose>
</div>