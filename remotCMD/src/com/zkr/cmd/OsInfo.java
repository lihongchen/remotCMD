package com.zkr.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zkr.control.GlobalFieldInformation;
import com.zkr.execCMD.ICmdExecMethod;
import com.zkr.utils.OperateProperties;

/**
 * 查询 操作系统运行信息，确定操作系统运行状态
 * @author lihongchen
 *
 */
public class OsInfo implements ICmdExecMethod{

	
	
	private static Log log = LogFactory.getLog(OsInfo.class);
	@Override
	public String exec(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String osinfo(String fid){
		
		OperateProperties op = OperateProperties.getInstance();
		String path = op.getValue("OSInfopath");
		if(path == null || "".equals(path))
			return "OSInfopath 为空 ，error";
		try {
			InsertLogs.insersql(fid, path.trim());
			return GlobalFieldInformation.exec_ok;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.toString());
			return e.toString();
			
		}
		
	}

	
	
	
	
	
	
	
	
}