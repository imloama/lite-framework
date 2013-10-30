package com.itwarcraft.lite.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwarcraft.lite.core.Lite;
import com.itwarcraft.lite.core.PluginFactory;
import com.itwarcraft.lite.mvc.render.FreemarkerRender;
import com.itwarcraft.lite.mvc.render.JsonRender;
import com.itwarcraft.lite.mvc.render.JspRender;
import com.itwarcraft.lite.mvc.render.Render;
import com.itwarcraft.lite.mvc.render.TemplateRender;
import com.itwarcraft.lite.mvc.render.TextRender;
import com.itwarcraft.lite.mvc.render.VelocityRender;
import com.itwarcraft.lite.mvc.render.XmlRender;
import com.itwarcraft.lite.plugin.XmlPlugin;

/**
 * Action处理结果封装类,根据要求不同，提供不同的视图
 * 
 * @author itwarcraft@gmail.com
 */
public class ActionResult {

	/**
	 * Action的处理结果   0=失败  1＝成功
	 */
	private int success = 0;
	/**
	 * 处理结果CODE，根据CODE的值不同，代表不同的含义
	 */
	private int messageCode;

	/**
	 * 视图文件路径     如果是转向的情况，可以在path前加上“redirect:”
	 */
	private String path;
	

	/**
	 * 数据内容，模型层
	 */
	private Map<String, Object> data = new HashMap<String, Object>();

	private ViewType type;

	public ActionResult() {

	}

	public ActionResult(ViewType type){
		this.type = type;
	}
	
	public ActionResult(String path) {
		this.path = path;
	}
	
	public ActionResult(String path,ViewType type) {
		this.path = path;
		this.type = type;
	}

	public ActionResult(String path, Map<String, Object> model) {
		this.path = path;
		this.data = model;
	}

	public ActionResult setOK() {
		messageCode = 1;
		return this;
	}

	public ActionResult setError() {
		messageCode = 0;
		return this;
	}

	public ActionResult addModel(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	
	public ActionResult addModel(Map<String,Object> model){
		for(String key : model.keySet()){
			this.addModel(key, model.get(key));
		}
		return this;
	}
	
	public int getMessageCode() {
		return messageCode;
	}
	public ActionResult setPath(String path){
		this.path = path;
		return this;
	}
	
	public ActionResult setViewType(ViewType type){
		this.type = type;
		return this;
	}
	
	
	public Map<String, Object> getData() {
		return data;
	}

	//默认的方式，返回的是jsp
	public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.addModel("success", this.success);
		this.addModel("messageCode", this.messageCode);
		String r = request.getContentType();
		if(Lite.APPLICATION_JSON.equalsIgnoreCase(r)){
			this.type = ViewType.JSON;
		}else if(Lite.APPLICATION_XML.equalsIgnoreCase(r)){
			this.type = ViewType.XML;
		}
		
		Render render = null;
		//默认采用Lite.ViewType的设定
		if(type==null){
			this.type = Lite.getViewType();
		}
		
		if(this.type==ViewType.FREE_MARKER){
			render = new FreemarkerRender(this.path,this.data);
		}else if(this.type==ViewType.VELOCITY){
			render = new VelocityRender(this.path,this.data);
		}else if(this.type==ViewType.JSP){
			render = new JspRender(this.path,this.data);
		}else if(this.type==ViewType.JSON){
			render = new JsonRender(PluginFactory.getJSONPlugin().toJSONString(this));
		}else if(this.type==ViewType.XML){
			render = new XmlRender(PluginFactory.getPlugin("xml", XmlPlugin.class).toXml(this));
		}else if(this.type == ViewType.TEXT){
			render = new TextRender((String)this.data.get("lite_view_text"));
			
		}else{
			render = new TemplateRender();
		}
		
		render.render(request, response);
	}

	
	
	public ViewType getViewType() {
		return type;
	}
}
