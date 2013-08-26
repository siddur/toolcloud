<%@page import="siddur.tool.core.ScriptUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/toolcloud/jquery/js/jquery-1.9.1.js"></script>
<script>
	var inputDiv, outputDiv;
	$(document).ready(function(){
		inputDiv = $("div.input div.unit").clone();
		outputDiv = $("div.output div.unit").clone();
		//$(":text").addClass("txt");

		selectLang($("#lang").val());
	});

	function add(flag){
		if(flag == 0){
			$(".input_container").append(inputDiv.clone());
		}else{
			$(".output_container").append(outputDiv.clone());
		}
		selectLang($("#lang").val());
	}

	
 	function selectLang(lang){
		if(lang == "client-side"){
			$(".input").css("display", "none");
			$(".output").css("display", "none");
		}else{
			$(".input").css("display", "block");
			$(".output").css("display", "block");

			if(lang == "java"){
				$(".input_tag").css("display", "none");
				$(".output_tag").css("display", "none");
				
				$(".out_default").css("display", "none");
				$(".out_type").css("display", "block");
			}else{
				$(".input_tag").css("display", "block");
				$(".output_tag").css("display", "block");
				
				$(".out_default").css("display", "block");
				$(".out_type").css("display", "none");
			}
		}

	}


	function validate(){
		$(".error").removeClass("error");
		var file = $("[name='toolfile']");
		if(!file.val()){
			file.next().addClass("error")
			return false;
		}
		var name = $("[name='name']");
		if(!name.val()){
			name.addClass("error");
			return false;
		}

		return true;
	}
</script>
<style>
	.attr-item-container div{
		padding-top:10px;
	}
	.title{
	    padding : 3px 5px;
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<s:file_upload_head multiple="false"></s:file_upload_head>
	<div class="body attr-item-container">
		<form method="post" action="/toolcloud/ctrl/tool/save" >
			<div>
				<span class="label">编程语言:</span>
				<select id="lang" name="lang" onchange="selectLang(this.value)">
				<%
					for(String lang : ScriptUtil.getLangs())
					{
				%>
					<option value="<%=lang%>" <%if(lang.equals("java")){%>selected="selected"<%}%>>
						<%=lang%>
					</option>
				<%
					}
				%>
					<option value="cmd">cmd</option>
					<option value="bash">bash</option>
					<option value="client-side">javascript</option>
				</select>
			</div>
			<s:file_upload fieldname="toolfile" displayname="上传工具文件.."/>
			<div><span class="label">名称:</span><input name="name"></div>
			<div><span class="label">类别:</span><input name="catelog"></div>
			<s:file_upload fieldname="icon" displayname="上传图标.." isImage="true"/>
			<div>
				<span class="label">描述:</span>
				<br>
				<textarea cols="40" rows="3" name="description"></textarea>
			</div>
			<div class="input">
				<span class="label" class="title">输入</span>
				<input type="button" value="增加一个输入框" class="btn" onclick="add(0)">
				<div class="input_container">
					<s:meta_input></s:meta_input>
				</div>
				<div style="clear: left;"></div>
			</div>
			
			<div class="output">
				<span class="label" class="title">输出</span>
				<input type="button" value="增加一个输出框" class="btn" onclick="add(1)">
				<div class="output_container">
					<s:meta_output></s:meta_output>
				</div>
				<div style="clear: left;"></div>
			</div>
			
			<center><input type="submit" class="btn" value="save" onclick="return validate();"></center>		
		</form>
	</div>
</body>