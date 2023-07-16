package org.xpression.ui;

import org.xpression.io.IOBase;
import org.xpression.listener.XIOListener;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class XIOWrapper implements Wrapper {
	
	protected IOBase io;
	
	private boolean showType = false;
	private boolean showValue = false;

	protected HBox main;
	protected Label label;
	protected Circle icon;
	
	private Line connection;
	
	Runnable run = new Runnable() {
		public void run() {
			updateUI();
		}
	};
	XIOListener listener = new XIOListener() {
		@Override
		public void onConnected(IOBase a, IOBase b) {
			Platform.runLater(run);
		}

		@Override
		public void onDisconnected(IOBase a, IOBase b) {
			Platform.runLater(run);
		}

		@Override
		public void onChange(Object prev, Object actual) {
			Platform.runLater(run);
		}
	};
	
	public XIOWrapper(IOBase io) {
		this.io = io;
		io.setWrapper(this);
		io.setListener(listener);
		this.initUI();
	}
	
	public void initUI() {
		main = new HBox();
		label = new Label();
		label.setText(this.getText());
		label.setPadding(new Insets(0, 10, 0, 10));
		if(io.isInput()) {
			icon = new Circle(0, 0, 5, Color.RED);
			main.getChildren().add(this.icon);
			main.getChildren().add(this.label);
			main.setAlignment(Pos.CENTER_LEFT);
			if(io.connected()) {
				connection = new Line();
				connection.setManaged(false);
				connection.setStartX(io.getWrapper().getX());
				connection.setStartY(io.getWrapper().getY());
				connection.setEndX(io.getConnection().getWrapper().getX());
				connection.setEndY(io.getConnection().getWrapper().getY());
				main.getChildren().add(connection);
			}
		}
		else {
			icon = new Circle(0, 0, 5, Color.BLUE);
			main.getChildren().add(this.label);
			main.getChildren().add(this.icon);
			main.setAlignment(Pos.CENTER_RIGHT);
		}
		this.onInitUI();
	}
	
	public void updateUI() {
		label.setText(this.getText());
		if(io.isInput()) {
			if(io.connected()) {
				if(connection == null) {
					System.out.println("generated line");
					connection = new Line();
					connection.setManaged(false);
					main.getChildren().add(connection);
				}
				connection.setStartX(icon.getLayoutX());
				connection.setStartY(icon.getLayoutY());
				connection.setEndX(io.getConnection().getWrapper().getX() - icon.getLocalToSceneTransform().getTx());
				connection.setEndY(io.getConnection().getWrapper().getY() - icon.getLocalToSceneTransform().getTy());
			}
		}
		else if(io.isOutput()) {
			for(IOBase i : io.getConnections()) {
				i.getWrapper().updateUI();
			}
		}
		this.onUpdateUI();
	}
	
	public void showType() {
		showType = true;
	}
	
	public void hideType() {
		showType = false;
	}
	
	public void showText() {
		this.label.setVisible(true);
		this.label.setManaged(true);
	}
	
	public void hideText() {
		this.label.setVisible(false);
		this.label.setManaged(false);
	}
	
	public String getText() {
		return io.getID() + (showValue ? "["+io.getValue().toString()+"]":"") + (showType ? "<"+io.getTypeName()+">" : "");
	}
	
	public Shape getIcon() {
		return icon;
	}
	
	public HBox getNode() {
		return main;
	}
	
	public IOBase getIO() {
		return io;
	}

	@Override
	public void destroyUI() {
		this.onDestroyUI();
	}

	@Override
	public double getX() {
		return icon.getLocalToSceneTransform().getTx() + icon.getRadius();
	}

	@Override
	public double getY() {
		return icon.getLocalToSceneTransform().getTy() + icon.getLayoutY();
	}

	/**
	 * Reverse looks up 
	 * @param n
	 * @return
	 */
	public boolean reverseLookup(Node n) {
		if(n.equals(icon) || n.equals(label) || n.equals(main)) {
			return true;
		}
		return false;
	}
}
