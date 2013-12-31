<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="toolDescriptor" type="siddur.tool.core.data.ToolDescriptor"%>
<%@ attribute name="updatable" type="java.lang.Boolean" %>

<div>
	<div>
	<c:if test="${updatable == true}">
		<a href="${root}/ctrl/tool/update?toolId=${toolDescriptor.pluginID }">
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
	<div class="left_float" style="clear: left;">
		${toolDescriptor.description}
	</div>
</div>

