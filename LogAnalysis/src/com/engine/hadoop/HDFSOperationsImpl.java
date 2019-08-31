package com.engine.hadoop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public  class HDFSOperationsImpl implements HDFSOperation{

	
	private String hdfsUrl =null;
	private Configuration configuration;
	private FileSystem fs;
	public HDFSOperationsImpl(String hdfsUrl) {
		 this.hdfsUrl = hdfsUrl ; 
		 configuration = new Configuration();
		 try {
			fs = FileSystem.get(new URI(hdfsUrl), configuration);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean checkDir(String path)  {
		Path srcPath = new Path(path); 
		try {
				return fs.exists(srcPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void insertFile(String location,String folderName){
		Path[] paths={new Path(location)};
		makeDir(folderName);
		Path dstPath = new Path("/NASA/"+folderName); 
		try {
			
			fs.copyFromLocalFile(false, true, paths[0], dstPath);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void deleteFile(String fileName){
		Path dstPath = new Path("/NASA/"+fileName); 
		try {
			if(fs.exists(dstPath))
				fs.delete(dstPath, true);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void makeDir(String filename){
		Path srcPath = new Path("/NASA/"+filename); 
		try {
				fs.mkdirs(srcPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFile(String fileName){
		Path srcPath = new Path("/NASA/"+fileName); 
		Path dstpath=new Path("/home/roshan/Downloads/");
		try {
			if(fs.exists(srcPath))
				fs.copyToLocalFile(srcPath, dstpath);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}



	public Path[] getFolders(String path) {
		try {
			FileStatus[] listStatus = fs.listStatus(new Path("/NASA/"+path));
			Path[] paths=FileUtil.stat2Paths(listStatus);
//			for (Path path : paths) {
//				System.out.println(path.getName());
//				String tokens[]=path.getName().split("_");
//				System.out.println(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(new Date(Long.parseLong(tokens[1]))));
//			}
			return paths;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
}
