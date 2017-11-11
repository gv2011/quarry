package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.testutil.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class ImplementationByProxyTest {

  private final InterfaceWithDefaultMethod proxy =
    ImplementationByProxy.createProxy(InterfaceWithDefaultMethod.class)
  ;

  @Test
  public void testHello() {
    assertThat(proxy.hello(), is("Hello from proxy."));
  }

  @Test
  public void testSuperHello() {
    assertThat(proxy.superHello(), is("Hello from interface."));
  }

}
