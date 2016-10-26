package com.github.gv2011.experiments.vars;

import static com.github.gv2011.util.ex.Exceptions.format;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Transaction{

  private static final Object commitLock = new Object();
  private static final AtomicInteger counter = new AtomicInteger();

  private static ThreadLocal<Transaction> transactions = new ThreadLocal<>();

  public static Transaction open() {
    Transaction current = transactions.get();
    if(current!=null) throw new IllegalStateException(format("{} still open.", current));
    current = new Transaction();
    transactions.set(current);
    return current;
  }

  public static Transaction get() {
    final Transaction current = transactions.get();
    assert current.thread==Thread.currentThread();
    if(current==null) throw new IllegalStateException();
    return current;
  }

  public static void commit() {
    synchronized(commitLock){get().doCommit();}
  }

  public static void abort() {
    synchronized(commitLock){get().doAbort();}
  }

  private final int id;
  private final Thread thread;
  private final Object lock = new Object();
  private final Map<AbstractVar<?>, Value> dirty = new HashMap<>();
  private boolean invalid;
  private boolean closed;

  private Transaction() {
    id = counter.incrementAndGet();
    thread = Thread.currentThread();
  }


  <V extends Value> V get(final AbstractVar<V> var, final V value) {
    check();
    final Optional<V> result = fromMap(var);
    return result.orElse(value);
  }

  private void check() {
    if(Thread.currentThread()!=thread) throw new IllegalStateException();
    final boolean invalid;
    final boolean closed;
    synchronized(lock){
      invalid = this.invalid;
      closed = this.closed;
    }
    if(invalid) {
      transactions.remove();
      throw new TransactionFailedException();
    }
    if(closed) throw new TransactionFailedException();
  }

  @SuppressWarnings("unchecked")
  private <V extends Value> Optional<V> fromMap(final AbstractVar<V> var) {
    synchronized(lock){
      return Optional.ofNullable((V)dirty.get(var));
    }
  }

  @SuppressWarnings("unchecked")
  private <V extends Value> Optional<V> put(final AbstractVar<V> var, final V value) {
    synchronized(lock){
      return Optional.ofNullable((V)dirty.put(var, value));
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void update(final Entry<AbstractVar<?>, Value> e) {
    update((AbstractVar)e.getKey(), e.getValue());
  }



  <V extends Value> V set(final AbstractVar<V> var, final V cleanValue, final V newValue) {
    synchronized(lock){
      check();
      final V result;
      final Optional<V> current = fromMap(var);
      if(current.isPresent()){
        result = current.get();
        if(!newValue.equals(current)){
          if(newValue.equals(cleanValue)) dirty.remove(var);
          else put(var, newValue);
        }
      }else{
        result = cleanValue;
        if(!newValue.equals(cleanValue)){
          put(var, newValue);
        }
      }
      return result;
    }
  }

  private void doCommit() {
    assert Thread.holdsLock(commitLock);
    assert Thread.currentThread()==thread;
    synchronized(lock){
      check();
      for(final Entry<AbstractVar<?>, Value> e: dirty.entrySet()) {
        update(e);
      }
      closed = true;
      transactions.remove();
    }
  }

  private void doAbort() {
    assert Thread.holdsLock(commitLock);
    assert Thread.currentThread()==thread;
    synchronized(lock){
      closed = true;
    }
    transactions.remove();
  }


  private <V extends Value> void update(final AbstractVar<V> var, final V value) {
    var.update(this, value);
  }

  public void invalidate() {
    assert Thread.holdsLock(commitLock);
    synchronized(lock){
      invalid = true;
      closed = true;
    }
  }

  @Override
  public String toString() {
    return "Transaction_" + id;
  }

}
