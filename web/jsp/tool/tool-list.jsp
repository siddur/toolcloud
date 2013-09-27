<%@page import="siddur.common.miscellaneous.Paging"%>
<%@page import="siddur.tool.cloud.action.ToolAction"%>
<%@page import="siddur.tool.core.IToolWrapper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:site>
<link rel="stylesheet" type="text/css" href="/toolcloud/css/tag.css" />
<style>
	.list{
		margin:7px;
		width:225px;
		height:150px;
		overflow: hidden;
		background-color: #CDCDCD;
		box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5) ;
		padding:3px;
	}
	
	.list:hover{
		cursor:pointer;
	}
	
	.paging{
		clear:both;
	}
}
</style>

<div class="screen">
	<c:forEach var="t" items="${paging.data}">
		<div class="list left_float" onclick="location.href='/toolcloud/ctrl/tool/detail?toolId=${t.descriptor.pluginID}';">
			<s:tool_detail updatable="${editable}" toolDescriptor="${t.descriptor}"></s:tool_detail>
		</div>
	</c:forEach>
	<s:paging pageIndex="${paging.pageIndex}" pageSize="${paging.pageSize}" total="${paging.total}"/>
</div>
</s:site>
