<%@tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@ attribute name="titlePart" fragment="true"%>
<%@ attribute name="headPart" fragment="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<c:choose>
	<c:when test="${not empty titlePart}"><jsp:invoke
			fragment="titlePart"></jsp:invoke></c:when>
	<c:otherwise>
		<title>ToolCloud</title>
	</c:otherwise>
</c:choose>
<meta http-equiv="Content-Language" content="zh-CN" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<script type="text/javascript" src="${root}/jquery/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${root}/js/toolcloud.js"></script>
<link rel="stylesheet" type="text/css" href="${root}/css/toolcloud.css" />
<jsp:invoke fragment="headPart"></jsp:invoke>
</head>
<body>
<div id="top">
	<div id="logo">ToolCloud</div>
	<ul id="menu">
		<li <c:if test="${empty cp || cp == 0}">class="selected"</c:if>><span onclick="location.href='${root}/'">工具云</span></li>
		<li <c:if test="${cp == 1}">class="selected"</c:if>><span>在线工具</span></li>
		<li <c:if test="${cp == 2}">class="selected"</c:if>><span>在线APP</span></li>
		<li <c:if test="${cp == 3}">class="selected"</c:if>><span>开源信息</span></li>
		<li <c:if test="${cp == 4}">class="selected"</c:if>><span>需求讨论</span></li>
		<li <c:if test="${cp == 5}">class="selected"</c:if>>
			<span>博客</span>
			<div class="sub_menu">
				<div>abcd</div>
				<div>12345</div>
			</div>
		</li>
		<li <c:if test="${cp == 6}">class="selected"</c:if>>
			<span>文档</span>
			<div class="sub_menu">
				<div>abcd</div>
				<div>12345</div>
			</div>
		</li>
		<li <c:if test="${cp == 7}">class="selected"</c:if>>
			<span>下载</span>
			<div class="sub_menu">
				<div>abcd</div>
				<div>12345</div>
			</div>
		</li>
	</ul>
	<div id="account">
		<span id="username">${user.name}</span>
		<div class="light_window">
			<c:choose>
				<c:when test="${not empty user}">
					<a class="item" href="${root}/ctrl/user/me">个人信息</a>
					<a class="item" href="${root}/ctrl/user/logout">退出</a>
				</c:when>
				<c:otherwise>
					<a class="item" href="${root}/jsp/user/login.jsp">登录</a>
					<a class="item" href="${root}/jsp/user/register.jsp">注册</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>


<jsp:doBody />
</body>
</html>
