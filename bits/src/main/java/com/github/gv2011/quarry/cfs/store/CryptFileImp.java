package com.github.gv2011.quarry.cfs.store;

import java.io.InputStream;
import java.util.function.Supplier;

import com.github.gv2011.quarry.cfs.CryptFile;
import com.github.gv2011.quarry.util.Hash;
import com.github.gv2011.quarry.util.Lazy;

public class CryptFileImp implements CryptFile{

  public static final int SIZE = 64;

  private final Supplier<InputStream> source;
  private final Lazy<Hash> hash;


  CryptFileImp(final Supplier<InputStream> source, final Supplier<Hash> hash) {
    this.source = source;
    this.hash = Lazy.create(hash);
  }

  @Override
  public Hash hash() {
    return hash.get();
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public InputStream newStream() {
    return source.get();
  }

}
