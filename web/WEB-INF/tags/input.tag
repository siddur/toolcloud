<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="inputModel" type="siddur.tool.core.data.DataTemplate"%>
<%@ attribute name="index" type="java.lang.Integer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<c:set var="type" value="${inputModel.dataType}"/>
<c:set var="tag" value="${inputModel.tag}"/>
<c:set var="desc" value="${inputModel.description}"/>
<c:if test="${input_parts_loaded != true }">
	<c:set var="input_parts_loaded" value="true" scope="request"></c:set>
	
	<style>
		.ui-datepicker{
			font-size: 62.5%;
			font-family: "Trebuchet MS", "Arial", "Helvetica", "Verdana", "sans-serif"; 
		}
		.splitter_container{
			padding-left:40px;
		}
	</style>
	<script>
		var optionsArray = [];
		$(function(){
			var dateField = $( ".dateField" );
			if(dateField.length){
				dateField.datepicker({
					dateFormat: "yy-mm-dd"
				});
			}
			
			
			$(".batch").change(function(_event){
				var obj = _event.target;
				var id = obj.id;
				var span = $(obj).next().next();
				if(obj.checked){
					span.css("display", "inline");
					span.children().last().attr("name", "splitter" + id);
				}else{
					span.css("display", "none");
					span.children().last().attr("name", null);
				}
			});
		});
		var validateResult;
		function validate(){
			validateResult = true;
			$(".error").removeClass("error");
			$("[name='input']").each(doValidate);
			return validateResult;
		}

		function doValidate(idx, item){
			var tag = item.tagName;
			var v = item.value.trim();
			if(!v){
				showError(item);
				return false;
			}

			var i = $(item);
			if(i.hasClass("INTEGER")){
				if(isNaN(v) || v.indexOf(".") > -1){
					showError(item);
					alert("Not a integer input");
					return false;
				}
			}
			else if(i.hasClass("DOUBLE")){
				if(isNaN(v)){
					showError(item);
					alert("Not a double input");
					return false;
				}
			}
		}

		function showError(item){
			validateResult = false;

			if(item.type == "hidden"){
				$(item).next().addClass("error");
			}else{
				$(item).addClass("error");
			}
		}
		
		function changeOption(obj, index){
			var i = obj.selectedIndex;
			obj.nextElementSibling.innerHTML = optionsArray[index][i];
		}
		
	</script>
</c:if>
	<script>
	<c:if test="${inputModel.constrantType=='ENUM'}">
		(function(){
			var options = [];
			<c:forEach var="item" items="${inputModel.options}">
				options.push("${item[1]}");
			</c:forEach>
			optionsArray[${index}] = options;
		})();
	</c:if>
	</script>

<div class="input_item">
	<c:if test='${tag != null && desc != "" }'>
		<div class="input_description">${desc}</div>
	</c:if>
	<c:if test='${tag != null && tag != "" }'>
		<span class="label">${tag}</span>
	</c:if>
	<c:choose>
		<c:when test="${inputModel.constrantType=='ENUM'}">
			<select name="input" class="input" onchange="changeOption(this, ${index})">
			<c:forEach var="item" items="${inputModel.options}">
				<option value="${item[0]}">${item[0]}</option>
			</c:forEach>
			</select>
			<span></span>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${'STRING' == type
					||'INTEGER' == type
					||'DOUBLE' == type
					}">
					<input type="text" class="input ${type}" name="input">
				</c:when>
				<c:when test="${'BOOLEAN' == type }">
					<input type="checkbox" class="input" value="1" name="input" style="position: relative; top: 2px;">
				</c:when>
				<c:when test="${'TEXT' == type}">
					<div><textarea class="input" style="width:99%" rows="3" name="input"></textarea></div>
					<div>
						<input type="checkbox" class="batch" name="batch" value="${index}" id="ch_${index}">
						<label for="ch_${index}">批量输入</label>
						<span id="batch${index}" class="splitter_container" style="display:none">
							<label for="select_${index}">分割符:</label>
							<select id="select_${index}" >
								<option value="0">逗号</option>
								<option value="1">分号</option>
								<option value="2">换行符</option>
								<option value="3">|||</option>
							</select>
						</span>
					</div>
				</c:when>
				<c:when test="${'FILE' == type || 'ZIPFILE' == type}">
					<s:file_upload fieldname="input" displayname="上传文件.."/>
				</c:when>
				<c:when test="${'IMAGE' == type}">
					<s:file_upload fieldname="input" displayname="上传图片.." isImage="true"/>
				</c:when>
				<c:when test="${'DATE' == type}">
					<input type="text" class="dateField" readonly="readonly">
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>
</div>