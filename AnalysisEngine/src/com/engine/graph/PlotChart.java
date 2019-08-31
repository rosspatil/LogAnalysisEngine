package com.engine.graph;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpSession;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class PlotChart {

	
	public static void plotChartForCluster(Map<String,Long> siteMap,String title,String X,String Y) {
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		int count=0;
		for (Map.Entry<String, Long> entry : siteMap.entrySet())
		{
			
		      dataset.addValue(entry.getValue(), "SiteVisited", "Cluster_"+ ++count);
			
		}
		
		JFreeChart barChart = ChartFactory.createBarChart(
				title, 
				X, Y, 
				dataset,PlotOrientation.VERTICAL, 
				true, true, false);
		CategoryAxis axis = barChart.getCategoryPlot().getDomainAxis();
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		ChartFrame chartFrame=new ChartFrame(title, barChart);
		chartFrame.setVisible(true);
		chartFrame.setSize(2000, 1000);
		
	}
	
	
public static void plotChartForAll(Map<String,Integer> siteMap,String title,String X,String Y) {
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		int count=0;
		for (Map.Entry<String, Integer> entry : siteMap.entrySet())
		{
			if(entry.getValue()>5){
		      dataset.addValue(entry.getValue(), "SiteVisited", entry.getKey());
			}
		}
		
		JFreeChart barChart = ChartFactory.createBarChart(
				title, 
				X, Y, 
				dataset,PlotOrientation.VERTICAL, 
				true, true, false);
		CategoryAxis axis = barChart.getCategoryPlot().getDomainAxis();
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		ChartFrame chartFrame=new ChartFrame(title, barChart);
		chartFrame.setVisible(true);
		chartFrame.setSize(2000, 1000);
		
	}
}
