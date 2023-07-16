package org.xpression.adapter;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements a label
 * 		Input[0] = T Input
 * 
 * 		Output[0] = T Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
		AllowSelfRecursion = false,
		AllowImplicitRecursion = false
)
public class XLabel extends XNode {
	
	String labelName;
	
	@XNodeConstructor(fields = {"Name", "Value"}, values= {"Label", "java.lang.Integer:0"})
	public XLabel(String name, Object value) {
		this.labelName = name;
		this.addInput(new Input(value.getClass(), value, "Input", this));
		this.addOutput(new Output(value.getClass(), value, "Output", this));
	}
	
	public String getNodeName() {
		return "Label";
	}
}
