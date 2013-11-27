<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="toolDescriptor" type="siddur.tool.core.data.ToolDescriptor"%>

<div class="tool_item">
	<a href="${root}/${toolDescriptor.pluginID }.html">
		<span class="ui-icon ui-icon-gear"></span>
		<span class="title">
			[${toolDescriptor.pluginName}]
			${toolDescriptor.description}
		</span>
	</a>
</div>

