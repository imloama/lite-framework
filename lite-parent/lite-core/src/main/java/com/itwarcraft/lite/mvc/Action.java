package com.itwarcraft.lite.mvc;

import java.lang.reflect.Method;

import com.itwarcraft.lite.base.Intercepter;


public class Action {
	public Action(Class<?> clasz, Method method, Intercepter[] intercepters) {
		this.clasz = clasz;
		this.method = method;
		this.parameterTypes = method.getParameterTypes();
		this.intercepters = intercepters;

	}

	public final Class<?> clasz;
	public final Method method;
	public final Class<?>[] parameterTypes;
	public final Intercepter[] intercepters;
}
