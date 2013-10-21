package com.itwarcraft.lite.converter;

public class BooleanConverter implements Converter<Boolean> {
	public Boolean convert(String string) {
		return Boolean.valueOf(Boolean.parseBoolean(string));
	}

}
