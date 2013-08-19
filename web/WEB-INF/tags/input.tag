<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="inputModel" type="siddur.tool.core.data.ToolData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<c:set var="type" value="${inputModel.dataType}"/>
<c:set var="tag" value="${inputModel.tag}"/>
<c:set var="desc" value="${inputModel.description}"/>
<c:if test="${input_parts_loaded != true }">
	<c:set var="input_parts_loaded" value="true" scope="request"></c:set>
	
	<script>
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
			if(i.hasClass("integer")){
				if(isNaN(v) || v.indexOf(".") > -1){
					showError(item);
					alert("Not a integer input");
					return false;
				}
			}
			else if(i.hasClass("double")){
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
		<c:when test="${'string' == type
			||'integer' == type
			||'double' == type
			}">
			<input type="text" class="input ${type}" name="input">
		</c:when>
		<c:when test="${'boolean' == type }">
			<input type="checkbox" class="input" value="${tag}" name="input" style="position: relative; top: 2px;">
		</c:when>
		<c:when test="${'text' == type}">
			<div><textarea class="input" cols="40" rows="3" name="input"></textarea></div>
		</c:when>
		<c:when test="${'file' == type || 'zipfile' == type}">
			<s:file_upload fieldname="input" displayname="upload tool file.."/>
		</c:when>
	</c:choose>
</div>