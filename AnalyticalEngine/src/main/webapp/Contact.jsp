<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contact</title>
<script src="http://www.w3schools.com/lib/w3data.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

	<jsp:include page="SimpleHeader.jsp"></jsp:include>


	<div class="container padding-32" id="contact">
		<h3 class="border-bottom border-light-grey padding-12">Contact</h3>
		<p>Lets get in touch and talk about your and our next project.</p>
		<form action="form.asp" target="_blank">
			<input class="input" type="text" placeholder="Name" required>
			<input class="input " type="text" placeholder="Email"
				required> <input class="input section" type="text"
				placeholder="Subject" required> <input
				class="input section" type="text" placeholder="Comment"
				required>
			<button class="btn section" type="button" onclick="send()">
				<i class="fa fa-paper-plane"></i> SEND MESSAGE
			</button>
		</form>
	</div>
	<script type="text/javascript">
		function send() {
			window.alert("Thankyou for contacting us!");
		}
	</script>
</body>
</html>