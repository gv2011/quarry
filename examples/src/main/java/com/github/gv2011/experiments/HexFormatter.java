package com.github.gv2011.experiments;

public final class HexFormatter {

public static void main(final String[] args)throws Exception{
//  final long n = -1;
//  System.out.println(toHex((byte)n));
//  System.out.println(toHex((short)n));
//  System.out.println(toHex((int)n));
//  System.out.println(toHex((long)n));
  for(int i=0; i<256; i++) System.out.println(toBin(i,1));
}

private static final String HEX_CHARS = "0123456789ABCDEF";

public static String toHex(final byte i){
  return toHex(i, 1);
}

public static String toHex(final short i){
  return toHex(i, 2);
}

public static String toHex(final int i){
  return toHex(i, 4);
}

public static String toHex(final long i){
  return toHex(i, 8);
}

public static String toHex(final long i, final int byteCount){
  final int charCount = byteCount*3-1;
  final char[] chars = new char[charCount];
  int shift = byteCount*8-4;
  for(int b=0; b<byteCount; b++){
    final int offset = b*3;
    if(b>0) chars[offset-1] = ' ';
    chars[offset]   = HEX_CHARS.charAt(((int)(i>>shift)) & 0xF);
    shift-=4;
    chars[offset+1] = HEX_CHARS.charAt(((int)(i>>shift)) & 0xF);
    shift-=4;
  }
  return new String(chars);
}

public static String toBin(final long i, final int byteCount){
  final int charsPerByte = 8;
  final int charCount = byteCount*(charsPerByte+1)-1;
  final char[] chars = new char[charCount];
  int shift = byteCount*8-1;
  for(int b=0; b<byteCount; b++){
    final int offset = b*9;
    if(b>0) chars[offset-1] = ' ';
    for(int c=0; c<charCount; c++){
      chars[offset+c] = HEX_CHARS.charAt(((int)(i>>shift)) & 0x1);
      shift--;
    }
  }
  return new String(chars);
}


}
