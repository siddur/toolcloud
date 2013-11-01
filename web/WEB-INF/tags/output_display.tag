<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="outputData" type="siddur.tool.core.data.DataTemplate"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" tagdir="/WEB-INF/tags"%>

<c:if test="${output_display_parts_loaded != true }">
	<c:set var="output_display_parts_loaded" value="true" scope="request"></c:set>
	
	<script>
		function populate(data){
			var tag = this.tagName;
			if(tag == "SPAN"){
				$(this).html(data);
			}
			else if(tag == "TEXTAREA"){
				$(this).val(data);
			}
			else if(tag == "A"){
				$(this).attr("href", "/toolcloud/file/" + data + "?d=1").html(data);
			}
			else if(tag == "TABLE"){

			}
			else if(tag == "IMG"){
				$(this).attr("src", "/toolcloud/file/" + data);
			}
			else{//file-browser
				initTree(data, this);
			}
		}
		
		$(document).ready(function(){
			$(".output").each(function(idx, item){
				item.populate = populate;
			})
		});
	</script>
</c:if>
<c:set var="type" value="${outputData.dataType}"/>
<c:set var="desc" value="${outputData.description}"/>
<c:if test='${desc != null && desc != ""}'>
	<span class="label">${desc}</span>
</c:if>
<c:choose>
	<c:when test="${'STRING' == type}">
		<textarea class="output" style="width:99%; height:120px;"></textarea>
	</c:when>
	<c:when test="${'HTML' == type}">
		<span class="output"></span>
	</c:when>
	<c:when test="${'FILE' == type}">
		<a class="output"></a>
	</c:when>
	<c:when test="${'IMAGE' == type}">
		<img class="output" src='#'/>
	</c:when>
	<c:when test="${'FILETREE' == type}">
		<div class="output">
			<s:file_browser></s:file_browser>
		</div>
	</c:when>
</c:choose>
