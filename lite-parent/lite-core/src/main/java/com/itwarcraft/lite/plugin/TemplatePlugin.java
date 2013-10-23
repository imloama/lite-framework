package com.itwarcraft.lite.plugin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TemplatePlugin extends IPlugin {
	
	

	public void render(HttpServletRequest request,HttpServletResponse response,String path,Map<String,Object> model)throws Exception;
	
}
