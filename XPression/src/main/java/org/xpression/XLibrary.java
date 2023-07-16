package org.xpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Notes:
 * 	Fully Qualified Name (FQN) : Library:Node
 * 
 *  Partially Qualified Name (PQN) : Node
 *  	In this case the Node will reference the first occurance in the library hierarchy
 *  	It is not recommended to use it as there might be duplicates.
 *  
 * @author Balazs
 */
public final class XLibrary {
	private static final Logger LOGGER = Logger.getLogger("XLibrary");
	
	private URLClassLoader loader;		//Loads Jar file
	private String name;				//Name of library
	private String jarName;				//Name of the Jar file
	private Map<String, Class<?>> classes = new HashMap<String, Class<?>>();	//Registered XNodes

	/**
	 * Constructs a new Library from a file
	 * @param filename 
	 * @throws IOException
	 */
	public XLibrary(String filename) throws IOException {
		this(new File(filename));
	}
	
	/**
	 * Constructs a new Library from a file
	 * @param file
	 * @throws IOException
	 */
	public XLibrary(File file) throws IOException {
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		loadXML(file);
	}
	
	/**
	 * Returns the XNode class for the given name
	 * 
	 * If the name contains a '.' then it is referencing it by classname (e.g: org.xpression.logic.XBoole)
	 * Otherwise it's the registered name of the XNode (e.g: Boole)
	 * 
	 * @param name Name of the XNode
	 * @return XNode class
	 */
	public Class<?> getNode(String name) {
		//TODO: detect reference by package
		if(name.contains(".")) {
			for(Entry<String, Class<?>> entry : classes.entrySet()) {
				if(entry.getValue().getName().equals(name)) {
					return entry.getValue();
				}
			}
			return null;
		}
		return classes.get(name);
	}
	
	/**
	 * Reverse looks up the node (Find the registered name for the given class)
	 * @param c Class of the XNode
	 * @return Name of the node
	 */
	public String getNode(Class<?> c) {
		return classes.entrySet().stream().filter(entry -> entry.getValue().equals(c)).findFirst().get().getKey();
	}
	
	/**
	 * Returns a class from the loaded jar file
	 * @param name Fully qualified name of the class
	 * @return Class from this library's jar file
	 * @throws ClassNotFoundException
	 */
	public Class<?> forName(String name) throws ClassNotFoundException {
		return loader.loadClass(name);
	}
	
	/**
	 * Loads a library file
	 * @param file XLibrary file to load, by specification it should be an xml file with .xlib extension
	 * @throws IOException
	 */
	private void loadXML(File file) throws IOException {
		LOGGER.info("Loading library: " + file.getCanonicalPath());
		Document document = Jsoup.parse(file, "UTF-8");
		Element root = document.getElementsByTag("xlib").first();
		if(root.hasAttr("name")) {
			this.name = root.attr("name");
		}
		
		//Loading referenced JAR file
		if(root.hasAttr("jar")) {
			File jar = new File(file.getParentFile(), root.attr("jar"));
			if(!jar.exists()) {
				LOGGER.severe("Failed to import jar: " + root.attr("jar"));
				throw new FileNotFoundException();
			}			
			jarName = jar.getName();
			loader = new URLClassLoader(new URL[] {jar.toURI().toURL()});
			LOGGER.info("Imported jar: " + jar.getCanonicalPath());
		}
		int nodes = 0;
		//Registering referenced classes
		for(Element e : document.getElementsByTag("xnode")) {
			if(!e.hasAttr("name")) {
				LOGGER.warning("Invalid XNode definition: " + e.html());
				continue;
			}
			if(e.hasAttr("class")) {
				try {
					this.registerClass(e.attr("name"), e.attr("class"));
					nodes++;
				} catch (ClassNotFoundException e1) {
					LOGGER.severe("Class " + e.attr("class") + " not found: " + e);
				} catch (NullPointerException e2) {
					LOGGER.severe("XLibrary doesn't have a linked JAR file!");
				}
				continue;
			}
			else {
				//TODO: create new instance of custom xnode
			}
		}
		
		for(Element e : document.getElementsByTag("xclass")) {
			if(e.hasAttr("class")) {
				try {
					Xpression.registerClass(this.forName(e.attr("class")));
				} catch (ClassNotFoundException e1) {
					LOGGER.severe("Class " + e.attr("class") + " not found: " + e);
				}
			}
		}
		
		LOGGER.info("Finished loading library. Added " + nodes + " XNodes to the library.");
	}
	
	/**
	 * Registers an XNode into the current library
	 * @param name Name of the XNode
	 * @param classname Classname of the XNode
	 * @throws ClassNotFoundException If class doesn't exist in the current jar file
	 */
	private void registerClass(String name, String classname) throws ClassNotFoundException {
		classes.put(name, loader.loadClass(classname));
		LOGGER.info("Registered " + classname + " as " + name);
	}
	
	/**
	 * Name of the library
	 * @return Library name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Registered XNodes
	 * @return Set of XNode names
	 */
	public Set<String> getClasses() {
		return classes.keySet();
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + "[name=" + name + ", jar=" + jarName + ", count=" + classes.size() + "]";
	}
}
