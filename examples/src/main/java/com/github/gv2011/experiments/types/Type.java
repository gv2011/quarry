package com.github.gv2011.experiments.types;

public interface Type<T> {

boolean isInstance(Object obj);

T cast(Object obj);

}
