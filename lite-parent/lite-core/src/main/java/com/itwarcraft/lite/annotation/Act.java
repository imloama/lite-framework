package com.itwarcraft.lite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * action类注解
 * @author Administrator
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface Act {
	String value() default "";
}
