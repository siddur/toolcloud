<%@page import="siddur.tool.core.data.DataTemplate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript" src="/toolcloud/jquery/js/jquery-1.9.1.js"></script>
<link rel="stylesheet" type="text/css" href="/toolcloud/jqueryui/jquery-ui-1.10.3.custom.min.css" />    
<font color="red">${error }</font>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<s:file_upload_head multiple="false"></s:file_upload_head>

display:inline-block; width: 60px; overflow: hidden; text-overflow: ellipsis;
upload tool file..
<s:file_upload fieldname="toolfile"/>


upload image..
<s:file_upload fieldname="icon"  isImage="true"/>

<% DataTemplate dt = new DataTemplate();
	dt.setDataType(siddur.tool.core.data.DataType.DATE.name());
%>

<s:input inputModel="<%=dt %>"></s:input>
