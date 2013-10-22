package com.itwarcraft.lite.base;

import com.itwarcraft.lite.mvc.ActionInvocation;


public interface Intercepter {

	public void doIntercept(ActionInvocation invocation);
	
}
