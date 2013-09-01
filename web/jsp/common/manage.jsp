<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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

<%@include file="/jsp/common/head.jsp" %>
<div class="left_bar">
	<a href="/toolcloud/ctrl/user/list">User Manage</a><br>
	<a href="/toolcloud/ctrl/role/list">Role Manage</a><br>
	<a href="/toolcloud/ctrl/tool/mine">Tool Manage</a><br>
</div>
