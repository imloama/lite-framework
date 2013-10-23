package com.itwarcraft.lite.mvc.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public abstract class Render {
	
	/**
	 * 返回时用到的头部内容
	 */
	private String contentType = "text/html;charset=utf-8";
	
	
	private String path;
	
	private Map<String,Object> model = new HashMap<String,Object>();

	/**
	 * Get response content type.
	 */
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
	public Map<String, Object> getModel() {
		return model;
	}
	public Render setModel(Map<String, Object> model) {
		this.model = model;
		return this;
	}
	
	public Render addModel(String key,Object value){
		this.model.put(key, value);
		return this;
	}
	
	public abstract void render(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	/**
	 * 判断如果存在path的话，直接是不是需要转向
	 * @return
	 */
	public boolean isRedirect(){
		if(!StringUtils.isEmpty(this.path)&&this.path.startsWith("redirect:")){
			return true;
		}
		return false;
	}
	
	public String getPath() {
		return path;
	}
	public Render setPath(String path) {
		this.path = path;
		return this;
	}
	
	public void renderer(HttpServletRequest request, HttpServletResponse response,String content) throws IOException{
		response.setContentType(this.getContentType());
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		out.print(content);
		out.flush();
		out.close();
	}
	
}
