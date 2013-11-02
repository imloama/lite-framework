package com.itwarcraft.lite.base;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.itwarcraft.lite.mvc.ActionContext;
import com.itwarcraft.lite.mvc.ActionResult;
import com.itwarcraft.lite.mvc.ViewType;
import com.itwarcraft.lite.util.RequestUtil;

/**
 * Action基础类，用来给Action提供方便的参数注入
 * @author itwarcraft@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public abstract class BaseAction<A extends BaseAction> {

	private ActionResult result = null;
	private Map<String,Object> attrs4request = new HashMap<String,Object>();
	private Map<String,Object> attrs4session = new HashMap<String,Object>();
	
	/**
	 * 从request中获取对象，该对象必须是继承自BaseModel类
	 * @param clasz
	 * @return
	 * @throws Exception 
	 */
	public <T> T getModelFromReq(Class<T> clasz) throws Exception{
		return RequestUtil.getObject(ActionContext.getRequest(), clasz);
	}
	
	
	public <T> List<T> getListModelFromReq(Class<T> clasz){
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public A setView(String path){
		this.makeActionResult();
		this.result.setPath(path);
		return (A)this;
	}
	
	@SuppressWarnings("unchecked")
	public A addModel(String key,Object value){
		this.result.addModel(key, value);
		return (A)this;
	}
	
	private void makeActionResult(){
		if(result==null)
			result = new ActionResult();
	}
	
	@SuppressWarnings("unchecked")
	public A setAttribute(String key,Object value){
		this.attrs4request.put(key, value);
		return (A)this;
	}
	
	@SuppressWarnings("unchecked")
	public A setSessionAttribute(String key,Object value){
		this.attrs4session.put(key, value);
		return (A)this;
	}
	
	/**
	 * 先从request中获取，获取失败再从session中获取
	 * @return
	 */
	public String getParameter(String name){
		return this.getParamFromReq(name);
	}
	
	public int getIntParam(String name){
		return Integer.parseInt(this.getParameter(name));
	}
	
	public double getDoubleParam(String name){
		return Double.parseDouble(this.getParameter(name));
	}
	
	public Date getDateParam(String name) throws ParseException{
		return DateUtils.parseDate(this.getParameter(name), new String[]{"yyyy-MM-dd HH:mm:ss"});
	}
	
	
	
	/**
	 * 先从request中获取，再从session中获取
	 * @return
	 */
	public Object getAttribute(String name){
		Object target = null;
		target = this.getAttrFromReq(name);
		if(target==null)
			target = this.getAttrFromSession(name);
		return target;
	}
	
	public String getParamFromReq(String name){
		return ActionContext.getParameter(name);
	}
	
	public Object getAttrFromReq(String name){
		return ActionContext.getAttribute(name);
	}
	
	public Object getAttrFromSession(String name){
		return ActionContext.getAttrFromSession(name);
	}
	
	/**
	 * 把参数保存到session中
	 */
	private void setAttrs2Session(){
		for(String key:this.attrs4session.keySet()){
			ActionContext.getHttpSession().setAttribute(key, this.attrs4session.get(key));
		}
	}
	
	public void render() throws Exception{
		//先setparameter，然后再
		this.result.addModel(this.attrs4request);
		this.setAttrs2Session();
		this.result.render(ActionContext.getRequest(), ActionContext.getResponse());
	}
	
	public void render(String path,Map<String,Object> model) throws Exception{
		this.result.setPath(path).addModel(model);
		this.render();
	}
	
	public void render(String path,Map<String,Object> model,ViewType type) throws Exception{
		this.result.setPath(path).addModel(model).setViewType(type);
		this.render();
	}
	
	public void render(String path) throws Exception{
		this.result.setPath(path);
		this.render();
	}
	
	public void renderText(String text) throws Exception{
		this.result.setViewType(ViewType.TEXT).addModel("lite_view_text",text);
		this.render();
	}
	
	public void OK()throws Exception{
		this.renderText("success!");
	}
	
}
