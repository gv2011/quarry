package com.github.gv2011.experiments.vars;

public class StrValue implements Value{

  private final String str;

  public StrValue(final String str) {
    this.str = str;
  }

  @Override
  public String toString() {
    return str;
  }


}
