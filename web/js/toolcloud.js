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


//search
ToolCloud.Search = {
	clearTip: function(obj){
		obj.value = "";
	},
	
	showTip: function(obj){
		if(!obj.value){
			obj.value = "输入关键字搜索工具";
		}
	},
	
	search: function() {
		var keyword = $("#keyword").value;
		window.location = ToolCloud.root + "/ctrl/tool/list?&key=" + keyword;
	},
	
	keyEntry: function(){
		$("#keyword").keypress(function(evt){
			if(evt.keyCode == 13){
				ToolCloud.Search.search();
			}
		});
	}
}
$(document).ready(ToolCloud.Search.keyEntry);

//light window
ToolCloud.LightWindow = {
	show: function(e){
		e.stopPropagation();
		var target = $(this);
		var win = target.find(".light_window");
		if(win.css("visibility") == "visible"){
			win.css("visibility", "hidden");
			return;
		}
		
		var x = y = 0;
		if(win.offsetParent().get(0) != target.get(0)){
			var targetOffset = target.position();
			var left = targetOffset.left;
			var top = targetOffset.top;
			var x =+ left;
			var y =+ top;
		}
		
		y += target.height();
		win.css("top", y).css("left", x).css("visibility", "visible");
		
		$(document).one("click", ToolCloud.LightWindow.hide);
	},
	
	hide: function(){$(".light_window").css("visibility", "hidden");}
}
$(document).ready(function(){
	$(".light_window").parent().click(ToolCloud.LightWindow.show);
});

//heavy window
ToolCloud.HeavyWindow = {
	windows: {},
	show: function(op){
		if(op == null || op.id == null){
			return;
		}
		var win = this.windows[op.id];
		if(win == null){
			var content = $("#" + op.id);
			content.addClass("w_content").show();
			var w = content.width();
			var h = content.height();
			var left = (window.innerWidth - w)/2;
			var top = (window.innerHeight - h)/2 - 80;
			win = $("<div class='heavy_window'></div>");
			if(op.title){
				win.append("<div class='w_title'>" + op.title + "</div>");
			}
			win.append(content)
				.append("<div class='w_bar'><span class='w_confirm btn'>确定</span><span class='w_cancel btn'>取消</span></div>")
				.appendTo(document.body)
				.css("top", top)
				.css("left", left);
			this.windows[op.id] = win;
			
			if(op.confirm)
				win.find(".w_confirm").click(op.confirm);
			win.find(".w_cancel").click(function(){$(this).parents(".heavy_window").hide()});
			if(op.movable){
			var tDiv = win.find(".w_title");
				tDiv.mousedown(function(e){
					e.stopPropagation();
					var p1 = win.offset();
					var x1 = e.clientX;
					var y1 = e.clientY;
					
					var move = function(e){
						e.stopPropagation();
						x0 = e.clientX - x1;
						y0 = e.clientY - y1;
						
						if(Math.abs(x0) < 10 && Math.abs(y0) < 10){
							return;
						}
						x1 = e.clientX;
						y1 = e.clientY;
						p1 = {left: p1.left + x0, top: p1.top + y0};
						win.css("left", p1.left).css("top", p1.top);
					}
					
					var up = function(e){
						$(document.body).unbind("mousemove", move);
						e.stopPropagation();
						x0 = e.clientX - x1;
						y0 = e.clientY - y1;
						
						x1 = e.clientX;
						y1 = e.clientY;
						p1 = {left: p1.left + x0, top: p1.top + y0};
						win.css("left", p1.left).css("top", p1.top);
					}
					$(document.body).on("mousemove", move).one("mouseup", up);
				});
			}
		}
		win.show();
		//return win;
	}
}
$(document).ready(function(){
/*	$(".accordion a").click(function(){
		var msg = $(this).html();
		ToolCloud.HeavyWindow.show({
			id: "testWin",
			title: "testWIN",
			confirm: function(){alert(msg)}
		});
	});
*/
});

//menu submenu
$(document).ready(function(){
	$("#menu > li")
		.mouseover(function(){
			$(this).find(".sub_menu").show();
		})
		.mouseout(function(){
			$(this).find(".sub_menu").hide();
		})
});
