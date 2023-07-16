package org.xpression.exception;

import org.xpression.io.IOBase;

public class IOClassException extends Exception {
	private static final long serialVersionUID = 1734141555773925516L;
	
	IOBase a;
	IOBase b;
	String msg;

	public IOClassException(IOBase a, IOBase b) {
		this.a = a;
		this.b = b;
	}
	
	public IOClassException(String message) {
		this.msg = message;
	}
	
	public String getMessage() {
		if(a != null && b != null) {
			return a + " <-> " + b;
		}
		return msg;
	}
}
