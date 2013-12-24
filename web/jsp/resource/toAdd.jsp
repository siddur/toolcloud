<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" tagdir="/WEB-INF/tags"%>
<s:site>
<jsp:attribute name="headPart">
	<script>
		function doSubmit(){
			var title = $("#title").val();
			title = $.trim(title);
			if(title.length < 3){
				alert("标题字数不能少于3");
				return false;
			}
			else if(title.length > 200){
				alert("标题字数不能多于200");
				return false;
			}
			var content = $("#content").val();
			if(content.length < 50){
				alert("正文字数不能少于50");
				return false;
			}
			else if(content.length > 20000){
				alert("正文字数不能多于20000");
				return false;
			}
			return true;
		}
	</script>
</jsp:attribute>
<jsp:body>
<div class="screen">
	<form method="post" action="${root}/ctrl/query/doask">
		<input type="hidden" name="id" value="${query.id}">
		<div>
			<span>标题</span>
			<input style="width:600px;" name="title" id="title" value="${query.title}">
		</div>
		<br>
		
		<span>内容(字数&lt;20K)</span>
		<div>
			<textarea  name="content" id="content" rows="10" cols="100">${query.content}</textarea>
		</div>
		<br>
		<input type="submit" value="发表" class="btn" onclick="return doSubmit();">
	</form>
</div>
</jsp:body>
</s:site>