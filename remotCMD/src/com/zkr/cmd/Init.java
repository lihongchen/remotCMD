package com.zkr.cmd;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.zkr.execCMD.ICmdExecMethod;
import com.zkr.utils.DBConn;
import com.zkr.utils.OperateProperties;

/**
 * 初始化client 信息 将用户名 密码 ip地址等信息上报的数据库中，
 * 并交换到外网，以供使用
 * @author lihongchen
 * 
 */
public class Init implements ICmdExecMethod {

	
	/**
	 * 初始化自己的信息
	 * @return
	 */
	public String init(){
		
		
		OperateProperties op = new OperateProperties();
		String m_user =  op.getValue("m_user");
		String m_pwd =  op.getValue("m_pwd");
		String m_ip =  op.getValue("m_ip");
		
		
		DBConn dbconn = new  DBConn();
		Connection conn =  null; 
		PreparedStatement ps =null;
		try {
			
			
			String currentfolder = Init.class.getClassLoader().getResource("").getPath();
			File f = new File(currentfolder);
			currentfolder = f.getPath();
			
			
			conn = dbconn.getConn();
			
			//编号为IP 地址先判断ip地址是否存在如果存在则更新
			String selectsql = "select count(*) as mycount from rmc_client_msg where id =?";
			ps = conn.prepareStatement(selectsql);
			
			ps.setString(1, m_ip);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			String sql = null;
			if(rs.getInt("mycount")>0){
				sql ="update rmc_client_msg set m_user=? ,m_pwd=?,m_ip=?,m_proLocat=? where id=?";
			}else{
				sql = "insert into rmc_client_msg(m_user,m_pwd,m_ip,m_proLocat,id) "
						+ "values(?,?,?,?,?)";
			}
			
			
			
            ps.close();ps=null;			
			
			ps  = conn.prepareStatement(sql);
			ps.setString(3,m_ip);
			ps.setString(1,m_user);
			ps.setString(2,m_pwd);
			ps.setString(4,currentfolder);
			ps.setString(5,m_ip);

			
			if(ps.executeUpdate()==1)
				return "ok";
			else
				return "基本信息维护失败";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "基本信息维护失败"+e.toString();
		}finally{
			try {
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {}
		}
	}

	@Override
	public String exec(String id) {
		// TODO Auto-generated method stub
		
		return init();
	}
}
