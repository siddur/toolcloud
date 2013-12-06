<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	out.println(Runtime.getRuntime().freeMemory());
	out.println(Runtime.getRuntime().totalMemory());

%>