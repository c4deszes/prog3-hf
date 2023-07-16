package com.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xpression.XNode;
import org.xpression.Xpression;

@RunWith(Parameterized.class)
public class BooleTest {
	
	boolean[] data = new boolean[3];
	static XNode node;
	
	@BeforeClass
	public static void setup() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Xpression.initialize();
		Xpression.loadLibrary("libraries/stdlib.xlib");
		node = Xpression.newNode("stdlib:Boole", Xpression.findClass("org.xpression.logic.Gate").getMethod("valueOf", String.class).invoke(null, "AND"), 2);
	}
	
	public BooleTest(boolean a, boolean b, boolean c) {
		data[0] = a;
		data[1] = b;
		data[2] = c;
	}
	
	@Test
	public void check() {
		node.getInput(0).setInput(data[0]);
		node.getInput(1).setInput(data[1]);
		
		node.evaluate();
		
		Assert.assertEquals(data[2], node.getOutput(0).getValue());
	}
	
	@Parameters
	public static List<Boolean[]> parameters() {
		List<Boolean[]> out = new ArrayList<Boolean[]>();
		out.add(new Boolean[]{ false, false, false});
		out.add(new Boolean[]{ true, false, false});
		out.add(new Boolean[]{ false, true, false});
		out.add(new Boolean[]{ true, true, true});
		return out;
	}
}
