package com.itwarcraft.lite.core;

public class App {

	private static boolean devMode = false;
	
	public static boolean getDevMode(){
		return devMode;
	}
	
	private App(){}
	
	
	static void init(boolean devMode){
		App.devMode = devMode; 
	}
	
	
}
