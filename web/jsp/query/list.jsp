<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
	.query_item{
		padding: 10px;
		background: #FFFFFF;
		margin: 10px;
		width: 100%;
		border:solid 1px #AAAAAA;
	}
	
	.query_item a{
		text-decoration: none;
	}
	
	.query_title{
		font-size: 18px;
		color:blue;
	}
	.query_date{
		font-size: 11px;
		color: grey;
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
			location.href = "${root}/ctrl/query/list?pageIndex=" + pageIndex + "&pageSize=" + pageSize;
		}
	</script>
	<c:forEach var="item" items="${queries.data}">
		<div class="query_item">
			<c:if test="${canDelQuery == true }">
				<span class="close_btn ui-icon ui-icon-closethick" 
					onclick="location.href='${root}/ctrl/query/delquery?id=${item.id}'">
				</span>
			</c:if>
			<div>
				<a href="${root}/ctrl/query/detail?id=${item.id}">
					<span class="query_title">${item.title }</span>
					<span class="query_date">
						<f:formatDate value="${item.publishAt}" pattern="yyyy-MM-dd hh:mm"/>
					</span>
				</a>
			</div>
			<div class="query_content">
				${item.plainContent}
			</div>
		</div>
	</c:forEach>
	
	<s:paging pageIndex="${queries.pageIndex }" pageSize="20" total="${queries.total }"></s:paging>
	<br/>
	<a href="${root}/ctrl/query/ask">发布需求</a>
	&nbsp;&nbsp;&nbsp;
	<a href="${root}/">返回首页</a>
	
</div>
</s:site>