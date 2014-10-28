package com.zkr.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testDBConn {

	
	
	public static void main(String[] args) {
		
		
		
		DBConn db = new DBConn();
		
		
	    Connection cc =	db.getConn();
	    if(cc == null) System.out.println("dddddddddddddd");
	    
	    try {
			Statement statement =   cc.createStatement();
			ResultSet rs =  statement.executeQuery("select * from buylicense");
			while (rs.next()) {
				 
				System.out.println(rs.getString("ID"));;
				
			}
			
			
			
			
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		
		
		
		
	}
}
