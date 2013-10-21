package com.itwarcraft.lite.core;

import com.itwarcraft.lite.plugin.JSONPlugin;
import com.itwarcraft.lite.plugin.fastjson.FastJsonPlugin;

public class PluginFactory {

	private static final JSONPlugin json = new FastJsonPlugin();
	
	static{
		json.init();
		json.start();
	}
	
	public static final JSONPlugin getJSONPlugin(){
		return json;
	}
}
