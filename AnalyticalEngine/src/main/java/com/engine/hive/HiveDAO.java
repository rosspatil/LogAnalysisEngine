package com.engine.hive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.engine.hadoop.HDFSOperations;

public class HiveDAO {

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
						+ folderName + "/'");
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

	public static void main(String[] args) {

		HDFSOperations operations = new HDFSOperations(
				"hdfs://rosspatil.domain.name:8020");

		/*********** code for create **********/
		/*String folderName = "table_" + new Date().getTime();
		operations.insertFile("/home/roshan/data.tsv", folderName);

		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<String> type = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			columns.add("col_" + i);
			type.add("varchar(20)");
		}
		new HiveDAO().createExternalTable(columns, type, folderName);
		 */
		/*********** code for drop **********/

		//		new HiveDAO().dropExternalTable("table_1486915399378");
		//		operations.deleteFile("table_1486915399378");

	}

}
