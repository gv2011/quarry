package com.github.gv2011.experiments.funt;

public class Value extends Source{

public static final Value NO_VALUE = new Value();
public static final Value ANY_VALUE = new Value();

@Override
public final Value get(final Value param, final Time time) {
  return this;
}

@Override
boolean hasChangedBetween(final Value param, final Time earlier, final Time later) {
  return false;
}

}
