<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<meta name="keywords" content="${tool.descriptor.keywords}"/>
<meta name="description" content="${tool.descriptor.description}"/>
<script>
	var runUrl = "/toolcloud/ctrl/tool/exec";
	<c:if test="${needConsole}">
	var consoleUrl = "/toolcloud/console?ticket=";
	var _console;
	</c:if>
	function doSubmit(){
		$(".duplication").remove();
		
		$(".tableHidden").each(function(idx, item){
			item.setValue();
		});
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
		$("#run_btn").attr("disabled", false);
		if(results){
			if(!results.startsWith("error")){
				$("#run_msg").css("color", "green").html("运行成功");
				var r = eval(results);
				$(".output").each(function(idx, item){
					_showEachResult(r[idx], item);
				});
			}else{
				$("#run_msg").css("color", "red").html("运行失败:" + results.substring(5, results.length));
			}
		}
	}
	
	function _showEachResult(value, ui){
		if(value.indexOf("|||") > 0){
			var a = value.split("|||");
			for(var i=0; i<a.length; i++){
				if(i > 0){
					var _ui = $(ui).clone();
					_ui.addClass("duplication");
					var _ui0 = _ui[0];
					_ui0.populate = populate;
					$(ui).after(_ui);
					ui = _ui0;
				}
				ui.populate(a[i]);
			}
		}else{
			ui.populate(value);
		}
	}

	var shown = false;
	function switchConsole(){
		var a = $("#switcher");
		if(!shown){
			shown = true;
			a.next().show();
			a.html("隐藏控制台");
		}else{
			shown = false;
			a.next().hide();
			a.html("显示控制台");
		}
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
		margin-top: 20px;
		clear:both;
	}
	.input_item{
		background-color: #EEEEEE;
		margin: 5px;
		padding: 5px;
		border: 1px solid #999999;
	}
	.output{
		word-wrap: break-word;
	}
	iframe.output{
		width:99.5%;
		height:500px;
		border:none;
	}
	.run_div{
		float:right; 
		position:relative; 
		top:20px; 
		right:50px;
	}
	#run_msg{
		font-size:13px;
	}
	.output_container{
		padding:4px;
	}
	#switcher{
		font-size:13px;
	}
</style>
</jsp:attribute>
<jsp:body>
	<s:file_upload_head/>
	<div class="screen">
		<div class="detail_head">
			<div class="run_div">
				<input type="button" id="run_btn" value="运行" onclick="doSubmit()">
				<span id="run_msg"></span>
			</div>
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
			<div>
				<a id="switcher" href="javascript:switchConsole()">显示控制台</a>
				<div style="width:600px; height:300px;display:none;">
					<s:console></s:console>
				</div>
			</div>
		</c:if>
		</div>
		<c:if test="${tool.status == 0}">
			<div>
				<a href="/toolcloud/ctrl/tool/approve?toolId=${tool.descriptor.pluginID}" >允许公开</a>
			</div>
		</c:if>
		<div style="clear:left; padding-top:20px;">
			<c:forEach var="t" items="${similars}">
			<div class="left_float" style="padding-right:20px; padding-bottom:5px;">
				<a id="${t.descriptor.pluginID}" href="/toolcloud/ctrl/tool/detail?toolId=${t.descriptor.pluginID}">
					<c:choose>
						<c:when test="${empty t.descriptor.icon}">
							<div class="tool_logo"><span>${t.descriptor.pluginName}</span></div>
						</c:when>
						<c:otherwise>
							<img height="64" width="64" src="${t.descriptor.displayIcon}" style="border:1px #333333 inset;"/>
						</c:otherwise>
					</c:choose>
				</a>
			</div>
			</c:forEach>
			<div style="font-size: 12px; clear:left;"><a href="/toolcloud/ctrl/tool/list">更多</a></div>
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
