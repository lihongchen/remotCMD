package com.zkr.control;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zkr.execCMD.ExecCMD;
import com.zkr.execSQL.ExecSQL;
import com.zkr.utils.StrUtils;
import com.zuji.c.dbconnpool.DbcpBean;

/**
 * 调用所有的方法，并将执行结果保存到数据库中
 * 
 * @author root
 * 
 */
public class Controller {
	private static Log log = LogFactory.getLog(Controller.class);
	private List<TaskBean> tasks = null;

	public Controller() {
		tasks = checkallTask();

	}

	// 查询要执行任务的信息
	private List<TaskBean> checkallTask() {

		Connection conn = null;
		PreparedStatement ps = null;
		List<TaskBean> tblist = new ArrayList<TaskBean>();
		try {

			DbcpBean db = new DbcpBean();
			
			System.out.println("数据库链接池信息" + db.getDataSourceStats());
			
			conn = db.getConn();
			String sql = "select t.id,t.exectype,t.rem_mip,t.sqlarea,t.cmdorsql,t.dmlorddl  from RMC_CMDINFO t where t.status='1000'";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				TaskBean tb = new TaskBean();
				String id = rs.getString("ID");
				tb.setId(id);

				// String username = rs.getString("username");
				// if (username != null)
				// tb.setUsername(username);
				// Date createdate = rs.getDate("createdate");
				// if (createdate != null)
				// tb.setCreatedate(createdate);
				// Date execdate = rs.getDate("execdate");
				// if (execdate != null)
				// tb.setExecdate(execdate);
				String exectype = rs.getString("exectype");
				if (exectype == null || "".equals(exectype))
					continue;
				tb.setExectype(exectype);

				String rem_mip = rs.getString("rem_mip");
				if (rem_mip != null)
					tb.setRem_mip(rem_mip);

				String sqlarea = rs.getString("sqlarea");
				if (sqlarea != null)
					tb.setSqlarea(sqlarea);

				String dmlorddl = rs.getString("dmlorddl");
				if (dmlorddl != null)
					tb.setDmlorddl(dmlorddl);

				String cmdorsql = rs.getString("cmdorsql");
				if (cmdorsql == null || "".equals(cmdorsql))
					continue;
				tb.setCmdorsql(cmdorsql);
				// String status = rs.getString("status");

				tblist.add(tb);
			}

			return tblist.size() > 0 ? tblist : null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
			}
		}

		return null;
	}

	// 执行查询出来的任务
	public void execTasks() {

		if (tasks == null || tasks.size() == 0)
			return;

		for (TaskBean tb : tasks) {

			// 任务分两类 1、执行sql命令 ；2、执行cmd 命令；
			String exectype = tb.getExectype();
			if (exectype == null || "".equals(exectype))
				continue;

			// 2001 sql ； 2002 cmd 2003 bat
			if ("2001".equals(exectype)) {

				//执行sql 语句
				try {
					
					IMethdRunable mr = new ExecSQL();

					// 执行任务
					String result = mr.runable(tb);

					// 保存任务执行结果
					saveresultinfo(tb.getId(), result);

					// 更新任务状态和执行时间

					saveTaskStatus(tb.getId(), GlobalFieldInformation.status_succ);
	
				} catch (Exception e) {

					saveresultinfo(tb.getId(), e.toString());
					saveTaskStatus(tb.getId(), GlobalFieldInformation.status_fail);

					log.error(" 执行sql 错误 id="+tb.getId() + e.toString());
					e.printStackTrace();
				}
				
			} else if ("2002".equals(exectype)) {
				
				//执行 cmd 命令
				
				IMethdRunable mr = new ExecCMD();
                String result = mr.runable(tb);	
            	// 保存任务执行结果
				saveresultinfo(tb.getId(), result);

				// 更新任务状态和执行时间

				saveTaskStatus(tb.getId(), GlobalFieldInformation.status_succ);

				

			}else if("2003".equals(exectype)){
				//判断执行bat命令
				
				IMethdRunable mr = new ExecCMD();
                String result = mr.runable(tb);				
            	// 保存任务执行结果
				saveresultinfo(tb.getId(), result);

				// 更新任务状态和执行时间

				saveTaskStatus(tb.getId(), GlobalFieldInformation.status_succ);

				
				
			}
		}
	}

	/**
	 * 保存任务执行结果
	 * 
	 * @param fid
	 *            任务id
	 * @param text
	 *            任务执行结果信息
	 */
	private void saveresultinfo(String fid, String text) {

		Connection conn = null;
		PreparedStatement ps = null;
		try {

			DbcpBean db = new DbcpBean();
			conn = db.getConn();

			String sql = "insert into rmc_cmdinfo_result(id,fid,textorfile) values(?,?,?)";
			String id = StrUtils.UUID();

			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, fid);
			ps.setBytes(3, text.getBytes());
			int res = ps.executeUpdate();
			if (res != 1)
				log.error(GlobalFieldInformation.insert_into_db_error
						+ "  fid= " + fid);

			// conn.setAutoCommit(false);// 取消自动提交功能

			// ps =
			// conn.prepareStatement("select textorfile from rmc_cmdinfo_result where id='"+id+"' for update");
			// ResultSet rs = ps.executeQuery();
			// if (rs.next()) {
			// // 得到java.sql.Blob对象后强制转换为oracle.sql.BLOB
			// oracle.sql.BLOB blob = (oracle.sql.BLOB)
			// rs.getBlob("textorfile");
			// // 通过getBinaryOutputStream()方法获得向数据库中插入图片的"管道"
			// // blob.setBytes(text.getBytes());
			// @SuppressWarnings({ "deprecation", "deprecation" })
			// OutputStream out = blob.getBinaryOutputStream();
			//
			//
			//
			// }
			// //更新大字段
			// conn.commit();
			// conn.setAutoCommit(true);// 恢复现场

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
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

	/**
	 * 更新任务表的状态字段
	 * 
	 * @param id
	 */
	public void saveTaskStatus(String id, String status) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {

			DbcpBean db = new DbcpBean();
			conn = db.getConn();

			String sql = "update rmc_cmdinfo t set t.execdate =? ,t.status=? where t.id=?";

			ps = conn.prepareStatement(sql);
			ps.setDate(1, new Date(System.currentTimeMillis()));
			ps.setString(2, status);
			ps.setString(3, id);
			int res = ps.executeUpdate();
			if (res != 1)
				log.equals(GlobalFieldInformation.update_status_error
						+ "  id= " + id);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
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

}
