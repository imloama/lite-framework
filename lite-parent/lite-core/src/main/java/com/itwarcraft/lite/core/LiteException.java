package com.itwarcraft.lite.core;

public class LiteException extends Exception {

	private static final long serialVersionUID = 8326845210126544421L;

	private String message;
	
	public LiteException(String message){
		this.message = message;
	}
	
	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
		super.printStackTrace();
	}
	
	@Override
	public String toString() {
		return "[error message]:"+this.message+"."+this.getCause().toString();
	}
}
