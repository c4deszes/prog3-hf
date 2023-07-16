package com.test;

import java.io.IOException;

import org.junit.*;
import org.xpression.Xpression;

public class StdlibTest {
	
	@Before
	public void setup() throws IOException {
		Xpression.initialize();
		Xpression.loadLibrary("libraries/stdlib.xlib");
	}
	
	/**
	 * Testing whether all the Stdlib nodes exist
	 */
	@Test
	public void checkComponents() {
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Label"));
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Link"));
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Result"));
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Value"));
		
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Boole"));
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Compare"));
		
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Math"));
		
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Counter"));
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Delay"));
		Assert.assertNotEquals(null, Xpression.findNode("stdlib:Pulse"));
	}
	
	@Test
	public void checkRegistry() throws ClassNotFoundException {
		Assert.assertTrue(Xpression.getClasses().contains(Xpression.findClass("org.xpression.math.XVector")));
	}
}
