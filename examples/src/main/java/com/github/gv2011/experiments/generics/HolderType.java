package com.github.gv2011.experiments.generics;

public interface HolderType<E> extends GType<Holder<E>>{

GType<E> elementType();

}
