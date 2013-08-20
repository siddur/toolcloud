<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
	.query_item{
		padding: 10px;
		background: #DDDDDD;
		margin: 10px;
		width: 600px;
	}
	
	.query_title{
		font-size: 18px;
		color:blue;
	}
	.query_date{
		font-size: 11px;
		color: grey;
	}
</style>
<s:site>
<div class="body">
	<script>
		function changePage(){
			location.href = "/toolcloud/ctrl/query/list?pageIndex=" + pageIndex + "&pageSize=" + pageSize;
		}
	</script>
	<c:forEach var="item" items="${queries.data}">
		<div class="query_item">
			<div>
				<a href="/toolcloud/ctrl/query/detail?id=${item.id}">
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
</div>
</s:site>