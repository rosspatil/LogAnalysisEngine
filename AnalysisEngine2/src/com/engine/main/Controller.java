package com.engine.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.engine.hadoop.HDFSOperation;
import com.engine.hadoop.HDFSOperationsImpl;
import com.engine.hive.HiveOperations;
import com.engine.hive.HiveOperationsImpl;
import com.engine.spark.Featurization;
import com.engine.spark.FeaturizationImpl;
import com.engine.spark.KMeanClusteringImpl;
/**
 * Servlet implementation class Controller
 */
@MultipartConfig
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "Login.html";
		String action = request.getParameter("action");
		String userName = null;
		String password = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userName")) {
					userName = cookie.getValue();
				}
				if (cookie.getName().equals("password")) {
					password = cookie.getValue();
				}
			}
		}

		if (action != null) {

			if (action.equals("login")) {
				url="Welcome.jsp";

			} else if (action.equals("columns")) {
				
			} else if (action.equals("success")) {

			} else if (action.equals("updatePassword")) {

			} else if (action.equals("logout")) {

			} else if (action.equals("fail")) {

			}
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			Part filepart = request.getPart("file");
			InputStream fin = filepart.getInputStream();
			HttpSession session=request.getSession(true);
			String folderName="table_"+new Date().getTime();
			session.setAttribute("foldername", folderName);
			HDFSOperation hdfsOperation=new HDFSOperationsImpl("hdfs://rosspatil.domain.name:8020");
			HiveOperations hiveOperations=new HiveOperationsImpl();
			
			/******* Insert Log File ********/
			hdfsOperation.makeDir(folderName+"/data");
			hdfsOperation.insertFile(fin,folderName+"/data/log.tsv" );
			
			Map<String, String[]> columnDetails = request.getParameterMap();
//			System.out.println(columnDetails.get("column").length);
			ArrayList<String> columns=new ArrayList<String>(Arrays.asList(columnDetails.get("column")));
			ArrayList<String> types=new ArrayList<String>(Arrays.asList(columnDetails.get("type")));
			
			/******** Create External Table on Log File ***********/
			hiveOperations.createExternalTable(columns, types, folderName);
			
			
			/******** Clustering ************/
			SparkConf conf = new SparkConf();
			conf.setMaster("local[*]").setAppName("SparkSQL")
			.setSparkHome("/usr/hdp/2.3.6.0-3796/spark");
			SparkContext sparkContext = new SparkContext(conf);
			JavaSparkContext context = new JavaSparkContext(sparkContext);
			FeaturizationImpl featurizationImpl=new FeaturizationImpl();
			KMeanClusteringImpl clusteringImpl=new KMeanClusteringImpl();
			try {
				featurizationImpl.startFeaturizationProcess(folderName,context);
				clusteringImpl.performClustering(folderName,context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("./Analysis.jsp");
			requestDispatcher.forward(request, response);
		} else {
			doGet(request, response);
		}
	}

}
