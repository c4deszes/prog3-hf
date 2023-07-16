package org.xpression.math;

import org.xpression.XNode;
import org.xpression.io.Input;
import org.xpression.io.Output;

public class Decimal2Binary extends XNode {

	public Decimal2Binary(Class<?> type) {
		if(type.equals(Long.class)) {
			this.addInput(new Input(new Long((long) 0), "In", this));
			for(int i=0;i<Long.SIZE;i++) {
				this.addOutput(new Output(false, String.valueOf(i), this));
			}
		}
		else if(type.equals(Integer.class)) {
			this.addInput(new Input(new Integer(0), "In", this));
			for(int i=0;i<Integer.SIZE;i++) {
				this.addOutput(new Output(false, String.valueOf(i), this));
			}
		}
		else if(type.equals(Short.class)) {
			this.addInput(new Input(new Short((short) 0), "In", this));
			for(int i=0;i<Short.SIZE;i++) {
				this.addOutput(new Output(false, String.valueOf(i), this));
			}
		}
		else if(type.equals(Byte.class)) {
			this.addInput(new Input(new Byte((byte) 0), "In", this));
			for(int i=0;i<Byte.SIZE;i++) {
				this.addOutput(new Output(false, String.valueOf(i), this));
			}
		}
	}
	
	public void evaluate() {
		
	}

}
