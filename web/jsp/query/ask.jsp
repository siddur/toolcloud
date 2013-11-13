<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>

<s:site>
<div class="screen">
	<form method="post" action="${root}/ctrl/query/doask">
		<div>
			<span>标题</span>
			<input name="title">
		</div>
		<br>
		
		<span>内容</span>
		<div>
			<textarea  name="content" id="content" rows="10" cols="70"></textarea>
		</div>
		<br>
		<input type="submit" value="发表" class="btn">
	</form>
</div>
</s:site>