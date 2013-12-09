//require jquery1.9.1
ToolCloud = {
	root: ""
};
TC = ToolCloud;

//Verification Code(Captcha)
ToolCloud.Captcha = {
	verify: function(code, callback){
		var url = ToolCloud.root + "/ctrl/util/checkcode?authenticode=" + $.trim(code);
		$.get(url, function(result){
			eval('var r = ' + result);
			var isOk = (r.type == "ok");
			if(callback){
				callback(isOk);
			}else{
				if(isOk){
					$("#captcha").css("border", "none");
				}else{
					$("#captcha").css("border", "solid 1px red");
				}
			}
		})
	},
	
	change: function(){
		var img = $("#captcha");
		img.attr("src", ToolCloud.root + "/ctrl/util/authenticode?a=" + new Date().getMilliseconds());
	}
}
$(document).ready(ToolCloud.Captcha.change);
