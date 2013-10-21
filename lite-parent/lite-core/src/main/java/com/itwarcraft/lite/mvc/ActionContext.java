package com.itwarcraft.lite.mvc;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action请求时，公共环境变量保存
 * @author itwarcraft@gmail.com
 * @time 二○一三年十月十九日 18时9分
 *
 */
public final class ActionContext {
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private static final ThreadLocal<ActionContext> LOCAL_ACTIONCONTEXT = new ThreadLocal<ActionContext>();
	
	public static String getParameter(String name){
		String value = LOCAL_ACTIONCONTEXT.get().request.getParameter(name);
		if(value==null)
			return null;
		if("get".equalsIgnoreCase(LOCAL_ACTIONCONTEXT.get().request.getMethod())){
			try {
				value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		return value;
	}
	
	public static Object getAttribute(String name){
		return LOCAL_ACTIONCONTEXT.get().request.getAttribute(name);
	}
	
	public static String[] getParameterValues(String name) {
		String[] values = LOCAL_ACTIONCONTEXT.get().request.getParameterValues(name);
		if(values==null)
			return null;
		if("get".equalsIgnoreCase(LOCAL_ACTIONCONTEXT.get().request.getMethod())){
			if(values!=null&&values.length==0)
				return null;
			
			int length = values.length;
			for(int i=0;i<length;i++){
				try {
					values[i] = new String(values[i].getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		
		return values;
	}
	
	public static ActionContext getActionContext(){
		return LOCAL_ACTIONCONTEXT.get();
	}
	
	public static HttpSession getHttpSession(){
		return LOCAL_ACTIONCONTEXT.get().request.getSession();
	}
	
	
	public static void setActionContext(ServletContext servletContext,HttpServletRequest request,HttpServletResponse response){
		ActionContext context = new ActionContext();
		context.setRequest(request);
		context.setResponse(response);
		context.setServletContext(servletContext);
		LOCAL_ACTIONCONTEXT.set(context);
	}
	
	public static void removeActionContext(){
		LOCAL_ACTIONCONTEXT.remove();
	}
	public static HttpServletRequest getRequest() {
		return LOCAL_ACTIONCONTEXT.get().request;
	}
	public static HttpServletResponse getResponse() {
		return LOCAL_ACTIONCONTEXT.get().response;
	}
	public static ServletContext getServletContext() {
		return LOCAL_ACTIONCONTEXT.get().servletContext;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	
}
