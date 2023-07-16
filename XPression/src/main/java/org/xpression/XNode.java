package org.xpression;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.nodes.Element;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.behavior.INode;
import org.xpression.io.Input;
import org.xpression.io.Output;
import org.xpression.listener.XNodeListener;
import org.xpression.ui.Wrapper;
import org.xpression.ui.XNodeWrapper;

@XNodePrefixes()
public class XNode extends Thread implements INode {
	protected static final Logger LOGGER = Logger.getLogger("XNode");
	private static int instances = 1;
	
	private final static UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			System.out.println(arg0.getName());
			arg1.printStackTrace();
		}
	};
	
	protected XSheet parent;					
	private Element xml;						//reference to xml element
	private String id = "";						//identifies the xnode within the sheet
	private int instance = 0;								
	
	private volatile XNodeState state;
	private volatile boolean running = false;
	private volatile boolean continuous = false;
	
	//MVC
	private Wrapper wrapper;
	private XNodeListener listener;
	
	private List<Input> inputs = new LinkedList<Input>();
	private List<Output> outputs = new LinkedList<Output>();
	
	/**
	 * Constructs a new XNode
	 */
	public XNode() {
		this.instance = XNode.instances;
		XNode.instances++;
		this.xml = new Element("xref");
		this.setID(this.getNodeName() + instance);
		//this.xml.attr("reference", Xpression.findNode(this.getClass()));
		//TODO: Parse
		this.setXNodeState(XNodeState.UNINITIALIZED);
		this.setUncaughtExceptionHandler(handler);
	}
	
	/**
	 * @return Preferred wrapper of this XNode type
	 */
	public Class<?> getPreferredWrapper() {
		try {
			return Xpression.findClass("org.xpression.wrappers.TextWrapper");
		} catch (ClassNotFoundException e) {
			return XNodeWrapper.class;
		}
	}
	
	/**
	 * @return The name of this XNode's type
	 */
	public String getNodeName() {
		return "XNode";
	}
	
	/**
	 * @return ID of this XNode
	 */
	public final String getID() {
		return id;
	}
	
	/** Sets the ID of this XNode
	 * @param id New ID of this XNode
	 */
	public final void setID(String id) {
		this.id = id;
		this.xml.attr("id", id);
		this.setName(this.getNodeName() + " - " + this.id);
	}
	
	public String toString() {
		return getNodeName() + "[id=" + getID() + "]";
	}
	
	/**
	 * Executes the evaluation function of this XNode
	 */
	@Override
	public final void run() {
		running = true;
		this.evaluate();
		while(running) {
			try {
				if(!this.continuous) {
					this.setXNodeState(XNodeState.WAITING);
					synchronized(this) {
						this.wait();
					}
				}
				continuous = false;
				boolean skip = false;
				for(Input i : inputs) {
					if(i.isLocked()) {
						skip = true;
						System.out.println(this + "'s " + i + " is locked.");
						break;
					}
				}
				if(!skip) {
					this.setXNodeState(XNodeState.EVALUATING);
					this.evaluate();
					if(listener != null) {
						listener.onEvaluation();
					}
				}
				this.finalizeOutputs();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		this.setXNodeState(XNodeState.DESTROYED);
	}
	
	/**
	 * Changes the state of the node
	 * @param s New state of the node
	 */
	private void setXNodeState(XNodeState s) {
		state = s;
		if(listener != null) {
			listener.onStateChange();
		}
	}
	
	/**
	 * Returns state of the node
	 * @return State of the node
	 */
	public XNodeState getXNodeState() {
		return state;
	}
	
	/**
	 * Repeats the evaluation, used for XNodes which need continuous evaluation
	 */
	public void repeat() {
		this.continuous = true;
	}
	
	/**
	 * Disposes the XNode, stops the thread
	 */
	@Override
	public final synchronized void dispose() {
		running = false;
		this.notifyAll();
		
		for(Input i : inputs) {
			i.dispose();
		}
		for(Output o : outputs) {
			o.dispose();
		}
	}
	
	/**
	 * Sets up the XNode
	 */
	@Override
	public void init() {
		this.setDaemon(false);
		this.setPriority(NORM_PRIORITY);
		this.start();
		this.setXNodeState(XNodeState.INITIALIZED);
	}
	
	/**
	 * Evaluation function of the XNode, must be overridden
	 */
	@Override
	public void evaluate() {
		
	}
	
	/**
	 * Finalizes the output stage if it has not been done
	 */
	public final void finalizeOutputs() {
		for(Output o : outputs) {
			o.push();
		}
	}
	
	/**
	 * @param listener The new listener through which this XNode communicates
	 */
	public final void setListener(XNodeListener listener) {
		this.listener = listener;
	}
	
	/**
	 * @param wrapper The new GUI Wrapper of this XNode
	 */
	public final void setWrapper(Wrapper wrapper) {
		this.wrapper = wrapper;
	}
	
	/**
	 * @return The wrapper of this XNode
	 */
	public final Wrapper getWrapper() {
		return wrapper;
	}
	
	/**
	 * @param sheet The new sheet in which this XNode is
	 */
	public final void setParent(XSheet sheet) {
		this.parent = sheet;
	}
	
	/**
	 * @return The sheet in which this XNode is
	 */
	public final XSheet getParent() {
		return parent;
	}
	
	/**
	 * @param xml The new XML element describing this XNode
	 */
	public final void setXML(Element xml) {
		this.xml = xml;
	}
	
	/**
	 * @return The XML element describing this XNode
	 */
	public final Element getXML() {
		return xml;
	}
	
	/**
	 * Returns an input of the node with given ID
	 */
	public final Input getInput(String id) {
		for(Input input : inputs) {
			if(input.getID().equals(id)) {
				return input;
			}
		}
		return null;
	}
	
	/**
	 * Returns the nth input of the node
	 */
	public final Input getInput(int index) {
		return inputs.get(index);
	}
	
	/**
	 * Returns an output of the node with given ID
	 */
	public final Output getOutput(String id) {
		for(Output output : outputs) {
			if(output.getID().equals(id)) {
				return output;
			}
		}
		return null;
	}
	
	/**
	 * Returns the nth output of the node
	 */
	public final Output getOutput(int index) {
		return outputs.get(index);
	}
	
	/**
	 * Returns all inputs of the node
	 */
	public final List<Input> getInputs() {
		return this.inputs;
	}
	
	/**
	 * Returns all outputs of the node
	 */
	public final List<Output> getOutputs() {
		return this.outputs;
	}
	
	/**
	 * Adds an input to the node
	 */
	public final void addInput(Input i) {
		inputs.add(i);
		if(listener != null) {
			listener.onAddInput(i);
		}
	}
	
	/**
	 * Removes an input from the node
	 */
	public final void removeInput(int i) {
		Input input = inputs.remove(i);
		if(listener != null) {
			listener.onRemoveInput(input);
		}
	}
	
	/**
	 * Adds an output to the node
	 */
	public final void addOutput(Output o) {
		outputs.add(o);
		if(listener != null) {
			listener.onAddOutput(o);
		}
	}
	
	/**
	 * Removes an output from the node
	 */
	public final void removeOutput(int i) {
		Output output = outputs.remove(i);
		if(listener != null) {
			listener.onRemoveOuput(output);
		}
	}
}
