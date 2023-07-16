package org.xpression.io;

import java.util.LinkedList;
import java.util.List;

import org.xpression.XNode;
import org.xpression.exception.IOClassException;
import org.xpression.listener.XIOListener;
import org.xpression.ui.Wrapper;

public abstract class IOBase {
	private XNode parent;
	
	private boolean isInput;
	private volatile boolean locked = false;
	private boolean changed = false;
	protected Class<? extends Object> type;		//Needed so we can force the type even if the value is null
	protected String id;						//ID of this IO
	
	protected volatile Object value;			//Value of this IO
	protected volatile Object previousValue;
	
	protected IOBase connection;		//reference to the output
	protected List<IOBase> connections = new LinkedList<IOBase>();		//references to the inputs
	
	private Wrapper wrapper;			//GUI Wrapper of the IO
	protected XIOListener listener;		//Listener of the IO
	
	/**
	 * Constructs a new IO
	 * @param type Type of the value held
	 * @param value Value of the IO
	 * @param isInput 
	 * @param name The ID of the IO
	 * @param parent The parent node of the IO
	 */
	public IOBase(Class<?> type, Object value, boolean isInput, String name, XNode parent) {
		this.isInput = isInput;
		this.value = value;
		this.id = name;
		this.parent = parent;
		try {
			this.type = type;
		}
		catch(NullPointerException e) {
			this.type = Object.class;
		}
	}
	
	/**
	 * @return True if the IO is an input
	 */
	public boolean isInput() {
		return isInput;
	}
	
	/**
	 * @return True if the IO is an output
	 */
	public boolean isOutput() {
		return !isInput;
	}
	
	/**
	 * @return The output this input is connected to
	 */
	public IOBase getConnection() {
		return connection;
	}
	
	/**
	 * @return The inputs this output is connected to
	 */
	public List<IOBase> getConnections() {
		return connections;
	}
	
	/**
	 * Disposes this IO
	 */
	public final void dispose() {
		this.disconnect();
	}
	/**
	 * Connects IO to another IO
	 * @param io
	 * @throws IOClassException If the IOs are incompatible (Input-Input, Output-Output) or if the types cannot be casted
	 */
	public abstract void connect(IOBase io) throws IOClassException;
	
	public abstract void disconnect();
	public abstract void disconnect(IOBase io);
	
	public boolean connected() {
		if(isInput()) {
			if(connection != null) {
				return true;
			}
			return false;
		}
		if(connections.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Updates the value of this IO
	 * @param t New value to be stored
	 */
	public final synchronized void setValue(Object t) {
		if(t != value) {
			previousValue = value;
			value = t;
			changed = true;
			if(listener != null) {
				listener.onChange(previousValue, value);
			}
			this.push();
		}
	}
	
	/**
	 * @return The value of this IO
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Pushes changes to the connected Outputs, or notifies the parent XNode
	 */
	public synchronized final void push() {
		if(isInput()) {
			//unlock();
			synchronized(this.parent) {
				this.parent.notifyAll();
				//System.out.println(this + " notified " + parent);
			}
		}
		else if(isOutput()) {
			for(IOBase input : connections) {
				//input.lock();
				input.setValue(this.getValue());
			}
		}
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void lock() {
		locked = true;
	}
	
	public void unlock() {
		locked = false;
	}
	
	/**
	 * @return True if the value has changed since the last observation
	 */
	public boolean hasChanged() {
		if(changed) {
			changed = false;
			return true;
		}
		return false;
	}
	
	/**
	 * @param listener The new listener of the this IO
	 */
	public void setListener(XIOListener listener) {
		this.listener = listener;
	}
	
	/**
	 * @param wrapper The new GUI Wrapper of the this IO
	 */
	public void setWrapper(Wrapper wrapper) {
		this.wrapper = wrapper;
	}
	
	/**
	 * @return The GUI Wrapper of this IO
	 */
	public Wrapper getWrapper() {
		return wrapper;
	}
	
	/**
	 * 
	 * @return Type of the object it holds
	 */
	public Class<? extends Object> getType() {
		return type;
	}
	
	/**
	 * 
	 * @return String representating the type
	 */
	public String getTypeName() {
		return type.getSimpleName();
	}
	
	/**
	 * @return String used to identify the Input/Output
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Returns String representation of this IO
	 */
	public String toString() {
		return "IOBase[id="+this.getID() + ", type=" + type.getSimpleName() + ", isInput="+isInput()+"]";
	}
}
