package com.github.gv2011.experiments.types;

import com.github.gv2011.experiments.types.Types.L;

public class LType<E> extends HType<E>{

  LType(final Type<E> elementType) {
    super(elementType);
  }

  @Override
  public boolean isInstance(final Object obj) {
    if(!(obj instanceof L)) return false;
    else{
      final L<?> h = (L<?>) obj;
      return elementType().isInstance(h.get());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public L<E> cast(final Object obj) {
    if(!isInstance(obj)) throw new ClassCastException();
    else return (L<E>)obj;
  }


}
