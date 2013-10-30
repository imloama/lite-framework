package com.itwarcraft.lite.core;

public class LiteException extends RuntimeException {

	private static final long serialVersionUID = 8326845210126544421L;

	private String message;
	private Throwable cause;
	
	public LiteException(String message,Throwable cause){
		this.message = message;
		this.cause = cause;
	}
	
	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
		super.printStackTrace();
	}
	
	@Override
	public String toString() {
		return "[error message]:"+this.message+"."+this.cause.toString();
	}
}
