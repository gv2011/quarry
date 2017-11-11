package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.util.ex.Exceptions.call;
import static com.github.gv2011.util.ex.Exceptions.notYetImplemented;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ImplementationByProxy{

  private static final Lookup PRIV_LOOKUP = call(()->getPrivLookup());

  private static Lookup getPrivLookup() throws Exception {
    final Field IMPL_LOOKUP = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
    IMPL_LOOKUP.setAccessible(true);
    return (MethodHandles.Lookup) IMPL_LOOKUP.get(null);
  }

  public static <I> I createProxy(final Class<I> ifClass){
    return ifClass.cast(
      Proxy.newProxyInstance(
        ifClass.getClassLoader(),
        new Class<?>[]{ifClass},
        new IH<>(ifClass)
      )
    );
  }


//  private static final Function<InterfaceWithDefaultMethod, String> superHelloInvoker =
//    DefaultMethodAccess.getDefaultInvoker(
//      MethodHandles.lookup(),
//      InterfaceWithDefaultMethod.class,
//      InterfaceWithDefaultMethod::hello
//    )
//  ;

  private static final class IH<I> implements InvocationHandler{

    private final Class<I> ifClass;
    private Supplier<String> superInvoker;

    private IH(final Class<I> ifClass) {
      this.ifClass = ifClass;
    }

    @Override
    public final Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
      Object result;
      if(method.getParameterCount()==0){
        final String name = method.getName();
        if(name.equals("hello")) result = "Hello from proxy.";
        else if(name.equals("superHello")) result = getInvoker(proxy, "hello").get();
        else result = notYetImplemented();
      }
      else result = notYetImplemented();
      return result;
    }

    private Supplier<String> getInvoker(final Object proxy, final String superMethodName) {
      if(superInvoker==null){
        final Lookup lookup = ProxyLookup.inProxyInterface(MethodHandles.lookup(), proxy, ifClass);
        assert ((lookup.lookupModes() & Lookup.PRIVATE) != 0);
        final I p = ifClass.cast(proxy);
        final Method superMethod = call(()->InterfaceWithDefaultMethod.class.getMethod(superMethodName));
        final Function<I, Object> invokerFunction = DefaultMethodAccess.getDefaultInvoker(
          lookup,
          ifClass,
          superMethod
        );
        superInvoker = ()->(String) invokerFunction.apply(p);;
      }
      return superInvoker;
    }

  }
}
