package com.itwarcraft.lite.plugin;

import com.itwarcraft.lite.core.LiteException;

/**
 * 插件的错误信息，继承自系统的LiteException
 * @author itwarcraft@gmail.com
 *
 */
public class PluginException extends LiteException {

	public PluginException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 369416171403721473L;

}
