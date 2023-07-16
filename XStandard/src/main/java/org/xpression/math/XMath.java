package org.xpression.math;

import java.lang.reflect.Method;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements math operations (+/-/x/:) between two types
 * 		Input[0] = A
 * 		Input[1] = B
 * 
 * 		Output[0] = Class(A op B) Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
		AllowSelfRecursion = false,
		AllowImplicitRecursion = false
)
public class XMath extends XNode {

	//private Operation operation;
	private Class<?> a; 
	private Class<?> b;
	private Method function;
	byte functionOwner = 0;
		/*
		 * 0 - static XMath
		 * 1 - Class A
		 * 2 - Class B
		 */
	
	@XNodeConstructor(fields = {"A", "B", "Operation"}, values= {"java.lang.Integer", "java.lang.Integer", "ADD"})
	public XMath(Class<?> a, Class<?> b, Operation op) throws NoSuchMethodException {
		//this.operation = op;
		this.a = a;
		this.b = b;
		this.addInput(new Input(a, null, "A", this));
		this.addInput(new Input(b, null, "B", this));
		this.setOperation(op);
	}
	
	public XMath(Class<?> a, Class<?> b) throws NoSuchMethodException {
		this(a, b, Operation.ADD);
	}
	
	public void setOperation(Operation op) throws NoSuchMethodException {
		//TODO: try finding the method
		try {
			functionOwner = 0;
			function = XMath.class.getMethod(op.name(), a, b);
		} 
		catch (NoSuchMethodException | SecurityException e) {
			try {
				function = a.getMethod(op.name(), b);
				functionOwner = 1;
			} 
			catch (NoSuchMethodException | SecurityException e1) {
				try {
					function = b.getMethod(op.name(), a);
					functionOwner = 2;
				} 
				catch (NoSuchMethodException | SecurityException e2) {
					throw new NoSuchMethodException("No XMath:Function was found for " + a.getSimpleName() + ", " + b.getSimpleName());
				}
			}
		}
		
		if(this.getOutputs().size() > 0 && !this.getOutput(0).getType().equals(function.getReturnType())) {
			this.removeOutput(0);
		}
		else {
			this.addOutput(new Output(this.function.getReturnType(), null, "Output", this));
		}
		//operation = op;
	}
	
	public void evaluate() {
		if(this.getInput(0).getInput() != null && this.getInput(1).getInput() != null) {
			try {
				switch(functionOwner) {
				case 0: this.getOutput(0).setOutput(function.invoke(null, getInput(0).getInput(), getInput(1).getInput())); break;
				case 1: this.getOutput(0).setOutput(function.invoke(getInput(0).getInput(), getInput(1).getInput())); break;
				case 2: this.getOutput(0).setOutput(function.invoke(getInput(1).getInput(), getInput(0).getInput())); break;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
		}
	}
	
	public static Number ADD(Number a, Number b) {
		return new Float(a.floatValue() + b.floatValue());
	}
	
	public static Integer ADD(Integer a, Integer b) {
		return new Integer(a.intValue() + b.intValue());
	}

	public String getNodeName() {
		return "Math";
	}
}
