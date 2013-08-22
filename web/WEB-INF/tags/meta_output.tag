<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="item" type="siddur.tool.core.data.DataTemplate"%>
<%@ attribute name="isScript" type="java.lang.Boolean" required="false"%>

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

<div class="unit unit_output">
	<span class="close_btn ui-icon ui-icon-closethick" onclick="del(this)"></span>
	
	<div class="out_default" <c:if test="${isScript != true}"> style="display: none" </c:if> >
		<span class="label" title="系统会根据该文件名查找脚本生成的文件">文件名</span>
		<input name="o_default" value="${item.defaultValue}" size="10">
	</div>
	<div class="out_type" <c:if test="${isScript == true}"> style="display: none" </c:if> >
		<span class="label">类型:</span>
		<select name="o_dataType">
			<option value="STRING" <c:if test="${item.dataType == 'STRING'}">selected="selected"</c:if> >文字</option>
			<option value="FILE" <c:if test="${item.dataType == 'FILE'}">selected="selected"</c:if> >文件</option>
			<option value="IMAGE" <c:if test="${item.dataType == 'IMAGE'}">selected="selected"</c:if> >图片</option>
		</select>
	</div>
	<div>
		<span class="label">描述:</span>
		<br>
		<textarea cols="22" rows="1" name="o_description">${item.description}</textarea>
	</div>
</div>