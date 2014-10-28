package com.zkr.utils;

import java.util.UUID;


/**
 * 字符串帮助类
 * @author lihongchen 
 *
 */
public class StrUtils{
	
	
	//获得uuid
	public static String UUID(){
		return UUID.randomUUID().toString().toUpperCase();
	}

}