<%@ taglib prefix="s" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:site>
<jsp:attribute name="headPart">
<meta name="keywords" content="toolcloud， 在线工具， 工具云， 工具集合， online tools"/>
<meta name="description" content="toolcloud是一个上传、管理和运行工具以及以多种方式显示运行结果的服务平台"/>
<style>
.tool_item {
	float:left;
	width:100%;
	padding-top:5px;
	border-bottom:dashed 1px grey;
}
.screen .ui-icon{
	float:left;
}
.tool_item .title{
	float:left;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
    width: 95%;
}
.tool_list, .favorite, .comment_list{
	border: 1px solid #CFCFCF;
	margin-bottom: 20px;
}
.latest_list{
	width:390px;
	float:left;
}
.hottest_list{
	width:390px;
	float:right;
}
.right{
	float:right;
	width:180;
}
.home_title{
	font-size:smaller;
	background-color: #EEEEEE;
	padding:3px;
	border: 1px solid #CFCFCF;
}
.favorite_item{
	padding:3px;
	width:120px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
.query_item{
	padding:3px;
	border-bottom: dashed 1px grey;
}
.query_item a{
	text-decoration: none;
}
</style>
</jsp:attribute>
<jsp:body>
	<div class="screen">
		<div style="width:800px; float:left;">
			<div style="width:100%;">
				<div class="latest_list">
					<s:tool_list title="最新工具" tools="${latest}"></s:tool_list>
				</div>
				<div class="hottest_list">
					<s:tool_list title="最热门工具" tools="${hottest}"></s:tool_list>
				</div>
			</div>
			<div class="comment_list" style="clear:left;">
				<div class="home_title">
					<b>需求讨论</b>
					<a href="${root}/ctrl/query/ask">我来发布需求</a>
					<div style="float:right;">
						<a href="${root}/ctrl/query/list">更多</a>
					</div>
				</div>
				<c:forEach items="${queries}" var="item">
					<div class="query_item">
						<a href="${root}/ctrl/query/detail?id=${item.id}">
							<span class="ui-icon ui-icon-document"></span>
							<span>${item.title}</span>
							<span style="color:#BBBBBB; float:right;">
								<f:formatDate value="${item.publishAt}" pattern="yyyy-MM-dd HH:mm"/>
							</span>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
		
		
		<div class="right">
			<div style="margin-bottom:10px;"><input type="button" class='btn' value="发布工具" onclick="location.href='${root}/ctrl/tool/toadd'"></div>
			<div class="favorite">
				<div class="home_title">
					您使用过的：
				</div>
				<div style="clear:right;"></div>
				<c:forEach items="${favorite}" var="item">
					<div class="favorite_item">
						<a href="${root}/ctrl/tool/detail?toolId=${item.descriptor.pluginID }">
							<span class="ui-icon ui-icon-gear"></span>
							${item.descriptor.pluginName }
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</jsp:body>
</s:site>