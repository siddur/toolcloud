<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="keyword" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:if test="${search_tag != true }">
	<c:set var="search_tag" value="true" scope="request"></c:set>
	<style>
.search_btn {
	float: right;
}

.close_btn {
	float: right;
}

.pac_search {
	border: 1px solid #CCCCCC;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	border-radius: 4px 4px 4px 4px;
	vertical-align: middle;
	width: 235px;
	padding: 3px;
	background-color: white;
}

.pac_search span {
	cursor: pointer;
}

.pac_search input {
	border: none;
	width: 200px;
}
</style>
	<script>
		var keyword = "";
		function search() {
			keyword = $("#keyword_input").val();
			pageIndex = 1;//初始化搜索结果的页面
			changePage();
		}
		function clearkw() {
			keyword = "";
			changePage();
		}

		$(document).ready(function() {
			$("#keyword_input").keypress(function(evt) {
				if (evt.keyCode == 13) {
					search();
				}
			});
		});
	</script>
</c:if>

<div class="pac_search">
	<input id="keyword_input" name="keyword_input" type="text"
		value="${keyword }" onkeypress="enter(this)"> <span
		class="ui-icon ui-icon-search search_btn" onclick="search()"></span>
	<c:if test="${keyword != null && keyword != ''}">
		<span class="ui-icon1 ui-icon-close close_btn" onclick="clearkw()"></span>
	</c:if>
</div>