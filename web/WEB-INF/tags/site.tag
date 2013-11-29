<%@tag language="java" pageEncoding="UTF-8"%><%@taglib prefix="s" tagdir="/WEB-INF/tags"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ attribute name="titlePart" fragment="true" %><%@ attribute name="headPart" fragment="true" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<c:choose><c:when test="${title eq '1'}"><jsp:invoke fragment="titlePart"></jsp:invoke></c:when><c:otherwise><title>ToolCloud</title></c:otherwise></c:choose>
	<meta http-equiv="Content-Language" content="zh-CN" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
	<script type="text/javascript" src="${root}/jquery/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${root}/js/search.js"></script>
	<link rel="stylesheet" type="text/css" href="${root}/css/common.css" />
	<link rel="stylesheet" type="text/css" href="${root}/jqueryui/jquery-ui-1.10.3.custom.min.css" />
	<link rel="stylesheet" type="text/css" href="${root}/css/tag.css" />
	<jsp:invoke fragment="headPart"></jsp:invoke>
</head>
<body>
	<div class="header">
		<div class="user">
		<c:choose>
			<c:when test="${user != null }">
				<a href="${root}/ctrl/user/me"><span class="ui-icon ui-icon-person"></span><b>${user.name }</b></a>&nbsp;&nbsp;
				<a href="${root}/ctrl/user/logout"><span class="ui-icon ui-icon-arrowreturnthick-1-w"></span>退出</a>
			</c:when>
			<c:otherwise>
				<a href="${root}/jsp/user/login.jsp"><span class="ui-icon ui-icon-arrowreturnthick-1-e"></span>登录</a>
				<a href="${root}/jsp/user/register.jsp"><span class="ui-icon ui-icon-arrowthickstop-1-e"></span>注册</a>
			</c:otherwise>
		</c:choose>
		</div>
		
		<div class="logo">
			ToolCloud
		</div>
		<div class="toolbar">
			<ul>
				<li>
					<a href="${root}/"><span class="ui-icon ui-icon-home"></span>首页</a>
				</li>
				<c:if test="${not empty user}">
				<li>
					<a href="${root}/ctrl/tool/blocks"><span class="ui-icon ui-icon-gear"></span>管理</a>
				</li>
				</c:if>
				<li>
					<a href="${root}/doc.jsp"><span class="ui-icon ui-icon-help"></span>文档</a>
				</li>
				<li>
					<a href="${root}/download.jsp"><span class="ui-icon ui-icon-arrowthickstop-1-s"></span>下载</a>
				</li>
			</ul>
			<div style="float: right; position: relative; right:50px; margin:4px;">
				<s:basic-serach></s:basic-serach>
			</div>
		</div>
	</div>
	
	<jsp:doBody/>
</body>
</html>
