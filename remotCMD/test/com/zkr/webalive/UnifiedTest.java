package com.zkr.webalive;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zkr.cmd.Unified;

public class UnifiedTest {

	@Test
	public void testGetInsert() {
		try {
			Unified uf=new Unified();
			uf.getInsert();
		} catch (Exception e) {
			fail("Not yet implemented");
		}
	}

}
