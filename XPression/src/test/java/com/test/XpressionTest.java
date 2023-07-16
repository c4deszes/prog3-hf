package com.test;

import java.io.IOException;

import org.xpression.XNode;
import org.xpression.XNodeState;
import org.xpression.XSheet;
import org.xpression.Xpression;
import org.junit.*;

public class XpressionTest {

	@Before
	public void setup() throws IOException {
		Xpression.initialize();
	}
	
	/**
	 * Testing whether loading an existing library throws any exception
	 * @throws IOException
	 */
	@Test
	public void testLoadingLibrary() throws IOException {
		Xpression.loadLibrary("libraries/stdlib.xlib");
		Assert.assertNotEquals(null, Xpression.getLibrary("stdlib"));
	}
	
	/**
	 * Testing whether loading a non existent library throws an exception
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testInvalidLibrary() throws IOException {
		Xpression.loadLibrary("libraries/invalid.xlib");
	}
	
	@Test
	public void testSheet() throws IOException {
		Xpression.loadLibrary("libraries/stdlib.xlib");
		XSheet sheet = new XSheet("sheets/example.xsh");
		sheet.dispose();
	}
	
	@Test(expected=IOException.class)
	public void testInvalidSheet() throws IOException {
		Xpression.loadLibrary("libraries/stdlib.xlib");
		XSheet sheet = new XSheet("sheets/invalid.xsh");
		sheet.dispose();
	}
	
	@Test
	public void testNodeState() throws InterruptedException {
		XNode node = new XNode();
		Assert.assertEquals(XNodeState.UNINITIALIZED, node.getXNodeState());
		node.init();
		Assert.assertEquals(XNodeState.INITIALIZED, node.getXNodeState());
		Thread.sleep(1000);
		Assert.assertEquals(XNodeState.WAITING, node.getXNodeState());
		node.dispose();
		Thread.sleep(1000);
		Assert.assertEquals(XNodeState.DESTROYED, node.getXNodeState());
	}
	
	
}
