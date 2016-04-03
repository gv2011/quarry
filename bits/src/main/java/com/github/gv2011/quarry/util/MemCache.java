package com.github.gv2011.quarry.util;

import java.lang.ref.WeakReference;
import java.util.function.Supplier;

public class MemCache<T> {

public static <T> MemCache<T> create(final Supplier<? extends T> source){
  return new MemCache<T>(source);
}

private final Object lock = new Object();

private final Supplier<? extends T> source;

private MemCache(final Supplier<? extends T> source) {
  this.source = source;
  }

private WeakReference<T> ref = new WeakReference<T>(null);

public T get(){
  T result = ref.get();
  if(result==null){
    synchronized(lock){
      result = ref.get();
      if(result==null){
        result = source.get();
        ref = new WeakReference<T>(result);
      }
    }
  }
  return result;
}

}
