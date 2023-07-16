package org.xpression.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XNodePrefixes {
	boolean AllowSelfRecursion() default false;
	boolean AllowImplicitRecursion() default false;
}
