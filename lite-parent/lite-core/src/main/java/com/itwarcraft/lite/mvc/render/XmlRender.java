package com.itwarcraft.lite.mvc.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XmlRender extends Render {

	private String content;
	
	public XmlRender(String content){
		this.content = content;
		this.setContentType("application/xml;chartset=utf-8");
	}
	
	
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.renderer(request, response, content);
	}

}
