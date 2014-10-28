package com.zkr.execCMD;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zkr.control.GlobalFieldInformation;
import com.zkr.control.IMethdRunable;
import com.zkr.control.MachineInfoBean;
import com.zkr.control.TaskBean;
import com.zkr.execSQL.ExecSQL;
import com.zuji.c.dbconnpool.DbcpBean;

/** 
 * 使用ssh 链接 远程服务器 执行cmd 命令
 * @author root
 *
 */
public class ExecCMD implements IMethdRunable{

	private static Log log = LogFactory.getLog(ExecCMD.class);
	private static Map<String,MachineInfoBean> mapmib = null;
	//加载所有能执行cmd命令的机器信息
	public ExecCMD(){
		if(mapmib == null)mapmib = loadmachineinfo();
	}
	
	
	
	
	
	/**
	 * 加载所有可以执行cmd命令的机器信息
	 */
	public Map<String,MachineInfoBean> loadmachineinfo(){
		
		Connection conn = null;
		PreparedStatement ps = null;
		Map<String,MachineInfoBean> miblist = new HashMap<String,MachineInfoBean>();
		try {

			DbcpBean db = new DbcpBean();
			conn = db.getConn();
			String sql = "select t.id,t.m_user,t.m_pwd,t.m_ip,t.m_proLocat from rmc_client_msg t ";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				MachineInfoBean mib = new MachineInfoBean();
				String id = rs.getString("ID");
				mib.setId(id);

				String m_user = rs.getString("m_user");
				if (m_user == null || "".equals(m_user))
					continue;
				mib.setM_user(m_user);

				String m_pwd = rs.getString("m_pwd");
				if (m_pwd != null)
					mib.setM_pwd(m_pwd);

				String m_ip = rs.getString("m_ip");
				if (m_ip != null)
					mib.setM_ip(m_ip);

				
				String m_proLocat = rs.getString("m_proLocat");
				if (m_proLocat == null || "".equals(m_proLocat))
					continue;
				mib.setM_proLocat(m_proLocat);
				
				
				
				
				miblist.put(id,mib);
			}
			return miblist.size()==0?null:miblist;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
			}
		}

		
		
	}
	
	
	
	@Override
	public String runable(TaskBean tb) {
		// TODO Auto-generated method stub
		
		try {
			return this.connRemotMe(tb);
		} catch (Exception e) {

			log.error(e.toString());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	
	/**
	 * 通过 ssh 链接远程机器
	 * @throws Exception 
	 * @throws Exception 
	 */
	public String connRemotMe(TaskBean tb) throws Exception{
		
		//C:\Program Files\IBM\WebSphere\AppServer\profiles\AppSrv01\bin>stopServer.bat se
		//rver1 -username websphere -password websphere
		// startServer.bat server1
		String stopwebsphere = "\"C:/Program Files/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/stopServer.bat server1 -username websphere -password websphere\"";
		String startwebsphere ="\"C:/Program Files/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/startServer.bat\" server1";
		String cdbin ="'C:/Program Files/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/startServer.bat' server1";
		
		//根据rem_mip 获得机器详细信息
		
		String rem_mip = tb.getRem_mip();
		if(rem_mip == null || "".equals(rem_mip)){
			log.error(GlobalFieldInformation.cmd_rem_ip_isnull);
			return GlobalFieldInformation.cmd_rem_ip_isnull;
		}
		
		MachineInfoBean mib = mapmib.get(tb.getRem_mip());

		if(mib == null ){
			log.error(GlobalFieldInformation.cmd_rem_id_notin_db);
			return GlobalFieldInformation.cmd_rem_id_notin_db;
		}
		
		RmtShellExecutor exe = new RmtShellExecutor(mib.getM_ip(), mib.getM_user(), mib.getM_pwd());
//        System.out.println(exe.exec(new String("f:/run.bat 很好".getBytes("GBK"))));
//        System.out.println(exe.exec(stopwebsphere));
        
//		System.out.println(exe.exec("f:/runbat/run.bat"));
		
		if("2002".equals(tb.getExectype())){
			return exe.exec("cmd.exe /c  " + tb.getCmdorsql());
		}else{
			String cmd = mib.getM_proLocat()+ "\\"+ "run.bat" +" "+tb.getCmdorsql()+" "+tb.getId();
			System.out.println(cmd);
//			cmd = "E:/remotCMD/run.bat Runs 1";
			return exe.exec(cmd);
		}
		
		
	}
	
}