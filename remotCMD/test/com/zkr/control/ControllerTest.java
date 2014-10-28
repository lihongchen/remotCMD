package com.zkr.control;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControllerTest {

	@Test
	public void testExecTasks() {
		try {

			Controller c = new Controller();
			c.execTasks();
		} catch (Exception e) {

			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	

	public static void main(String[] args) {
		new ControllerTest().testExecTasks();
		
	}
}
