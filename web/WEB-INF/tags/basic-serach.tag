<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.basicSearch {
    background-color: white;
    border-radius: 10px 10px 10px 10px;
    margin-top: 1px;
    padding: 1px 1px 1px 8px;
    width: 200px;
}

.basicSearchInput {
	border: none;
	width: 180px;
	color: #9B9B9B;
}

.basicSearchInput:focus {
	color: black;
}

.basic_search_btn {
	float: right;
	position: relative;
    right: 3px;
    top: 1px;
}

.basicSearch span {
	cursor: pointer;
}
</style>
<script>
	function clearTip(obj){
		obj.value = "";
	}

	function showTip(obj){
		if(!obj.value){
			obj.value = "seach tool";
		}
	}

	function basicSearch() {
		var basicKeword = document.getElementById("basicKeword").value;
		window.location = "/tool/ctrl/search/basicsearch?&basicKeword=" + basicKeword;
	}
	
	$(document).ready(function(){
		$("#basicKeword").keypress(function(evt){
			if(evt.keyCode == 13){
				basicSearch();
			}	
		});
	});
	
</script>
<div class="basicSearch">
	<input type="text" name="basicKeword" id="basicKeword" class="basicSearchInput" value="search query" onfocus="clearTip(this)" onblur="showTip(this)">
	<span class="ui-icon ui-icon-search basic_search_btn"
		onclick="basicSearch()"></span>
</div>

