package com.itwarcraft.lite.demo.action;

import java.util.ArrayList;
import java.util.List;

import com.itwarcraft.lite.annotation.Act;
import com.itwarcraft.lite.annotation.Path;
import com.itwarcraft.lite.mvc.ActionResult;
import com.itwarcraft.lite.mvc.ViewType;

@Act
public class DemoAction {

	@Path("get:/hello/$1")
	public String hello(String name){
		return "Hello,"+name+"!";
	}
	
	
	@Path("get:/findAll")
	public ActionResult findAll(){
		List<String> all = new ArrayList<String>();
		all.add("admin");
		all.add("test");
		ActionResult result = new ActionResult("/all.jsp",ViewType.JSP);
		result.addModel("users", all);
		return result;
	}
	
}
