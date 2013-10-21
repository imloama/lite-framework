package com.itwarcraft.lite.converter;

public class IntegerConverter implements Converter<Integer> {
	public Integer convert(String string) {
		return Integer.valueOf(Integer.parseInt(string));
	}

}
