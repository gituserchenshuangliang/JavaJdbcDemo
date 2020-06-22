package com.jdbc.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 * MetaDate 诠读数据的数据 RowSet 数据列集合
 * 
 * @author Cherry 2020年4月18日
 */
public class JavaMetaDataAndRowSet {
	private static Connection conn = null;

	public static void main(String[] args) {
		// metaDateShow();
		//rowSetShow();
	}

	public static void metaDateShow() {
		conn = DButil.getConnetcions();
		try {
			DatabaseMetaData meta = conn.getMetaData();
			// 获得当前数据库以及驱动的信息
			meta.getDatabaseProductName();// 用以获得当前数据库是什么数据库。比如oracle，access等。返回的是字符串。
			String s = meta.getDatabaseProductVersion();// 获得数据库的版本。返回的字符串。
			String b = meta.getDriverVersion();// 获得驱动程序的版本。返回字符串。
			meta.getTypeInfo();// 获得当前数据库的类型信息
			System.out.println(b);
			/*
			 * 获得当前数据库中表的信息 meta.getTables（String catalog,String schema,String
			 * tableName,String[] types）; String catalog：要获得表所在的编目。"“”"意味着没有任何编目，Null表示所有编目。
			 * String schema：要获得表所在的模式。"“”"意味着没有任何模式，Null表示所有模式。
			 */
			// 获得某个表的列信息
			ResultSet set = meta.getColumns(null, null, "teacher", null);
			while (set.next()) {
				System.out.printf("name:%s, type:%s, size:%d%n", set.getString("COLUMN_NAME"),
						set.getString("TYPE_NAME"), set.getInt("COLUMN_SIZE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.closeAll();
		}
	}

	// RowSet接口下面有JdbcRowSet和CachedRowSet
	public static void rowSetShow() {
		// RowSetProvider提供RowSetFactory获取实例
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			RowSetFactory factory = RowSetProvider.newFactory();
			JdbcRowSet rowSet = factory.createJdbcRowSet();

			Properties info = new Properties();
			info.load(new FileInputStream(Paths.get(".", "jdbc.properties").toString()));

			// 资源必须在classpath之中
			ResourceBundle rb = ResourceBundle.getBundle("jdbc");
			// System.out.println(rb.getString("url"));

			rowSet.setUrl(info.getProperty("url"));
			rowSet.setUsername(info.getProperty("name"));
			rowSet.setPassword(info.getProperty("password"));

			String sql = "select * from teacher";
			rowSet.setCommand(sql);

			rowSet.execute();

			while (rowSet.next()) {
				System.out.printf("UUID:%d, name:%s, age:%d%n", rowSet.getInt(1), rowSet.getString(2),
						rowSet.getInt(3));
			}

			if (rowSet != null)
				rowSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}