package com.itwarcraft.lite.mvc.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwarcraft.lite.core.PluginFactory;
import com.itwarcraft.lite.plugin.TemplatePlugin;



/**
 * 自定义模板的Render
 * @author Administrator
 *
 */
public class TemplateRender extends Render {

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PluginFactory.getPlugin("template", TemplatePlugin.class).render(request, response, 
				this.getPath(), this.getModel());
	}

}
