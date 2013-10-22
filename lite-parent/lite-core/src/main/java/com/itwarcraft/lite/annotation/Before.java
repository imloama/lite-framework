package com.itwarcraft.lite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.itwarcraft.lite.base.Intercepter;


/**
 * 支持类和方法注解，表示在类/方法执行前  执行该拦截器
 * @author itwarcraft@gmail.com
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface Before {
	Class<? extends Intercepter>[] value();
}
