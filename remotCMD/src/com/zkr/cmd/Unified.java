package com.zkr.cmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.zkr.control.IMethdRunable;
import com.zkr.control.TaskBean;
import com.zkr.utils.DBConn;

/**
 * 把数据库中的表,字段,类型,插入都插入表中
 * @author xhx
 * 
 */
public class Unified implements IMethdRunable{
	@Override
	public String runable(TaskBean tb){
		String xinxi="";
		try {
			xinxi=getInsert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xinxi;
	}
	public static String getInsert(){
	    String xinxi="";
	    Connection con=null;
	    PreparedStatement pmt=null;
	    try {
			DBConn bc=new DBConn();
			con=bc.getConn();
			String sql="insert into tables(TABLE_NAME,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,NULLABLE)select s.TABLE_NAME,s.COLUMN_NAME,s.DATA_TYPE,s.DATA_LENGTH,s.NULLABLE from sys.user_tab_columns s";
			pmt=con.prepareStatement(sql);
			pmt.executeUpdate();
			xinxi="插入成功";
			System.out.println(xinxi);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			xinxi="插入异常";
		}finally{
			try {
				pmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				xinxi="链接关闭异常";
			}
		}
		return xinxi;
		}
	
	/*public static String getInsert() throws SQLException{
	    String xinxi="";
	    Connection con=null;
	    PreparedStatement pmt=null;
	    PreparedStatement pmts=null; 
	    ResultSet rs=null;
	    try {
			DBConn bc=new DBConn();
			con=bc.getConn();
			String sql="select s.TABLE_NAME,s.COLUMN_NAME,s.DATA_TYPE,s.DATA_LENGTH,s.NULLABLE from sys.user_tab_columns s";
			pmt = con.prepareStatement(sql);
			System.out.println("----"+sql);
			rs=pmt.executeQuery();
			String sqls="insert into tables (ID,TABLE_NAME,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,NULLABLE,CREATDATE) values (?,?,?,?,?,?,sysdate)";
			System.out.println("-sqls--"+sqls);
			while(rs.next()){
				System.out.println("--TABLE_NAME--"+rs.getString("TABLE_NAME")+"--"+uuid());
				pmts = con.prepareStatement(sqls);
				pmts.setString(1, uuid());
				pmts.setString(2, rs.getString("TABLE_NAME"));
				pmts.setString(3, rs.getString("COLUMN_NAME"));
				pmts.setString(4, rs.getString("DATA_TYPE"));
				pmts.setString(5, rs.getString("DATA_LENGTH"));
				pmts.setString(6, rs.getString("NULLABLE"));
				//pmts.setDate(7, sysdate);
				pmts.executeUpdate();
				pmts.close();
			}
			xinxi="插入成功";
			System.out.println(xinxi);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			xinxi="插入异常";
		}finally{
			rs.close();
			pmt.close();
			con.close();
		}
		return xinxi;
		}*/

	public static String uuid() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	public static void main(String[] args) throws SQLException {
		try {
			getInsert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
