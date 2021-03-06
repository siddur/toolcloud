var Color = function(h, s, l){
	this.h = h;
	this.s = s;
	this.l = l;
	this.r = null;
	this.g = null;
	this.b = null;
	
	this._rgb2hsl = function(r, g, b){
		r = r/255;
		g = g/255;
		b = b/255;
		var max, min, diff, r_dist, g_dist, b_dist;
		max = Math.max(Math.max(r, g), b);
		min = Math.min(Math.min(r, g), b);
		diff = max - min;
		var h, s, l;
		l = (max + min)/2;
		if(diff == 0){
			s = 0;
			h = 0;//undefine
		}
		else{
			if(l < 0.5){
				s = diff/(max + min);
			}
			else{
				s = diff/(2 - max - min);
			}
			r_dist = (max - r)/diff;
			g_dist = (max - g)/diff;
			b_dist = (max - b)/diff;
			if(r == max){
				h = b_dist - g_dist;
			}
			else if(g == max){
				h = 2 + r_dist - b_dist;
			}
			else {
				h = 4 + g_dist - r_dist;
			}
			
			h = h * 60;
			h = Math.round(h);
			if(h < 0){
				h += 360;
			}
			else if(h >= 360){
				h -= 360;
			}
		}
		return [h, s, l];
	}
	this._hsl2rgb = function(h, s, l){
		var r, g, b;
		if(s == 0){
			r = g = b = l*255;
		}
		else{
			var q = (l<0.5)?(l * (1.0+s)):(l+s - (l*s));
			var p = (2.0 * l) - q;
			var Hk = h/360.0;
			var T = [];
			T[0] = Hk + 0.3333333;
			T[1] = Hk;
			T[2] = Hk - 0.3333333;
			for(var i=0; i<3; i++){
				if(T[i] < 0) T[i] += 1.0;
				if(T[i] > 1) T[i] -= 1.0;
				if((T[i]*6) < 1){
					T[i] = p + ((q-p)*6.0*T[i]); 
				}
				else if((T[i]*2.0) < 1){
					T[i] = q; 
				}
				else if((T[i]*3.0) < 2){
					T[i] = p + (q-p) * ((2.0/3.0) - T[i]) * 6.0;
				}
				else{
					T[i] = p;
				}
			}
			r = T[0]*255;  
	        g = T[1]*255;  
	        b = T[2]*255;
	        
	        r = (r>255)? 255 : ((r<0)?0 : r);
	        g = (g>255)? 255 : ((g<0)?0 : g);
	        b = (b>255)? 255 : ((b<0)?0 : b);
		}
		r = Math.round(r);
        b = Math.round(b);
        g = Math.round(g);
		return [r, g, b];
	}
	
	this._hex2rgb = function(hex){
		var h = [];
		h[0] = hex.substring(1,3);
		h[1] = hex.substring(3,5);
		h[2] = hex.substring(5,7);
		var r, g, b;
		r = parseInt(h[0], 16);
		g = parseInt(h[1], 16);
		b = parseInt(h[2], 16);
		return [r, g, b];
	}
	
	this._rgb2hex = function(r, g, b){
		r = r.toString(16);
		if(r.length == 1){
			r = "0" + r;
		}
		g = g.toString(16);
		if(g.length == 1){
			g = "0" + g;
		}
		b = b.toString(16);
		if(b.length == 1){
			b = "0" + b;
		}
		return "#" + r + g + b;
	}
	
	
	this.updateHSL = function(){
		var a = this._hsl2rgb(this.h, this.s, this.l);
		this.r = a[0];
		this.g = a[1];
		this.b = a[2];
	}
	this.updateRGB = function(){
		var a = this._rgb2hsl(this.r, this.g, this.b);
		this.h = a[0];
		this.s = a[1];
		this.l = a[2];
	}
	
	this.toRGBString = function(){
		return this._rgb2hex(this.r, this.g, this.b);
	}
	
	this.toHSLString = function(){
		return "hsl("+this.h+","+(this.s * 100)+"%,"+(this.l * 100)+"%)"
	}
	
	this.setRGB = function(r, g, b){
		this.r = r;
		this.g = g;
		this.b = b;
		this.updateRGB();
	}
	
	this.setR = function(r){
		this.r = r;
		this.updateRGB();
	}
	this.setG = function(g){
		this.g = g;
		this.updateRGB();
	}
	this.setB = function(b){
		this.b = b;
		this.updateRGB();
	}
	this.setHSL = function(h, s, l){
		this.h = h;
		this.s = s;
		this.l = l;
		this.updateHSL();
	}
	
	this.setH = function(h){
		this.h = h;
		this.updateHSL();
	}
	this.setS = function(s){
		this.s = s;
		this.updateHSL();
	}
	this.setL = function(l){
		this.l = l;
		this.updateHSL();
	}
}
Color.rgb = function(r, g, b){
	var c = new Color();
	c.setRGB(r, g, b);
	return c;
}

/**
 * {color, applyTo}
 */
var ColorPicker = function(dialog, options){
	var applyTo = options.applyTo;
	var pick = options.pick;
	var This = this;
	var jqueryObj = $("#" + applyTo);
	
	this.color = options.color ? options.color : new Color(360, 1, 0.5);
	jqueryObj.click(function(){
		dialog.open();
		dialog.setTarget(This);
	});
	this.onpick = function(){
		this.setBackgroundColor();
		if (pick) {
			pick();
		}
	}
	
	this.setBackgroundColor = function(){
		jqueryObj.css("background-color", this.color.toRGBString());
	}
	
	
	this.setBackgroundColor();
}

ColorPickerPanel = function(_color){
	var color = _color ? _color : new Color(360, 1, 0.5);
	var _init = function(){
		$("#color_container").click(function(a){
			var offset = $(a.target).offset();
			var i = a.pageX - offset.left;
			var j = a.pageY - offset.top;
			pick(i, j);
		});
		
		var HSL_L = $(".HSL_L");
		for (var i=0; i < 250; i = i + 5) {
			var hsl = "hsl("+color.h+", "+(color.s*100)+"%, (i/2.5)%)";
			HSL_L.append("<div style='height:5px;'></div>");
		};
		updateSlider(color.h, color.s);
		var initTop = -8;
		var len = 250;
		var arrow = $("#arrow");
		arrow.css("top", len/2 + initTop);
		var down = false;
		HSL_L.mousedown(function(e){
			down = true;
			var distance = e.clientY - HSL_L.offset().top; 
			arrow.css("visibility", "visible");
			arrow.css("top", distance + initTop);
			slide(distance / len);
		});
		$(document).mouseup(function(e){
			arrow.css("visibility", "hidden");
			down = false;
		});
		
		$(document).mousemove(function(e){
			if(down){
				var distance = e.clientY - HSL_L.offset().top; 
				if(distance <= 250 && distance >= 0){
					arrow.css("top", distance + initTop);
					slide(distance / len);
				}
			}
		});
		
		$(".input input").blur(function(e){
			var id = e.target.id;
			var v = e.target.value;
			if(isNaN(v)){
				error("not a number");
				return;
			}
			
			if(id == "R" || id == "B" || id == "G"){
				v = parseInt(v);
				if(v < 0 || v > 255){
					error("0-255");
					return;
				}
			}else if(id == "S" || id == "L"){
				v = parseFloat(v);
				if(v > 1 || v < 0){
					error("0-1");
					return;
				}
			}else if(id == "H"){
				v = parseInt(v);
				if(v > 360 || v < 0){
					error("0-360");
					return;
				}
			}
			color["set" + id](v);
			show();
		});
		
		//grb
		var colorMatrix = [
			[255, 128, 128],[255, 255, 128],[128, 255, 128],[0, 225, 128],[128, 225, 225],[0, 128, 255],[255, 128, 192],[255, 128, 255],
			[255, 0, 0],[255, 255, 0],[128, 255, 0],[0, 225, 64],[0, 225, 225],[0, 128, 192],[128, 128, 192],[255, 0, 255],
			[128, 64, 64],[255, 128, 64],[0, 255, 0],[0, 128, 128],[0, 64, 128],[128, 128, 255],[128, 0, 64],[255, 0, 128],
			[128, 0, 0],[255, 128, 0],[0, 128, 0],[0, 128, 64],[0, 0, 225],[0, 0, 160],[128, 0, 128],[128, 0, 255],
			[64, 0, 0],[128, 64, 0],[0, 64, 0],[0, 64, 64],[0, 0, 128],[0, 0, 64],[64, 0, 64],[64, 0, 128],
			[0, 0, 0],[128, 128, 0],[128, 128, 64],[128, 128, 128],[64, 128, 128],[192, 192, 192],[64, 0, 64],[255, 255, 255]
		];
		var total = 48
		var colors = $(".colors");
		for (var i=0; i < 48; i++) {
			var r = colorMatrix[i][0];
			var g = colorMatrix[i][1];
			var b = colorMatrix[i][2];
			var rgb = "rgb("+r+","+g+","+b+")"
		  	colors.append("<div class='color' onclick='ColorPickerPanel.select("+r+", "+g+", "+b+")' style='background-color:"+rgb+"'></div>")
		};
		
		color.updateHSL();
		show();
	}


	ColorPickerPanel.select = function(r, g, b){
		color.setRGB(r,g,b);
		show();
		updateSlider(color.h, color.s);
	}
	
	var error = function(msg){
		alert(msg);
	}
	
	var slide = function(distance){
		color.setL(distance);
		show();
	}
	var updateSlider = function(h, s){
		var slider = $(".HSL_L");
		slider.children().each(function(idx, item){
			var hsl = "hsl("+h+", "+(s*100)+"%, "+(idx*2)+"%)";
			$(item).css("background-color", hsl);
		});
	}
	var pick = function(i, j){
		var h = i;
		var s = (250-j) / 250;
		color.setHSL(h, s, 0.5);
		show();
		updateSlider(color.h, color.s);
	}
	
	var show = function(){
		$("#H").val(color.h);
		$("#S").val(color.s);
		$("#L").val(color.l);
		
		$("#R").val(color.r);
		$("#B").val(color.b);
		$("#G").val(color.g);
		
		var rgb = color.toRGBString();
		$(".current_color").css("background-color", rgb);
		$("#result").val(rgb);
	}
	this.repaint = function(){
		show();
	}
	this.setColor = function(_color){
		color = _color;
	}
	var getHSL = function(i, j){
		var h = i;
		j = 250 - j;
		var s = Math.floor(j / 2.5) + "%";
		return "hsl("+h+","+s+","+(color.l * 100)+"%)"
	}
	
	_init();
}

/**
 * {color, applyTo}
 */
ColorPickerDialog = function(_color){
	var color = _color ? _color : new Color(360, 1, 0.5);
	var panel = new ColorPickerPanel(_color);
	var target = null;
	
	this.open = function(){
		$( "#colorpicker_dialog" ).dialog("open");
	}
	this.setTarget = function(_target){
		color = _target.color;
		target = _target;
		panel.setColor(color);
		panel.repaint();
	}
	var close = function(){
		$( "#colorpicker_dialog" ).dialog("close");
	}
	var _init = function(){
		$( "#colorpicker_dialog" ).dialog({
			autoOpen: false,
			resizable:false,
			width:410,
			height:455,
			modal:true,
			title:"Color Picker"
		});
		$(document).keydown(function(e){
			if(e.keyCode == 13){
				pickColor();
			}
		})
		$("#color_container").dblclick(function(e){
			pickColor();
		});
		
		$("#pcd_confirm").click(function(){
			pickColor();
		})
		$("#pcd_cancel").click(function(){
			close();
		})
	}
	var pickColor = function(){
		close();
		if(target.onpick){
			target.onpick();
		}
	}
	_init();
}
