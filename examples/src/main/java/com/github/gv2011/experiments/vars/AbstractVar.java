package com.github.gv2011.experiments.vars;

import static com.github.gv2011.util.Comparison.max;
import static com.github.gv2011.util.ex.Exceptions.run;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractVar<V extends Value> implements Supplier<V>{

  static enum State{NORMAL, PREPARE_COMMIT}

  private final ExecutorService es;
  protected final Set<Transaction> localListeners = new HashSet<>();
  protected final Set<VarListener> remoteListeners = new HashSet<>();
  protected final UUID id;
  private State state;

  protected AbstractVar(final ExecutorService es, final UUID id) {
    this.es = es;
    this.id = id;
  }

  public UUID id(){
    return id ;
  }

  protected abstract V value();

  @Override
  public synchronized V get(){
    final Transaction transaction = Transaction.get();
    localListeners.add(transaction);
    return transaction.get(this, value());
  }

  public synchronized V get(final VarListener listener){
    waitIfNecessary(listener);
    remoteListeners.add(listener);
    return value();
  }

  private void waitIfNecessary(final VarListener listener) {
    if(state==State.PREPARE_COMMIT) waitUntilNormal();
  }

  public synchronized V set(final V value){
    final Transaction transaction = Transaction.get();
    localListeners.add(transaction);
    return transaction.set(this, this.value(), value);
  }

  private void waitUntilNormal() {
    while(state!=State.NORMAL){
      run(()->this.wait());
    }
  }

  synchronized void notifyRemoteListeners(){
    if(state!=State.NORMAL) throw new IllegalStateException();
    state = State.PREPARE_COMMIT;
    notifyAll();
    for(final VarListener l: remoteListeners){
      es.submit(()->{
        l.invalidate();
        remove(l);
      });
    }
  }

  private synchronized void remove(final VarListener l) {
    remoteListeners.remove(l);
    if(remoteListeners.isEmpty()) notifyAll();
  }

  /**
   * Update must happen by a transaction.
   */
  synchronized final void update(final Transaction transaction, final V value) {
    notifyAll();

    setValue(value);
    state = State.NORMAL;
    notifyAll();
  }

  abstract void setValue(V value);

}
