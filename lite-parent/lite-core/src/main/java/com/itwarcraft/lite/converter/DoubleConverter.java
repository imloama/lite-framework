package com.itwarcraft.lite.converter;

public class DoubleConverter implements Converter<Double> {
	public Double convert(String string) {
		return Double.valueOf(Double.parseDouble(string));
	}

}
