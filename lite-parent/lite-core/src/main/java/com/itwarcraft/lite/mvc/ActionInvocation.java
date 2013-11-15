package com.itwarcraft.lite.mvc;

import com.itwarcraft.lite.base.Intercepter;


/**
 * 执行Action
 * @author Administrator
 *
 */
public class ActionInvocation {

	
	public ActionInvocation(Action action, Object... args) {
		this.action = action;
		this.intercepters = action.intercepters;
		if (action.intercepters != null && action.intercepters.length != 0) {
			this.length = action.intercepters.length;
		}
		this.args = args;
	}

	protected ActionInvocation() {
	}

	private Action action;
	private Intercepter[] intercepters;
	private int index = 0;
	private int length = 0;
	
	private Object[] args;

	// 调用方法返回值
	Object result = null;

	public Action getAction() {
		return action;
	}

	public Object invoke() {
		if (length == 0) {
			try {

				Object object = Class.forName(this.action.clasz.getName()).newInstance();
				// 调用Action中目的方法
				result = action.method.invoke(object, this.args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			if (index <= length - 1) {
				this.intercepters[index++].doIntercept(this);
			} else {

				Object object = null;
				try {
					object = Class.forName(this.action.clasz.getName()).newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				//调用Action中目的方法
				try {
					result = action.method.invoke(object, this.args);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
			}

		}
		return result;
	}

}
