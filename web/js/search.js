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
	window.location = "/toolcloud/ctrl/tool/list?&key=" + basicKeword;
}

$(document).ready(function(){
	$("#basicKeword").keypress(function(evt){
		if(evt.keyCode == 13){
			basicSearch();
		}	
	});
});