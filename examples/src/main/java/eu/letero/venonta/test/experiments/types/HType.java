package eu.letero.venonta.test.experiments.types;

import eu.letero.venonta.test.experiments.types.Types.H;

public class HType<E> extends AbstractType<H<E>>{

  private final Type<E> elementType;

  HType(final Type<E> elementType) {
    this.elementType = elementType;
  }

  public Type<E> elementType(){
    return elementType;
  }

  @Override
  public boolean isInstance(final Object obj) {
    if(!(obj instanceof H)) return false;
    else{
      final H<?> h = (H<?>) obj;
      return elementType.isInstance(h.get());
    }
  }

  @Override
  public
  H<E> cast(final Object obj) {
    return null;
  }

}
