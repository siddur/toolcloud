<%@page import="siddur.common.security.Permission"%><%@page import="siddur.common.security.RequestUtil"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@taglib prefix="s" tagdir="/WEB-INF/tags"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %><c:set var="title" value="1" scope="request"></c:set><s:site><jsp:attribute name="titlePart"><title>${query.title }</title>
	<meta name="description" content="${query.title}"/></jsp:attribute>
<jsp:attribute name="headPart">
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
<script>
	function changePage(){
		location.href = "${root}/query/${query.id}.html?pageIndex=" + pageIndex + "&pageSize=" + pageSize;
	}
</script>
</jsp:attribute>
<jsp:body>
<div class="screen">
	<div class="query_item">
		<div>
			<span class="query_title">${query.title }</span>
			<span class="query_date">
				<f:formatDate value="${query.publishAt}" pattern="yyyy-MM-dd hh:mm"/>
			</span>
		</div>
		<pre class="source">${query.content}</pre>
	</div>
	
<!-- Baidu Button BEGIN -->
<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
<span class="bds_more">分享到：</span>
<a class="bds_qzone"></a>
<a class="bds_tsina"></a>
<a class="bds_tqq"></a>
<a class="bds_renren"></a>
<a class="bds_t163"></a>
<a class="shareCount"></a>
</div>
<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=5954609" ></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000)
</script>
<!-- Baidu Button END -->
	<div style="clear:left;">
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
	</div>
	<div style="clear:left; padding-top:50px;">
		<form method="post" action="${root}/ctrl/query/comment">
			<script>
				function doSubmit(){
					var comment = $("#comment").val();
					comment = $.trim(comment);
					if(comment.length < 3){
						alert("字数不能少于3");
						return false;
					}
					if(comment.length > 5000){
						alert("字数太多");
						return false;
					}
					return true;
				}
			</script>
			<textarea name="comment" id="comment" rows="6" cols="80"></textarea>
			<input type="submit" class="btn" value="评论(字数&lt;5K)" onclick="return doSubmit();"><br/>
			<input type="hidden" name="queryId" value="${query.id}">
		</form>
	</div>
</div>
</jsp:body>
</s:site>