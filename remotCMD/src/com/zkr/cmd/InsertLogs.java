package com.zkr.cmd;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zkr.execCMD.ICmdExecMethod;
import com.zkr.utils.DBConn;
import com.zkr.utils.OperateProperties;
import com.zkr.utils.StrUtils;

/**
 * 日志插入数据库中
 * @author xhx
 */
public class InsertLogs implements  ICmdExecMethod{
	//1.把日志文件以大字段的形式写入数据库中
	
	private static Log log = LogFactory.getLog(InsertLogs.class);

	  public static String insersql(String fid,String path) throws Exception{
		 String xinxi="";
		 Connection con=null;
		 PreparedStatement pmt=null;
		try {
			DBConn bc=new DBConn();
			con=bc.getConn();
			pmt = con.prepareStatement("insert into rmc_cmdinfo_result(id,fid,filename,textorfile) values(?,?,?,?)");
			pmt.setString(1, StrUtils.UUID());
			

			
			String filename = new File(path).getName();

			
			pmt.setString(2, fid);
			pmt.setString(3, filename);
			byte[] content = readFile(path);
			pmt.setBytes(4,content);
			pmt.executeUpdate();
			xinxi="日志添加成功";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("数据id=" +fid +"   " +e.toString());
			xinxi="异常";
		}finally{
			pmt.close();
			con.close();
		}
		return xinxi;
	}
	
	//读取文件并获得文件中数据   
	public static byte[] readFile(String filepath) throws IOException{
	    File file =new File(filepath);
	    if(filepath==null || filepath.equals("")){
	      throw new NullPointerException("无效的文件路径");
	    }
	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    
	    BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
	    int read = bufferedInputStream.read(bytes);
	    if (read != len){
	      throw new IOException("读取文件不正确");
	    }
	    bufferedInputStream.close();
	    return bytes;
	}



  //读取文件并获得文件中数据   
  /* public static String readfile(String filepath){
	   String s1 = null; 
	   try{ 
		   FileReader reader = new FileReader(filepath); 
		   BufferedReader br = new BufferedReader(reader); 
		   int line=0; 
		   while((s1 = br.readLine()) != null) { 
			   ++line; 
			   System.out.println("line:--"+line); 
			   System.out.println("=="+s1); 
		   } 
		   br.close(); 
		   reader.close(); 
	   }catch(IOException e){ 
		   e.printStackTrace();
	   }  
	   return s1;
   }*/
	   public static void main(String[] args) throws IOException {
		   try {
			   OperateProperties op = OperateProperties.getInstance();
				String path = op.getValue("logpath");
			insersql("111", path.trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	   }
	@Override
	public String exec(String id) {

		
		OperateProperties op = OperateProperties.getInstance();
		String path = op.getValue("logpath");
		
        try {
        	return InsertLogs.insersql(id, path.trim());
		} catch (Exception e) {
            log.error("数据id=" +id +"   " +e.toString());
            return e.toString();
		}
	}

}

