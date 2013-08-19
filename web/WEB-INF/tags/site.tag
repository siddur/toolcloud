<%@tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Tool Cloud</title>
	<link rel="stylesheet" type="text/css" href="/toolcloud/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/toolcloud/css/icon_sets.css" />
</head>
<body>
	<div class="header">
		<div class="user">
		<c:choose>
			<c:when test="${user != null }">
				<a href="/toolcloud/ctrl/user/me"><span class="ui-icon ui-icon-person"></span><b>${user.name }</b></a>&nbsp;&nbsp;
				<a href="/toolcloud/ctrl/user/logout"><span class="ui-icon ui-icon-arrowreturnthick-1-w"></span>退出</a>
			</c:when>
			<c:otherwise>
				<a href="/toolcloud/jsp/user/login.jsp"><span class="ui-icon ui-icon-arrowreturnthick-1-e"></span>登录</a>
				<a href="/toolcloud/jsp/user/register.jsp"><span class="ui-icon ui-icon-arrowthickstop-1-e"></span>注册</a>
			</c:otherwise>
		</c:choose>
		</div>
		
		<div class="logo">
			ToolCloud
		</div>
		<div class="toolbar">
			<ul>
				<li>
					<a href="/toolcloud"><span class="ui-icon ui-icon-home"></span>首页</a>
				</li>
				<li>
					<a href="/toolcloud/ctrl/user/list"><span class="ui-icon ui-icon-gear"></span>管理</a>
				</li>
			</ul>
			<div style="float: right; position: relative; right:50px; margin-top:5px;">
				<s:basic-serach></s:basic-serach>
			</div>
		</div>
	</div>
	
	<jsp:doBody/>
</body>
</html>
