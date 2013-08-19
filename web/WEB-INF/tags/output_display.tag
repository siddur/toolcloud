<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="outputData" type="siddur.tool.core.data.DataTemplate"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${output_display_parts_loaded != true }">
	<c:set var="output_display_parts_loaded" value="true" scope="request"></c:set>
	
	<script>
		function populate(data){
			var tag = this.tagName;
			if(tag == "SPAN"){
				$(this).html(data);
			}
			else if(tag == "A"){
				$(this).attr("href", "/toolcloud/file/" + data).html(data);
			}
			else if(tag == "TABLE"){

			}
			else if(tag == "IMG"){
				$(this).attr("src", data.replace("/fileserver", ""));
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
	<c:when test="${'string' == type
		||'int' == type
		||'double' == type
		||'text' == type
		}">
		<span class="output"></span>
	</c:when>
	<c:when test="${'file' == type}">
		<a class="output"></a>
	</c:when>
	<c:when test="${'html.table' == type}">
		<table class="output">
		</table>
	</c:when>
	<c:when test="${'diagram' == type}">
	
	</c:when>
	<c:when test="${'image' == type}">
		<img src='#'/>
	</c:when>
</c:choose>
