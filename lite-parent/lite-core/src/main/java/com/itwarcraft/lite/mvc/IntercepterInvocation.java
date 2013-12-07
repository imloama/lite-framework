package com.itwarcraft.lite.mvc;

import java.util.List;

import com.itwarcraft.lite.base.Intercepter;

/**
 * 执行拦截器的方法
 * @author itwarcraft@gmail.com
 * @time 2013-12-04
 */
public class IntercepterInvocation {

	private List<Intercepter> intercepters = null;
	private ActionInvocation actionInvocation = null;
	
	public IntercepterInvocation(List<Intercepter> list,ActionInvocation action){
		this.intercepters = list;
		this.actionInvocation = action;
	}
	
	public void invoke(){
		if(this.intercepters!=null&&this.intercepters.size()>0){
			for(Intercepter intercepter : this.intercepters){
				intercepter.doIntercept(this.actionInvocation);
			}
		}
	}
	
}
