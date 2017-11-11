package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.util.Verify.verify;
import static com.github.gv2011.util.ex.Exceptions.call;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.function.Function;

import com.github.gv2011.util.ReflectionUtils;

public class DefaultMethodAccess {

  public static <I> MethodHandle getDefaultMethodHandle(
    final Lookup lookup, final Class<I> interfaze, final Function<I,?> methodLambda
  ){
    final Method m = ReflectionUtils.attributeMethod(interfaze, methodLambda);
    return getDefaultMethodHandle(lookup, interfaze, m);
  }

  public static <I> MethodHandle getDefaultMethodHandle(
    final Lookup lookup, final Class<I> interfaze, final Method method
  ){
    verify(method.getParameterCount()==0);
    final MethodType mt = MethodType.methodType(method.getReturnType());
    return call(()->lookup.findSpecial(interfaze, method.getName(), mt, lookup.lookupClass()));
  }

  public static <I,R> Function<I,R> getDefaultInvoker(
    final Lookup lookup, final Class<I> interfaze, final Function<I,R> methodLambda
  ){
    final MethodHandle mh = getDefaultMethodHandle(lookup, interfaze, methodLambda);
    return createInvoker(mh);
  }

  public static <I,R> Function<I,R> getDefaultInvoker(
    final Lookup lookup, final Class<I> interfaze, final Method method
  ){
    final MethodHandle mh = getDefaultMethodHandle(lookup, interfaze, method);
    return createInvoker(mh);
  }

  private static <I, R> Function<I, R> createInvoker(final MethodHandle mh) {
    return (o)->{
      try {return (R) mh.invoke(o);}
      catch (final Throwable e) {throw new RuntimeException(e);}
    };
  }


}
