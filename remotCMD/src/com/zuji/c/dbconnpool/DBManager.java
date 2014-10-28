package com.zuji.c.dbconnpool;


 import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 /**
 * tomcat数据库连接池管理类<br>
 * 使用为tomcat部署环境<br>
 * 需要在类路径下准备数据库连接配置文件dbcp.properties
 * 
 * @author 宋信强
 * @mail songxinqiang123@gmail.com
 * 
 * @time 2013-12-27
 * 
 */
public class DBManager {
 private static final Log log = LogFactory.getLog(DBManager.class);
 private static final String configFile = "jdbcinit.ini"; 
  private static DataSource dataSource; 
  static {
  Properties dbProperties = new Properties();
  try {
   dbProperties.load(DBManager.class.getClassLoader()
     .getResourceAsStream(configFile));
   dataSource = BasicDataSourceFactory.createDataSource(dbProperties); 
    Connection conn = getConn();
   DatabaseMetaData mdm = conn.getMetaData();
   log.info("Connected to " + mdm.getDatabaseProductName() + " "
     + mdm.getDatabaseProductVersion());
   if (conn != null) {
    conn.close();
   }
  } catch (Exception e) {
	  e.printStackTrace();
   log.error("初始化连接池失败：" + e);
  }
 } 
  private DBManager() {
 } 
  /**
  * 获取链接，用完后记得关闭
  * 
  * @see {@link DBManager#closeConn(Connection)}
  * @return
  */
 public static final Connection getConn() {
  Connection conn = null;
  try {
   conn = dataSource.getConnection();
  } catch (SQLException e) {
   log.error("获取数据库连接失败：" + e);
  }
  return conn;
 } 
  /**
  * 关闭连接
  * 
  * @param conn
  *            需要关闭的连接
  */
 public static void closeConn(Connection conn) {
  try {
   if (conn != null && !conn.isClosed()) {
    conn.setAutoCommit(true);
    conn.close();
   }
  } catch (SQLException e) {
   log.error("关闭数据库连接失败：" + e);
  }
 } 
 }
 