package com.github.gv2011.experiments.vars;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class RemoteVar<V extends Value> extends AbstractVar<V>{

  RemoteVar(final ExecutorService es, final UUID id, final Remote remote) {
    super(es, id);
    this.remote = remote;
  }

  private final Remote remote;
  private final boolean valid = false;
  private Optional<V> value = Optional.empty();

  @Override
  protected V value() {
    if(!valid) value = Optional.of(remote.getValue(id));
    return value.get();
  }



  @Override
  public synchronized V set(final V value) {
    throw new UnsupportedOperationException();
  }



  @Override
  void setValue(final V value) {
    throw new UnsupportedOperationException();
  }

}
