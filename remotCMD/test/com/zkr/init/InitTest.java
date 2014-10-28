package com.zkr.init;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zkr.cmd.Init;

public class InitTest {

	@Test
	public void testInit() {
		
		try {

		   System.out.println(new 	Init().init());
		} catch (Exception e) {
			e.printStackTrace();
			
			
			// TODO: handle exception
			fail("Not yet implemented");
		}
	}

}
