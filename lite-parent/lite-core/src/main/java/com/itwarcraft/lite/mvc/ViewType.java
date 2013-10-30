package com.itwarcraft.lite.mvc;

import com.itwarcraft.lite.core.Lite;

/**
 * 视图类型
 * @author itwarcraft@gmail.com
 */
public enum ViewType {
	JSP(Lite.RENDER_JSP),
	HTML(Lite.RENDER_HTML),
	TEXT(Lite.RENDER_TEXT),
	JSON(Lite.RENDER_JSON),
	XML(Lite.RENDER_XML),
	FREE_MARKER(Lite.RENDER_FREEMARKER),
	VELOCITY(Lite.RENDER_VELOCITY),
	CUSTOM(Lite.RENDER_FREEMARKER);
	
	
	private String className;
	
	ViewType(String className){
		this.className = className;
	}
	
	public ViewType setTempate(String templatePluginClassName){
		this.className = templatePluginClassName;
		return this;
	}
	
	public String getTempate(){
		return this.className;
	}
	
	public static ViewType getViewTypeByContentType(String contentType){
		if(Lite.APPLICATION_JSON.equalsIgnoreCase(contentType)){
			return ViewType.JSON;
		}else if(Lite.APPLICATION_XML.equalsIgnoreCase(contentType)){
			return ViewType.XML;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "the view type is :"+this.className;
	}
	
}
