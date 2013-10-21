package com.itwarcraft.lite.converter;

public class LongConverter implements Converter<Long> {
	public Long convert(String string) {
		return Long.valueOf(Long.parseLong(string));
	}

}
