package com.zkr.cmd;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.zkr.control.IMethdRunable;
import com.zkr.control.TaskBean;
import com.zkr.utils.DBConn;

/**
 * 数据上报对比
 * @author xhx 
 */
public class ContrastReported implements IMethdRunable {@Override
	public String runable(TaskBean tb) {
	    return getcount();
    }
	
	/**
	 * 获得链接的信息
	 * @return
	 */
    private static Map<String, String> iplist = null;
	public static Map<String, String> getIplist() {
		if (iplist == null) {
			iplist = new HashMap<String, String>();
			java.util.Properties props = new java.util.Properties();
			InputStream fis = ContrastReported.class.getClassLoader().getResourceAsStream("init.properties");

			try {
				props.load(fis);
			} catch (IOException e) {

				throw new RuntimeException(e);
			}
			iplist.put("js", props.getProperty("js"));
			iplist.put("sh", props.getProperty("sh"));
			iplist.put("bj", props.getProperty("bj"));
			iplist.put("wang", props.getProperty("wang"));//标示内网还是外网
			iplist.put("area", props.getProperty("area"));
			iplist.put("ip", props.getProperty("ip"));
			return iplist;
		} else {
			return iplist;
		}

	}
	
	public String getcount() {
		String xinxi="";
		System.out.println("------内网------");
		Statement statementjs = null;
		Statement statementsh = null;
		Statement statementbj = null;

		int ygbuyjs = 0, ygtrajs = 0, ygbuysh = 0, ygtrash = 0, ygbuybj = 0, ygtrabj = 0;
		int sjbuyjs = 0, sjtrajs = 0, sjbuysh = 0, sjtrash = 0, sjbuybj = 0, sjtrabj = 0;
		try {
			String ygsqlbuy = "	select count(0) as num  from bs_license bl where bl.type_code = '8001' and bl.other_place !=1";//应该购买证数量
			String ygsqltra = "	select count(0) as num  from bs_license bl where bl.type_code = '8002' and bl.other_place !=1";//应该运输证数量
		    //实际购买证数量
			String sjsqlbuy = "select count(0) as num from buylicense tl where tl.license_no in (select bl.license_no  from bs_license bl where bl.type_code = '8001' and bl.other_place !=1)";
			//实际运输证数量
			String sjsqltra = "select count(0) as num from tralicense tl where tl.license_no in (select bl.license_no  from bs_license bl where bl.type_code = '8002' and bl.other_place !=1)";
			System.out.println("ygsqlbuy---"+ygsqlbuy);

			if (getIplist().get("wang").equalsIgnoreCase("n")) {
				System.out.println("------------");

				//江苏
				statementjs = getStatement("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@" + getIplist().get("js") + ":1521:orcljs", "pis", "pis");
				ygbuyjs = getCountStr(ygsqlbuy, statementjs);//应该购买证数量
				ygtrajs = getCountStr(ygsqltra, statementjs);//应该运输证数量
				sjbuyjs = getCountStr(sjsqlbuy, statementjs);//实际购买证数量
				sjtrajs = getCountStr(sjsqltra, statementjs);//实际运输证数量
				getInsert("江苏",ygbuyjs,ygtrajs,sjbuyjs,sjtrajs);//插入数据库中
				System.out.println("-----江苏-------");
				//上海
				statementsh = getStatement("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@" + getIplist().get("sh") + ":1521:orclsh", "pis", "pis");
				ygbuysh = getCountStr(ygsqlbuy, statementsh);//应该购买证数量
				ygtrash = getCountStr(ygsqltra, statementsh);//应该运输证数量
				sjbuysh = getCountStr(sjsqlbuy, statementsh);//实际购买证数量
				sjtrash = getCountStr(sjsqltra, statementsh);//实际运输证数量
				getInsert("上海",ygbuysh,ygtrash,sjbuysh,sjtrash);//插入数据库中
				System.out.println("-----上海-------");
				
				//北京
				statementbj = getStatement("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@" + getIplist().get("bj") + ":1521:orclbj", "pis", "pis");
				ygbuybj = getCountStr(ygsqlbuy, statementbj);//应该购买证数量
				ygtrabj = getCountStr(ygsqltra, statementbj);//应该运输证数量
				sjbuybj = getCountStr(sjsqlbuy, statementbj);//实际购买证数量
				sjtrabj = getCountStr(sjsqltra, statementbj);//实际运输证数量
				getInsert("北京",ygbuybj,ygtrabj,sjbuybj,sjtrabj);//插入数据库中
				System.out.println("-----北京-------");
			} else {
				String area = "";
				if (getIplist().get("area").equals("b")) {
					area = "北京";
				} else if (getIplist().get("area").equals("s")) {
					area = "上海";
				} else {
					area = "江苏";
				}
				System.out.println("------外网------"+area);
				statementjs = getStatement("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@" + getIplist().get("ip") + ":1521:orcl", "pise", "pise");
				System.out.println("-----statementjs-------"+statementjs);
				ygbuyjs = getCountStr(ygsqlbuy, statementjs);//应该购买证数量
				ygtrajs = getCountStr(ygsqltra, statementjs);//应该运输证数量
				sjbuyjs = getCountStr(sjsqlbuy, statementjs);//实际购买证数量
				sjtrajs = getCountStr(sjsqltra, statementjs);//实际购买证数量

				getInsert(area,ygbuyjs,ygtrajs,sjbuyjs,sjtrajs);//插入数据库中
			
			}
			xinxi="成功";
		} catch (Exception e) {
			e.printStackTrace();
			xinxi="异常";
		} finally {
			try {
				if (statementjs != null) {
					statementjs.close();
					statementjs.getConnection().close();
				}
				if (statementsh != null) {
					statementsh.close();
					statementsh.getConnection().close();
				}
				if (statementbj != null) {
					statementbj.close();
					statementbj.getConnection().close();
				}
			} catch (Exception e2) {
				xinxi="关闭连接异常";
			}
		}
		return xinxi;
	}

	public static Statement getStatement(String driver, String url, String user, String password) {
		try {
			Connection m_conn = null;
			Statement m_stmt = null;
			Class.forName(driver);
			m_conn = DriverManager.getConnection(url, user, password);
			if (m_conn != null) {
				m_stmt = m_conn.createStatement();
			}
			return m_stmt;
		} catch (Exception e) {
			return null;
		}
	}

	public int getCountStr(String sql, Statement statement) throws SQLException {
		int counstr = 0;
		ResultSet rs = statement.executeQuery(sql);
		System.out.println(sql);
		while (rs.next()) {
			counstr = rs.getInt("num");
		}
		return counstr;
	}
	public void getInsert(String deptname,int ygbuyjs,int ygtrajs,int sjbuyjs,int sjtrajs) throws SQLException{
		Connection con=null;
		PreparedStatement pmt=null;
		try {
			DBConn bc=new DBConn();
			con=bc.getConn();
			String sql="insert into statisticsbt (id,DEPTNAME,YGBUYJS,YGTRAJS,SJBUYJS,SJTRAJS) values(?,?,?,?,?,?)";
			System.out.println("-----insertsql-------"+sql);
			pmt = con.prepareStatement(sql);
			pmt.setString(1, uuid());
			pmt.setString(2, deptname);
			pmt.setInt(3,ygbuyjs );
			pmt.setInt(4,ygtrajs );
			pmt.setInt(5,sjbuyjs);
			pmt.setInt(6,sjtrajs);
			pmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			pmt.close();
			con.close();
		}
	}
	public static String uuid() {
		return UUID.randomUUID().toString().toUpperCase();
	}
}
