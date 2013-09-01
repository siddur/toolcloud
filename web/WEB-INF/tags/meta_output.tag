<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="item" type="siddur.tool.core.data.DataTemplate"%>
<%@ attribute name="isScript" type="java.lang.Boolean" required="false"%>


<div class="unit unit_output">
	<span class="close_btn ui-icon ui-icon-closethick" onclick="del(this)"></span>
	
	<div class="out_type column first_column" <c:if test="${isScript == true}"> style="display: none" </c:if> >
		<span class="label">类型:</span>
		<select name="o_dataType">
			<option value="STRING" <c:if test="${item.dataType == 'STRING'}">selected="selected"</c:if> >文字</option>
			<option value="HTML" <c:if test="${item.dataType == 'HTML'}">selected="selected"</c:if> >HTML</option>
			<option value="FILE" <c:if test="${item.dataType == 'FILE'}">selected="selected"</c:if> >文件</option>
			<option value="IMAGE" <c:if test="${item.dataType == 'IMAGE'}">selected="selected"</c:if> >图片</option>
		</select>
	</div>
	<div class="out_default column" <c:if test="${isScript != true}"> style="display: none" </c:if> >
		<span class="label" title="系统会根据该文件名查找脚本生成的文件">文件名</span>
		<input name="o_default" value="${item.defaultValue}" size="10">
	</div>
	<div class="column">
		<span class="label">描述:</span>
		<br>
		<textarea cols="30" rows="2" name="o_description">${item.description}</textarea>
	</div>
</div>