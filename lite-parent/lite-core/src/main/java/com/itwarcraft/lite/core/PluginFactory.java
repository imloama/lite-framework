package com.itwarcraft.lite.core;

import java.util.HashMap;
import java.util.Map;

import com.itwarcraft.lite.plugin.IPlugin;
import com.itwarcraft.lite.plugin.JSONPlugin;
import com.itwarcraft.lite.plugin.TemplatePlugin;
import com.itwarcraft.lite.plugin.impl.FastJsonPlugin;
import com.itwarcraft.lite.plugin.impl.FreemarkerPlugin;

public final class PluginFactory {

	
	/**
	 * 所有用到的插件都需要在这里注册，然后再获取
	 */
	private static Map<String,IPlugin> plugins = new HashMap<String,IPlugin>();
	
	PluginFactory(){
		IPlugin json = new FastJsonPlugin();
		json.init();json.start();
		plugins.put("json", json);
		IPlugin freemarker = new FreemarkerPlugin();
		freemarker.init();freemarker.start();
		plugins.put("freemarker", freemarker);
	}
	
	
	public static JSONPlugin getJSONPlugin() throws Exception{
		return getPlugin("json", JSONPlugin.class);
		/*
		 * IPlugin target = plugins.get("json");
		if(target!=null)
			return (JSONPlugin)target;
		return null;
		 */
	}
	
	public static TemplatePlugin getFreemarkerPlugin() throws Exception{
		return getPlugin("freemarker", TemplatePlugin.class);
	}
	
	/**
	 * 添加插件
	 * @param name 插件的名称
	 *  **@param interfaces 插件的接口  自定义的接口需要继承IPlugin
	 * @param implementClass 插件接口的实现类  所有的实现类需要提供无参构造函数
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	static void addPlugin(String name,Class<? extends IPlugin> implementClass) throws InstantiationException, IllegalAccessException{
		IPlugin value = implementClass.newInstance();
		value.init();value.start();
		plugins.put(name, value);
	}
	 
	 @SuppressWarnings("unchecked")
	public static <T> T getPlugin(String name,Class<T> clasz)throws Exception{
		 IPlugin target = plugins.get(name);
		 
		 try {
			T value = (T)target;
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LiteException("获取插件错误！请检查要导出的插件类型！");
		}
	 }
	
	 public static void stopPlugin(){
		 for(IPlugin plugin:plugins.values()){
			 plugin.stop();
		 }
	 }
	 
	 public static void destoryPlugin(){
		 for(IPlugin plugin:plugins.values()){
			 plugin.destory();
		 }
	 }
	 
	 public static void stopPlugin(String name){
		 IPlugin value = plugins.get(name);
		 if(value!=null)
			 value.stop();
	 }
	
	 
	 public static void destoryPlugin(String name){
		 IPlugin value = plugins.get(name);
		 if(value!=null)
			 value.destory();
	 }
	 
}
