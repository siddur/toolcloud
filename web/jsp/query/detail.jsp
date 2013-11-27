<%@page import="siddur.common.security.Permission"%>
<%@page import="siddur.common.security.RequestUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
	.query_item{
		margin-bottom: 5px;
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
			location.href = "${root}/query/${query.id}.html&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
		}
	</script>
	<div class="query_item">
		<div>
			<span class="query_title">${query.title }</span>
			<span class="query_date">
				<f:formatDate value="${query.publishAt}" pattern="yyyy-MM-dd hh:mm"/>
			</span>
		</div>
		<pre class="source">${query.content}</pre>
	</div>
	<c:forEach var="comment" items="${comments.data}" varStatus="vs">
		<s:comment comment="${comment}" 
			subjectId="${query.id}"
			index="${vs.index + 1}"
			scope="query"
			closable="${canDelComment}" >
		</s:comment>
	</c:forEach>
	
	<s:paging pageIndex="${comments.pageIndex }" pageSize="20" total="${comments.total }"></s:paging>
	<a href="javascript:history.back()">返回</a>
	
	<div style="clear:left; padding-top:50px;">
		<form method="post" action="${root}/ctrl/query/comment">
			<input type="submit" class="btn" value="评论(字数&lt;5K)"><br/>
			<textarea name="comment" id="comment" rows="6" cols="80"></textarea>
			<input type="hidden" name="queryId" value="${query.id}">
		</form>
	</div>
</div>
</s:site>