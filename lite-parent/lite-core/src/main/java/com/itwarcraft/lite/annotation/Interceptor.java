package com.itwarcraft.lite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.itwarcraft.lite.base.Intercepter;

/**
 * 拦截器注解  
 * 用于注解全局拦截器
 * @author itwarcraft@gmail.com
 * @time 2013年11月2日 8时20分
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface Interceptor {
	Class<? extends Intercepter>[] value();
}
