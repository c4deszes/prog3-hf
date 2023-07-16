package com.editor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.xpression.XNode;
import org.xpression.Xpression;
import org.xpression.annotations.XNodeConstructor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NodeCreationDialog extends Dialog<XNode> {

	private static Image icon = new Image(NodeCreationDialog.class.getResourceAsStream("add.png"));
	
	private Constructor<?> constructor;
	private XNodeConstructor annotation;
	
	/**
	 * Creates a new dialog that can be used to construct a library component
	 * @param component The library component to create
	 */
	@SuppressWarnings("unchecked")
	public NodeCreationDialog(String component) {
		this.setTitle("Add node");
		this.setHeaderText(component);
		
		DialogPane pane = this.getDialogPane();
		pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Stage)pane.getScene().getWindow()).getIcons().add(icon);
		
		Class<?> node = Xpression.findNode(component);
		VBox dialog = new VBox(5);
		
		try {
			for(Constructor<?> c : node.getConstructors()) {
				if(c.isAnnotationPresent(XNodeConstructor.class)) {
					annotation = c.getAnnotation(XNodeConstructor.class);
					constructor = c;
				}
			}
			ObservableList<Class<?>> classes = FXCollections.observableArrayList(Xpression.getClasses());
			
			for(int i=0;i<constructor.getParameterCount();i++) {
				//TODO: add array support
				//TODO: add object support (class:value)
				if(constructor.getParameterTypes()[i].equals(Boolean.class)) {
					CheckBox check = new CheckBox(annotation.fields()[i]);
					dialog.getChildren().add(check);
				}
				else if(constructor.getParameterTypes()[i].isEnum()) {
					ObservableList<Object> list = FXCollections.observableArrayList(constructor.getParameterTypes()[i].getEnumConstants());
					ComboBox<Object> combo = new ComboBox<Object>(list);
					try {
						Object obj = constructor.getParameterTypes()[i].getMethod("valueOf", String.class).invoke(null, annotation.values()[i]);
						combo.setValue(obj);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					dialog.getChildren().add(combo);
				}
				else if(constructor.getParameterTypes()[i].equals(Object.class)) {
					ComboBox<Class<?>> combo = new ComboBox<Class<?>>(classes);
					TextField field = new TextField();
					String[] data = annotation.values()[i].split(":");
					try {
						combo.setValue(Xpression.findClass(data[0]));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					field.setText(data[1]);
					field.setPromptText(annotation.fields()[i]);
					HBox container = new HBox(2);
					container.getChildren().addAll(combo, field);
					dialog.getChildren().add(container);
				}				
				else if(constructor.getParameterTypes()[i].equals(Class.class)) {
					ComboBox<Class<?>> combo = new ComboBox<Class<?>>(classes);
					try {
						combo.setValue(Xpression.findClass(annotation.values()[i]));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dialog.getChildren().add(combo);
				}
				else {
					TextField field = new TextField();
					field.setText(annotation.values()[i]);
					field.setPromptText(annotation.fields()[i]);
					dialog.getChildren().add(field);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		pane.setContent(dialog);
		
		this.setResultConverter((ButtonType button) -> {
	        if (button == ButtonType.OK) {
	        	Object[] params = new Object[constructor.getParameterCount()];
 	        	for(int i=0;i<constructor.getParameterCount();i++) {
		            Node n = dialog.getChildren().get(i);
		            if(constructor.getParameterTypes()[i].equals(Boolean.class)) {
		            	CheckBox ch = (CheckBox) n;
		            	params[i] = ch.isSelected();
		            }
		            else if(constructor.getParameterTypes()[i].isEnum()) {
		            	ComboBox<Object> obj = (ComboBox<Object>) n;
		            	params[i] = obj.getValue();
		            }
		            else if(constructor.getParameterTypes()[i].equals(Object.class)) {
		            	ComboBox<Class<?>> obj = (ComboBox<Class<?>>) ((HBox)n).getChildren().get(0);
		            	TextField txt = (TextField) ((HBox)n).getChildren().get(1);
		            	try {
							params[i] = obj.getValue().getConstructor(String.class).newInstance(txt.getText());
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
		            }
		            else if(constructor.getParameterTypes()[i].equals(Class.class)) {
		            	ComboBox<Class<?>> obj = (ComboBox<Class<?>>) n;
		            	params[i] = obj.getValue();
		            }
		            else {
		            	TextField txt = (TextField) n;
		            	try {
							params[i] = constructor.getParameterTypes()[i].getConstructor(String.class).newInstance(txt.getText());
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
		            }
	        	}
 	        	return Xpression.newNode(component, params);
	        }
	        return null;
		});
	}
}
