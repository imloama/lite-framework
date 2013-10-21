package com.itwarcraft.lite.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwarcraft.lite.core.PluginFactory;

/**
 * Action处理结果封装类,根据要求不同，提供不同的视图
 * 
 * @author itwarcraft@gmail.com
 */
public class ActionResult {

	private static final String JSON_CONTENT_TYPE = "application/json";
	
	
	/**
	 * 处理结果CODE，根据CODE的值不同，代表不同的含义
	 */
	private int messageCode;

	/**
	 * 视图文件路径
	 */
	private String path;
	
	/**
	 * 如果是文本内容
	 */
	private String text;

	/**
	 * 数据内容，模型层
	 */
	private Map<String, Object> model = new HashMap<String, Object>();

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
		this.model = model;
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
		this.model.put(key, value);
		return this;
	}
	
	public int getMessageCode() {
		return messageCode;
	}
	
	public Map<String, Object> getModel() {
		return model;
	}

	//默认的方式，返回的是jsp
	public void render(HttpServletRequest request, HttpServletResponse response) {

	}

	public void renderToJSON(HttpServletRequest request,
			HttpServletResponse response) {
		PluginFactory.getJSONPlugin().toJSONString(this);
	}

	public void renderToXML(HttpServletRequest request,
			HttpServletResponse response) {

	}

	public void renderToText(HttpServletRequest request,
			HttpServletResponse response) {

	}

	public void renderToHtml(HttpServletRequest request,
			HttpServletResponse response) {

	}
	
	public static ActionResult getJsonInstance(){
		
		return null;
	}


}
