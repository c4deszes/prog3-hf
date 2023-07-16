package org.xpression.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.xpression.XNode;
import org.xpression.XSheet;
import org.xpression.exception.IOClassException;
import org.xpression.io.IOBase;
import org.xpression.listener.XSheetListener;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class XSheetWrapper implements Wrapper {
	
	private IOBase a;
	private IOBase b;

	private XSheet sheet;
	private XSheetListener listener = new XSheetListener() {
		@Override
		public void onNodeAdd(XNode node) {
			addNode(node);
		}
		@Override
		public void onNodeRemove(XNode node) {
			
		}
		@Override
		public void onDispose() {
			
		}
	};
	private List<XNodeWrapper> children = new LinkedList<XNodeWrapper>();
	
	private Pane pane;
	private Label title;
	
	/**
	 * Wraps the XSheet in 
	 * @param sheet
	 */
	public XSheetWrapper(XSheet sheet) {
		this.sheet = sheet;
		this.sheet.setListener(listener);
		initUI();
	}
	
	public void initUI() {
		pane = new Pane();
		title = new Label(sheet.getSheetName());
		title.setLayoutX(10);
		title.setLayoutY(10);
		
		pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(!(arg0.getTarget() instanceof Line)) {
					for(XNodeWrapper wrapper : children) {
						XIOWrapper n = wrapper.reverseLookup((Node)arg0.getTarget());
						if(n != null) {
							if(n.getIO().isInput()) {
								a = n.getIO();
								System.out.println("selected "+a);
								if(b != null) {
									try {
										a.connect(b);
										System.out.println(a + " connected to " + b);
									} catch (IOClassException e) {
									}
									a = null;
									b = null;
								}
							}
							else if(n.getIO().isOutput()) {
								b = n.getIO();
								System.out.println("selected "+b);
								if(a != null) {
									try {
										b.connect(a);
										System.out.println(a + " connected to " + b);
									} catch (IOClassException e) {
									}
									a = null;
									b = null;
								}
							}
							break;
						}
					}
				}
			}
		});		
		pane.getChildren().add(title);
		
		for(XNode node : sheet.getNodes()) {
			this.addNode(node);
		}
	}
	
	public void addNode(XNode node) {
		try {
			XNodeWrapper wrapper = (XNodeWrapper) node.getPreferredWrapper().getConstructor(XNode.class).newInstance(node);
			children.add(wrapper);
			
			wrapper.getPane().setOnMouseDragged(event -> {
				if(!(event.getTarget() instanceof Line)) {
					wrapper.getPane().setTranslateX(event.getX() + wrapper.getPane().getTranslateX());
					wrapper.getPane().setTranslateY(event.getY() + wrapper.getPane().getTranslateY());
					updateUI();
				}
			});
			
			LOGGER.info("Added wrapper: " + wrapper);
			pane.getChildren().add(wrapper.getPane());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public void removeNode(XNode node) {
		
	}

	@Override
	public void updateUI() {
		for(XNodeWrapper node : children) {
			node.updateUI();
		}
	}

	@Override
	public void destroyUI() {
		sheet.dispose();
	}
	
	public Pane getPane() {
		return pane;
	}
	
	public XSheet getSheet() {
		return sheet;
	}

	@Override
	public double getX() {
		return 0;
	}

	@Override
	public double getY() {
		return 0;
	}
	
	public String toString() {
		return "XSheetWrapper[name="+sheet.getSheetName() + " ,count=" + sheet.getNodes().size() + "]";
	}

}
