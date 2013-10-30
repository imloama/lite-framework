package com.itwarcraft.lite.core;

import com.itwarcraft.lite.mvc.ViewType;

public final class Lite {
	
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String PACKAGE = "com.itwarcraft.lite";
	public static final String RENDER_JSP = PACKAGE+".mvc.render.JspRender";
	public static final String RENDER_HTML = PACKAGE+".mvc.render.HtmlRender";
	public static final String RENDER_TEXT = PACKAGE+".mvc.render.TextRender";
	public static final String RENDER_JSON = PACKAGE+".mvc.render.JsonRender";
	public static final String RENDER_XML = PACKAGE + ".mvc.render.XmlRender";
	public static final String RENDER_FREEMARKER = PACKAGE + ".mvc.render.FreemarkerRender";
	public static final String RENDER_VELOCITY = PACKAGE + ".mvc.render.VelocityRender";
	//自定义的模板路径
	public static final String RENDER_TEMPLATE = PACKAGE + ".mvc.render.TemplateRender";
	
	private static boolean devMode = false;
	
	private static ViewType type;
	
	public static boolean getDevMode(){
		return devMode;
	}
	
	private Lite(){}
	
	
	static void init(boolean devMode,ViewType type){
		Lite.devMode = devMode; 
		Lite.type = type;
	}
	
	
	public static ViewType getViewType(){
		return type;
	}
}
