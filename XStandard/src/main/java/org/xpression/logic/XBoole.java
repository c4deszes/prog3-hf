package org.xpression.logic;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements the boolean operator
 * 		Input[0] = boolean A
 * 		Input[1] = boolean B
 * 		...
 * 		Input[k] = boolean K
 * 
 * 		Output[0] = boolean Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
		AllowSelfRecursion = true,
		AllowImplicitRecursion = true
)
public class XBoole extends XNode {
	
	private Gate gate;		//Type of operation
	
	@XNodeConstructor(fields = {"Gate", "Inputs"}, values= {"AND", "2"})
	public XBoole(Gate gate, Integer n) {
		this.gate = gate;
		for(int i = 0; i < n; i++) {
			this.addInput(new Input(Boolean.class, Boolean.FALSE, String.valueOf((char)('A' + i)), this));
		}
		this.addOutput(new Output(Boolean.FALSE, "Output", this));
	}
	
	public void evaluate() {
		boolean xor = false;
		boolean and = true;
		boolean or = false;
		for(Input input : this.getInputs()) {
			if(((Boolean)input.getInput()).booleanValue()) {
				xor = !xor;
				or = true;
				if(gate == Gate.OR || gate == Gate.NOR) {
					break;
				}
			}
			else {
				and = false;
				if(gate == Gate.AND || gate == Gate.NAND) {
					break;
				}
			}
		}
		switch(gate) {
		case AND: this.getOutput(0).setOutput(and); break;
		case NAND:this.getOutput(0).setOutput(!and); break;
		case NOR:this.getOutput(0).setOutput(!or); break;
		case NOT:this.getOutput(0).setOutput(!and); break;
		case OR:this.getOutput(0).setOutput(or); break;
		case XNOR:this.getOutput(0).setOutput(!xor); break;
		case XOR:this.getOutput(0).setOutput(xor); break;		
		}
	}
	

	public String getNodeName() {
		return "Boole";
	}
}
