<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="basicSearch">
	<input type="text" name="basicKeword" id="basicKeword" class="basicSearchInput" value="search tool" onfocus="clearTip(this)" onblur="showTip(this)">
	<span class="ui-icon ui-icon-search basic_search_btn"
		onclick="basicSearch()"></span>
</div>

