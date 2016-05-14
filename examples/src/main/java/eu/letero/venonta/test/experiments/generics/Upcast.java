package eu.letero.venonta.test.experiments.generics;

public interface Upcast<T> {

static interface Type<T>{
}

static interface HType<E> extends Type<H<E>>{
Upcaster<E,? extends H<E>> upcaster();
}

static interface LType<E> extends HType<E>{
@Override
Upcaster<E,L<E>> upcaster();
}

static interface A{}
static interface B extends A{}
static interface Upcaster<H,T>{}



static interface HA{
A get();
}

static interface HB extends HA{
@Override
B get();
}

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
static void la(){
  {
    HA ha = create();
    final HB hb = create();
    ha = hb;
  }
  {
    H<A> ha = create();
    final H<B> hb = create();
//    ha = (H)hb; //doesn't work
    ha = hb.upcast(ha.type().upcaster());

    final L<A> la = create();
    final L<B> lb = create();
    ha = la;
//    ha = lb;
    ha = lb.upcast(ha.type().upcaster());
  }
  {
    final Type<A> typeA = type(A.class);
  }
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
