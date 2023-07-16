package org.xpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xpression.behavior.INode;
import org.xpression.listener.XSheetListener;

public class XSheet extends Thread {
	final static Logger LOGGER = Logger.getLogger("XSheet");
	private static int instances = 1;
	
	private int instance;
	private String sheetName;			//Name of the sheet
	private List<XNode> nodes = new LinkedList<XNode>();	//Nodes in the sheet
	private XSheetListener listener;	//Listener of this object
	private Element xml;				//Source XML
	
	/**
	 * Creates a new empty sheet
	 */
	public XSheet() {
		instance = instances;
		instances++;
		sheetName = "Sheet" + instance;
		xml = new Element("xsheet");
	}

	/**
	 * Loads a sheet from the given file
	 * @param filename Name of the file to load
	 * @throws IOException Thrown if the field doesn't exist
	 */
	public XSheet(String filename) throws IOException {
		this(new File(filename));
	}
	
	/**
	 * Loads a sheet from the given file
	 * @param file File to load
	 * @throws IOException Thrown if the file doesn't exist
	 */
	public XSheet(File file) throws IOException {
		this();
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		load(file);
	}
	
	/**
	 * Loads sheet from file
	 * @param file File to load
	 * @throws IOException Thrown if the file doesn't exist
	 */
	public void load(File file) throws IOException {
		LOGGER.info("Loading sheet: " + file.getCanonicalPath());
		Document document = Jsoup.parse(file, "UTF-8");
		xml = document.getElementsByTag("xsheet").first();
		if(xml.hasAttr("name")) {
			sheetName = xml.attr("name");
		}
		
		for(Element ref : xml.getElementsByTag("xref")) {
			try {
				if(!ref.hasAttr("id") || !ref.hasAttr("reference")) {
					throw new IllegalArgumentException("Illegal arguments");
				}
				ArrayList<Object> params = new ArrayList<Object>();
				
				Main:
				for(Element param : ref.getElementsByTag("param")) {
					try {
						Object obj = Xpression.findClass(param.attr("type")).getConstructor(String.class).newInstance(param.attr("value"));
						params.add(obj);
					}
					catch(NoSuchMethodException | SecurityException e) {
						Class<?> c = Xpression.findClass(param.attr("type"));
						for(Method method : c.getMethods()) {
							if(Modifier.isStatic(method.getModifiers()) && method.getReturnType().equals(c) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(String.class)) {
								Object obj = method.invoke(null, param.attr("value"));
								params.add(obj);
								continue Main;
							}
						}
						throw new NoSuchMethodException("No constructor or method was found for " + c + " with String parameter.");
					}
				}
				
				XNode node = Xpression.newNode(ref.attr("reference"), params.toArray());
				if(node != null) {
					try {
						node.setParent(this);
						node.setID(ref.attr("id"));
						node.setXML(ref);
						
						for(Element input : ref.getElementsByTag("input")) {
							Object obj = node.getInput(input.attr("input")).getType().getConstructor(String.class).newInstance(input.attr("value"));
							node.getInput(input.attr("input")).setInput(obj);
						}
						
						//node.pushChanges();
					} catch(NumberFormatException | NullPointerException e) {
						e.printStackTrace();
					}
					this.addNode(node);
				}
				else {
					System.out.println(ref);
					throw new IllegalArgumentException("Couldn't create XRef.");
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		for(Element xcon : xml.getElementsByTag("xconnection")) {
			try {
				if(!xcon.hasAttr("from") || !xcon.hasAttr("to")) {
					throw new IllegalArgumentException("Invalid xml element.");
				}
				String[] source = xcon.attr("from").split("\\.");
				String[] destination = xcon.attr("to").split("\\.");
				try {
					this.get(source[0]).getInput(source[1]).connect(this.get(destination[0]).getOutput(destination[1]));
				}
				catch(NullPointerException n) {
					this.get(source[0]).getOutput(source[1]).connect(this.get(destination[0]).getInput(destination[1]));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Disposes all the nodes
	 */
	public void dispose() {
		for(INode node : nodes) {
			node.dispose();
		}
	}
	
	/**
	 * Saves the sheet to the source XML
	 */
	public void save() {
		
	}
	
	/**
	 * Returns node with the given ID
	 * @param id
	 * @return Node with the given ID
	 */
	public INode get(String id) {
		return nodes.stream().filter(p -> p.getID().equals(id)).findAny().get();
	}
	
	/**
	 * Returns all nodes within this sheet
	 * @return Nodes in the sheet
	 */
	public List<XNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Adds node to the sheet and initializes it
	 * @param node
	 */
	public void addNode(XNode node) {
		nodes.add(node);
		node.init();
		if(listener != null) {
			listener.onNodeAdd(node);
		}
	}
	
	/**
	 * Removes node from the sheet, also disposes it
	 * @param node
	 */
	public void removeNode(XNode node) {
		nodes.remove(node);
		node.dispose();
		if(listener != null) {
			listener.onNodeRemove(node);
		}
	}
	
	/**
	 * Sets the listener reference
	 * @param listener
	 */
	public void setListener(XSheetListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Returns name of the sheet
	 * @return Name of the XSheet
	 */
	public String getSheetName() {
		return sheetName;
	}
	
	/**
	 * Loads sheet
	 * @param filename Name of the XSheet file
	 * @return loaded XSheet
	 * @throws IOException
	 */
	public static XSheet load(String filename) throws IOException {
		return new XSheet(filename);
	}
}
