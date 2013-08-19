<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${file_upload_loaded != true }">
	<c:set var="file_upload_loaded" value="true" scope="request"></c:set>
	<div style="display: none;" id="upload_div">
		<form id="upload_form" method="post" enctype="multipart/form-data" target="uploadIframe">
			<input type="file" name="file_input" id="file_input">
			<input type="submit">
		</form>
		<iframe id="uploadIframe" name="uploadIframe" src="about:blank"></iframe>
		<script>
			var uploading = false;
			var who = null; // who is uploading
			$(document).ready(function(){
				$("#uploadIframe").load(function(){
					var responseText = $(window.frames["uploadIframe"].document.body).text();
					uploading = false;
					
					if(responseText){
						var fileArray = eval(responseText);
						var fileList = $(".file-list");
						var url = fileArray[0].filepath;
						$(who).next().val(url);
						var target = $(who).next().next();
						if(target[0].tagName == "IMG"){
							//var home = "http://" + location.host + (location.port ? ":" + location.port : "");
							url = url.replace("/fileserver", "");
							target.attr("src", /*home + */url);
						}else{
							target.html(fileArray[0].filename);
						}
					}
	
					who = null;
				});
			});
			function upload(obj, isImg){
				if(uploading){
					alert("Another file is uploading.");
					return;
				}
				who = obj;
				var fileInput = $("#file_input");
				fileInput.change(function(){
					fileInput.unbind();
					var action = "/toolcloud/file/";
					if(isImg){
						action += "image";
					}
					$("#upload_form").attr("action", action).submit();
					uploading = true;
				});
				fileInput.click();
			}
		</script>
	</div>
</c:if>
