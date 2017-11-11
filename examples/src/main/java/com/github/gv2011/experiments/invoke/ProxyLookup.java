package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.util.ex.Exceptions.call;
import static com.github.gv2011.util.ex.Exceptions.format;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class ProxyLookup {

  private static final Lookup PRIV_LOOKUP = call(()->getPrivLookup());

  private static final Lookup getPrivLookup() throws Exception {
    final Field IMPL_LOOKUP = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
    IMPL_LOOKUP.setAccessible(true);
    return (MethodHandles.Lookup) IMPL_LOOKUP.get(null);
  }


  public static final Lookup inProxyInterface(
    final Lookup invocationHandlerLookup, final Object proxy, final Class<?> interfaze
  ){
    final Class<?> ihClass = invocationHandlerLookup.lookupClass();
    final Class<? extends InvocationHandler> proxyIhClass = Proxy.getInvocationHandler(proxy).getClass();
    if(ihClass!=proxyIhClass){
      throw new IllegalArgumentException(format("{}!={}",ihClass, proxyIhClass));
    }
    if(!interfaze.isInstance(proxy)){
      throw new IllegalArgumentException(format("{} does not implement {}", proxy, interfaze));
    }
    return PRIV_LOOKUP.in(interfaze);
  }

}
