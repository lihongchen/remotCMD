package com.zkr.webalive;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zkr.cmd.Runs;

public class RunsTest {

	@Test
	public void testRunable() {
		try {
			Runs rs=new Runs();
			rs.getCon("http://192.168.1.195:9080/yzd/");
		} catch (Exception e) {
			// TODO: handle exception
			fail("Not yet implemented");
		}
	}

}
