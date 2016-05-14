package com.github.gv2011.experiments.funt;

import java.util.SortedMap;
import java.util.TreeMap;

public class Variable extends Source{

private final SortedMap<Time,Value> values = new TreeMap<>();

  @Override
  public Value get(final Value param, final Time time) {
    final SortedMap<Time, Value> earlier = values.headMap(time);
    if(earlier.isEmpty())return null;
    else return earlier.get(earlier.lastKey());
  }

  @Override
  boolean hasChangedBetween(final Value param, final Time earlier, final Time later) {
    return !values.subMap(earlier, later).isEmpty();
  }



}
