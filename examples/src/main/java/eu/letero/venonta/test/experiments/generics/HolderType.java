package eu.letero.venonta.test.experiments.generics;

public interface HolderType<E> extends GType<Holder<E>>{

GType<E> elementType();

}
