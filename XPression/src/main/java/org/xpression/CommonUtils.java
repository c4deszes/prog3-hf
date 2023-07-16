package org.xpression;

public final class CommonUtils {
	
	/**
	 * Returns the primitive class of a boxing class
	 * @param type Primitive's boxing class (Integer, Boolean, Short, etc.)
	 * @return Primitive class (int, boolean, short, etc.)
	 */
	public static Class<?> getPrimitive(Class<?> type) {
	   if(type.equals(Integer.class)) {
		   return int.class;
	   }
	   else if(type.equals(Long.class)) {
		   return long.class;
	   }
	   else if(type.equals(Short.class)) {
		   return short.class;
	   }
	   else if(type.equals(Byte.class)) {
		   return byte.class;
	   }
	   else if(type.equals(Double.class)) {
		   return double.class;
	   }
	   else if(type.equals(Float.class)) {
		   return float.class;
	   }
	   else if(type.equals(Character.class)) {
		   return char.class;
	   }
	   else if(type.equals(Boolean.class)) {
		   return boolean.class;
	   }
	   return null;
	}
}
