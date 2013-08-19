<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="comment" required="true" type="siddur.common.miscellaneous.Comment"%>
<%@ attribute name="toolId" required="false" type="java.lang.String"%>
<%@ attribute name="closable" required="true" type="java.lang.Boolean"%>

<c:if test="${comment_tag != true }">
	<c:set var="comment_tag" value="true" scope="request"></c:set>
	<style>
		.comment_item {
		    border-bottom: 1px solid #FCF8E3;
		    background-color: #D6E9C6;
		    margin: 3px;
		    padding: 7px;
		}
		.comment_content {
		    font-size: 16px;
		    max-height: 40px;
		    overflow: hidden;
		}
		.comment_detail{
			font-size: 14px;
		    font-style: italic;
		    text-indent: 5px;
		}
		.close_btn{
			float:right;
			position:relative;
			top:-3px;
			right:-3px;
		}
		.close_btn:hover{
			cursor: pointer;
		}
	</style>
	
</c:if>
<div class="comment_item">
	<div class="comment_content">
		<c:if test="${closable == true }">
			<span class="close_btn ui-icon ui-icon-closethick" 
				onclick="location.href='/toolcloud/ctrl/tool/delcomment?id=${comment.commentId}&toolId=${toolId}'">
			</span>
		</c:if>
		${comment.content }
	</div>
	<div class='comment_detail'>
		asked 
		by <font color='#FAA732'>${comment.saidBy }</font> 
		at <font color='#5BB75B'><f:formatDate value="${comment.saidAt}" pattern="yyyy-MM-dd hh:mm"/></font>
	</div>
</div>