package com.github.gv2011.jsplit.imp;

import java.io.InputStream;

import com.github.gv2011.quarry.cfs.HashImp;
import com.github.gv2011.quarry.util.Hash;

class Entry {
    static final int SIZE = HashImp.SIZE;
    final int size;
    final Hash hash;
    Entry(final byte[] buffer, final int size) {
      this.size = size;
      hash = HashImp.digest(buffer, 0, size);
    }
    Entry(final InputStream in) {
      size = 0;
      hash= HashImp.read(in);
    }
    int write(final byte[] buffer, final int offset) {
      return hash.write(buffer, offset);
    }

}
