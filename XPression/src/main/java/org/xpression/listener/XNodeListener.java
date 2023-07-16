package org.xpression.listener;

import org.xpression.io.Input;
import org.xpression.io.Output;

public interface XNodeListener {
	public void onAddInput(Input i);
	public void onRemoveInput(Input i);
	
	public void onAddOutput(Output o);
	public void onRemoveOuput(Output o);
	
	public void onEvaluation();
	public void onStateChange();
	
	public void onIDChange(String id);
}
