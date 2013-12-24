<%@ tag language="java" pageEncoding="UTF-8"%>


<script type="text/javascript" src="${root}/jqueryui/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="${root}/jqueryui/treeview/jquery.treeview.js"></script>
<link rel="stylesheet" href="${root}/jqueryui/treeview/jquery.treeview.css" />
<style>
	.file-browser{
		height: 600px;
		font-size: 11px;
		visibility: hidden;
	}
	.navigator-parent{
		width: 200px;
		height: 100%;
		float: left;
		margin-right:5px;
		border: 1px solid #AAAAAA;
		border-bottom-right-radius: 4px;
		border-bottom-left-radius: 4px;
		border-top-right-radius: 4px;
		border-top-left-radius: 4px;
		background-color: white;
		font-family: Verdana,helvetica,arial,sans-serif;
	}
	.browser{
		overflow: auto;
		word-wrap:normal;
		height:570px;
	}
	.viewer{
		height: 100%;
		background: white;
		float: left;
		width: 720px;
	}
	.viewer pre{
		height:540px;
		overflow: auto;
		clear: left;
	}
	.ui-tabs-nav {
		height: 28px;
		background: none;
		font-size: 11px;
		border:none;
		border-bottom: 1px solid #AAAAAA;
	}
	.ui-tabs .ui-tabs-panel{
		font-size: 12px;
		padding: 5px;
		white-space: pre-wrap;
		
	}
	.ui-tabs .ui-tabs-nav li a.ui-tabs-anchor{
		text-overflow: ellipsis;
		overflow: hidden;
		padding: 0.5em 0 0.5em 0.8em;
		width: 80px;
		word-wrap:normal;
	}
	.ui-icon-close { 
		float: left; 
		margin: 0.1em 0.1em 0 0; 
		cursor: pointer; 
	}
	.ui-tabs{
		padding:0;
	}
	.toggle .ui-button-text {
	    padding: 0.2em 1em;
	}
	.file{
		cursor: default;
	}
	.file:hover{
		color: blue;
	}
</style>
<script>

//for tab
var increment = 100;

//click the tree node(<li>)
var itemClick = function(){
	var me = $(this);
	var file = me.attr("url");
	var t = me.children().first().html();
	var id = me.attr("fileId");
	
	var exist = false;
	
	var tabTitles = $(".viewer ul li");
	tabTitles.each(function(idx, item){
		if(id == $(item).attr("id")){
			exist = true;
			$(".viewer").tabs("option", "active", idx);
		}
	});
	
	if(!exist){
		addTab(file, id, t, tabTitles.length);
	}
}

var updateHeight = function(){
	$(".viewer pre").height(($(".viewer").height() - $(".viewer pre:visible").position().top) - 25);
}

var addTab = function(fileUrl, id, text, index){
	var tabId = "tab-" + (increment++);
	$(".viewer ul").append('<li id="'+id+'"><a href="#'+tabId+'">'+text+'</a><span class="ui-icon ui-icon-close"></span></li>');
	$(".viewer").tabs("refresh");
	var load = function(file){
		$('<pre id="'+tabId+'"></pre>').appendTo(".viewer").text(file);
		$(".viewer").tabs("refresh");
		$(".viewer").tabs("option", "active", index);
		updateHeight();
	}
	$.get("${root}/ctrl/file/file", {path: fileUrl}, load);
}

var initTree = function(url, context){
	var me = $(context);
	$.get("${root}/ctrl/file/dir", {path: url}, function(fileModel){
		var fileModel = $.parseJSON(fileModel);
		var context = me.find(".browser");
		//reset tree
		context.empty();
		$(".toggle").unbind();

		initNode(context, fileModel);

		$(".file-browser").css("visibility", "visible");
		$(".toggle").button();
		context.treeview({
			control:"#treecontrol"
		});
		context.find("li").each(function(){
			var me = $(this);
			if(me.children().length == 1){
				me.click(itemClick);
			}
		})

		var tabs = $(".viewer").tabs();
		tabs.delegate( "span.ui-icon-close", "click", function() {
			var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
			$( "#" + panelId ).remove();
			tabs.tabs( "refresh" );
			updateHeight();
		});
	});
}

var initNode = function(parent, fileModel){
	var folder = $('<li url="'+fileModel.url+'"></li>').appendTo(parent).attr("fileId", "f" + fileModel.id);
	if(fileModel.isDir){
		$('<span class="folder">'+ fileModel.name +'</span>').appendTo(folder);
		if(fileModel.children.length > 0){
			var ui = $('<ul></ul>').appendTo(folder);
			for(var i = 0; i < fileModel.children.length; i++){
				initNode(ui, fileModel.children[i]);
			}
		}
	}
	else{
		$('<span class="file">'+ fileModel.name +'</span>').appendTo(folder);
	}
}


</script>
<div class="file-browser">
	<div class="navigator-parent">
		<div class="navigator">
			<div id="treecontrol" style="display:block;  padding:4px; margin:0; border-bottom:solid 1px gray;">
				<a class="toggle" href="#">收起</a>
				<a class="toggle" href="#">展开</a>
			</div>
			<ul class="browser filetree">
			</ul>
		</div>
	</div>
	<div class="viewer">
	 	<ul></ul>
	</div>
</div>

