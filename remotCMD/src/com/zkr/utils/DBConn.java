package com.zkr.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import com.zuji.c.dbconnpool.DbcpBean;

/**
 * 获得数据库链接 
 * 
 * @author lihongchen
 * 
 */
public class DBConn {

	// 全局变量
	public static DbcpBean db = null;

	// 默认初始化DB
	public DBConn() {

		if (db == null)
			db = new DbcpBean();

	}

	/**
	 * 获得一个可用的数据库链接
	 */
	public Connection getConn() {
		return DbcpBean.getConn();
	}

	public Connection getConn(String ip, String un, String pwd)
			throws Exception {
		// 定义oracle的数据库驱动程序
		final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
		// 定义oracle数据库的连接地址
		final String DBURL = "jdbc:oracle:thin:@"+ip;
		// oracle数据库的连接用户名
//		final String DBUSER = "pis";
		// oracle数据库的连接密码
//		final String DBPASS = "pis";
		Class.forName(DBDRIVER);// 加载驱动程序
		return DriverManager.getConnection(DBURL, un, pwd);
		
	}
	
	
	
	
	
	
	
	
	

}
