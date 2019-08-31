<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Analysis Portal</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<jsp:include page="SimpleHeader.jsp" />
	<div class="container-fluid theme-showcase" role="main">
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-4"></div>
				<div class="col-md-4">
					<h1>
						<font>Analysis Portal</font>
					</h1>
				</div>
			</div>
		</div>
	</div>
	<center>
		<%
			String action = request.getParameter("action");
			if (action == null) {
		%>
		<form action="#" method="post">
			Enter Coloumn Number: <input type="text" name="columns" /> <input
				type="hidden" name="action" value="columns"> <input
				type="submit" value="Submit">
		</form>
		<%
			}
			if (action != null && action.equals("columns")) {
				int columnNo = Integer
						.parseInt(request.getParameter("columns"));
		%>

		<form action="./Controller" method="post"
			enctype="multipart/form-data">
			<table>
				<tr>
					<th>Colomun No</th>
					<th>Column Name</th>
					<th>Data type</th>
				</tr>

				<%
					for (int i = 0; i < columnNo; i++) {
				%>
				<tr>
					<td><%=i + 1%></td>
					<td><input type="text" name="column" /></td>
					<td><input type="text" name="type" /></td>
				</tr>

				<%
					}
				%>
				<tr>
					<td>Enter File</td>
					<td><input type="file" name="file" /></td>
					<td><input type="submit" value="Submit"></td>
				</tr>
				<input type="hidden" name="columns" value="<%=columnNo%>" />
				<input type="hidden" name="action" value="columns" />

			</table>
		</form>
		<%
			}
		%>
	</center>
</body>
</html>