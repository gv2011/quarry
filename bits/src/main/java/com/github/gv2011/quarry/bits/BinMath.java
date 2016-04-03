package com.github.gv2011.quarry.bits;

public class BinMath {

public static long twoToThePowerOf(final int exp){
  if(exp<0) throw new IllegalArgumentException();
  if(exp>62) throw new IllegalArgumentException();
  return 1l<<exp;
}

}
