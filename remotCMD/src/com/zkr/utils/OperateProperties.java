package com.zkr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;




public class OperateProperties {

	private static  Properties properties=null;
	
	public static OperateProperties propertiesUtil=null;
	private static final String FILE_PATH="init.properties";
	public OperateProperties(){
		properties=new Properties();
		

		try {
			InputStream is= OperateProperties.class.getClassLoader().getResourceAsStream(FILE_PATH);
			//InputStream is=this.getClass().getClassLoader().getResourceAsStream(FILE_PATH);
			properties.load(is);
			is.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//得到properties 文件操作对象 
	public static OperateProperties getInstance(){
			if(propertiesUtil==null)
				return new OperateProperties();
			else 
				return propertiesUtil;
	}
	
	
	public List<String> getallkey(){
		
		Enumeration<?> et = properties.keys();

		List<String> lists = new ArrayList<String>();
		while(et.hasMoreElements()){
			lists.add(et.nextElement().toString());
		}
		
		
		System.out.println(lists);
		return lists;
	}
	
	
	
	
	//从配置文件中取值
	public  String getValue(String key){
		String value="";
		if(StringUtils.isEmpty(key))return value;
		value=properties.getProperty(key);
		return value;
	}
	
	//把值存入properties文件中
	public  void storeValue(String key,String value){
		//将值放入properties文件中
		properties.setProperty(key, value);
		URL url = this.getClass().getClassLoader().getResource("");
		System.out.println(url.getPath());
//		String classPathName =  url.getPath();
//		  String path=classPathName.substring(0, classPathName.lastIndexOf("classes"))+"classes"+File.separatorChar+FILE_PATH;
		String path = url.getPath()+File.separator+FILE_PATH;
		  if(path.indexOf("%20")>=0)path=path.replaceAll("%20", " ");
		try {
			OutputStream out=new BufferedOutputStream(new FileOutputStream(path));
			  System.out.println(out);
			//保存配置文件信息
			properties.store(out, "save value-->key:"+key+" value:"+value);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//读取配置文件中所有的信息, 放入Map 
	public static Map readValues(){
		Map psMap=new LinkedHashMap();
		Enumeration en=properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value=properties.getProperty(key);
			psMap.put(key, value);
		}
		return psMap;
	}
	
	public static void main(String[] args) {
		OperateProperties psUtil=OperateProperties.getInstance();
//		psUtil.storeValue("A", "--AAA");
		//System.out.println(psUtil.properties);
//        System.out.println("t---"+psUtil.getValue("A")); 
		
		psUtil.getallkey();
		
	}
}