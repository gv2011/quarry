package com.github.gv2011.experiments.types;

public interface Types<T> {

static interface Typed{
  Type<?> type();
}

static interface HType<E> extends Type<H<E>>{
Upcaster<E,? extends H<E>> upcaster();
}

static interface LType<E> extends HType<E>{
@Override
Upcaster<E,L<E>> upcaster();
}

static interface A extends Typed{}
static interface B extends A{}

static interface Upcaster<O,T>{}

static interface H<T>{
HType<T> type();
T get();
//<S> H<S> upcast(Class<S super T> type); doesn't work
<S> S upcast(Upcaster<? super T,S> upcaster);
}

static interface L<T> extends H<T>{
@Override
LType<T> type();
}

@SuppressWarnings("unused")
static void test(){
  final Type<A> typeA = type(A.class);
}

static <T> Type<T> type(final Class<T> class1){
  return null;
}

static <T> Type<H<T>> htype(final Type<T> elementType){
  return null;
}

static <T> T create(){
  return null;
}

}
