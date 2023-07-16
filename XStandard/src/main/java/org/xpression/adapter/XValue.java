package org.xpression.adapter;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Output;
import org.xpression.wrappers.TextWrapper;

/**
 * This node defines a value as object
 * 		Output[0] = T Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */
@XNodePrefixes(
		AllowSelfRecursion = true,
		AllowImplicitRecursion = true
)
public class XValue extends XNode {
	
	@XNodeConstructor(fields = { "Value" }, values = { "java.lang.Integer:0" })
	public XValue(Object t) {
		this.addOutput(new Output(t, "Output", this));
	}
	
	///Only use this from code/xml
	public XValue(Class<?> type) {
		this.addOutput(new Output(type, null, "Output", this));
	}
	
	public void evaluate() {
		
	}
	
	public Class<?> getPreferredWrapper() {
		return TextWrapper.class;
	} 

	public String getNodeName() {
		return "Value";
	}
}
