<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" tagdir="/WEB-INF/tags" %>
<%@ attribute name="tools" type="java.util.List"%>
<%@ attribute name="title" type="java.lang.String"%>


<div class="tool_list">
	<div class="home_title">
		<b>${title }</b>
		<div style="float:right;">
			<a href="/toolcloud/ctrl/tool/list">更多</a>
		</div>
	</div>
	<c:forEach var="item" items="${tools}">
		<s:tool_item toolDescriptor="${item.descriptor}"></s:tool_item>
	</c:forEach>
	<div style="clear:left;"></div>
</div>

