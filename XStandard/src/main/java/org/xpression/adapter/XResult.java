package org.xpression.adapter;

import org.xpression.*;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.wrappers.TextWrapper;

/**
 * This node implements an output node
 * 		Input[0] = A
 * 
 * @author Balazs Eszes
 * @see XNode 
 */

@XNodePrefixes(
		AllowSelfRecursion = false,
		AllowImplicitRecursion = false
)
public class XResult extends XNode {
	
	@XNodeConstructor(fields = {"Type"}, values= {"java.lang.Integer"})
	public XResult(Class<?> type) {
		super();
		this.addInput(new Input(type, null, "Input", this));
	}
	
	public void evaluate() {
		
	}
	
	public Class<?> getPreferredWrapper() {
		return TextWrapper.class;
	} 

	public String getNodeName() {
		return "Result";
	}
}
