package org.xpression.ui;

import java.util.logging.Logger;

public interface Wrapper {
	public static Logger LOGGER = Logger.getLogger("FXWrapper");
	
	void initUI();
	default void onInitUI() {}
	
	void updateUI();
	default void onUpdateUI() {};
	
	void destroyUI();
	default void onDestroyUI() {};
	
	double getX();
	double getY();
}
