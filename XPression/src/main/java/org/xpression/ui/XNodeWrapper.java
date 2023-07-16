package org.xpression.ui;

import java.util.LinkedList;
import java.util.List;

import org.xpression.XNode;
import org.xpression.io.Input;
import org.xpression.io.Output;
import org.xpression.listener.XNodeListener;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class XNodeWrapper implements Wrapper {
	
	protected XNode node;
	protected XNodeListener listener;
	protected List<XIOWrapper> children = new LinkedList<XIOWrapper>();
	
	protected VBox main;
	protected HBox top;
	protected Rectangle state; 
	protected Label label;
	protected BorderPane io;
	protected VBox in;
	protected VBox out;

	/**
	 * Wraps the given XNode in a UI
	 * @param node
	 */
	public XNodeWrapper(XNode node) {
		this.node = node;
		this.node.setWrapper(this);
		this.node.setListener(listener);
		this.initUI();
	}
	
	public void initUI() {
		main = new VBox(5);
		top = new HBox(5);
		state = new Rectangle(10, 10, Color.DARKGRAY);
		
		label = new Label(node.getNodeName() + ":" + node.getID());
		label.setPadding(new Insets(2, 8, 2, 8));
		top.getChildren().add(state);
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		
		io = new BorderPane();
		io.setStyle("-fx-background-color: #DDDDDD;");
		
		in = new VBox(5);
		in.setAlignment(Pos.CENTER_LEFT);
		in.setPadding(new Insets(8));
		
		out = new VBox(5);
		out.setAlignment(Pos.CENTER_RIGHT);
		out.setPadding(new Insets(8));
		
		for(Input i : node.getInputs()) {
			XIOWrapper wrapper = new XIOWrapper(i);
			children.add(wrapper);
			in.getChildren().add(wrapper.getNode());
		}
		
		for(Output o : node.getOutputs()) {
			XIOWrapper wrapper = new XIOWrapper(o);
			children.add(wrapper);
			out.getChildren().add(wrapper.getNode());
		}
		
		io.setLeft(in);
		io.setRight(out);
		
		main.setStyle("-fx-background-color: #888888; -fx-border-color: black;");
		main.setPadding(new Insets(5,0,0,0));
		main.setAlignment(Pos.CENTER);
		main.setSpacing(5);
		main.getChildren().add(top);
		main.getChildren().add(io);
		try {
			if(node.getXML() != null) {
				if(node.getXML().hasAttr("x") && node.getXML().hasAttr("y")) {
					main.setLayoutX(Double.parseDouble(node.getXML().attr("x")));
					main.setLayoutY(Double.parseDouble(node.getXML().attr("y")));
				}
			}
		}
		catch(NumberFormatException e) {
			
		}
		
		listener = new XNodeListener() {

			@Override
			public void onAddInput(Input i) {
				
			}

			@Override
			public void onRemoveInput(Input i) {
				
			}

			@Override
			public void onAddOutput(Output o) {
				
			}

			@Override
			public void onRemoveOuput(Output o) {
				
			}

			@Override
			public void onEvaluation() {
				
			}

			@Override
			public void onStateChange() {
				Platform.runLater(new Runnable() {
					public void run() {
						switch(node.getXNodeState()) {
						case INITIALIZED:
							state.setFill(Color.GREEN);
							break;
						case DESTROYED:
							break;
						case EVALUATING:
							state.setFill(Color.PURPLE);
							break;
						case WAITING:
							state.setFill(Color.BLUE);
							break;
						default:
							break;
						};
					}
				});
			}

			@Override
			public void onIDChange(String id) {
				
			}
			
		};
		node.setListener(listener);
		node.setWrapper(this);
		listener.onStateChange();
	}
	
	public void updateUI() {
		for(XIOWrapper io : children) {
			io.updateUI();
		}
	}
	
	void collapse() {
		
	}
	
	/**
	 * @return Main box of the Node
	 */
	public VBox getPane() {
		return main;
	}

	@Override
	public void destroyUI() {
		
	}

	@Override
	public double getX() {
		return 0;
	}

	@Override
	public double getY() {
		return 0;
	}
	
	/**
	 * @param n Graphic node describing 
	 * @return IOWrapper containing the node
	 */
	public XIOWrapper reverseLookup(Node n) {
		for(XIOWrapper wrapper : children) {
			if(wrapper.reverseLookup(n)) {
				return wrapper;
			}
		}
		return null;
	}
	
	public String toString() {
		return "XNodeWrapper[" + node + "]";
	}
}
