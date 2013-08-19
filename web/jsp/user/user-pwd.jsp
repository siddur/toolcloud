<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ChangePassword</title>
<style>
	.changepwd div{
		width:300px;
		text-align: right;
		padding-bottom: 10px;
	}

</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
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
</body>
</html>