package org.xpression.time;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements a counter
 * 		Input[0] = T Input
 * 		Input[1] = T Reference
 * 		Input[2] = Bool Reset
 * 
 * 		Output[0] = Integer Count
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
		AllowSelfRecursion = false,
		AllowImplicitRecursion = true
)
public class XCounter extends XNode {
	
	@XNodeConstructor(fields = {"Reference", "Initial"}, values= {"java.lang.Integer:0", "0"})
	public XCounter(Object reference, int initial) {
		this.addInput(new Input(reference.getClass(), null, "Input", this));
		this.addInput(new Input(reference, "Reference", this));
		this.addInput(new Input(Boolean.FALSE, "Reset", this));
		this.addOutput(new Output(initial, "Count", this));
	}
	
	public XCounter(Object reference) {
		this(reference, 0);
	}
	
	public void evaluate() {
		if(this.getInput("Input").getInput() != null && this.getInput("Input").getInput().equals(this.getInput("Reference").getInput())) {
			this.getOutput("Count").setOutput((((Integer)this.getOutput("Count").getOutput()).intValue() + 1));
			
		}
		if(((Boolean)this.getInput("Reset").getInput()).booleanValue()) {
			this.getOutput("Count").setOutput(0);
		}
	}
	
	public String getNodeName() {
		return "Counter";
	}
}
