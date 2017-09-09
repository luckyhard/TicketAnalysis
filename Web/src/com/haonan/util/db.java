package com.haonan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class db {
	//创建数据库连接
	public static Connection createConnection(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("数据库驱动加载出错!");
		}
		try {
			//没有给conn赋值，一直数据库查询错误
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/soft_match?useSSL=false"
					,"root","123456");
		} catch (SQLException e) {
			System.out.println("数据库连接失败");
		}
		return conn;
	}
	//sql语句执行函数
	public static PreparedStatement prepare(Connection conn,String sql){
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			System.out.println("数据库语句执行出错！");
		}
		return ps;
	}
	
	public static void close(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("数据库连接关闭出错！");
		}
	}
	public static void close(PreparedStatement ps){
		try {
			ps.close();
		} catch (SQLException e) {
			System.out.println("ps关闭错误!");
		}
	}
	public static void close(ResultSet rs){
		try {
			rs.close();
		} catch (SQLException e) {
			System.out.println("rs关闭错误！");
		}
	}
}
