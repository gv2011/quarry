package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.util.Verify.notNull;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

public class MethodLoader {

  public static <T> Method method(final Class<T> clazz, final Function<T,?> method){
    final Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    final AtomicReference<Method> mRef = new AtomicReference<>();
    enhancer.setCallback((InvocationHandler) (proxy, m, args) -> {
      mRef.set(m);
      return null;
    });
//    enhancer.setCallback((MethodInterceptor) (obj, method1, args, proxy) -> {
//      mRef.set(method1);
//      return null;
//    });
    final T proxy = clazz.cast(enhancer.create());
    method.apply(proxy);
    return notNull(mRef.get());
  }
}
