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
	<div>
		<c:choose>
			<c:when test="${isScript == true}">
				<span class="label">file:</span>
				<input name="o_default" value="${item.defaultValue}" size="10">
			</c:when>
			<c:otherwise>
				<span class="label">type:</span>
				<select name="o_dataType">
					<option value="text" <c:if test="${item.dataType == 'text'}">selected="selected"</c:if> >text</option>
					<option value="file" <c:if test="${item.dataType == 'file'}">selected="selected"</c:if> >file</option>
					<option value="diagram" <c:if test="${item.dataType == 'diagram'}">selected="selected"</c:if> >diagram</option>
					<option value="table" <c:if test="${item.dataType == 'table'}">selected="selected"</c:if> >table</option>
				</select>
			</c:otherwise>
		</c:choose>
		
	</div>
	<div>
		<span class="label">description:</span>
		<br>
		<textarea cols="22" rows="1" name="o_description">${item.description}</textarea>
	</div>
</div>