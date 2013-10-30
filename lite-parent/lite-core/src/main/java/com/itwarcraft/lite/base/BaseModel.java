package com.itwarcraft.lite.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 新建项目时，所有的模型层对象继承该类，
 * 为各对象提供基本的CRUD方法
 * 不提供复杂的查询等操作方法
 * 只提供最基本的事务机制
 * @author itwarcraft@gmail.com
 *
 */
public class BaseModel {

	//1 所有的属性，采用map
	
	private Map<String,Object> attrs = new HashMap<String,Object>();
	
	//2 哪些属性被更新过  更新的值是多少？map
	
	
	public Map<String, Object> getAttrs() {
		return attrs;
	}
	
	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}
	
}
