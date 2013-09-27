<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.detail_head:AFTER{
		content: ".";
		visibility: hidden;
		clear:both;  
  		display:block; 
  		height: 0;
	}
	.comments{
		float:left;
		margin-top: 10px;
	}
	.tool_container{
		float:left;
		width:100%;
		height:500px;
		border: 1px solid #848877;
		-moz-box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
		-webkit-box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
		box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
	}
</style>
</jsp:attribute>
<jsp:body>
	<div class="screen">
		<div class="detail_head">
			<s:tool_detail toolDescriptor="${tool.descriptor}"/>
		</div>
		<iframe class="tool_container" src="/toolcloud/file/${toolFile}"></iframe>
		<div class="comments">
			<c:forEach var="c" items="${comments}">
				<s:comment comment="${c}" 
					toolId="${tool.descriptor.pluginID}"
					closable="${canDelComment}" ></s:comment>
			</c:forEach>
			
			<form method="post" action="/toolcloud/ctrl/tool/comment">
				<textarea name="comment" id="comment" rows="6" cols="60"></textarea>
				<input type="hidden" name="toolId" value="${tool.descriptor.pluginID}">
				<input type="submit" class="btn" value="评论">
			</form>
		</div>
	</div>
</jsp:body>
</s:site>
