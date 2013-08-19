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
<script>
	var runUrl = "/toolcloud/ctrl/tool/exec";
	<%if(!needConsole){%>
	var consoleUrl = "/toolcloud/console?ticket=";
	var _console;
	<%}%>
	function doSubmit(){
		if(!validate()) return;
		$("#run_btn").attr("disabled", true);
		
		var ticket = new Date().getTime();
		var inputs = [];
		$("[name='input']").each(function(idx, item){
			var v = item.value;
			if(item.type == 'checkbox' && item.checked == false){
				v = "";
			}
			inputs.push(v);
		});
		var data = {
				id:"<%=tool.getDescriptor().getPluginID()%>",
				input:inputs,
				ticket:ticket
			};
		$.post(
			runUrl,
			data,
			showResult
		);
		<%if(!needConsole){%>
		var url = consoleUrl + ticket;
		_console = new Console(url);
		_console.loop();
		<%}%>
	}
	function showResult(results){
		<%if(!needConsole){%>
		_console.close();
		<%}%>
		if(results){
			var r = eval(results);
			$(".output").each(function(idx, item){
				var value = r[idx];
				item.populate(value);
			});
		}
		$("#run_btn").attr("disabled", false);
	}
</script>
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
		min-width: 800px;
	}
	
	.input_container{
		border-bottom: solid 2px green;
		margin-bottom: 20px;
		padding-bottom: 20px;
	}
	.tool_item{
		float:left;
	}
	.upload_item{
		display: inline;
	}
	.comments{
		float:left;
		position: relative;
		left:-20px;
	}
	.input_item{
		background-color: #EEEEEE;
		margin: 2px;
		padding: 5px
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<s:file_upload_common/>
	<div class="body">
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
			<div class="input_container">
			<%
				DataTemplate[] inputs = tool.getDescriptor() == null ? null : tool.getDescriptor().getInputModel();
					if(inputs != null){
						for(DataTemplate input : inputs){
			%>
					<s:input inputModel="<%=input%>"></s:input>
			<%
				}
					}
			%>
				<input type="button" id="run_btn" value="run"  onclick="doSubmit()" style="float:right; position: relative; bottom: 30px; right: 300px;">
			</div>
			<div class="output_container">
			<%
				DataTemplate[] outputs = tool.getDescriptor() == null ? null : tool.getDescriptor().getOutputModel();
					if(outputs != null){
						for(DataTemplate output : outputs){
			%>
						<s:output_display outputData="<%=output%>"></s:output_display>
					<%
					}
				}
			%>	
			</div>
			<%if(!needConsole){ %>
				<div style="width:600px; height:300px;">
					<s:console></s:console>
				</div>
			<%} %>
		</div>
	</div>
</body>
</html>