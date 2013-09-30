<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="toolDescriptor" type="siddur.tool.core.data.ToolDescriptor"%>
<%@ attribute name="updatable" type="java.lang.Boolean" %>

<div class="left_float" style="padding-right:5px; padding-bottom:5px;">
	<c:choose>
		<c:when test="${empty toolDescriptor.icon}">
			<div class="tool_logo"><span>${toolDescriptor.pluginName}</span></div>
		</c:when>
		<c:otherwise>
			<img height="64" width="64" src="${toolDescriptor.displayIcon}" style="border:1px #333333 inset;"/>
		</c:otherwise>
	</c:choose>
</div>
<div class="left_float">
	<div>
	<c:if test="${updatable == true}">
		<a href="/toolcloud/ctrl/tool/update?toolId=${toolDescriptor.pluginID }">
			<b>${toolDescriptor.pluginName}</b>
		</a>
	</c:if>
	<c:if test="${updatable != true}">
		<b>${toolDescriptor.pluginName}</b>
	</c:if>
	</div>
	<div>
		<fmt:formatDate type="date" pattern="yyyy-MM-dd" value="${toolDescriptor.publishAt}"/>
	</div>
	<div>
		${toolDescriptor.catalog}
	</div>
</div>

<div class="left_float" style="clear: left;">
	${toolDescriptor.description}
</div>
