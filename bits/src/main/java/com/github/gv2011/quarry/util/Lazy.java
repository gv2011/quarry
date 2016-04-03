package com.github.gv2011.quarry.util;

import java.util.function.Supplier;

public class Lazy<T>{

public static <T> Lazy<T> create(final Supplier<? extends T> source){
  return new Lazy<T>(source);
}

private final Object lock = new Object();

private final Supplier<? extends T> source;

private Lazy(final Supplier<? extends T> source) {
  this.source = source;
}

private T value;

public T get(){
  if(value==null){
    synchronized(lock){
      if(value==null) value = source.get();
    }
  }
  return value;
}

}
