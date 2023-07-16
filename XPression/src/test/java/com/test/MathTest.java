package com.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xpression.XNode;
import org.xpression.Xpression;

@RunWith(Parameterized.class)
public class MathTest {
	
	static XNode node;
	
	int a;
	int b;
	int c;
	
	@BeforeClass
	public static void setup() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Xpression.initialize();
		Xpression.loadLibrary("libraries/stdlib.xlib");
		node = Xpression.newNode("stdlib:Math", Integer.class, Integer.class, Xpression.findClass("org.xpression.math.Operation").getMethod("valueOf", String.class).invoke(null, "ADD"));
	}
	
	public MathTest(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Test
	public void check() {
		node.getInput(0).setInput(a);
		node.getInput(1).setInput(b);
		
		node.evaluate();
		
		Assert.assertEquals(c, node.getOutput(0).getValue());
	}
	
	final static int NUMBER_OF_TESTS = 40;
	
	@Parameters
	public static List<Integer[]> parameters() {
		List<Integer[]> out = new ArrayList<Integer[]>();
		
		Random random = new Random();
		for(int i = 0;i<NUMBER_OF_TESTS;i++) {
			int p = (random.nextInt(Integer.MAX_VALUE) * (int)Math.signum(random.nextDouble()-0.5));
			int q = (random.nextInt(Integer.MAX_VALUE) * (int)Math.signum(random.nextDouble()-0.5));
			out.add(new Integer[] {p, q, p + q });
		}
		return out;
	}
}
