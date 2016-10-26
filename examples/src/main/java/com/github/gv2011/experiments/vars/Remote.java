package com.github.gv2011.experiments.vars;

import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class Remote {

  private final AbstractVar<StrValue> local;

  public Remote(final ExecutorService es) {
    local = new RemoteVar<>(es, UUID.randomUUID(), this);
  }


  @SuppressWarnings("unchecked")
  public <V extends Value> AbstractVar<V> getRemoteVar(final UUID id){
    if(!id.equals(local.id())) throw new NoSuchElementException();
    return (AbstractVar<V>) local;
  }

  public <V extends Value> V getValue(final UUID id) {
    // TODO Auto-generated method stub
    throw notYetImplementedException();
  }

}
