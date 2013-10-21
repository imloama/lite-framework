package com.itwarcraft.lite.plugin;

/**
 * 所有用到的通用插件都需要从这里注册，而不能够在其他的地方调用
 * @author itwarcraft@gmail.com
 *
 */
public interface IPlugin {

	public void init();
	public void start();
	public void stop();
	public void destory();
	
}
