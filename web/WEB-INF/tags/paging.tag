<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="pageIndex" required="true" type="java.lang.Integer"%>
<%@ attribute name="pageSize" required="true" type="java.lang.Integer"%>
<%@ attribute name="total" required="true" type="java.lang.Integer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${total > pageSize }">
	<%@ variable name-given="pages" scope="NESTED" %>
	<%@ variable name-given="maxPages" scope="NESTED" %>
	<c:set var="pages" value="<%=(int)Math.ceil((double)total/pageSize) %>"></c:set>
	<c:set var="maxPages" value="20"></c:set>
	
	<c:if test="${paging_tag != true }">
		<c:set var="paging_tag" value="true" scope="request"></c:set>
	<script>
		var pageIndex = ${pageIndex};
		var pageSize = ${pageSize};
		var pages = ${pages};
		var keyword;
		$(document).ready(function(){
			$(".unselected").click(function(){
				var index = $(this).text();
				if(index == 'last' || index == "next"){
					if(index == 'last'){
						lastPage();
					}
					else if(index == 'next'){
						nextPage();
					}
				}else{
					pageSelected(index);
				}
			});
		});
	
		function lastPage(){
			var newPage = pageIndex - 1;
			if(newPage < 1){
				return;
			}
			pageSelected(newPage);
		}
	
		function nextPage(){
			var newPage = pageIndex + 1;
			if(newPage > pages){
				return;
			}
			pageSelected(newPage);
		}
	
		function pageSelected(newPage){
			pageIndex = newPage;
			changePage();
		}
	</script>
	</c:if>
	<c:if test="${total > 0}">
	<div class='paging'>
		<span id='last' class="unselected">last</span>
		<c:forEach var="index" begin="${pageIndex - maxPages > 1 ? pageIndex - maxPages : 1 }" end="${pageIndex - 1}">
			<span class="unselected">${index }</span>
		</c:forEach>
		<span class="selected">${pageIndex }</span>
		<c:forEach var="index" begin="${pageIndex + 1}" end="${pages - pageIndex > maxPages ? pageIndex + maxPages : pages }">
			<span class="unselected">${index }</span>
		</c:forEach>
		
		<span id='next' class="unselected">next</span>
	</div>
	</c:if>

</c:if>