package com.github.gv2011.experiments.funt;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class Cache<T> {

private final Supplier<T> constructor;
private final Object lock = new Object();
private final AtomicReference<WeakReference<T>> ref = new AtomicReference<WeakReference<T>>(null);



public Cache(final Supplier<T> constructor) {
  this.constructor = constructor;
}



public T get(){
  T result;
  WeakReference<T> weak = ref.get();
  result = weak==null?null:weak.get();
  if(result == null){
    synchronized(lock){
      weak = ref.get();
      result = weak==null?null:weak.get();
      if(result==null){
        result = constructor.get();
        ref.set(new WeakReference<T>(result));
      }
    }
  }
  return result;
}

}
