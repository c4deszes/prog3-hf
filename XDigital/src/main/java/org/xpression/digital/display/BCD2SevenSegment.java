package org.xpression.digital.display;

import org.xpression.XNode;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements a binary to 7 segment converter
 * 		Input[0...3] = LSB - MSB of the binary number (bool)
 * 
 * 		Output[0...6] = ABCDEFG of the 7segment (bool)
 * 
 * @author Balazs Eszes
 * @see XNode
 * @param [decimal] The initial value of the counter
 */

public class BCD2SevenSegment extends XNode {
	
	public BCD2SevenSegment(int decimal) {
		//TODO: to binary
		this.addInput(new Input(Boolean.FALSE, "0", this));
		this.addInput(new Input(Boolean.FALSE, "1", this));
		this.addInput(new Input(Boolean.FALSE, "2", this));
		this.addInput(new Input(Boolean.FALSE, "3", this));
		
		this.addOutput(new Output(Boolean.FALSE, "A", this));
		this.addOutput(new Output(Boolean.FALSE, "B", this));
		this.addOutput(new Output(Boolean.FALSE, "C", this));
		this.addOutput(new Output(Boolean.FALSE, "D", this));
		this.addOutput(new Output(Boolean.FALSE, "E", this));
		this.addOutput(new Output(Boolean.FALSE, "F", this));
		this.addOutput(new Output(Boolean.FALSE, "G", this));
	}

	public BCD2SevenSegment() {
		this(0);
	}
	
	public void evaluate() {
		byte in = 0;
		for(int i=0;i<4;i++) {
			if(((Boolean)this.getInput(i).getInput()).booleanValue()) {
				in |= 1;
				in <<= 1;
			}
		}
		in >>= 1;
		//switch()
	}
}
