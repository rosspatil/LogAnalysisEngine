package com.engine.hive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.engine.hadoop.HDFSOperationsImpl;

public class HiveOperationsImpl implements HiveOperations {

	/* (non-Javadoc)
	 * @see com.engine.hive.HiveOperations#createExternalTable(java.util.ArrayList, java.util.ArrayList, java.lang.String)
	 */
	public void createExternalTable(ArrayList<String> columns,
			ArrayList<String> type, String folderName) {
		Connection connection = ConnectionFactory.getConnection();
		int size = columns.size();
		StringBuffer sql = new StringBuffer("CREATE EXTERNAL TABLE "
				+ folderName + " ( ");
		for (int i = 0; i < size; i++) {
			if (i == (size - 1)) {
				sql.append(columns.get(i) + " " + type.get(i)
						+ ")   ROW FORMAT DELIMITED FIELDS TERMINATED BY "
						+ "'\t' STORED AS TEXTFILE LOCATION '/NASA/"
						+ folderName + "/data/'");
				break;
			}
			sql.append(columns.get(i) + " " + type.get(i) + ",");
		}

		try {
			PreparedStatement statement = connection.prepareStatement(sql
					.toString());
			statement.execute();
			ConnectionFactory.shutdownConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see com.engine.hive.HiveOperations#dropExternalTable(java.lang.String)
	 */
	public void dropExternalTable(String folderName) {
		Connection connection = ConnectionFactory.getConnection();
		String sql = "DROP TABLE " + folderName;
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();
			ConnectionFactory.shutdownConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws FileNotFoundException {

		HDFSOperationsImpl operations = new HDFSOperationsImpl(
				"hdfs://rosspatil.domain.name:8020");

		/*********** code for create **********/
		String folderName = "table_" + new Date().getTime();
		operations.insertFile(new FileInputStream(new File("/home/roshan/data.tsv")), folderName);

		/*ArrayList<String> columns = new ArrayList<String>();
		ArrayList<String> type = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			columns.add("col_" + i);
			type.add("varchar(20)");
		}
		new HiveDAO().createExternalTable(columns, type, folderName);*/
		 
		/*********** code for drop **********/

//				new HiveDAO().dropExternalTable("table_1487568933130");
//				operations.deleteFile("table_1487568933130");
		
		/*********** code for getting folders *********/
//		operations.getFolders("table_1487569319520");

	}

}
