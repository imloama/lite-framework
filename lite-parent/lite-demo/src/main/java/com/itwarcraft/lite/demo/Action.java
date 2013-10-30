package com.itwarcraft.lite.demo;

import com.itwarcraft.lite.annotation.Act;
import com.itwarcraft.lite.annotation.Path;
import com.itwarcraft.lite.mvc.ActionResult;

@Act
public class Action {

	@Path("get:/index")
	public ActionResult index(){
		ActionResult result = new ActionResult("/index.jsp");
		result.addModel("name", "itwarcraft.com");
		return result;
	}
	
}
