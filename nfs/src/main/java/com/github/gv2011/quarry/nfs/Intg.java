package com.github.gv2011.quarry.nfs;

import java.math.BigInteger;

public interface Intg extends Value{

  BigInteger toBigInteger();

  long toLong();

  int toInt();
}
