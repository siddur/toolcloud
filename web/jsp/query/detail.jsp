<%@page import="siddur.common.security.Permission"%>
<%@page import="siddur.common.security.RequestUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
	.query_item{
		padding: 10px;
		border-bottom: solid 1px blue;
		margin-bottom: 20px;
	}
	
	.query_title{
		font-size: 18px;
		color:blue;
	}
	.query_date{
		font-size: 11px;
		color: grey;
	}
	.comment_item {
	    border-bottom: 1px solid #FCF8E3;
	    background-color: #D6E9C6;
	    margin: 3px;
	    padding: 7px;
	}
	.comment_content {
	    font-size: 16px;
	    overflow: hidden;
	}
	.comment_detail{
		font-size: 14px;
	    font-style: italic;
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
<s:site>
<div class="screen">
	<script>
		function changePage(){
			location.href = "/toolcloud/ctrl/query/detail?id=${query.id}&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
		}
	</script>
	<div class="query_item">
		<div>
			<span class="query_title">${query.title }</span>
			<span class="query_date">
				<f:formatDate value="${query.publishAt}" pattern="yyyy-MM-dd hh:mm"/>
			</span>
		</div>
		<div class="query_content">
			${query.plainContent}
		</div>
	</div>
	<c:forEach var="comment" items="${comments.data}">
		<div class="comment_item">
			<div class="comment_content">
				<c:if test="<%RequestUtil.hasPerm(request, Permission.COMMENT_DEL)%>">
					<span class="close_btn ui-icon ui-icon-closethick" 
						onclick="location.href='/toolcloud/ctrl/query/delcomment?id=${comment.commentId}&queryId=${query.id}'">
					</span>
				</c:if>
				<pre style="margin:0">${comment.preContent }</pre>
			</div>
			<div class='comment_detail'>
				<font color='#FAA732'>${comment.saidBy }</font> 
				<font color='#5BB75B'><f:formatDate value="${comment.saidAt}" pattern="yyyy-MM-dd hh:mm"/></font>
			</div>
		</div>
	</c:forEach>
	
	<s:paging pageIndex="${comments.pageIndex }" pageSize="20" total="${comments.total }"></s:paging>
	<a href="javascript:history.back()">返回</a>
	
	<div style="clear:left; padding-top:50px;">
		<form method="post" action="/toolcloud/ctrl/query/comment">
			<textarea name="comment" id="comment" rows="6" cols="35"></textarea>
			<input type="hidden" name="queryId" value="${query.id}">
			<input type="submit" class="btn" value="评论">
		</form>
	</div>
</div>
</s:site>