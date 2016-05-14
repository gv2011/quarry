package com.github.gv2011.jsplit.imp;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.gv2011.jsplit.SliceReader;
import com.github.gv2011.jsplit.SliceWriter;
import com.github.gv2011.jsplit.Splitter;
import com.github.gv2011.quarry.bits.Serializer;
import com.github.gv2011.quarry.util.Hash;

class SplitterImp implements Splitter{

  private int blockSize;

  @Override
  public Hash split(final InputStream in, final SliceWriter sliceWriter) {
    return new Index().split(in, sliceWriter);
  }

  @Override
  public void join(final Hash ref, final SliceReader sliceSource, final OutputStream out) {
    final InputStream in = sliceSource.get(ref);
    final byte[] buffer = new byte[blockSize];
    final int entriesCount = Serializer.readShort(in);
    final int lastEntrySize = Serializer.readShort(in);
    final boolean inlined = Serializer.readBoolean(in);
    for(int i=0; i<entriesCount; i++){
      final Hash hash = new Entry(in).hash;

    }

  }

}
