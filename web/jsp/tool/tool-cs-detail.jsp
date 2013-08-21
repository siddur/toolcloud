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
<%
	boolean needConsole = !ConsoleTool.class.isAssignableFrom(tool.getToolClass());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/toolcloud/jquery/js/jquery-1.9.1.js"></script>
<style>
	.tool_description, .input_output{
		float:left;
	}
	
	.tool_description{
		width: 300px;
	}
	
	.input_output{
		border-left: solid 2px green;
		padding-left: 10px;
		margin-left:10px;
		min-height: 400px;
		width: 695px;
	}
	.tool-container{
		width:100%;
		height:100%;
	}
	.comments{
		float:left;
		position: relative;
		left:-20px;
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<s:file_upload_head/>
	<div class="screen">
		<div class="tool_description">
			<s:tool_detail toolDescriptor="<%=tool.getDescriptor()%>"/>
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
					<textarea name="comment" id="comment" rows="6" cols="35"></textarea>
					<input type="hidden" name="toolId" value="<%=tool.getDescriptor().getPluginID()%>">
					<input type="submit" class="btn" value="say">
				</form>
			</div>
			
		</div>
		<div class="input_output">
			<iframe id="tool-container" src="file/${toolFile}"></iframe>
		</div>
	</div>
</body>
</html>