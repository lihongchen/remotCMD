package com.zkr.execCMD;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zkr.control.TaskBean;

public class ExecCMDTest {

	@Test
	public void testRunable() {
		try {

			
			ExecCMD ec = new ExecCMD();
			TaskBean tb = new TaskBean();
			
			ec.connRemotMe(tb);
			
		} catch (Exception e) {
			// TODO: handle exception
			fail("Not yet implemented");
		}
	}

}
