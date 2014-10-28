package com.zkr.cmd;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.zkr.execCMD.ICmdExecMethod;
import com.zkr.utils.OperateProperties;

/**
 * 检查网站是否正常运行
 * @author xhx
 */
public class Runs implements ICmdExecMethod{

		public static String getCon(String url){
			String connect="";
			try {
				URL u = new URL(url);
				try {
					  /**此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类 
			                                  的子类HttpURLConnection,故此处最好将其转化为HttpURLConnection类型的对象,以便用到 */

					HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();
					try {
						uConnection.connect();// 连接(HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。)
						System.out.println("--"+uConnection.getResponseCode());//uConnection.getResponseCode()可以获取状态码
						
						/* 
					     * 超时设置，防止 网络异常的情况下，可能会导致程序僵死而不继续往下执行 
						   */
						 HttpURLConnection urlCon = (HttpURLConnection)u.openConnection();  
						 urlCon.setConnectTimeout(30000);  
						 urlCon.setReadTimeout(30000);  
						 if(uConnection.getResponseCode()==200){
							 connect="ok";
						 }else{
							 connect=String.valueOf(uConnection.getResponseCode()); 
						 }

					} catch (Exception e) {
						e.printStackTrace();
						connect = "连接异常";
					}
					
				} catch (IOException e) {
					e.printStackTrace();
					connect = "io异常";
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
				connect = "url异常";
			}
			return connect;
		}
		@Override
		public String exec(String id) {
			// TODO Auto-generated method stub
			
			OperateProperties op = OperateProperties.getInstance();
			String runsurl = op.getValue("runsurl");
			
			
			return Runs.getCon(runsurl.trim());
		}
}
