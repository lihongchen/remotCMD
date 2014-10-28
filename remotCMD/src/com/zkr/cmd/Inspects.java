package com.zkr.cmd;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zkr.control.Controller;
import com.zkr.execCMD.ICmdExecMethod;
import com.zkr.utils.OperateProperties;

/**
 * 检查是否生成临时文件
 * @author xhx
 */

public class Inspects implements ICmdExecMethod{
	private static Log log = LogFactory.getLog(Controller.class);
	/* public static void main(String[] args) throws Exception {
		 Inspects s=new Inspects(); 
		 s.getxinxi();
	 } */
	 /*
	  * 得到某一路径下所有的文件
	  */
	 public String getFiles(String filePath,String filejh){
		 String xinxi="";
		 try {
		  filePath=new String(filePath.getBytes("ISO-8859-1"),"GBK"); 
	      File baseDir = new File(filePath);   
	      if (!baseDir.exists() || !baseDir.isDirectory()){   
	    	  xinxi="文件查找失败：" + filePath + "不是一个目录！"; 
	          log.error("临时数据删除时路径不对");
	      }else{   
			 File root = new File(filePath);
		     File[] files = root.listFiles();
		     for (File file : files) { 
		    	 if(!file.isDirectory()){
		    		 String filename=file.getName();
		    		 if(!filejh.contains(filename) && !filename.contains("velocity.log")){
		    			 DeletFile.delFolder(file.getAbsolutePath());//文件路径
						 xinxi="临时数据已删除";
		    		 }
		    	 }
		     }
		 }
		 } catch (Exception e) {
			 e.printStackTrace();
			 xinxi="异常";
			 log.error("临时数据已删除异常");
		 }
	        return xinxi;
		 }
	 public String getxinxi(){
		String xinxi="";
		OperateProperties op = OperateProperties.getInstance();
		String filePath = op.getValue("deleturl").trim();
		String filejh = op.getValue("filejh").trim();
		String xinxis=getFiles(filePath,filejh);
		if(!"".equals(xinxis)){
			xinxi=xinxis;
		}else{
			xinxi="没有临时文件!";
		}
		 return xinxi;
	 }
	@Override
	public String exec(String id) {
		return getxinxi();
	}
}