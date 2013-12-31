<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="search">
	<span class="ui-icon-search"
		onclick="TC.Search.search()"></span>
	<input type="text" id="key" name="key" 
		class="searchInput"
		value="输入关键字搜索工具"
		onfocus="TC.Search.clearTip(this)" 
		onblur="TC.Search.showTip(this)">
</div>

