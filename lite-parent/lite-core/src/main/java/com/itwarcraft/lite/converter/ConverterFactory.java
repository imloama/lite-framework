package com.itwarcraft.lite.converter;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

	public ConverterFactory() {
		Converter<?> converter = null;

		converter = new BooleanConverter();
		this.converterMap.put(Boolean.TYPE, converter);
		this.converterMap.put(Boolean.class, converter);

		converter = new ByteConverter();
		this.converterMap.put(Byte.TYPE, converter);
		this.converterMap.put(Byte.class, converter);

		converter = new ShortConverter();
		this.converterMap.put(Short.TYPE, converter);
		this.converterMap.put(Short.class, converter);

		converter = new IntegerConverter();
		this.converterMap.put(Integer.TYPE, converter);
		this.converterMap.put(Integer.class, converter);

		converter = new LongConverter();
		this.converterMap.put(Long.TYPE, converter);
		this.converterMap.put(Long.class, converter);

		converter = new FloatConverter();
		this.converterMap.put(Float.TYPE, converter);
		this.converterMap.put(Float.class, converter);

		converter = new DoubleConverter();
		this.converterMap.put(Double.TYPE, converter);
		this.converterMap.put(Double.class, converter);
	}

	private Map<Class<?>, Converter<?>> converterMap = new HashMap<Class<?>, Converter<?>>();

	public boolean canConvert(Class<?> clazz) {
		return (clazz.equals(String.class)) || (this.converterMap.containsKey(clazz));
	}

	public Object convert(Class<?> clazz, String string) {

		Converter<?> converter = this.converterMap.get(clazz);
		return converter.convert(string);
	}

}
