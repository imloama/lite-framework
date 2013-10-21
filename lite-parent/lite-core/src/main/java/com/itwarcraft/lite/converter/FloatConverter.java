package com.itwarcraft.lite.converter;

public class FloatConverter implements Converter<Float> {
	public Float convert(String string) {
		return Float.valueOf(Float.parseFloat(string));
	}

}
