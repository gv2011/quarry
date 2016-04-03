package com.github.gv2011.quarry.bits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Formatter {
	
	private static String HEX_CHARS = "0123456789ABCDEF";
	
	public static String toString(byte[] bytes){
		char[] result = new char[bytes.length*2];
		for(int i=0; i<bytes.length; i++){
			int b = bytes[i];
			result[i*2] = HEX_CHARS.charAt((b>>4) & 0xF);
			result[i*2+1] = HEX_CHARS.charAt(b & 0xF);
		}
		return new String(bytes);
	}

	public static String toString(List<Byte> bytes){
		char[] result = new char[bytes.size()*2];
		for(int i=0; i<bytes.size(); i++){
			int b = bytes.get(i);
			result[i*2] = HEX_CHARS.charAt((b>>4) & 0xF);
			result[i*2+1] = HEX_CHARS.charAt(b & 0xF);
		}
		return new String(result);
	}

}
