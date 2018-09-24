package com.github.gv2011.quarry.bits;

import java.util.List;

public class Formatter {

	private static String HEX_CHARS = "0123456789ABCDEF";

	public static String toString(final byte[] bytes){
		final char[] result = new char[bytes.length*2];
		for(int i=0; i<bytes.length; i++){
			final int b = bytes[i];
			result[i*2] = HEX_CHARS.charAt((b>>4) & 0xF);
			result[i*2+1] = HEX_CHARS.charAt(b & 0xF);
		}
		return new String(bytes);
	}

	public static String toString(final List<Byte> bytes){
		final char[] result = new char[bytes.size()*2];
		for(int i=0; i<bytes.size(); i++){
			final int b = bytes.get(i);
			result[i*2] = HEX_CHARS.charAt((b>>4) & 0xF);
			result[i*2+1] = HEX_CHARS.charAt(b & 0xF);
		}
		return new String(result);
	}

}
