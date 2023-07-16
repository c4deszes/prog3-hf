package org.xpression.behavior;

import java.util.List;

import org.xpression.io.Input;
import org.xpression.io.Output;

public interface INode {
	
	public void setID(String id);
	public String getID();
	
	public void init();
	public void evaluate();
	public void dispose();
	
	public void addInput(Input i);
	public void addOutput(Output o);
	
	public void removeInput(int i);
	public void removeOutput(int i);
	
	public Input getInput(int i);
	public Input getInput(String id);
	public Output getOutput(int i);
	public Output getOutput(String id);
	
	public List<Input> getInputs();
	public List<Output> getOutputs();
}
