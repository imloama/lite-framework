package com.itwarcraft.lite.converter;

public class ShortConverter implements Converter<Short> {
	public Short convert(String string) {
		return Short.valueOf(Short.parseShort(string));
	}

}
