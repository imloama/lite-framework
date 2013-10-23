package com.itwarcraft.lite.util;

import org.junit.Test;

import com.itwarcraft.lite.base.ModelTest;

public class RequestUtilTest {

	
	@Test
	public void getModel(){
		String s1 = ModelTest.class.getName();
		String s2 = ModelTest.class.getPackage().getName();
		String s3 = ModelTest.class.getSimpleName();
		System.out.println("name:"+s1+",package:"+s2+",simplename:"+s3);
		
		String key = "user";
		String name = "user.age";
		System.out.println(name.substring(key.length()));
		
	}
	
	
}
