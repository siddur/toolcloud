<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="inputModel" type="siddur.tool.core.data.DataTemplate"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<c:set var="type" value="${inputModel.dataType}"/>
<c:set var="tag" value="${inputModel.tag}"/>
<c:set var="desc" value="${inputModel.description}"/>
<c:if test="${input_parts_loaded != true }">
	<c:set var="input_parts_loaded" value="true" scope="request"></c:set>
	
	<style>
		.ui-datepicker{
			font-size: 62.5%;
			font-family: "Trebuchet MS", "Arial", "Helvetica", "Verdana", "sans-serif"; 
		}
	</style>
	<script type="text/javascript" src="/toolcloud/jqueryui/jquery-ui-1.10.3.custom.min.js"></script>
	<script>
		$(function(){
			$( ".dateField" ).datepicker({
				dateFormat: "yy-mm-dd"
			});
		});
		var validateResult;
		function validate(){
			validateResult = true;
			$(".error").removeClass("error");
			$("[name='input']").each(doValidate);
			return validateResult;
		}

		function doValidate(idx, item){
			var tag = item.tagName;
			var v = item.value.trim();
			if(!v){
				showError(item);
				return false;
			}

			var i = $(item);
			if(i.hasClass("INTEGER")){
				if(isNaN(v) || v.indexOf(".") > -1){
					showError(item);
					alert("Not a integer input");
					return false;
				}
			}
			else if(i.hasClass("DOUBLE")){
				if(isNaN(v)){
					showError(item);
					alert("Not a double input");
					return false;
				}
			}
		}

		function showError(item){
			validateResult = false;

			if(item.type == "hidden"){
				$(item).next().addClass("error");
			}else{
				$(item).addClass("error");
			}
		}
	</script>
</c:if>

<div class="input_item">
	<c:if test='${tag != null && desc != "" }'>
		<div class="input_description">${desc}</div>
	</c:if>
	<c:if test='${tag != null && tag != "" }'>
		<span class="label">${tag}</span>
	</c:if>
	<c:choose>
		<c:when test="${'STRING' == type
			||'INTEGER' == type
			||'DOUBLE' == type
			}">
			<input type="text" class="input ${type}" name="input">
		</c:when>
		<c:when test="${'BOOLEAN' == type }">
			<input type="checkbox" class="input" value="${tag}" name="input" style="position: relative; top: 2px;">
		</c:when>
		<c:when test="${'TEXT' == type}">
			<div><textarea class="input" cols="40" rows="3" name="input"></textarea></div>
		</c:when>
		<c:when test="${'FILE' == type || 'ZIPFILE' == type}">
			<s:file_upload fieldname="input" displayname="upload tool.."/>
		</c:when>
		<c:when test="${'DATE' == type}">
			<input type="text" class="dateField" readonly="readonly">
		</c:when>
	</c:choose>
</div>