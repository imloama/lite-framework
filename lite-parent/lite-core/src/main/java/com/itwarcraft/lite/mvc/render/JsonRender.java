package com.itwarcraft.lite.mvc.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonRender extends Render {

	private String jsonString;
	
	public JsonRender(String jsonString){
		this.jsonString = jsonString;
		this.setContentType("application/json; charset=utf-8");
	}
	
	
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.renderer(request, response, this.jsonString);
	}

}
