package org.xpression.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Usage of this annotation:
 * 	Say we have an XNode subclass
 * 		public MyXNode : XNode
 *  then this XNOde will have some constructors that
 *  are used to instantiate the class.
 *  
 *  In order to create an instance using a user interface
 *  we must specify the constructor the user will use.
 *  
 *  Since annotations doesn't allow you to specify a Constructor
 *  we must use it's parameters to identify it.
 *  And we also must define the default values.
 *  As well as the field names you wish to display
 *  	XNodeConstructor (
 *  		parameters = { Class<?>, Class<?>, ... }
 *  		fields = { "fieldname1", "fieldname2" }
 *  		values = { "value1", "value2", ... }
 * 
 */

@Retention(RUNTIME)
@Target(CONSTRUCTOR)
public @interface XNodeConstructor {
	String[] fields();
	String[] values();
}
