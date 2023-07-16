package org.xpression.listener;

import org.xpression.XNode;

public interface XSheetListener {
	public void onNodeAdd(XNode node);
	public void onNodeRemove(XNode node);
	
	public void onDispose();
}
