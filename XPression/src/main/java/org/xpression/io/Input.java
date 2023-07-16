package org.xpression.io;

import org.xpression.XNode;
import org.xpression.exception.IOClassException;

public class Input extends IOBase {	
	public Input(Class<?> type, Object value, String name, XNode parent) {
		super(type, value, true, name, parent);
	}
	
	public Input(Object value, String name, XNode parent) {
		super(value.getClass(), value, true, name, parent);
	}
	
	public void setInput(Object t) {
		super.setValue(t);
	}
	
	public Object getInput() {
		return super.getValue();
	}
	
	public String toString() {
		return "Input[id="+this.getID() + ", type=" + type.getSimpleName() + "]";
	}

	@Override
	public void connect(IOBase io) throws IOClassException {
		if(io.isInput() || !io.getType().equals(this.getType())) {
			throw new IOClassException(this, io);
		}
		if(io.isOutput()) {
			this.disconnect();
			connection = io;
			connection.getConnections().add(this);
			this.setValue(connection.getValue());
		}
	}

	@Override
	public void disconnect() {
		if(connection != null) {
			connection.disconnect(this);
		}
	}

	@Override
	public void disconnect(IOBase io) {
		if(connection != null) {
			connection.disconnect(this);
		}
	}
}
