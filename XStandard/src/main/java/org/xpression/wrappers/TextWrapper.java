package org.xpression.wrappers;

import org.xpression.XNode;
import org.xpression.io.IOBase;
import org.xpression.io.Input;
import org.xpression.io.Output;
import org.xpression.listener.XNodeListener;
import org.xpression.ui.XIOWrapper;
import org.xpression.ui.XNodeWrapper;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TextWrapper extends XNodeWrapper {

	public TextWrapper(XNode node) {
		super(node);
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
			XIOTextWrapper wrapper = new XIOTextWrapper(i);
			children.add(wrapper);
			in.getChildren().add(wrapper.getNode());
		}
		
		for(Output o : node.getOutputs()) {
			XIOTextWrapper wrapper = new XIOTextWrapper(o);
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRemoveInput(Input i) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAddOutput(Output o) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRemoveOuput(Output o) {
				// TODO Auto-generated method stub
				
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
				// TODO Auto-generated method stub
				
			}
			
		};
		node.setListener(listener);
		node.setWrapper(this);
		listener.onStateChange();
	}
	
	private static class XIOTextWrapper extends XIOWrapper {
		
		public XIOTextWrapper(IOBase io) {
			super(io);
		}
		
		public String getText() {
			return io.getValue() != null ? io.getValue().toString() : "null";
		}
		
	}
}
