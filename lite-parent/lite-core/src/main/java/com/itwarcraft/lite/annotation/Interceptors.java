package com.itwarcraft.lite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.itwarcraft.lite.mvc.Intercepter;

/**
 * 拦截器注解 
 * @author Administrator
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface Interceptors {
	Class<? extends Intercepter>[] value();
}
