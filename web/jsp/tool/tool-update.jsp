<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<c:set var="td" scope="page" value="${tool.descriptor}"></c:set>
<s:site>
<jsp:attribute name="headPart">
<script>
	var inputDiv, outputDiv;
	$(document).ready(function(){
		inputDiv = $("div.input div.instance div.unit").clone();
		outputDiv = $("div.output div.instance div.unit").clone();
		//$(":text").addClass("txt");
	
		selectLang('${td.lang}');
	});
	
	function add(flag){
		if(flag == 0){
			$(".input_container").append(inputDiv.clone());
		}else{
			$(".output_container").append(outputDiv.clone());
		}
		selectLang('${td.lang}');
	}
	
	function selectLang(lang){
 		$(".paramOverride").css("display", "block");
 		$(".input").css("display", "block");
		$(".output").css("display", "block");
		$(".input_tag").css("display", "block");
		$(".output_tag").css("display", "block");
		$(".out_default").css("display", "block");
		
 	 	
		if(lang == "client-side"){
			$(".input").css("display", "none");
			$(".output").css("display", "none");
			$(".overrodeParam").css("display", "none");
		}
		else if(lang == "java"){
			$(".input_tag").css("display", "none");
			$(".output_tag").css("display", "none");
			$(".overrodeParam").css("display", "none");
			
			$(".out_default").css("display", "none");
		}else{
			//$(".out_type").css("display", "none");
		}

	}
	
	function validate(){
		$(".error").removeClass("error");
		var name = $("[name='name']");
		if(!name.val()){
			name.addClass("error");
			return false;
		}

		$(".instance").remove();
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
	.instance{
		display: none;
	}
</style>
</jsp:attribute>
<jsp:body>
	<div class="crumb">
		${crumb}
	</div>
	<s:file_upload_head multiple="false"/>
	<div class="body attr-item-container">
		<form method="post" action="${root}/ctrl/tool/save" >
			<input type="hidden" name="toolId" value="${td.pluginID}">
			<div>
				<span class="label">名称:</span>
				<input name="name" value="${td.pluginName}">
			</div>
			<div>
				<span class="label">类别:</span>
				<input name="catelog" value="${td.catalog}">
			</div>
			<br/>
			<s:file_upload fieldname="icon" displayname="上传图标" isImage="true" file="${td.icon}"/>
			<div>
				<span class="label">描述:</span>
				<br>
				<textarea cols="40" rows="3" name="description">${td.description}</textarea>
			</div>
			<div class="overrodeParam">
				<span class="label">参数重写</span>
				<input style="width:500px;" name="overrodeParam" value="${td.overrodeParam}">
			</div>
			<c:if test="${td.lang != 'client-side' }">
				<div class="input">
					<span class="label" class="title">输入</span>
					<input type="button" value="增加一个输入框" class="btn" onclick="add(0)">
					<div class="instance">
						<s:meta_input></s:meta_input>
					</div>
					<div class="input_container">
					<c:forEach var="i" items="${td.inputModel}">
						<s:meta_input item="${i}"></s:meta_input>
					</c:forEach>
					</div>
					<div style="clear: left;"></div>
				</div>
				
				<div class="output">
					<span class="label" class="title">输出</span>
					<input type="button" value="增加一个输出框" class="btn" onclick="add(1)">
					<div class="instance">
						<s:meta_output></s:meta_output>
					</div>
					<div class="output_container">
					<c:forEach var="o" items="${td.outputModel}">
						<s:meta_output item="${o}"></s:meta_output>
					</c:forEach>
					</div>
					<div style="clear: left;"></div>
				</div>
			</c:if>
			<center>
				<input type="button" class="btn" value="取消" onclick="history.back()">
				<input type="submit" class="btn" value="保存" onclick="return validate();">
			</center>	
		</form>
	</div>
</jsp:body>
</s:site>