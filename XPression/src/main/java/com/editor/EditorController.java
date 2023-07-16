package com.editor;

import java.io.File;
import java.util.Optional;

import org.xpression.XLibrary;
import org.xpression.XNode;
import org.xpression.XSheet;
import org.xpression.Xpression;
import org.xpression.ui.XSheetWrapper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class EditorController {
	
	private XSheet sheet;
	private XSheetWrapper wrapper;
	
	@FXML private BorderPane base;
	@FXML private Menu addComponent;
	@FXML private Menu EditMenu;
	
	EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			MenuItem src = (MenuItem)arg0.getSource();
			
			NodeCreationDialog dialog = new NodeCreationDialog(src.getParentMenu().getText()+":"+src.getText());
			Optional<XNode> result = dialog.showAndWait();
			result.ifPresent((XNode out) -> {
				sheet.addNode(out);
			});			
		}
	};
	
	public void initialize() {
		Image img = new Image(this.getClass().getResource("grid.png").toExternalForm(),16,16,false,true);
		BackgroundImage myBI= new BackgroundImage(img,
		        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		base.setBackground(new Background(myBI));
		
		//creates adding menu for every component
		for(XLibrary lib : Xpression.getLibraries()) {
			Menu lib_menu = new Menu(lib.getName());
			for(String node : lib.getClasses()) {
				MenuItem addNode = new MenuItem(node);
				addNode.setOnAction(handler);
				lib_menu.getItems().add(addNode);
			}
			addComponent.getItems().add(lib_menu);
		}
	}
	
	@FXML
	public void xsheetOpen(ActionEvent actionEvent) {
	   FileChooser fileChooser = new FileChooser();
	   fileChooser.setTitle("Open sheet file");
	   fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"),"sheets"));
	   fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XSheet", "*.xsh"));
	   
	   File file = fileChooser.showOpenDialog(base.getScene().getWindow());
	   if(file != null) {
		   xsheetOpen(file);
	   }
	}
	
	public void xsheetOpen(File file) {		
		try {
			if(wrapper != null) {
				sheet.dispose();
			}
			sheet = new XSheet(file);
		   	wrapper = new XSheetWrapper(sheet);
		   	base.setCenter(wrapper.getPane());
		   	wrapper.updateUI();
		   	//stage.setTitle("Xpression Editor - " + wrapper.getSheet().getSheetName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void xsheetUpdate() {
		if(wrapper != null) {
			wrapper.updateUI();
		}
	}
	
	public XSheetWrapper getWrapper() {
		return wrapper;
	}
	
	@FXML
	public void xsheetNew(ActionEvent actionEvent) {
		System.out.println("New");
	}

}
