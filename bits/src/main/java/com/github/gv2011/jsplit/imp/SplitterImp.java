package com.github.gv2011.jsplit.imp;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.gv2011.jsplit.SliceReader;
import com.github.gv2011.jsplit.SliceWriter;
import com.github.gv2011.jsplit.Splitter;
import com.github.gv2011.quarry.util.Hash;

class SplitterImp implements Splitter{

  @Override
  public Hash split(final InputStream in, final SliceWriter sliceWriter) {
    return new Index(sliceWriter).split(in);
  }

  @Override
  public void join(final Hash ref, final SliceReader sliceSource, final OutputStream out) {
    // TODO Auto-generated method stub

  }

}
