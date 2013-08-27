<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="toolDescriptor" type="siddur.tool.core.data.ToolDescriptor"%>
<%@ attribute name="updatable" type="java.lang.Boolean" %>

<div class="left_float" style="padding-right:5px; padding-bottom:5px;">
	<a href="/toolcloud/ctrl/tool/detail?toolId=${toolDescriptor.pluginID }">
		<img src="${toolDescriptor.displayIcon}" style="border:1px #333333 inset;"/>
	</a>
</div>
<div class="left_float">
	<div>
		<b>${toolDescriptor.pluginName}</b>
		<c:if test="${updatable == true}">
			<a href="/toolcloud/ctrl/tool/update?toolId=${toolDescriptor.pluginID }">update</a>
		</c:if>
	</div>
	<div>
		<fmt:formatDate type="date" pattern="yyyy-MM-dd hh:mm:ss" value="${toolDescriptor.publishAt}"/>
	</div>
	<div>
		${toolDescriptor.catalog}
	</div>
</div>

<div class="left_float" style="clear: left;">
	${toolDescriptor.description}
</div>
