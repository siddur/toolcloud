<%@page import="siddur.common.miscellaneous.Paging"%>
<%@page import="siddur.tool.cloud.action.ToolAction"%>
<%@page import="siddur.tool.core.IToolWrapper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<link rel="stylesheet" type="text/css" href="/toolcloud/css/tag.css" />
<style>
	.list{
		margin:7px;
		width:250px;
		height:150px;
		overflow: hidden;
		background-color: #A9B1BD;
		box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5) ;
		padding:3px;
		
	}
	
	.list:hover{
		cursor:pointer;
	}
	
	.paging{
		clear:both;
	}
}
</style>
<script type="text/javascript" src="/toolcloud/jquery/js/jquery-1.9.1.js"></script>

<div>
<%
	Paging<IToolWrapper> paging =(Paging<IToolWrapper>)request.getAttribute("paging");
	if(paging != null){
		for(IToolWrapper t : paging.getData()){
%>
			<div class="list left_float" onclick="location.href='/toolcloud/ctrl/tool/detail?toolId=<%=t.getDescriptor().getPluginID()%>';">
				<s:tool_detail updatable="true" toolDescriptor="<%=t.getDescriptor()%>"></s:tool_detail>
			</div>
	<%
		}
	%>
		
		<s:paging pageIndex="<%=paging.getPageIndex() %>" pageSize="<%=paging.getPageSize() %>" total="<%=paging.getTotal() %>"/>
		
		<script>
			var pageIndex = <%= paging.getPageIndex()%>;
			$(document).ready(function(){
				var spans = $(".paging span");
				spans.click(function(e){
					var span = e.currentTarget;
					var id = span.id;
					if(id == "last"){
						pageIndex += -1;
					}
					else if(id == "next"){
						pageIndex += 1;
					}
					else{
						pageIndex = id;
					}
					if(pageIndex < 0){
						pageIndex = 0
					}else if(pageIndex > spans.length - 3){
						pageIndex = spans.length - 3
					}
					$("[name='pageIndex']").val(pageIndex);
					$("form").submit();
				});
			});
		</script>
	<%
	}
%>
	
</div>
