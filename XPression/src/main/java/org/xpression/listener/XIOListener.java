package org.xpression.listener;

import org.xpression.io.IOBase;

public interface XIOListener {
	public void onChange(Object prev, Object actual);
	
	public void onConnected(IOBase a, IOBase b);
	public void onDisconnected(IOBase a, IOBase b);
}