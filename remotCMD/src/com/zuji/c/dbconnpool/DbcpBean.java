package com.zuji.c.dbconnpool;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbcp.DelegatingConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author space
 * @date Aug 12, 2008 3:25:49 PM
 *
 * dbcp 实用类，提供了dbcp连接，不允许继承；
 * 
 * 该类需要有个地方来初始化 DS ，通过调用initDS 方法来完成，可以在通过调用带参数的构造函数完成调用，可以在其它类中调用，也可以在本类中加一个static{}来完成；
 */
public final class DbcpBean {
	
	private static Log log = LogFactory.getLog(DbcpBean.class);
	
	/** 数据源，static */
	private static DataSource DS=null;

	public  DataSource getDS() {
		return DS;
	}






	/** 从数据源获得一个连接 */
	public static final  Connection getConn() {

		 Connection conn = null;
		  try {
		   conn = DS.getConnection();
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
	
	
	
	private static final String configFile = "jdbcinit.ini"; 
	 static {
		  Properties dbProperties = new Properties();
		  try {
		   dbProperties.load(DbcpBean.class.getClassLoader().getResourceAsStream(configFile));
		   System.out.println(dbProperties);
		   DS = BasicDataSourceFactory.createDataSource(dbProperties); 
		   
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
	
	
	
	/** 默认的构造函数 */
	public DbcpBean() {
		
//		Properties ps =	InitJDBCParameters.getProperties();
//		BasicDataSource ds = new BasicDataSource();
		if( DS==null ){
		
			log.error("初始化 Properties： jdbcinit.ini中的参数错误");
			throw new RuntimeException("初始化 Properties： jdbcinit.ini中的参数错误");
		}
		
		
	}

	/** 构造函数，初始化了 DS ，指定 数据库 */
	public DbcpBean(String connectURI) {
		initDS(connectURI);
	}

	/** 构造函数，初始化了 DS ，指定 所有参数 */
	public DbcpBean(String connectURI, String username, String pswd, String driverClass, int initialSize,
			int maxActive, int maxIdle, int maxWait) {
		initDS(connectURI, username, pswd, driverClass, initialSize, maxActive, maxIdle, maxWait);
	}

	/**
	 * 创建数据源，除了数据库外，都使用硬编码默认参数；
	 * 
	 * @param connectURI 数据库
	 * @return*/
	 
	public static void initDS(String connectURI) {
		initDS(connectURI, "pis", "pis", "oracle.jdbc.driver.OracleDriver", 5, 100, 30, 10000);
	}

	/** 
	 * 指定所有参数连接数据源
	 * 
	 * @param connectURI 数据库
	 * @param username 用户名
	 * @param pswd 密码
	 * @param driverClass 数据库连接驱动名
	 * @param initialSize 初始连接池连接个数
	 * @param maxActive 最大激活连接数
	 * @param maxIdle 最大闲置连接数
	 * @param maxWait 获得连接的最大等待毫秒数
	 * @return*/
	
	public static void initDS(String connectURI, String username, String pswd, String driverClass, int initialSize,
			int maxActive, int maxIdle, int maxWait) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClass);
		ds.setUsername(username);
		ds.setPassword(pswd);
		ds.setUrl(connectURI);
		ds.setInitialSize(initialSize); // 初始的连接数；
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		ds.setRemoveAbandoned(true);
		DS = ds;
	}
 
	
	/** 
	 * 指定所有参数连接数据源
	 * 
	 * @param connectURI 数据库
	 * @param username 用户名
	 * @param pswd 密码
	 * @param driverClass 数据库连接驱动名
	 * @param initialSize 初始连接池连接个数
	 * @param maxActive 最大激活连接数
	 * @param maxIdle 最大闲置连接数
	 * @param maxWait 获得连接的最大等待毫秒数
	 * @return
	 */
	public static void initDS(String connectURI, String username, String pswd, String driverClass, int initialSize,
			int maxActive, int maxIdle, int maxWait,String connectionProperties) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClass);
		ds.setUsername(username);
		ds.setPassword(pswd);
		ds.setUrl(connectURI);
		ds.setInitialSize(initialSize); // 初始的连接数；
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		DS = ds;
	}

	
	
	
	
	
	/** 获得数据源连接状态 */
	public static Map<String, Integer> getDataSourceStats() throws SQLException {
		BasicDataSource bds = (BasicDataSource) DS;
		Map<String, Integer> map = new HashMap<String, Integer>(2);
		map.put("active_number", bds.getNumActive());
		map.put("idle_number", bds.getNumIdle());
		return map;
	}

	/** 关闭数据源 */
	protected static void shutdownDataSource() throws SQLException {
		BasicDataSource bds = (BasicDataSource) DS;
		bds.close();
	}

	
	
	/**
	 * 将Properties 对象中的内容 赋值给 BasicDataSource对象
	 * @param ds BasicDataSource 对象
	 * @param ps Properties 对象
	 * @return
	 */
	private static boolean ps2ds(BasicDataSource ds,Properties ps){
		if(ds == null || ps== null )return  false;
		else{
			 BeanInfo beanInfo = null;
			 String name = null,type=null;Object value=null ;
			try {
				beanInfo = Introspector.getBeanInfo(ds.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					if (pd.getWriteMethod() != null) {
						  name = pd.getName();
						  type = pd.getPropertyType().getName();
						  if (!isSimpleType(type))
							continue;
						value = toType(type, ps.get(name)) ;
						if(value == null )continue;
						pd.getWriteMethod().invoke(ds,value);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("ps2ds 方法中出错："+e.toString());
			}
			return true;
		}
	}
	
	/**
	 * 将指定的对象 修改为指定的类型的数据
	 * @param type  类型
	 * @param value 值
	 * @return
	 */
	public static Object toType(String type, Object value) {

		if ("java.lang.String".equals(type)) {
			if (value == null || value.equals(""))
				return null;
			return value;
		} else if ("java.lang.Integer".equals(type) || "int".equals(type)) {
			if (value == null || value.equals(""))
				return null;
			return Integer.parseInt(value.toString());
		} else if ("java.lang.Float".equals(type) || "float".equals(type)) {
			if (value == null)
				return null;
			return Float.parseFloat(value.toString());
		} else if ("java.lang.Double".equals(type) || "double".equals(type)) {
			if (value == null)
				return null;
			return Double.parseDouble(value.toString());
		}else if ("java.lang.Long".equals(type) || "long".equals(type)) {
			if (value == null)
				return null;
			return Long.parseLong(value.toString());
		}else if ("java.lang.Boolean".equals(type) || "boolean".equals(type)) {
			if (value == null)
				return null;
			return Boolean.parseBoolean(value.toString());
		} else if ("java.util.Date".equals(type)) {
			if (value == null || value.equals(""))
				return null;
			if (value.getClass().getName().equals("java.lang.String")) {
				value = value.toString().trim();
				SimpleDateFormat f = null;
				int valueL = value.toString().length();
				if (valueL == 10)
					f = new SimpleDateFormat("yyyy-MM-dd");
				else if (valueL == 16)
					f = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				else if (valueL == 19)
					f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				else
					throw new RuntimeException("错误的日期格式，" + value + ",数据类型 :"
							+ value.getClass().getName() + "请联系管理员");
				try {
					return f.parseObject(value.toString());
				} catch (ParseException e) {

					throw new RuntimeException("错误的日期格式，" + value + ",数据类型 :"
							+ value.getClass().getName() + "请联系管理员");
				}
			} else {
				return value;
			}
		}
		throw new RuntimeException("未处理的数据类型，请联系管理员" + type);
	}
	
	/**
	 * 判断 是否为简单类型
	 * @param type 类型
	 * @return 
	 */
	public static boolean isSimpleType(String type) {
		boolean i = "java.lang.String".equals(type)
				|| "java.lang.Integer".equals(type) || "int".equals(type)
				|| "java.lang.Float".equals(type) || "float".equals(type)
					|| "java.lang.Long".equals(type) || "long".equals(type)
				|| "java.util.Date".equals(type) || "java.lang.Double".equals(type)
				|| "double".equals(type) || "java.lang.Boolean".equals(type)
				|| "boolean".equals(type) ;
		 //System.out.println(i);
		return i;
	}
	
	
	
	public static void main(String[] args) {
		
//		DbcpBean db = new DbcpBean("jdbc:oracle:thin:@192.168.1.195:1521:orcl");
		DbcpBean db = new DbcpBean();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from dual   ");
			System.out.println("Results:");
			int numcols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print("\t" + rs.getString(i) + "\t");
				}
				System.out.println("");
			}
			
			
			for (int i = 0; i < 130; i++) {
				
			
			conn = db.getConn();
			System.out.println(getDataSourceStats());
			 try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 DbcpBean.closeConn(conn);
			}
			
//			while(true){
//				
//				//BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//				try {
//					if("0".equals("2"))
//					break;
//					
//					Thread.sleep(100);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				log.fatal(getDataSourceStats()+ "   "+ new Date());
//				System.out.println(getDataSourceStats());
//			}
			
			
			
			
			
			
			System.out.println(getDataSourceStats());
			shutdownDataSource();
			System.out.println(getDataSourceStats());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (db != null)
					shutdownDataSource();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
