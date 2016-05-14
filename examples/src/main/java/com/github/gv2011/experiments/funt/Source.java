package com.github.gv2011.experiments.funt;

public abstract class Source {

boolean hasChangedBetween(final Value param, final Time earlier, final Time later) {
  return true;
}

public abstract Value get(final Value param, final Time time);
}
