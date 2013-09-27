<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<script>
	var runUrl = "/toolcloud/ctrl/tool/exec";
	<c:if test="${needConsole}">
	var consoleUrl = "/toolcloud/console?ticket=";
	var _console;
	</c:if>
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

		var splitters = [];
		$(".splitter").each(function(idx, item){
			var v = item.value;
			if(v == "\\n"){
				v = "\n";
			}
			var index = item.id;
			v = index + "=" + v;
			splitters.push(v);
		});
		var data = {
				id:"${tool.descriptor.pluginID}",
				input:inputs,
				splitter:splitters,
				ticket:ticket
			};
		$.post(
			runUrl,
			data,
			showResult
		);
		<c:if test="${needConsole}">
		var url = consoleUrl + ticket;
		_console = new Console(url);
		_console.loop();
		</c:if>
	}
	function showResult(results){
		<c:if test="${needConsole}">
		_console.close();
		</c:if>
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
	.detail_head:AFTER{
		content: ".";
		visibility: hidden;
		clear:both;  
  		display:block; 
  		height: 0;
	}
	.input_output{
		border: solid 2px green;
		padding: 6px;
		min-height: 400px;
	}
	
	.input_container{
		border-bottom: solid 2px green;
		padding-bottom: 15px;
	}
	.upload_item{
		display: inline;
	}
	.comments{
		float:left;
		margin-top: 10px;
	}
	.input_item{
		background-color: #EEEEEE;
		margin: 2px;
		padding: 5px
	}
	.output{
		word-wrap: break-word;
	}
	#run_btn{
		float:right; 
		position:relative; 
		top:20px; 
		right:50px;
	}
</style>
</jsp:attribute>
<jsp:body>
	<s:file_upload_head/>
	<div class="screen">
		<div class="detail_head">
			<input type="button" id="run_btn" value="运行" onclick="doSubmit()">
			<s:tool_detail toolDescriptor="${tool.descriptor}"/>
		</div>
		<div class="input_output">
			<div class="input_container">
			<c:forEach var="input" items="${tool.descriptor.inputModel}" varStatus="status">
				<s:input inputModel="${input}" index="${status.index}"></s:input>
			</c:forEach>
			</div>
			<div class="output_container">
			<c:forEach var="output" items="${tool.descriptor.outputModel}">
				<s:output_display outputData="${output}"></s:output_display>
			</c:forEach>
			</div>
			<c:if test="${needConsole}">
				<div style="width:600px; height:300px;">
					<s:console></s:console>
				</div>
			</c:if>
		</div>
		<div class="comments">
			<c:forEach var="c" items="${comments}">
				<s:comment comment="${c}" 
						toolId="${tool.descriptor.pluginID}"
						closable="${canDelComment}" ></s:comment>
			</c:forEach>
			<form method="post" action="/toolcloud/ctrl/tool/comment">
				<textarea name="comment" id="comment" rows="6" cols="60"></textarea>
				<input type="hidden" name="toolId" value="${tool.descriptor.pluginID}">
				<input type="submit" class="btn" value="评论">
			</form>
		</div>
	</div>
</jsp:body>
</s:site>