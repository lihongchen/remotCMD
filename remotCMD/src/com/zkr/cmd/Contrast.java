package com.zkr.cmd;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zkr.control.IMethdRunable;
import com.zkr.control.TaskBean;
import com.zkr.utils.DBConn;

/**
 * 对比内外网中数据是否一致
 * @author xhx 
 */
public class Contrast implements IMethdRunable{
	
	@Override
	public String runable(TaskBean tb) {
		return getSelect();
	}
	public static String getSelect(){
		    String xinxi="";
		    Connection con=null;
		    PreparedStatement pmt=null;
		    ResultSet rs=null;
		    try {
				DBConn bc=new DBConn();
				con=bc.getConn();
				
				
				

				
				
				String sqltabnum="select vv.num as wwnum ,cc.num as nwnum from "+
				"(select count(distinct table_name ) as num from user_tab_columns) vv, "+
				"(select count(distinct table_name ) as num from tables) cc";
				 
				 pmt = con.prepareStatement(sqltabnum);
			        System.out.println("returns---"+sqltabnum);
					 rs=pmt.executeQuery();
					while(rs.next()){
						xinxi+="(wwnum 外网表的个数 --)"+rs.getString("wwnum")+"\t";
						xinxi+="(nwnum 内网表的个数 --)"+rs.getString("nwnum")+"\n";
					}
				
				
				
					pmt.close();
					pmt = null;
 


				
				
				
				String sqlcounnum="select vv.table_name ,vv.num as wwnum ,cc.num as nwnum from "+ 
				"(select b.table_name, count(*) as num  from user_tab_columns b group by b.TABLE_NAME) vv, "+
				"(select t.table_name, count(*) as num   from tables t group by t.table_name) cc "+
				"where vv.table_name = cc. table_name ";
				   pmt = con.prepareStatement(sqlcounnum);
			        System.out.println("returns---"+sqlcounnum);
					 rs=pmt.executeQuery();
					while(rs.next()){
						xinxi+="(表名称--)"+rs.getString("table_name")+"\t";
						xinxi+="(外网字段数量--)"+rs.getString("wwnum")+"\t";
						xinxi+="(内网字段数量--)"+rs.getString("nwnum")+"\n";
					}
				
				
				
					pmt.close();
					pmt = null;

				
				
				
				
				
				
				
		        String sql="select t.table_name,t.column_name,t.data_type,t.data_length,t.nullable,s.data_length as ww_length from tables t left join sys.user_tab_columns s " +
		        		   "on t.table_name = s.table_name and t.column_name=s.COLUMN_NAME where t.data_type<>s.DATA_TYPE or t.data_length<>s.DATA_LENGTH";
		        pmt = con.prepareStatement(sql);
		        System.out.println("returns---"+sql);
				rs=pmt.executeQuery();
				while(rs.next()){
					xinxi+="(表名--)"+rs.getString("table_name")+"\t";
					xinxi+="(字段名称--)"+rs.getString("column_name")+"\t";
					xinxi+="(字段类型--)"+rs.getString("data_type")+"\t";
					xinxi+="(内网长度--)"+rs.getString("data_length")+"\t";
					xinxi+="(外网长度--)"+rs.getString("ww_length")+"\n";
				    
				}
				System.out.println("xinxi--\n"+xinxi);
				
				FileOutputStream fos = new FileOutputStream(new File("out.txt"));
				fos.write(xinxi.getBytes());
				fos.flush();
				fos.close();
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				xinxi="异常";
		    }
			return xinxi;
 }
	public static void main(String[] args) {
		getSelect();
	}
	
}
