package com.zkr.execSQL;

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
import com.zkr.control.TaskBean;
import com.zkr.utils.DBConn;
import com.zkr.utils.OperateProperties;

/**
 * 执行sql 语句并返回结果 
 * 
 * @author lihongchen
 * 
 */
public class ExecSQL implements IMethdRunable {

	private static Log log = LogFactory.getLog(ExecSQL.class);
	private static Map<String, String> sqlmachine = new HashMap<String, String>();
	private static String DML = "DML";
	private static String DDL = "DDL";

	public ExecSQL() {
		getallmachine();
	}

	@Override
	public String runable(TaskBean tb) {
		// TODO Auto-generated method stub
		
		return execsql(tb);
	}

	// 读取配置文件中执行sql的地址信息
	private void getallmachine() {

		if (sqlmachine.size() == 0) {

			OperateProperties op = OperateProperties.getInstance();
			List<String> listkey = op.getallkey();

			// 获得key 结尾是 db 的 key的信息 和value 将这部分内容缓存到 sqlmachine 中
			for (String key : listkey) {
				if (key != null && key.endsWith("db")) {
					sqlmachine.put(key, op.getValue(key));
				}
			}

		}
	}

	/**
	 * 执行sql语句并返回执行后的结果
	 * 
	 * @return
	 */
	public String execsql(TaskBean tb) {

		String retstr = null;

		String sqlarea = tb.getSqlarea();
		if (sqlarea == null || "".equals(sqlarea) || !sqlarea.endsWith("db")) {
			log.error(GlobalFieldInformation.sqlarea_error);
			return GlobalFieldInformation.sqlarea_error;
		}
		if (sqlmachine.size() == 0) {
			log.error(GlobalFieldInformation.machine_does_not_exist);
			return GlobalFieldInformation.machine_does_not_exist;
		}

		// 分配机器配置信息

		String dbinf = sqlmachine.get(tb.getSqlarea());
		if (dbinf == null || "".equals(dbinf)) {
			log.error(GlobalFieldInformation.db_not_in_ini);
			return GlobalFieldInformation.db_not_in_ini;
		}

		String[] dbinfs = dbinf.split(";");
		if (dbinfs.length != 3) {
			log.error(GlobalFieldInformation.db_info_in_init_error);
			return GlobalFieldInformation.db_info_in_init_error;
		}

		String dotype = tb.getDmlorddl();
		if (dotype == null || "".equals(dotype)) {
			log.error(GlobalFieldInformation.sql_dmlorddl_error);
			return GlobalFieldInformation.sql_dmlorddl_error;
		}

		// dml 测试
		// String sql
		// ="select * from PIS.C_ENTERPRISE_INFO where id='B9DB7160-A671-4A80-9726-6C6750C99F38'";
		// String sql
		// ="update pis.C_ENTERPRISE_INFO t set t.EI_ADDRESS = t.EI_ADDRESS ||' ddd ' where id='B9DB7160-A671-4A80-9726-6C6750C99F38'";
		// String sql
		// ="delete pis.C_ENTERPRISE_INFO t where t.ID='249D87F8-EFB3-441E-BC0F-9B3B8F12A3D2'";

		// ddl测试 创建表，修改表中字段，删除表，添加索引 ，重建索引，修改字段类型
		// String sql
		// ="CREATE TABLE TESTDDL (ID NVARCHAR2(50),NAME NVARCHAR2(100))";
		// String sql ="ALTER TABLE testddl modify(id nvarchar2(50))";
		// String sql ="drop table testddl";
		// String sql
		// ="alter table testddl add constraint pk_test_id primary key(id)";
		// String sql = "create index   index_testddl_name on testddl (name)";
		// String sql = "drop index index_testddl_name";
		// String sql = "alter index index_testddl_name rebuild";
		// String sql = "ALTER TABLE testddl modify(id number)";

		String sql = tb.getCmdorsql();

		if (sql == null || "".equals(sql)) {
			log.error(GlobalFieldInformation.sql_info_error);
			return GlobalFieldInformation.sql_info_error;
		}

		DBConn dbc = new DBConn();
		Connection conn = null;
		PreparedStatement ps = null;
		try {

			conn = dbc.getConn(dbinfs[0], dbinfs[1], dbinfs[2]);

			if (DML.equals(dotype)) {
				sql = sql.trim();
				if (sql == null || "".equals(sql)){
					log.error(GlobalFieldInformation.sql_cmd_is_null);
					return GlobalFieldInformation.sql_cmd_is_null;
				}
				if(sql.endsWith(";")){
					sql = sql.substring(0, sql.length()-1);
				}
				
				
				
				
				
				//判断是查询语句还是更新语句
				if (!sql.toUpperCase().startsWith("SELECT")) {
					ps = conn.prepareStatement(sql);
					int count = ps.executeUpdate();
					System.out.println("影响 " + count + " 条数据");
					ps.close();
					retstr = "影响 " + count + " 条数据" ;
					ps = null;
				}

				//更新语句
				ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				StringBuffer sb = new StringBuffer();
				while (rs.next()) {
					int ccount = rs.getMetaData().getColumnCount();

					for (int i = 1; i <= ccount; i++) {
						sb.append(rs.getMetaData().getColumnName(i))
								.append(":");
						sb.append(
								rs.getString(rs.getMetaData().getColumnName(i)))
								.append("\n");
					}

					sb.append("\n\n--------------------------------------------\n\n");
				}
				retstr = sb.toString();
				
			} else if (DDL.equals(dotype)) {

				ps = conn.prepareStatement(sql);
				ps.execute();
				// System.out.println(bool);
				retstr = GlobalFieldInformation.exec_ok;
			}

		} catch (Exception e) {
			e.printStackTrace();
			retstr = e.toString();
			throw new RuntimeException(e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
			}
		}
		return retstr;
	}

}