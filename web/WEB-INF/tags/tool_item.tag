<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="toolDescriptor" type="siddur.tool.core.data.ToolDescriptor"%>

<div class="tool_item">
	<a href="/toolcloud/ctrl/tool/detail?toolId=${toolDescriptor.pluginID }">
		<span class="title">
			[${toolDescriptor.pluginName}]
			${toolDescriptor.description}
		</span>
		<span class="catalog">${toolDescriptor.catalog}</span>
	</a>
</div>

