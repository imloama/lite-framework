package com.itwarcraft.lite.mvc.render;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VelocityRender extends Render {

	public VelocityRender(String path, Map<String, Object> model) {
		this.setPath(path);
		this.setModel(model);
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}

}
