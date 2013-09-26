<%@page import="siddur.common.security.Permission"%>
<%@page import="siddur.common.security.RequestUtil"%>
<%@page import="siddur.tool.core.ConsoleTool"%>
<%@page import="siddur.common.miscellaneous.Comment"%>
<%@page import="java.util.List"%>
<%@page import="siddur.tool.cloud.action.ToolAction"%>
<%@page import="siddur.tool.core.data.DataTemplate"%>
<%@page import="siddur.tool.core.data.DataType"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<jsp:useBean id="tool" scope="request" type="siddur.tool.core.IToolWrapper"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/toolcloud/jquery/js/jquery-1.9.1.js"></script>
<style>
	.detail_head:AFTER{
		content: ".";
		visibility: hidden;
		clear:both;  
  		display:block; 
  		height: 0;
	}
	.comments{
		float:left;
		margin-top: 10px;
	}
	.tool_container{
		float:left;
		width:100%;
		height:500px;
		border: 1px solid #848877;
		-moz-box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
		-webkit-box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
		box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="screen">
		<div class="detail_head">
			<s:tool_detail toolDescriptor="<%=tool.getDescriptor()%>"/>
		</div>
		<iframe class="tool_container" src="/toolcloud/file/${toolFile}"></iframe>
		<div class="comments">
			<%
				List<Comment> list = (List<Comment>)request.getAttribute("comments");
										for(Comment c: list){
			%>
					<s:comment comment="<%=c%>" 
						toolId="<%=tool.getDescriptor().getPluginID()%>"
						closable="<%=RequestUtil.hasPerm(request, Permission.COMMENT_DEL)%>" ></s:comment>
			<%
				}
			%>
			
			<form method="post" action="/toolcloud/ctrl/tool/comment">
				<textarea name="comment" id="comment" rows="6" cols="60"></textarea>
				<input type="hidden" name="toolId" value="<%=tool.getDescriptor().getPluginID()%>">
				<input type="submit" class="btn" value="评论">
			</form>
		</div>
	</div>
</body>
</html>