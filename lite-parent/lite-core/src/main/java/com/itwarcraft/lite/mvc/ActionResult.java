package com.itwarcraft.lite.mvc;

import java.io.IOException;
import java.io.PrintWriter;
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
	
	public int getMessageCode() {
		return messageCode;
	}
	
	public Map<String, Object> getData() {
		return data;
	}

	//默认的方式，返回的是jsp
	public void render(HttpServletRequest request, HttpServletResponse response) {
		if(type!=null){
			if(this.type==ViewType.FREE_MARKER){
				
			}else if(this.type==ViewType.VELOCITY){
				
			}else if(this.type==ViewType.JSP){
				
			}else{
				
			}
		}else{
			//默认采用Lite.ViewType的设定
		}
	}

	/**
	 * 返回JSON格式的视图
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void renderToJSON(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		this.success = 1;
		String result = PluginFactory.getJSONPlugin().toJSONString(this);
		out.print(result);
		out.flush();
		out.close();
	}

	public void renderToXML(HttpServletRequest request,
			HttpServletResponse response) {

	}

	public void renderString(HttpServletRequest request,
			HttpServletResponse response,String text) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		this.success = 1;
		out.print(text);
		out.flush();
		out.close();
	}

	public void renderToHtml(HttpServletRequest request,
			HttpServletResponse response) {

	}
	
	
	public static ActionResult getJsonInstance(){
		
		return null;
	}


	
	public ViewType getViewType() {
		return type;
	}
}
