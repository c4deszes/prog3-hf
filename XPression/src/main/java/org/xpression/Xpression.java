package org.xpression;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public final class Xpression {
	private final static Logger LOGGER = Logger.getLogger("Xpression");
	
	private final static List<XLibrary> libraries = new LinkedList<XLibrary>();		//Loaded libraries
	private final static List<Class<?>> classes = new LinkedList<Class<?>>();		//Registered classes
	
	/**
	 * Loads a library
	 * @param filename Library file name ( .../library.xml )
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void loadLibrary(String filename) throws IOException {
		XLibrary lib = new XLibrary(filename);
		libraries.add(lib);
		LOGGER.info("Loaded library: " + lib);
	}
	
	/**
	 * Loads a library
	 * @param file Library file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void loadLibrary(File file) throws IOException {
		XLibrary lib = new XLibrary(file);
		libraries.add(lib);
		LOGGER.info("Loaded library: " + lib);
	}
	
	/**
	 * Finds the library with the given name
	 * @param name Name of the library
	 * @return Reference to Library
	 */
	public static XLibrary getLibrary(String name) {
		for(XLibrary lib : libraries) {
			if(lib.getName().equals(name)) {
				return lib;
			}
		}
		return null;
	}
	
	/**
	 * Returns all libraries
	 * @return List of all the libraries
	 */
	public static List<XLibrary> getLibraries() {
		return libraries;
	}
	
	/**
	 * Finds a class based on it's name in the framework (including loaded libraries)
	 * @param name Name of the type
	 * @return Class reference of the type
	 * @throws ClassNotFoundException
	 */
	public static Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			Class<?> c = Class.forName(name);
			return c;
		} catch (ClassNotFoundException e) {}
		for(XLibrary lib : libraries) {
			try {
				Class<?> c = lib.forName(name);
				return c;
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
		throw new ClassNotFoundException();
	}
	
	/**
	 * Returns all registered class
	 * @return List of registered classes
	 */
	public static List<Class<?>> getClasses() {
		return classes;
	}
	
	/**
	 * Registers a class in the framework
	 * @see XNodeWrapper, NodeCreationDialog
	 * @param c Class to register in the framework
	 */
	public static void registerClass(Class<?> c) {
		classes.add(c);
		LOGGER.info("Registered " + c.getName() + " in Xpression");
	}
	
	/**
	 * @param name Name of the type (e.g: stdlib:value) should be FQN
	 * @return Class reference of XNode
	 */
	public static Class<?> findNode(String name) {
		String[] input = name.split(":");
		for(XLibrary lib : libraries) {
			//FQN
			if(input.length == 2 && input[0].equals(lib.getName())) {
				Class<?> a = lib.getNode(input[1]);
				return a;
			}
			//PQN
			else {
				Class<?> a = lib.getNode(name);
				if(a != null) {
					return a;
				}
			}
		}
		return null;
	}
	
	/**
	 * Does a reverse node lookup
	 * @param c Class to lookup
	 * @return FQN Name of the XNode
	 */
	public static String findNode(Class<?> c) {
		for(XLibrary lib : libraries) {
			String node = lib.getNode(c);
			if(node != null) {
				return lib.getName() + ":" + node;
			}
		}
		return "XNode";
	}
	
	/**
	 * Creates a new instance of the given node. This method will look for constructors with the given
	 * parameters. If it can't find one it will try to deduce the typelist for an existing constructor
	 * based on inheritance.
	 * @param name Name of the type (e.g: stdlib:Value)
	 * @param objects Parameter list
	 * @return new XNode, null if it couldn't create the node
	 */
	public static XNode newNode(String name, Object... objects) {
		Class<?> node = findNode(name);
		if(node == null) {
			return null;
		}
		Class<?>[] typelist = new Class<?>[objects.length];
		for(int i = 0;i<objects.length;i++) {
			typelist[i] = objects[i].getClass();
		}
		try {
			return (XNode)node.getConstructor(typelist).newInstance(objects);
		} catch(Exception e) {
			for(Constructor<?> constructor : node.getConstructors()) {
				if(typelist.length == constructor.getParameterCount()) {
					for(int i = 0;i<constructor.getParameterCount();i++) {
						if(constructor.getParameterTypes()[i].isAssignableFrom(typelist[i])) {
							typelist[i] = constructor.getParameterTypes()[i];
						}
					}
				}
			}
			try {
				return (XNode) node.getConstructor(typelist).newInstance(objects);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Sets up the Xpression environment
	 */
	public static void initialize() {
		registerClass(String.class);	
		registerClass(Character.class);	
		registerClass(Boolean.class);	
		registerClass(Byte.class);	
		registerClass(Short.class);	
		registerClass(Integer.class);
		registerClass(Long.class);	
		registerClass(Float.class);	
		registerClass(Double.class);	
	}
	
}
