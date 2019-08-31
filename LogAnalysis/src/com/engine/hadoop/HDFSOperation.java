package com.engine.hadoop;

import org.apache.hadoop.fs.Path;

public interface HDFSOperation {

	  boolean checkDir(String path);
	  void insertFile(String location,String folderName);
	  void deleteFile(String fileName);
	  void makeDir(String filename);
	  void saveFile(String fileName);
	  Path[] getFolders(String path);
}
