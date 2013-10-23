package com.itwarcraft.lite.mvc.render;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspRender extends Render {

	
	public JspRender(String path){
		this.setPath( path);
	}
	
	public JspRender(String path,Map<String, Object> model){
		this.setPath( path).setModel(model);
	}
	
	public JspRender(String path,String key,Object model){
		this.setPath( path);
		this.getModel().put(key, model);
	}
	
	public JspRender addModel(String key,Object value){
		this.getModel().put(key, value);
		return this;
	}
	
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 Set<String> keys = this.getModel().keySet();
		 for(String key:keys){
			 request.setAttribute(key, this.getModel().get(key));
		 }
		 
		 if(this.isRedirect()){
			 response.sendRedirect(this.getPath());
		 }else{
			 request.getRequestDispatcher(this.getPath()).forward(request, response);
		 }
		 
		
	}
	
	
}
