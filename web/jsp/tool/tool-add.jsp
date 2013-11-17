<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<script>
	var inputDiv, outputDiv;
	$(document).ready(function(){
		inputDiv = $("div.input div.instance div.unit");
		outputDiv = $("div.output div.instance div.unit");
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
 		$(".overrodeParam").css("display", "block");
 		$(".input").css("display", "block");
		$(".output").css("display", "block");
		$(".input_tag").css("display", "block");
		$(".output_tag").css("display", "block");
		$(".out_default").css("display", "block");
		
 	 	
		if(lang == "client-side"){
			$(".input").css("display", "none");
			$(".output").css("display", "none");
			$(".paramOverride").css("display", "none");
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
		var file = $("[name='toolfile']");
		if(!file.val()){
			$("#toolfile").prev().addClass("error")
			return false;
		}
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
	<s:file_upload_head multiple="false"></s:file_upload_head>
	<div class="body attr-item-container">
		<form method="post" action="${root}/ctrl/tool/save" >
			<div>
				<span class="label">编程语言:</span>
				<select id="lang" name="lang" onchange="selectLang(this.value)">
				<c:forEach var="lang" items="${langs}">
					<option value="${lang}" <c:if test="${lang == 'java'}"> selected="selected" </c:if> >
						${lang}
					</option>
				</c:forEach>
					<option value="cmd">command(cmd/bash)</option>
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
			<div class="overrodeParam">
				<span class="label">参数重写</span>
				<input style="width:500px;"  name="overrodeParam">
			</div>
			<div class="input">
				<span class="label" class="title">输入</span>
				<input type="button" value="增加一个输入框" class="btn" onclick="add(0)">
				<div class="instance">
					<s:meta_input></s:meta_input>
				</div>
				<div class="input_container">
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
				</div>
				<div style="clear: left;"></div>
			</div>
			
			<center>
				<input type="button" class="btn" value="取消" onclick="history.back()">
				<input type="submit" class="btn" value="保存" onclick="return validate();">
			</center>			
		</form>
	</div>
</jsp:body>
</s:site>