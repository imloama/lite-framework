package com.itwarcraft.lite.mvc.render;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TextRender extends Render {
	public TextRender() {
	}

	public TextRender(String text) {
		this.text = text;
	}


	private String text;

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType(this.getContentType());
		PrintWriter pw = response.getWriter();
		pw.write(this.text);
		pw.flush();
	}


	public void setText(String text) {
		this.text = text;
	}
}
