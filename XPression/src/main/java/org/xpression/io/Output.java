package org.xpression.io;

import org.xpression.XNode;
import org.xpression.exception.IOClassException;

public class Output extends IOBase {	
	public Output(Class<?> type, Object value, String name, XNode parent) {
		super(type, value, false, name, parent);
	}
	
	public Output(Object value, String name, XNode parent) {
		super(value!=null?value.getClass():Object.class, value, false, name, parent);
	}
	
	public synchronized void setOutput(Object t) {
		super.setValue(t);
	}
	
	public synchronized Object getOutput() {
		return super.getValue();
	}
	
	public String toString() {
		return "Output[id="+this.getID() + ", type=" + type.getSimpleName() + "]";
	}

	@Override
	public void connect(IOBase io) throws IOClassException {
		if(io.isOutput() || !io.getType().equals(this.getType())) {
			throw new IOClassException(this, io);
		}
		if(io.isInput()) {
			if(!connections.contains(io)) {
				connections.add(io);
				io.disconnect();
				io.connection = this;
				io.setValue(this.getValue());
			}
		}
	}

	@Override
	public void disconnect() {
		for(IOBase i : connections) {
			i.connection = null;
		}
		connections.clear();
	}

	@Override
	public void disconnect(IOBase io) {
		io.connection = null;
		connections.remove(io);
	}
	
	
}
