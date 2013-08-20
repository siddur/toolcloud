<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="fieldname" required="false" %>
<%@ attribute name="files" required="false" %>
<%@ attribute name="isImage" required="false"%>
<%@ attribute name="displayname" required="false"%>
<c:if test="${fieldname == null }">
	<c:set var="fieldname" value="file"></c:set>
</c:if>
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
		
		<input type="file" class="file_input" name='${fieldname}' onchange="selectFile(this, ${isImage})">
	</div>
	<c:forEach var="f" items="${files}">
		<div class="file_item">
			<input type='hidden' name='file' value="${f}">
			<c:choose>
				<c:when test="${isImage}">
					<a href="/pacquery/ctrl/fileio/download?path=${f}"></a>
				</c:when>
				<c:otherwise>
					<img src="/pacquery/ctrl/fileio/download?path=${f}">
				</c:otherwise>
			</c:choose>
			<span class="ui-icon ui-icon-close" onclick="close(this)"></span>
		</div>
	</c:forEach>
</div>