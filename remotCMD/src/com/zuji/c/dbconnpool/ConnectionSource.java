package com.zuji.c.dbconnpool;

import java.sql.Connection;


public class ConnectionSource {
 public static void main(String[] args){
  long begin=System.currentTimeMillis();
  for(int i=0;i<10000;i++){
   
   try {
	
	  Connection conn=DBManager.getConn();
   System.out.print(i+"   ");
   DBManager.closeConn(conn);
   

} catch (Exception e) {
	e.printStackTrace();
	// TODO: handle exception
}
   
  }
  long end=System.currentTimeMillis();
  System.out.println("用时："+(end-begin));
 }
}
