package org.xpression.logic;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.exception.IOClassException;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements a comparator, the output is a boolean indicating if the specified relation between the
 * two inputs is true
 * 		Input[0] = T A
 * 		Input[1] = T B
 * 
 * 		Output[0] = boolean Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
		AllowSelfRecursion = false,
		AllowImplicitRecursion = false
)
public class XCompare extends XNode {

	Relation relation;
	Class<?> type;
	
	@XNodeConstructor(fields = { "Type", "Relation" }, values = { "java.lang.Integer", "EQUAL" })
	public XCompare(Class<?> type, Relation relation) throws IOClassException {
		super();
		if(!Comparable.class.isAssignableFrom(type)) {
			throw new IOClassException(type + " doesn't implement Comparable<" + type.getSimpleName() + "> !");
		}
		this.type = type;
		this.relation = relation;
		this.addInput(new Input(type, null, "A", this));
		this.addInput(new Input(type, null, "B", this));
		this.addOutput(new Output(Boolean.FALSE, "Output", this));
		this.setName("XCompare<" + this.getInput(0).getTypeName() + ">");
	}
	
	public void evaluate() {
		//TODO: write
		//if(((Comparable<?>)this.getInput("A").getValue()).compareTo(this.getInput("B").getValue())) {
	}
	
	public String getNodeName() {
		return "Compare";
	}
}
