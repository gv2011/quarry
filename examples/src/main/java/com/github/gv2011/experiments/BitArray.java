package com.github.gv2011.experiments;

import java.util.AbstractList;
import java.util.Collection;

public class BitArray extends AbstractList<Boolean>{

public static void main(final String[] args) throws Exception{
  final byte b0 = 0;
  final byte b1 = -1;
  for(int i=0; i<8; i++){
    final byte b = setBit(b0,i,true);
    assert getBit(b,i) == true;
    System.out.println(HexFormatter.toBin(b,1));
  }
  for(int i=0; i<8; i++){
    final byte b = setBit(b1,i,false);
    assert getBit(b,i) == false;
    System.out.println(HexFormatter.toBin(b,1));
  }
}


private final int size;
private final byte[] bytes;

public BitArray(final Collection<Boolean> c) {
  this.size=c.size();
  this.bytes = new byte[(size+7)/8];
  int i=0;
  for(final Boolean bit: c){
    final int byteIndex = i/8;
    final int bitIndex = i%8;
    bytes[byteIndex] = setBit(bytes[byteIndex], bitIndex, bit.booleanValue());
    i++;
  }
}

public static byte setBit(final byte b, final int bitIndex, final boolean bit) {
  if(bitIndex<0 || bitIndex>7) throw new IndexOutOfBoundsException();
  final int mask = 0x80 >> bitIndex;
  return (byte) (bit ? (b & ~mask)|mask : (b & ~mask) );
}

public static boolean getBit(final byte b, final int bitIndex) {
  if(bitIndex<0 || bitIndex>7) throw new IndexOutOfBoundsException();
  final int mask = 0x80 >> bitIndex;
  return (b & mask)!=0;
}

@Override
public Boolean get(final int index) {
  final byte b = bytes[getByteIndex(index)];
  return null;
}

private int getByteIndex(final int index) {
  return index/8;
}

@Override
public int size() {
  assert false:"auto generated";
  return 0;
}

}
