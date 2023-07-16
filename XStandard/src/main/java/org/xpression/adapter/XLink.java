package org.xpression.adapter;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements the linker (output = input)
 * 		Input[k] = T[k] Input~k
 * 
 * 		Output[0] = boolean Out~k
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
		AllowSelfRecursion = false,
		AllowImplicitRecursion = true
)
public class XLink extends XNode {

	@XNodeConstructor(fields = {"Type"}, values= {"java.lang.Integer"})
	public XLink(Class<?>... classes) {
		for(int i = 0;i<classes.length;i++) {
			this.addInput(new Input(classes[i], null, "Input"+i, this));
			this.addOutput(new Output(classes[i], null, "Output"+i, this));
		}
	}
	
	public void evaluate() {
		for(int i=0;i<this.getInputs().size();i++) {
			this.getOutput(i).setOutput(this.getInput(i).getInput());
		}
	}
	
	public String getNodeName() {
		return "Link";
	}

}
