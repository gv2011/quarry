package com.github.gv2011.quarry.bits;

import java.io.InputStream;

public class Serializer {

  public static boolean readBoolean(final InputStream in) {
    // TODO Auto-generated method stub
    return false;
  }


  public static int readShort(final InputStream in) {
    // TODO Auto-generated method stub
    return 0;
  }


  public static int writeShort(final int sh, final byte[] buffer, final int offset) {
    assert sh>=0 && sh <= 0xFFFF;
    //contract.
    buffer[offset] = (byte) (sh>>8);
    buffer[offset+1] = (byte) sh;
    return 2;
  }


  public static int writeBoolean(final boolean b, final byte[] buffer, final int offset) {
    // TODO Auto-generated method stub
    return 0;
  }


}
