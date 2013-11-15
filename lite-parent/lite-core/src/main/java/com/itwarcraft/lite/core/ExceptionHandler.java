package com.itwarcraft.lite.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统EXCEPTION处理的接口类,用于表示所有的异常处理,都需要继承该类
 * @author Administrator
 *
 */
public interface ExceptionHandler {

	public void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception;
	
}
