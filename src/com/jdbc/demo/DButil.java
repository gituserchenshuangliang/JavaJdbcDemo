package com.jdbc.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Tools
 * @author Cherry
 * 2020年4月18日
 */
public class DButil {

	private static Connection conn = null;
	private static PreparedStatement pre = null;

	// 获取数据库链接
	public static Connection getConnetcions() {
		try {
			Properties info = new Properties();
			info.load(new FileInputStream(Paths.get(".","jdbc.properties").toString()));
			String url = info.getProperty("url");
			// 创建链接
			Connection con = DriverManager.getConnection(url, info);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}

	// 手动关闭资源
	public static void closeAll() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pre != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
