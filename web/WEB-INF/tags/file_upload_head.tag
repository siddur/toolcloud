<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="multiple" required="false" type="java.lang.Boolean" %>
<c:if test="${multiple == null }">
	<c:set var="multiple" value="false"></c:set>
</c:if>
<c:if test="${file_upload_loaded != true }">
	<c:set var="file_upload_loaded" value="true" scope="request"></c:set>
		<style>
			.file_input{
				opacity: 0.01;
				cursor:pointer;
				width:100px; 
				border: none;
				left : -108px;
				position: relative; 
			}
			.upload_btn{
				padding: 3px 0;
				display: inline-block;
			}
			.file_item{
				font-size:12px;
				display: inline-block;
				position: relative;
				top:-10px;
				left:5px;
			}
			
			.file_item .ui-icon{
				display: inline-block;
			    left: -5px;
			    position: relative;
			    top: 3px;
			}
		</style>
		<div style="display: none">
			<form id="file_form" method="post" enctype="multipart/form-data" target="uploadIframe">
			</form>
			<iframe id="uploadIframe" name="uploadIframe" src="about:blank"></iframe>
		</div>
		<script>
			var fileItemDiv = "";
			var uploading = false;
			var who = null; // who is uploading
			var fieldname;
			var oldParent = null;
			var multiple = ${multiple};
			var isImage = false;
			$(document).ready(function(){
				$("#uploadIframe").load(function(){
					if(uploading == false){
						return;
					}
					var responseText = $(window.frames["uploadIframe"].document.body).text();
					uploading = false;
					oldParent.append(who); //move back
					if(responseText){
						var fileArray = eval(responseText);
						appendFile(fileArray[0], who.parent().parent());
					}
	
					who = null;
				});
			});

			function appendFile(file, container){
				var url = "/toolcloud/file/" + file.filepath;
				if(container.children().length == 1 || multiple){
					var fileItemDiv = createFileComponent();
					container.append(fileItemDiv);
				}
				var hidden = container.children().last().children().first();
				hidden.val(file.filepath);
				var a = hidden.next();
				if(isImage){
					//<img>
					a.attr('src', url);
				}else{
					//a.attr('href', url);
					a.html(file.filename);
				}
			}

			function selectFile(obj, _isImage){
				if(uploading){
					alert("Another file is uploading.");
					return;
				}
				who = $(obj);
				fieldname = obj.id;
				isImage = _isImage;
				oldParent = $(obj).parent();
				var theForm = $("#file_form");
				theForm.append(who); //move into form

				var action = "/toolcloud/file/";
				if(isImage){
					action += "image";
				}
				theForm.attr("action", action).submit();
				theForm.submit();
				uploading = true;
			}

			
			function dele(obj){
				$(obj).parent().remove();
			}

			function createFileComponent(){
				var append = [];
				_startFileComponent(append);
				_createHiddenComponent(append);
				_createFileDisplayComponent(append);
				if(multiple)
					_createCloseBtn(append);
				_endFileComponent(append);
				return append.join('\n');
			}
			
			function _startFileComponent(append){
				append.push('<div class="file_item">');
			}
			function _endFileComponent(append){
				append.push('</div');
			}

			function _createHiddenComponent(append){
				append.push('<input type="hidden" name="'+ fieldname +'">');
			}
			
			function _createFileDisplayComponent(append){
				if(isImage){
					append.push('<img>');
				}else{
					//append.push("<a><a>");
					append.push('<span></span>');
				}
			}

			function _createCloseBtn(append){
				append.push('<span class="ui-icon ui-icon-close" onclick="dele(this)"></span>');
			}
		</script>
		
	</div>
</c:if>
