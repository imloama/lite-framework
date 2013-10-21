package com.itwarcraft.lite.mvc;

import java.lang.reflect.Method;


public class Action {
	public Action(Class<?> clasz, Method method, Interceptor[] interceptors) {
		this.clasz = clasz;
		this.method = method;
		this.parameterTypes = method.getParameterTypes();
		this.interceptors = interceptors;

	}

	public final Class<?> clasz;
	public final Method method;
	public final Class<?>[] parameterTypes;
	public final Interceptor[] interceptors;
}
