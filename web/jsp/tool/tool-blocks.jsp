<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:manage>
<jsp:attribute name="headPart">
<style>
	.body{
		width:1000px;
	}
	
	#currentItem{
		height: 200px;
		border-bottom: solid 1px green;
	}
	
	.item{
		float:left;
		padding:10px;
		cursor:pointer;
	}
	.fieldlabel{
		font-size:13px;
	}
}
</style>
<script>
<c:if test="${not empty current}">
	$(function(){
		var similars = "${current.descriptor.similars}".split(",");
		if(similars.length > 0){
			for(var i = 0; i < similars.length; i++){
				$("#" + similars[i]).addClass("selected");
			}
		}
	});
</c:if>
	var doSubmit = function(){
		var similars = [];
		$(".item span.selected").each(function(idx, item){
			similars.push(item.id);
		});
		var ids = similars.join(",");
		$("#similars").val(ids);
	}
	var select = function(obj){
		$(obj).toggleClass("selected");
	}
</script>
</jsp:attribute>
<jsp:body>
<div class="crumb">
	${crumb}
</div>
<div class="body">
	<form action="/toolcloud/ctrl/tool/blocks" method="POST">
		<div id="currentItem">
			<c:if test="${not empty current}">
				<div>
					<a href="/toolcloud/ctrl/tool/update?toolId=${toolDescriptor.pluginID }" class="txt">${current.descriptor.pluginName}</a>
				</div>
				<div>${current.descriptor.description}</div>
				<div>
					<br/>
					<input id="similars" name="similars" type="hidden">
					<input name="toolId" type="hidden" value="${current.descriptor.pluginID}">
					<label class="fieldlabel">关键字（间隔符为逗号+空格）:</label><br/>
					<textarea name="keywords" rows="2" cols="50" id="keywords">${current.descriptor.keywords}</textarea>
				</div>
				<br/>
				<input class="btn" type="submit" value="保存" onclick="doSubmit()">
			</c:if>
		</div>
		<div class="fieldlabel">关联的工具:</div>
		<c:forEach var="item" items="${list}">
			<div class="item">
				<span class="left_float ui-icon ui-icon-circle-arrow-n" onclick="location.href='/toolcloud/ctrl/tool/blocks?toolId=${item.descriptor.pluginID}'"></span>
				<span class="txt" id="${item.descriptor.pluginID}" onclick="select(this)">${item.descriptor.pluginName}</span>		
			</div>
		</c:forEach>
	</form>
</div>
</jsp:body>
</s:manage>
