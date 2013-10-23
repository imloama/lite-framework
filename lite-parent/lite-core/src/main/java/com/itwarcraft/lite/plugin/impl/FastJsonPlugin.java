package com.itwarcraft.lite.plugin.impl;

import com.alibaba.fastjson.JSON;
import com.itwarcraft.lite.plugin.JSONPlugin;

public class FastJsonPlugin implements JSONPlugin {

	public void init() {

	}

	public void start() {

	}

	public void stop() {

	}

	public void destory() {

	}

	public String toJSONString(Object target) {
		return JSON.toJSONString(target);
	}

}
