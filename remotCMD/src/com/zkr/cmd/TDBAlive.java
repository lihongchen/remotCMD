package com.zkr.cmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zkr.control.IMethdRunable;
import com.zkr.control.TaskBean;
import com.zkr.execCMD.ICmdExecMethod;
import com.zkr.utils.DBConn;

/** 
 * 对数据库的健康检查 1、是否可以链接 2、是否能查询数据 3、连接数是否超过一定数量 4、表空间使用情况
 * 
 * @author lihongchen
 * 
 */
public class TDBAlive implements ICmdExecMethod {

	@Override
	public String exec(String id) {
		// TODO Auto-generated method stub
		return dbAlive();
	}


	// 1、是否可以链接 2、是否能查询数据 3、连接数是否超过一定数量 4、表空间使用情况
	public String dbAlive() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String retstr = "";//最后返回的字符串
		try {

			// 测试数据库是否能链接
			DBConn db = new DBConn();
			if ((conn = db.getConn()) == null)
				return "获得数据库链接失败";

			// 测试数据库是否可以查询数据
			String sql = "select * from dual";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (!rs.next())
				return "数据库查询失败";

			ps = null;
			rs = null;

			// 测试数据库链接数是否太多
			String sqlsession = "select count(*) as sessionnum from v$session";
			ps = conn.prepareStatement(sqlsession);
			rs = ps.executeQuery();

			rs.next();
			int count = rs.getInt("sessionnum");
			if (count > 85)
				retstr += "数据库连接数据大于85 \n";
			
			ps = null;
			rs = null;
			// 测试数据库表空间使用情况
			String sqlspuse = "SELECT UPPER(F.TABLESPACE_NAME)  tpname,"
					+ "D.TOT_GROOTTE_MB tpsize,"
					+ "D.TOT_GROOTTE_MB - F.TOTAL_BYTES tpuse,"
					+ "TO_CHAR(ROUND((D.TOT_GROOTTE_MB - F.TOTAL_BYTES) / D.TOT_GROOTTE_MB * 100,2),'990.99')  tpuse_p,"
					+ "F.TOTAL_BYTES tpunuse,"
					+ "F.MAX_BYTES maxblok "
					+ "FROM (SELECT TABLESPACE_NAME,"
					+ "ROUND(SUM(BYTES) / (1024 * 1024), 2) TOTAL_BYTES,"
					+ "ROUND(MAX(BYTES) / (1024 * 1024), 2) MAX_BYTES "
					+ "FROM SYS.DBA_FREE_SPACE "
					+ "GROUP BY TABLESPACE_NAME) F, "
					+ "(SELECT DD.TABLESPACE_NAME, "
					+ "ROUND(SUM(DD.BYTES) / (1024 * 1024), 2) TOT_GROOTTE_MB "
					+ "FROM SYS.DBA_DATA_FILES DD "
					+ "GROUP BY DD.TABLESPACE_NAME) D "
					+ "WHERE D.TABLESPACE_NAME = F.TABLESPACE_NAME  and F.TABLESPACE_NAME in ('PIS','PIS_IMAGE') ";
			ps = conn.prepareStatement(sqlspuse);
			rs = ps.executeQuery();

			
			while(rs.next()){
                String tpname = rs.getString("tpname"); 
                double tpuse_p = rs.getDouble("tpuse_p");
                
                if(tpuse_p >85)
                	retstr += " 表空间 ：" +tpname +" 使用率 ："+tpuse_p + "% 大于 85% \n";
                }
			
			
			if(!"".equals(retstr)) return retstr;

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}

		return "ok";
	}

	

}
