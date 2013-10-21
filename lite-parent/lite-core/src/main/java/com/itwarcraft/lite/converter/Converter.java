package com.itwarcraft.lite.converter;

public abstract interface Converter<T> {
	public abstract T convert(String string);
}
