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
	.paging{
		clear:both;
	}
}
</style>
<script>
	var isMine = ${mine};
	var url = "${root}/ctrl/tool/";
	
	function changePage(){
		var aUrl = url;
		if(isMine){
			aUrl += "mine";
		}else{
			aUrl += "list";
		}
		aUrl += "?pageSize=" + pageSize + "&pageIndex=" + pageIndex;
		location.href = aUrl;
	}
</script>
</jsp:attribute>
<jsp:body>
<div id="middle">
	<div id="left">
		
	</div>
	<div id="center">
		<div class="static_window divide1">
			<div class="w_title">
				<s:serach></s:serach>
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
			<s:paging pageIndex="${paging.pageIndex}" pageSize="${paging.pageSize}" total="${paging.total}"/>
		</div>
	</div>
</div>
</jsp:body>
</s:site>
