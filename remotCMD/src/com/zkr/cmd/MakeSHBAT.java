package com.zkr.cmd;

import java.io.File;
import java.io.FileOutputStream;
/**
 * 生成 bat 和sh 文件
 * @author lihongchen
 *
 */
public class MakeSHBAT {

	public static void main(String[] args) throws Exception {
		
		if (args.length > 0) {
			
			
			
			try {
				//编译时生成 默认 bat 和sh
				if(args[0].equals("build/remotCMD")){
				System.out.println(args[0]);
				// 生成 run.bat 和 run.sh 文件
				FileOutputStream fos = new FileOutputStream(new File(args[0]
						+ File.separator + "run1.bat"));
				String shstr = "#!/bin/bash \n" + "APP_HOME=.  \n"
						+ "CLASSPATH=$APP_HOME/lib \n"
						+ "#引进所有的jar包，这里用的循环，当然也可以按照这个格式一个一个  写 \n"
						+ "for i in \"$APP_HOME\"/lib/*.jar \n" + "do \n"
						+ "#环境变量就这格式 \n"
						+ " CLASSPATH=\"$CLASSPATH\":\"$i\"    \n" +

						"done   \n" + "#不写这个可能会说找不到main类  \n"
						+ "export CLASSPATH=.:$CLASSPATH:$APP_HOME/conf/   \n" +

						"#打印环境变量，可以不写  \n" + "echo ${CLASSPATH}   \n " +

						"java com.zkr.client.clientService Init initmachineinfo \n";

				fos.write(shstr.getBytes());
				fos.flush();
				fos.close();

				
				FileOutputStream fos2 = new FileOutputStream(new File(args[0]
						+ File.separator + "run.bat"));
				//获得lib下的所有文件
				File f =new File("lib");
				File[] fs = f.listFiles();
				String libpath = "";
				for (File file : fs) {
					libpath +="lib"+File.separator+file.getName()+";";
				}
				
				
				String batstr = "@set  classpath=%classpath%;"+libpath+"lib/remotCMD.jar;conf\n"+
				"%~d0 \n"+
				"cd %~dp0 \n"+		
				"java com.zkr.client.clientService %1 %2 \n"+   
				"pause ;";

				fos2.write(batstr.getBytes());
				fos2.flush();
				fos2.close();
				}else{
					System.out.println(" 方法调用成功  ");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
}
