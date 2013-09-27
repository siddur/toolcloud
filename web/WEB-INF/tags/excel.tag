<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="containerId" required="true" type="java.lang.String"%>

<c:if test="${table_tag != true }">
	<c:set var="table_tag" value="true" scope="request"></c:set>
	<script src="/toolcloud/js/ZeroClipboard.min.js"></script>
	<script src="/toolcloud/js/table.js"></script>
	<link rel="stylesheet" href="/toolcloud/css/table.css"/>
</c:if>
<div id="${containerId}"></div>
<input type="hidden" name="input" class="tableHidden">
<script>
    var table_${containerId} = new Table({
		applyTo: "#${containerId}"
	});
	$("#${containerId}").next()[0].setValue = function(){
		$(this).val(table_${containerId}.getValue());
	};
</script>