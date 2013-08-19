<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="item" type="siddur.tool.core.data.DataTemplate"%>

<c:if test="${meta_css_loaded != true }">
	<c:set var="meta_css_loaded" value="true" scope="request"></c:set>
	<style>
		.unit{
			border: 1px solid #CCCCCC;
		    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
		    transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
			border-radius: 4px 4px 4px 4px;
			padding : 15px;
			margin: 5px;
			width:220px;
			float:left;
		}
		.unit_input{
			background-color: #D0E9C6;
		}
		.unit_output{
			background-color: #DD514C;
		}
		.close_btn{
			float:right;
			position:relative;
			top:-10px;
			right:-10px;
		}
		.close_btn:HOVER {
			cursor: pointer;
		}
	</style>
</c:if>

<c:if test="${meta_js_loaded != true }">
	<c:set var="meta_js_loaded" value="true" scope="request"></c:set>
	<script>
		function del(obj){
			var d = $(obj).parent().remove();
		}
	</script>
</c:if>
	
<div class="unit unit_input">
	<span class="close_btn ui-icon ui-icon-closethick" onclick="del(this)"></span>
	<div class="input_tag">
		<span class="label">tag:</span>
		<input name="i_tag" size="5" value="${item.tag }">
	</div>
	<div>
		<span class="label">type:</span>
		<select name="i_dataType" >
			<option value="integer" <c:if test="${item.dataType == 'integer'}">selected="selected"</c:if> >integer</option>
			<option value="double" <c:if test="${item.dataType == 'double'}">selected="selected"</c:if> >double</option>
			<option value="string" <c:if test="${item.dataType == 'string'}">selected="selected"</c:if> >string</option>
			<option value="boolean" <c:if test="${item.dataType == 'boolean'}">selected="selected"</c:if> >boolean</option>
			<option value="file" <c:if test="${item.dataType == 'file'}">selected="selected"</c:if> >file</option>
			<option value="zipfile" <c:if test="${item.dataType == 'zipfile'}">selected="selected"</c:if> >zipfile</option>
		</select>
	</div>
	<div>
		<span class="label">description:</span>
		<br>
		<textarea cols="22" rows="1" name="i_description">${item.description }</textarea>
	</div>
</div>
