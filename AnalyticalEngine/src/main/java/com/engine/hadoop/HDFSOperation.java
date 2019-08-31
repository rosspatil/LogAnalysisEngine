package com.engine.hadoop;

import java.io.InputStream;

import org.apache.hadoop.fs.Path;

public interface HDFSOperation {

	  boolean checkDir(String path);
	  void insertFile(InputStream inputStream,String folderName);
	  void deleteFile(String fileName);
	  void makeDir(String filename);
	  void saveFile(String fileName);
	  Path[] getFolders(String path);
}
