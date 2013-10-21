package com.itwarcraft.lite.mvc;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionExceptionHandler {

	/**
	 * Handle exception that print stack trace on HTML page.
	 */
	public void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		pw.write("<html><head><title>出错了！</title></head><body><pre>");
		e.printStackTrace(pw);
		pw.write("</pre></body></html>");
		pw.flush();
	}
	
}
