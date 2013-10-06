Console = function(url){
	this._init = function(){
		this.url = url;
		this.active = true;
		this.times = 0;
		this.console = $('.console-body');
		this.console.empty();
	}
	
	this.log = function(msg){
		msg = msg.replace(/<file-url>/g, "/file/output/")
		this.console.append(msg);
		this.console.scrollTop(this.console[0].scrollHeight);
	};
	
	this._fetchOne = function(){
		var doLog = this.log;
		var set = {
			url : url,
			async : false
		}
		var response = $.ajax(set);
		return response.responseText;
	};
	
	
	this.loop = function(){
		this.times += 1;
		this._log();
		if(this.active && this.times < 100){
			setTimeout.call(this, this.loop, 100);
		}
	}
	
	this._log = function(){
		var msg = this._fetchOne();
		if(msg){
			this.log(msg);
		}
	}
	
	this.close = function(){
		setTimeout.call(this, this._log, 100);
		this.active = false;
	}
	
	this._init();
}

var __nativeST__ = window.setTimeout;
window.setTimeout = function (vCallback, nDelay /*, argumentToPass1, argumentToPass2, etc. */) {
  var oThis = this, aArgs = Array.prototype.slice.call(arguments, 2);
  return __nativeST__(vCallback instanceof Function ? function () {
    vCallback.apply(oThis, aArgs);
  } : vCallback, nDelay);
};

