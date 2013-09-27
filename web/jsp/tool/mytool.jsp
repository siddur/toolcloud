<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:manage>
<jsp:attribute name="headPart">
<style>
	.body{
		width:1000px;
	}
	.list{
		margin:7px;
		width:225px;
		height:150px;
		overflow: hidden;
		background-color: #A9B1BD;
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
</jsp:attribute>
<jsp:body>
<div class="crumb">
	${crumb}
</div>
<div class="body">
	<c:forEach var="item" items="${paging.data}">
		<div class="list left_float" onclick="location.href='/toolcloud/ctrl/tool/detail?toolId=${item.descriptor.pluginID}';">
			<s:tool_detail updatable="true" toolDescriptor="${item.descriptor}"></s:tool_detail>
		</div>
	</c:forEach>
		
	<s:paging pageIndex="${paging.pageIndex}" pageSize="${paging.pageSize}" total="${paging.total}"/>
</div>
</jsp:body>
</s:manage>
