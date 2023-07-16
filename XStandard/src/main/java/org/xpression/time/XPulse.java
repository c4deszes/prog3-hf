package org.xpression.time;

import org.xpression.XNode;
import org.xpression.annotations.XNodeConstructor;
import org.xpression.annotations.XNodePrefixes;
import org.xpression.io.Input;
import org.xpression.io.Output;

/**
 * This node implements the pulse generator
 * The output will alternate between on and off values with the given delays
 * 		Input[0] = T onValue
 * 		Input[1] = T offValue
 * 		Input[2] = Integer onTime 
 * 		Input[2] = Integer offTime
 * 
 * 		Output[0] = T Output
 * 
 * @author Balazs Eszes
 * @see XNode
 */

@XNodePrefixes(
	AllowSelfRecursion = false,
	AllowImplicitRecursion = false
)
public class XPulse extends XNode {
	
	@XNodeConstructor(fields = { "Type" }, values = { "java.lang.Boolean" })
	public XPulse(Class<?> type) {
		this.addInput(new Input(type, null, "On", this));
		this.addInput(new Input(type, null, "Off", this));
		this.addInput(new Input(Integer.class, null, "OnTime", this));
		this.addInput(new Input(Integer.class, null, "OffTime", this));
		this.addOutput(new Output(type, null, "Output", this));
	}
	
	public void evaluate() {
		if(this.getInput("OnTime").getInput() != null && this.getInput("OffTime").getInput() != null) {
			try {
				this.getOutput("Output").setOutput(this.getInput("Off").getInput());
				Thread.sleep((Integer)this.getInput("OffTime").getInput());
				this.getOutput("Output").setOutput(this.getInput("On").getInput());
				Thread.sleep((Integer)this.getInput("OnTime").getInput());
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.repeat();
		}
	}

	public String getNodeName() {
		return "Pulse";
	}
}
