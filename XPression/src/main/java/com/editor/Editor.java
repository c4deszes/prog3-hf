package com.editor;

import java.io.File;

import org.xpression.Xpression;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Editor extends Application {

	EditorController controller;
	Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
		loader.load();
		controller = loader.getController();
		scene = new Scene(loader.getRoot(), 1200, 800);
		
		String filename = this.getParameters().getNamed().get("Xfile");
		if(filename != null) {
			controller.xsheetOpen(new File(filename));
		}
		stage.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				controller.xsheetUpdate();
			}
		});
		stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		stage.setTitle("XPression Editor");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Called on exit 
	 */
	public void stop() {
		controller.getWrapper().destroyUI();
	}
	
	public static void main(String[] args) {
		try {
			Xpression.initialize();
			Xpression.loadLibrary("libraries/stdlib.xlib");
		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(args);
	}
}
