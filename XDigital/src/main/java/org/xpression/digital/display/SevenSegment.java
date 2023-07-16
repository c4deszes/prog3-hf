package org.xpression.digital.display;

import org.xpression.XNode;
import org.xpression.io.Input;

/**
 * This node implements a 7 segment
 * 		Input[0...6] = ABCDEFG of the 7segment (bool)
 * 
 * @author Balazs Eszes
 * @see XNode
 */

public class SevenSegment extends XNode {

	public SevenSegment() {
		this.addInput(new Input(Boolean.FALSE, "A", this));
		this.addInput(new Input(Boolean.FALSE, "B", this));
		this.addInput(new Input(Boolean.FALSE, "C", this));
		this.addInput(new Input(Boolean.FALSE, "D", this));
		this.addInput(new Input(Boolean.FALSE, "E", this));
		this.addInput(new Input(Boolean.FALSE, "F", this));
		this.addInput(new Input(Boolean.FALSE, "G", this));
	}
	
	public void evaluate() {
		
	}

}
