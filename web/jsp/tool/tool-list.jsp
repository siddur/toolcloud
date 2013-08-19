<%@page import="siddur.common.security.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>home</title>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<a class="add_tool" href="/toolcloud/jsp/tool/tool-add.jsp"><b>Add a tool</b></a>
		<br/>
		<br/>
		<br/>
		<%@include file="/jsp/tool/_tool-list.jsp" %>
	</div>
</body>
</html>