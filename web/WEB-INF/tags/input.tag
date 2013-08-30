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
<c:if test="${type.constrantType=='ENUM'}">
	var options = [];
	<c:forEach var="item" items="$(type.options)">
		options.push(${item[1]});
	</c:forEach>
		function changeOption(obj){
			var i = obj.selectedIndex;
			obj.nextSibling.innerHTML = options[i];
		}
</c:if>
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
		<c:when test="${type.constrantType=='ENUM'}">
			<select name="input" class="input" onselect="changeOption(this)">
			<c:forEach var="item" items="$(type.options)">
				<option value="${item[0]}">${item[0]}</option>
			</c:forEach>
			</select>
			<span></span>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${'STRING' == type
					||'INTEGER' == type
					||'DOUBLE' == type
					}">
					<input type="text" class="input ${type}" name="input">
				</c:when>
				<c:when test="${'BOOLEAN' == type }">
					<input type="checkbox" class="input" value="1" name="input" style="position: relative; top: 2px;">
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
		</c:otherwise>
	</c:choose>
</div>