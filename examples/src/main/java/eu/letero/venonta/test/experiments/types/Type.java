package eu.letero.venonta.test.experiments.types;

public interface Type<T> {

boolean isInstance(Object obj);

T cast(Object obj);

}
