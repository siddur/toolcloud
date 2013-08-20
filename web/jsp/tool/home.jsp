<%@ taglib prefix="s" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>home</title>
<style>
.tool_item {
	width:100%;
}
.tool_item .title{
	width: 80%;
	overflow: hidden;
	text-overflow: ellipsis;
}
.tool_list ul{
	padding-left: 15px;
}
.tool_item .catalog{
	float:right;
}
.latest_list{
	width:45%;
	float:left;
}
.hottest_list{
	width:45%;
	float:right;
}
.right{
	float:right;
	width:20%;
	text-align: right;
}
.home_title{
	font-size:smaller;
	background-color: #EEEEEE;
	padding:3px;
	border: 1px solid #CFCFCF;
}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<div style="width:100%">
			<div style="width:80%; float:left;">
				<div style="width:100%;">
					<div class="latest_list">
						<s:tool_list title="最新工具" tools="${latest}"></s:tool_list>
					</div>
					<div class="hottest_list">
						<s:tool_list title="最热门工具" tools="${hottest}"></s:tool_list>
					</div>
				</div>
				<div style="clear:left;">
					<div class="home_title">
						<b>需求讨论</b>
						<a href="/toolcloud/ctrl/query/ask">我来发布需求</a>
						<div style="float:right;">
							<a href="/toolcloud/ctrl/query/list">更多</a>
						</div>
					</div>
					<c:forEach items="${queries}" var="item">
						<div class="query_item">
							<a href="/toolcloud/ctrl/query/detail?id=${item.id}">
								<span>${item.title}</span>
								<span style="color:#BBBBBB">
									<f:formatDate value="${item.publishAt}" pattern="yyyy-MM-dd hh:mm"/>
								</span>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="right">
				<div style="float:right;" class="home_title">
					您使用过的：
				</div>
				<div style="clear:right;"></div>
				<c:forEach items="${favorite}" var="item">
					<div class="favorite_item">
						<a href="/toolcloud/ctrl/tool/detail?toolId=${item.descriptor.pluginID }">
							${item.descriptor.pluginName }
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
</html>