<%@page import="org.apache.hadoop.fs.FSDataOutputStream"%>
<%@page import="org.apache.hadoop.fs.FSDataInputStream"%>
<%@page import="org.apache.hadoop.fs.Path"%>
<%@page import="org.apache.hadoop.fs.FileSystem"%>
<%@page import="org.apache.hadoop.conf.Configuration"%>
<%@page import="com.engine.graph.PlotChart"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="com.engine.hadoop.HDFSOperationsImpl"%>
<%@page import="com.engine.hadoop.HDFSOperation"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
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

	<%  
String folderName=request.getParameter("folder");
String type=request.getParameter("type");
Configuration configuration = new Configuration();
configuration.set("fs.defaultFS", "hdfs://rosspatil.domain.name:8020");
FileSystem fs = FileSystem.get(configuration);

if(type.equals("cluster")){
Path filePath = new Path(
        "hdfs://rosspatil.domain.name:8020/NASA/"+folderName+"/SiteVisited/usersCount.txt");

FSDataInputStream fsDataInputStream = fs.open(filePath);
ObjectInputStream input=new ObjectInputStream(fsDataInputStream);

Map<String,Long> siteVisited=(Map<String,Long>)input.readObject();
System.out.print(siteVisited);
session.setAttribute("siteVisited", siteVisited);


Path filePath1 = new Path(
        "hdfs://rosspatil.domain.name:8020/NASA/"+folderName+"/SiteVisited/sites.txt");

FSDataInputStream fsDataInputStream1 = fs.open(filePath1);
ObjectInputStream input1=new ObjectInputStream(fsDataInputStream1);

Map<String,Integer> sites=(Map<String,Integer>)input1.readObject();


PlotChart.plotChartForCluster(siteVisited, "Site Visited", "Cluster", "Count");
session.setAttribute("sites", sites);
response.sendRedirect("./Report.jsp?folder="+folderName);
}else if(type.equals("all")){
	Path filePath = new Path(
	        "hdfs://rosspatil.domain.name:8020/NASA/"+folderName+"/SiteVisited/sites.txt");

	FSDataInputStream fsDataInputStream = fs.open(filePath);
	ObjectInputStream input=new ObjectInputStream(fsDataInputStream);

	Map<String,Integer> siteVisited=(Map<String,Integer>)input.readObject();
	System.out.print(siteVisited);
	PlotChart.plotChartForAll(siteVisited, "Site Visited By Host", "Sites", "Count");
	response.sendRedirect("./Report.jsp");
}

 %>

</body>
</html>