package com.itwarcraft.lite.core;

import com.itwarcraft.lite.mvc.ViewType;

public final class Lite {
	
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	
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
