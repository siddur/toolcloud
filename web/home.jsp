<%@ taglib prefix="s" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:site>
<jsp:attribute name="headPart">
<meta name="keywords" content="toolcloud， 在线工具， 工具云， 工具集合， online tools"/>
<meta name="description" content="toolcloud是一个上传、管理和运行工具以及以多种方式显示运行结果的服务平台"/>
<style>
	#description{
		white-space: pre-wrap; 
		margin:10px;
		font-size: 13px;
	}
</style>
</jsp:attribute>
<jsp:body>
<div id="middle">
	<div class="static_window divide1">
		<pre id="description">  工具云是一个上传、管理和运行工具以及以多种方式显示运行结果的服务平台。ToolCloud不同于AppStore，它不是用来运行应用（App）的容器。应用是一个完整的软件程序。一个游戏、一个聊天工具、一个计算器都是应用。应用通常包含的代码量较大，而工具往往只有一个代码文件或编译后的文件。

  如果您想把一批文件的编码由ANSI改为Unicode，您可能需要使用Python、Perl、Groovy甚至Java编写一个小程序。这样的小程序就是本文所说的工具。试想您的这个工具使用python写的，运行的效果非常棒，您所在的团队都非常喜欢使用，因为它能极大的提供他们的工作效率。问题来了，他们都是某些业务人员，对程序一窍不通。他们甚至从来没有听说过Python为何物，更不知道如何安装Python环境、如何执行Python脚本。您可能会说：培训。

  可是您又碰到了一个需求，您的同事需要将一批表格数据生成图表。您知道使用Java第三方库JFreechart非常容易。您开发了这样一个工具，现在又要给同事培训安装Java环境、执行Java程序。而您的同事已经很厌烦他们需要掌握如此多而复杂的软件知识，并且电脑里需要安装如此多的他们不熟悉甚至不信任的环境。

  这时您真的需要一个服务器，安装所有工具运行的环境，提供web接口，让您的同事通过浏览器访问。ToolCloud应运而生了，它提供统一的参数输入界面、运行按钮以及统一的结果输出界面，用户不必关心工具是由什么语言开发。

  您的同事中有人擅长您不熟悉语言，他开发的工具也可以注册到ToolCloud上，供您和其他同事使用。ToolCloud的工具越来越多，它们被有效的分类管理。</pre>
	</div>
	
	<div class="static_window divide2">
		<div class="w_title">
			<span>在线工具</span>
			<a href="#">发布工具</a>
			<a class="more" href="/ctrl/tool/list">更多&gt;&gt;</a>
		</div>
		<div class="w_list">
		<c:forEach var="item" items="${latest}">
			<div class="tool_item">
				<a href="/${item.descriptor.pluginID}.html">
					<span class="title">
						<span class="name">[${item.descriptor.pluginName}]</span>
						<span>${item.descriptor.description}</span>
					</span>
				</a>
			</div>
		</c:forEach>
		</div>
	</div>
	<div class="static_window divide2">
		<div class="w_title">
			<span>开源信息</span>
			<a href="/ctrl/res/toadd">发布信息</a>
			<a class="more" href="/ctrl/res/list?type=3">更多&gt;&gt;</a>
		</div>
		<div class="w_list">
		<c:forEach var="item" items="${news}">
			<div class="tool_item">
				<a href="/res/${item.id}.html">
					<span class="title">
						<span>${item.title}</span>
					</span>
				</a>
			</div>
		</c:forEach>
		</div>
	</div>
	<div class="static_window divide2">
		<div class="w_title">
			<span>博客</span>
			<a href="/ctrl/res/toadd">发布博客</a>
			<a class="more" href="/ctrl/res/list?type=1">更多&gt;&gt;</a>
		</div>
		<div class="w_list">
		<c:forEach var="item" items="${blogs}">
			<div class="tool_item">
				<a href="/res/${item.id}.html">
					<span class="title">
						<span>${item.title}</span>
					</span>
				</a>
			</div>
		</c:forEach>
		</div>
	</div>
	<div class="static_window divide2">
		<div class="w_title">
			<span>需求讨论</span>
			<a href="/ctrl/res/toadd">发布需求</a>
			<a class="more" href="/ctrl/res/list?type=2">更多&gt;&gt;</a>
		</div>
		<div class="w_list">
		<c:forEach var="item" items="${needs}">
			<div class="tool_item">
				<a href="/res/${item.id}.html">
					<span class="title">
						<span>${item.title}</span>
					</span>
				</a>
			</div>
		</c:forEach>
		</div>
	</div>
</div>
</jsp:body>
</s:site>