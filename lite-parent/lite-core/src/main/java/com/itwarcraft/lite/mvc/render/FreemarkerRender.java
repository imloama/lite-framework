package com.itwarcraft.lite.mvc.render;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FreemarkerRender extends Render{

	private String path;
	
	private Map<String,Object> model = new HashMap<String, Object>();
	
	public FreemarkerRender(){}
	public FreemarkerRender(String path){
		this.path = path;
	}
	public FreemarkerRender(String path,Map<String,Object> model){
		this.path = path;
		this.model = model;
	}
	
	public String getPath() {
		return path;
	}
	
	public FreemarkerRender setPath(String path) {
		this.path = path;
		return this;
	}
	public Map<String, Object> getModel() {
		return model;
	}
	public FreemarkerRender setModel(Map<String, Object> model) {
		this.model = model;
		return this;
	}
	public FreemarkerRender addModel(String key,Object value){
		this.model.put(key, value);
		return this;
	}
	
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//PluginFactory.getFreeMarkerPlugin().render(request, response, this.path, model);
	}

}
