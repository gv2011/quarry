package com.github.gv2011.quarry.nfs;

import java.io.OutputStream;

public interface Data extends Value{

  Intg length();

  Hash hash();

  byte[] toBytes(long offset, int length);

  default void write(final OutputStream stream){
    write(stream, 0, length().toLong());
  }

  void write(OutputStream stream, long fromIndex, long toIndex);

}
