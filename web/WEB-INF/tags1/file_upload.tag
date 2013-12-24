<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="fieldname" required="true" %>
<%@ attribute name="files" required="false" %>
<%@ attribute name="file" required="false" %>
<%@ attribute name="isImage" required="false"%>
<%@ attribute name="displayname" required="false"%>

<c:set var="fileSize" value="10K"></c:set>

<c:if test="${isImage == null }">
	<c:set var="isImage" value="false"></c:set>
	<c:set var="fileSize" value="5M"></c:set>
</c:if>
<c:if test="${displayname == null }">
	<c:set var="displayname" value="Attach Files.."></c:set>
</c:if>
<div class="file-container">
	<div class="btn-container">
		<div class="upload_btn">
			<span class="ui-icon ui-icon-folder-open" style="float:left; position: relative; top:-2px;"></span>
			<span style="font-size:12px; color:blue;float:left ;width: 90px; "><font color='red'>[&lt;${fileSize}]</font>${displayname}</span>
		</div>
		
		<input type="file" class="file_input" id='${fieldname}' name="file" onchange="selectFile(this, ${isImage})">
	</div>
	<c:forEach var="f" items="${files}">
		<div class="file_item">
			<input type='hidden' name='${fieldname}' value="${f}">
			<c:choose>
				<c:when test="${isImage}">
					<img src="${root}/file/${f}">
				</c:when>
				<c:otherwise>
					<a href="${root}/file/${f}"></a>
				</c:otherwise>
			</c:choose>
			<span class="ui-icon ui-icon-close" onclick="close(this)"></span>
		</div>
	</c:forEach>
	<c:if test="${not empty file}">
		<div class="file_item">
			<input type='hidden' name='${fieldname}' value="${file}">
			<c:choose>
				<c:when test="${isImage}">
					<img src="${root}/file/${file}">
				</c:when>
				<c:otherwise>
					<a href="${root}/file/${file}"></a>
				</c:otherwise>
			</c:choose>
			<span class="ui-icon ui-icon-close" onclick="close(this)"></span>
		</div>
	</c:if>
</div>