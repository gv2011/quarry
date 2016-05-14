package eu.letero.venonta.test.experiments.types;

abstract class AbstractType<T> implements Type<T>{

@SuppressWarnings("unchecked")
@Override
public T cast(final Object obj) {
  if(!isInstance(obj)) throw new ClassCastException();
  else return (T) obj;
  }

}
