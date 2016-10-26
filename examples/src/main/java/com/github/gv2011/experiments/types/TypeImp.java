package com.github.gv2011.experiments.types;

public class TypeImp<T> extends AbstractType<T>{

private final Class<T> simple;

  TypeImp(final Class<T> simple) {
  this.simple = simple;
}

  @Override
  public boolean isInstance(final Object obj) {
    return simple.isInstance(obj);
  }

}
