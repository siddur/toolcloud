<%@page import="siddur.common.miscellaneous.Paging"%>
<%@page import="siddur.tool.cloud.action.ToolAction"%>
<%@page import="siddur.tool.core.IToolWrapper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:site>
<jsp:attribute name="headPart">
<style>
	
.ui-icon-search{
	float:right;
	width: 16px;
	height: 16px;
	background-image: url("../../img/search.png");
}

.search {
    background-color: white;
    border-radius: 10px 10px 10px 10px;
    margin-top: 1px;
    padding: 1px 1px 1px 8px;
    width: 200px;
}

.searchInput {
	border: none;
	width: 160px;
	color: #9B9B9B;
}

.searchInput:focus {
	color: black;
}

.search span {
	cursor: pointer;
}

.paging{
	float: right;
}
</style>
<script>
	var url = "${root}/ctrl/tool/";
	
	function changePage(){
		var aUrl = url + "list";
		aUrl += "?pageSize=" + pageSize + "&pageIndex=" + pageIndex;
		location.href = aUrl;
	}
</script>
</jsp:attribute>
<jsp:body>
<div id="middle">
	<div id="left">
		<div style="margin-top: 50px; text-align: center; color: red;">热门推荐</div>
		<div class="accordion">
		<c:forEach var="item" items="${hottest}">
			<div class="item">
				<a href="/${item.descriptor.pluginID}.html">${item.descriptor.pluginName}</a>
			</div>
		</c:forEach>
		</div>
	</div>
	<div id="center">
		<div class="static_window divide1">
			<div class="w_title">
				<s:paging pageIndex="${paging.pageIndex}" 
					pageSize="${paging.pageSize}"
					total="${paging.total}"/>
				<s:serach/>
			</div>
			<div class="w_list">
				<c:forEach var="item" items="${paging.data}">
					<div class="tool_item">
						<a href="/${item.descriptor.pluginID}.html">
							<span class="title">
								<span class="name">[${item.descriptor.pluginName}]</span>
								<span>${item.descriptor.description}</span>
							</span>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
	
	<div id="right" class="accordion">
		<c:forEach var="item" items="${favorite}">
			<div class="item" style="text-align: left; font-size:13px;">
				<a href="/${item.descriptor.pluginID}.html">${item.descriptor.pluginName}</a>
			</div>
		</c:forEach>
	</div>
</div>
</jsp:body>
</s:site>
