package com.itwarcraft.lite.mvc.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * HTML类型的视图进行展示
 * @author itwarcraft@gmail.com
 *
 */
public class HtmlRender extends Render {

	public HtmlRender(String path){
		this.setPath( path);
	}
	
	
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 if(this.isRedirect()){
			 response.sendRedirect(this.getPath());
		 }else{
			 request.getRequestDispatcher(this.getPath()).forward(request, response);
		 }
		 
		
	}
	
	
}
