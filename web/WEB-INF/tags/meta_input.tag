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
		<span class="label">前缀:</span>
		<input name="i_tag" size="5" value="${item.tag }">
	</div>
	<div>
		<span class="label">类型:</span>
		<select name="i_dataType" >
			<option value="STRING" <c:if test="${item.dataType == 'STRING'}">selected="selected"</c:if> >字符串</option>
			<option value="INTEGER" <c:if test="${item.dataType == 'INTEGER'}">selected="selected"</c:if> >整数</option>
			<option value="DOUBLE" <c:if test="${item.dataType == 'DOUBLE'}">selected="selected"</c:if> >双精度</option>
			<option value="BOOLEAN" <c:if test="${item.dataType == 'BOOLEAN'}">selected="selected"</c:if> >布尔</option>
			<option value="DATE" <c:if test="${item.dataType == 'DATE'}">selected="selected"</c:if> >日期</option>
			<option value="TEXT" <c:if test="${item.dataType == 'TEXT'}">selected="selected"</c:if> >文本</option>
			<option value="FILE" <c:if test="${item.dataType == 'FILE'}">selected="selected"</c:if> >文件</option>
			<option value="ZIPFILE" <c:if test="${item.dataType == 'ZIPFILE'}">selected="selected"</c:if> >zip文件</option>
		</select>
	</div>
	<div>
		<span class="label">约束</span>
		<br>
		<textarea cols="22" rows="1" name="i_constraint">${item.constraint }</textarea>
	</div>
	<div>
		<span class="label">描述:</span>
		<br>
		<textarea cols="22" rows="1" name="i_description">${item.description }</textarea>
	</div>
</div>
