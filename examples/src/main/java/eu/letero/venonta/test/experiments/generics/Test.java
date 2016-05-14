package eu.letero.venonta.test.experiments.generics;

import static com.github.gv2011.util.ex.Exceptions.bug;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import com.github.gv2011.util.ReflectionUtils;



public class Test {


@SuppressWarnings("rawtypes")
@org.junit.Test
public void test() {
  final BeanType<Bean1> beanType = getBeanType(Bean1.class);
  final GType<Holder<Holder<String>>> t = getAttributeType(beanType , Bean1::att1);
  assertThat(((HolderType)((HolderType)t).elementType()).elementType().clazz()==String.class, is(true));
}

public static <B extends Bean<B>> BeanType<B> getBeanType(final Class<B> beanClass){
  return ()->beanClass;
}

public static <T> SimpleType<T> getSimpleType(final Class<T> simpleClass){
  return new SimpleType<T>(){
    @Override
    public Class<T> clazz() {
      return simpleClass;
    }
    @Override
    public String toString() {
      return simpleClass.getSimpleName();
    }
  };
}

public static <E> HolderType<E> getHolderType(final GType<E> elementType){
  return new HolderType<E>(){
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<Holder<E>> clazz() {
      return (Class) Holder.class;
    }

    @Override
    public GType<E> elementType() {
      return elementType;
    }

    @Override
    public String toString() {
      return "Holder<"+elementType+">";
    }
  };
}

@SuppressWarnings({ "unchecked", "rawtypes", "cast" })
private static <V> GType<V> getType(final Type type){
  if(type instanceof Class){
    final Class<?> rawClass = (Class<?>) type;
    if(!Bean.class.equals(rawClass) && Bean.class.isAssignableFrom(rawClass))
      return (GType<V>) getBeanType((Class)rawClass);
    else if(!Bean.class.isAssignableFrom(rawClass) && !Holder.class.isAssignableFrom(rawClass))
      return getSimpleType((Class)rawClass);
    else throw new IllegalArgumentException();
  }
  if(type instanceof ParameterizedType){
    final ParameterizedType pType = (ParameterizedType) type;
    if(!Holder.class.equals(pType.getRawType())) throw new IllegalArgumentException();
    final Type[] typeArgs = pType.getActualTypeArguments();
    if(typeArgs.length!=1) throw bug();
    return (GType<V>) getHolderType(getType(typeArgs[0]));
  }
  else throw new IllegalArgumentException();
}


private <B extends Bean<B>,T> GType<T> getAttributeType(final BeanType<B> beanType, final Function<B,T> method) {
  final Method m = ReflectionUtils.method(beanType.clazz(), method);
  return getType(m.getGenericReturnType());

}

}
