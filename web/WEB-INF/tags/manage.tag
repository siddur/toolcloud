<%@tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="headPart" fragment="true" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.left_bar{
		float:left;
		width:180px;
		height:500px;
		border: 1px solid #CCCCCC;
		background-color:#FCF8E3;
	    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	}
	.left_bar a{
		text-decoration: none;
		display: block;
		text-align: center;
		background-color: #BCE8F1;
		border-radius: 4px 4px 4px 4px;
		padding:5px;
		position: relative;
		top:30px;
		left:30px;
		width:100px;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	}
	.body{
		float:left;
	}
</style>
<jsp:invoke fragment="headPart" />
</jsp:attribute>
<jsp:body>
	<div class="left_bar">
		<a href="${root}/ctrl/user/list">User Manage</a><br>
		<a href="${root}/ctrl/role/list">Role Manage</a><br>
		<a href="${root}/ctrl/tool/blocks">Tool Manage</a><br>
	</div>
	<jsp:doBody/>
</jsp:body>
</s:site>
