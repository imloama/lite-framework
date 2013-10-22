package com.itwarcraft.lite.core;

public class Lite {

	private static boolean devMode = false;
	
	public static boolean getDevMode(){
		return devMode;
	}
	
	private Lite(){}
	
	
	static void init(boolean devMode){
		Lite.devMode = devMode; 
	}
	
	
}
