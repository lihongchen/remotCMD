package com.zkr.DBAlive;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.zkr.cmd.TDBAlive;
import com.zkr.control.TaskBean;

public class TDBAliveTest {

	@Test
	public void testRunable() {
		try {
			TaskBean tb = new TaskBean();
			TDBAlive tdba = new TDBAlive();
			System.out.println(tdba.dbAlive());
			
		} catch (Exception e) {
			// TODO: handle exception
			fail("Not yet implemented");
		}
	}

}
