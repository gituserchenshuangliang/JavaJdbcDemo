package com.jdbc.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * JDBC学习
 * 
 * @author Cherry 2020年4月18日
 */
public class JavaJdbcDemo {
	public static Connection conn = null;
	public static PreparedStatement pre = null;
	public static ResultSet set = null;
	public static Savepoint point = null;// 存储点
	
	public static void main(String[] args) {
		// singleUpdate();
		// batchUpdate();
		// addBlobClobShow();
		transactionShow();
	}

	// 单条查询和添加
	public static void singleUpdate() {
		try {
			conn = getConnetcions();
			// 获取PreppareStament预先编译SQL

			// 添加数据
			String insert = "insert into teacher(name,sex,age,major,salary) values (?,?,?,?,?) ";
			pre = conn.prepareStatement(insert);
			Teacher t = new Teacher("Jack", 40, "M", "physic", 3400);
			pre.setString(1, t.getName());
			pre.setString(2, t.getSex());
			pre.setInt(3, t.getAge());
			pre.setString(4, t.getMajor());
			pre.setDouble(5, t.getSalary());
			// pre.executeUpdate();

			// 查询
			String query = "select * from teacher where UUID = ?";
			pre = conn.prepareStatement(query);
			pre.setInt(1, 3);
			set = pre.executeQuery();
			ArrayList<Teacher> list = new ArrayList<Teacher>(10);
			while (set.next()) {
				list.add(new Teacher(set.getInt(1) + "", set.getString(2), set.getInt(3), set.getString(5),
						set.getString(4), set.getDouble(6)));
			}
			list.forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
	}

	// 批量更新
	public static int[] batchUpdate() {
		ArrayList<Teacher> list = new ArrayList<Teacher>();
		list.add(new Teacher("Jhon", 40, "F", "Music", 4000));
		list.add(new Teacher("Tom", 34, "M", "Social", 2000));
		conn = getConnetcions();

		String insert = "insert into teacher(name,sex,age,major,salary) values (?,?,?,?,?) ";
		try {
			pre = conn.prepareStatement(insert);
			for (int i = 0; i < list.size(); i++) {
				Teacher t = list.get(i);
				pre.setString(1, t.getName());
				pre.setString(2, t.getSex());
				pre.setInt(3, t.getAge());
				pre.setString(4, t.getMajor());
				pre.setDouble(5, t.getSalary());
				pre.addBatch();// 收集SQL
			}
			int[] i = pre.executeBatch();// 发送SQL集合
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return null;
	}

	// 添加Blob和Clob数据
	public static void addBlobClobShow() {
		conn = getConnetcions();
		String sql = "update teacher set attachment = ? , picture = ? where uuid = ?";
		try {
			pre = conn.prepareStatement(sql);
			InputStream tiny = new FileInputStream("E:\\MyStudy\\BAT\\c.txt");
			InputStream large = new FileInputStream("E:\\MyStudy\\BAT\\a.txt");
			pre.setBinaryStream(1, tiny);
			pre.setBlob(2, large);
			pre.setInt(3, 1);
			int i = pre.executeUpdate();
			System.out.println(i);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
	}

	// 数据库交易Database Transaction 原则:原子性，一致性，隔离行为，持续性
	public static void transactionShow() {
		conn = getConnetcions();
		try {
			/*
			  * 数据库交易隔离行为 <code>Connection</code> 
			 * <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>		更新遗失
			 * <code>Connection.TRANSACTION_READ_COMMITTED</code>		脏读
			 * <code>Connection.TRANSACTION_REPEATABLE_READ</code>		无法重复读取
			 * <code>Connection.TRANSACTION_SERIALIZABLE</code>			幻读
			 * <code>Connection.TRANSACTION_NONE</code>					无隔离措施
			 */
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			String sql = "update teacher set attachment = ? , picture = ? where uuid = ?";

			conn.setAutoCommit(false);// 设置手动提交

			pre = conn.prepareStatement(sql);
			InputStream tiny = new FileInputStream("E:\\MyStudy\\BAT\\c.txt");
			InputStream large = new FileInputStream("E:\\MyStudy\\music.mp3");
			pre.setBinaryStream(1, tiny);
			pre.setBlob(2, large);
			pre.setInt(3, 1);

			point = conn.setSavepoint();// 存储点设置

			int i = pre.executeUpdate();
			System.out.println(i);
			conn.commit();// 提交
		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					// conn.rollback();//发生异常撤回之前的交易
					conn.rollback(point);// 撤回到存储点
					System.out.println("撤销之前的操作交易");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
	}

	// 获取数据库链接
	public static Connection getConnetcions() {
		// 加载数据库驱动,只要jar中有services/java.sql.Driver文档就会自动加载
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//		}
		Properties info = new Properties();
		info.put("user", "chen");
		info.put("password", "chen");
		String url = "jdbc:mysql://127.0.0.1:3306/chen?serverTimezone=UTC&useSSL=false";
		// 创建链接
		try {
			Connection con = DriverManager.getConnection(url, info);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
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
		if (set != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
