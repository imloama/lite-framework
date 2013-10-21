package com.itwarcraft.lite.converter;

public class ByteConverter implements Converter<Byte> {
	public Byte convert(String string) {
		return Byte.valueOf(Byte.parseByte(string));
	}

}
