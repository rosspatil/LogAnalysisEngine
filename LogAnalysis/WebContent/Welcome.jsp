<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body>
	<jsp:include page="SimpleHeader.jsp"></jsp:include>
	<div class="container-fluid theme-showcase" role="main">
		<div class="jumbotron bg-3">
			<div class="row">
				<div class="col-md-4"></div>
				<div class="col-md-4">
					<h2>
						<font>Welcome Admin</font>
					</h2>
				</div>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-3">
				<a href="./Analysis.jsp">
					<div class="row">
						<img src="images/doAnalysis.png" width="175" height="175">
					</div>
					<div class="row">
						<font size="15" color="black">Analysis</font>
					</div>
			</div>
			</a>
			<div class="col-md-3">
				<a href="./Welcome.jsp">
					<div class="row">
						<img src="images/activities.png" width="230" height="175">
					</div>
					<div class="row">
						<font size="15" color="black">Last Activity</font>
					</div>
				</a>
			</div>

		</div>
	</div>
</body>
</html>