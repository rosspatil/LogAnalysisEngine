package com.engine.hive;

import java.util.ArrayList;

public interface HiveOperations {

	public  void createExternalTable(ArrayList<String> columns,
			ArrayList<String> type, String folderName);

	public  void dropExternalTable(String folderName);

}