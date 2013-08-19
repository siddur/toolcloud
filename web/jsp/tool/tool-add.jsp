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
	});

	function add(flag){
		if(flag == 0){
			$(".input_container").append(inputDiv.clone());
		}else{
			$(".output_container").append(outputDiv.clone());
		}
	}

/*	
 	function selectLang(lang){
		if(lang == "java"){
			$(".input_tag").css("display", "none");
		}else{
			$(".input_tag").css("display", "block");
		}
	}
*/

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
	<s:file_upload_common/>
	<div class="body attr-item-container">
		<form method="post" action="/toolcloud/ctrl/tool/save" >
			<div>
				<span class="label">language:</span>
				<select name="lang">
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
				</select>
			</div>
			<s:file_upload fieldname="toolfile" displayname="upload tool file.."/>
			<div><span class="label">name:</span><input name="name"></div>
			<div><span class="label">catalog:</span><input name="catelog"></div>
			<s:file_upload fieldname="icon" displayname="upload image.." isImg="true"/>
			<div>
				<span class="label">description:</span>
				<br>
				<textarea cols="40" rows="3" name="description"></textarea>
			</div>
			<div class="input">
				<span class="label" class="title">input</span>
				<button class="btn1" onclick="add(0); return false">
					<span class="left_float ui-icon ui-icon-plus"></span>
					<span>one more</span>
				</button>
				<div class="input_container">
					<s:meta_input></s:meta_input>
				</div>
				<div style="clear: left;"></div>
			</div>
			
			<div class="output">
				<span class="label" class="title">output</span>
				<input type="button" value="add one more" class="btn1" onclick="add(1)">
				<div class="output_container">
					<s:meta_output></s:meta_output>
				</div>
				<div style="clear: left;"></div>
			</div>
			
			<center><input type="submit" class="btn" value="save" onclick="return validate();"></center>		
		</form>
	</div>
</body>