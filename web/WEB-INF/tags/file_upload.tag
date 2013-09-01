<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="fieldname" required="true" %>
<%@ attribute name="files" required="false" %>
<%@ attribute name="file" required="false" %>
<%@ attribute name="isImage" required="false"%>
<%@ attribute name="displayname" required="false"%>
<c:if test="${isImage == null }">
	<c:set var="isImage" value="false"></c:set>
</c:if>
<c:if test="${displayname == null }">
	<c:set var="displayname" value="Attach Files.."></c:set>
</c:if>
<div class="file-container">
	<div class="btn-container">
		<div class="upload_btn">
			<span class="ui-icon ui-icon-folder-open" style="float:left; position: relative; top:-2px;"></span>
			<span style="font-size:12px; color:blue;float:left ;width: 90px; ">${displayname}</span>
		</div>
		
		<input type="file" class="file_input" id='${fieldname}' name="file" onchange="selectFile(this, ${isImage})">
	</div>
	<c:forEach var="f" items="${files}">
		<div class="file_item">
			<input type='hidden' name='file' value="${f}">
			<c:choose>
				<c:when test="${isImage}">
					<img src="/toolcloud/file/${f}">
				</c:when>
				<c:otherwise>
					<a href="/toolcloud/file/${f}"></a>
				</c:otherwise>
			</c:choose>
			<span class="ui-icon ui-icon-close" onclick="close(this)"></span>
		</div>
	</c:forEach>
	<c:if test="${file != null && file != ''}">
		<div class="file_item">
			<input type='hidden' name='file' value="${file}">
			<c:choose>
				<c:when test="${isImage}">
					<img src="/toolcloud/file/${file}">
				</c:when>
				<c:otherwise>
					<a href="/toolcloud/file/${file}"></a>
				</c:otherwise>
			</c:choose>
			<span class="ui-icon ui-icon-close" onclick="close(this)"></span>
		</div>
	</c:if>
</div>