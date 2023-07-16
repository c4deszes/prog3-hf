package org.xpression.time;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements a delay line
 * 		Input[0] = T Input
 * 		Input[1] = Integer Delay
 * 
 * 		Output[0] = T Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
	AllowSelfRecursion = false,
	AllowImplicitRecursion = true
)
public class XDelay extends XNode {
	
	@XNodeConstructor(fields = { "Type" }, values = { "java.lang.Boolean" })
	public XDelay(Class<?> type) {
		super();
		this.addInput(new Input(type, null, "Input", this));
		this.addOutput(new Output(type, null, "Output", this));
		this.addInput(new Input(Integer.class, null, "Delay", this));
	}
	
	public void evaluate() {
		//TODO: fix
		try {
			Thread.sleep((Integer)this.getInput(1).getInput());
		} catch (InterruptedException | NullPointerException e) {}
		this.getOutput(0).setOutput(this.getInput(0).getInput());
	}

	public String getNodeName() {
		return "Delay";
	}
}
