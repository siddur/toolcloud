<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<style>
	.changepwd div{
		width:300px;
		text-align: right;
		padding-bottom: 10px;
	}

</style>
</jsp:attribute>
<jsp:body>
	<div class="body">
		<form class="changepwd" method="post" action="/query/user/changepwd">
			<div>
				Old password:
				<input type="text" name="old_password">
			</div>
			<div>
				New password:
				<input type="password" name="new_password">
			</div>
			<div>
				New password again:
				<input type="password" name="new_password_again">
			</div>
			
			<div>
				<input type="submit" class="btn" value="save">
			</div>
		</form>
	</div>
</jsp:body>
</s:site>