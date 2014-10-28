package com.zkr.webalive;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zkr.cmd.Contrast;


public class ContrastTest {

	@Test
	public void testGetSelect() {
       try {
    	   Contrast cn = new Contrast();
		   cn.getSelect();
		} catch (Exception e) {
		     fail("Not yet implemented");
	    }
	}
}
