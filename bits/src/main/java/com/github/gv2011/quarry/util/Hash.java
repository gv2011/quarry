package com.github.gv2011.quarry.util;

import com.github.gv2011.quarry.bits.TypedBytes;

public interface Hash extends TypedBytes<Hash>{

	@Override
  int size();

	@Override
  int write(byte[] buffer, int offset);

}
