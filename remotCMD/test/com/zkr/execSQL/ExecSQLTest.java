package com.zkr.execSQL;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zkr.control.TaskBean;

public class ExecSQLTest {

	@Test
	public void testExecsql() {
		try {

			TaskBean tb = new TaskBean();
			tb.setExectype("2001");
			tb.setSqlarea("bjdb");
			tb.setCmdorsql("select * from dual");
			tb.setDmlorddl("DML");
			
			System.out.println(new ExecSQL().execsql(tb));
			
			
		} catch (Exception e) {
			// TODO: handle exception
			fail("Not yet implemented");
		}
		
	}

}
