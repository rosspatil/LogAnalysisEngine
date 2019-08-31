<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<style type="text/css">
input[type=button],input[type=submit],input[type=reset] {
	background-color: #4CAF50;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.w3-table.no-gutters {
	margin-right: 0;
	margin-left: 0;
}
</style>
<title>header</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="/LogBasedAnalyticalEngine/Welcome.jsp">Log Based Analytical Enigne</a>
		</div>
		<ul class="nav navbar-nav w3-right w3-hide-small"
			style="padding: 0px;">
			<li class="active"><a href="/LogBasedAnalyticalEngine">Home</a></li>
			
			<li><a href="./About.jsp">About
			</a></li>
			<li><a href="./Contact.jsp">Contact
			</a></li>
			<li><a href="./Controller?action=logout">Logout</a></li>
			<li class="w3-right w3-hide-small"><a
				href="./profileCard.jsp">Admin</i></a> </li>
		</ul>
	</div>
	</nav>
</body>
</html>