<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Scanner"%>
<%@page import="java.util.Map"%>
<%@page import="com.engine.hadoop.HDFSOperationsImpl"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.hadoop.fs.Path"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="SimpleHeader.jsp" />
	<div class="container-fluid theme-showcase" role="main">
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-4"></div>
				<div class="col-md-4">
					<h3>
						<font>Report Generation Portal</font>
					</h3>
				</div>
			</div>
		</div>
	</div>
	<center>
		<table border="1" background="grey">
			<thead>
				<h2>Previous Analysis Dates</h2>
			</thead>
			<tr><th>Click For Cluster</th><th>Click For All</th></tr>
			<% 
	HDFSOperationsImpl hdfsOperationsImpl=new HDFSOperationsImpl("hdfs://rosspatil.domain.name:8020");
	Path[] paths=hdfsOperationsImpl.getFolders("/NASA");
	
	for (Path path : paths) {
				String tokens[]=path.getName().split("_");
			%>
			
			<tr>
				<td><a href="./Results.jsp?type=cluster&folder=<%=path.getName() %>"> <%=new SimpleDateFormat("dd-MM-yyyy hh:mm").format(new Date(Long.parseLong(tokens[1]))) %></a></td>
				<td><a href="./Results.jsp?type=all&folder=<%=path.getName() %>"> <%=new SimpleDateFormat("dd-MM-yyyy hh:mm").format(new Date(Long.parseLong(tokens[1]))) %></a></td>
			</tr>
			<% }%>
		</table>
	</center>

	<% String folder=request.getParameter("folder");
		if(folder!=null){%>
		
		<table border="1" background="grey">
			<thead>
				<h2>Cluster Information</h2>
			</thead>
			<tr><th>Cluster Number</th><th>Sit Visited</th></tr>
		
		<%			 paths=hdfsOperationsImpl.getFolders("/NASA/"+folder+"/KMeanClusters");
			 Map<String,Integer> sites=(Map<String,Integer>)session.getAttribute("sites");
			 ArrayList<String> list=new ArrayList();
			 for (Map.Entry<String, Integer> entry : sites.entrySet())
			 {
			  	list.add(entry.getKey());   
			 }
			 StringBuffer sb=new StringBuffer();
			 int count=0;
			 for (Path path : paths) {
				 Scanner sc=new Scanner(path.getName());
				 sc.useDelimiter("_");
				 while(sc.hasNext()){
					 int siteNo=Integer.parseInt(sc.next().trim());
					sb.append(list.get(siteNo-1)+" ");
				 }
					
					
	%>
	<tr>
				<td>Cluster_<%=++count %></td>
				<td><a href="#"> <%=sb.toString() %></a></td>
			</tr>
	
	<%} 
	
	}%>

</body>
</html>


