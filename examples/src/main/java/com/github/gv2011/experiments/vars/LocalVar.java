package com.github.gv2011.experiments.vars;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class LocalVar<V extends Value> extends AbstractVar<V>{

  private V value;

  public LocalVar(final ExecutorService es, final V value) {
    super(es, UUID.randomUUID());
    this.value = value;
  }

  @Override
  protected V value() {
    return value;
  }

  @Override
  void setValue(final V value) {
    this.value = value;
  }

}
