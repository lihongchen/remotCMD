package com.zkr.webalive;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.zkr.cmd.ContrastReported;


public class ContrastReportedTest {
	@Test
	public void testGetSelect() {
       try {
    	   ContrastReported cn = new ContrastReported();
		   cn.getcount();
		} catch (Exception e) {
		     fail("Not yet implemented");
	    }
	}
}
