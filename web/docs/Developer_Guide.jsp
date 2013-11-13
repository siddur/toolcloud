<%@ taglib prefix="s" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:site>
	<div style="width: 960px; margin:0 auto">
		<h4><a href="${root}/docs/Overview.jsp">ToolCloud概述</a></h4>
		<h4><a href="${root}/docs/Developer_Guide.jsp">ToolCloud开发者指南</a></h4>
		<h4><a href="${root}/docs/User_Guide.jsp">ToolCloud用户指南</a></h4>
		<hr>
		<div>
			<jsp:include page="Developer_Guide.htm"></jsp:include>
		</div>
	</div>
<div></div>
</s:site>

