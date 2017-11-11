package com.github.gv2011.experiments.invoke;

import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public class ClassOverridingMethod implements InterfaceWithDefaultMethod{

  private static final Function<InterfaceWithDefaultMethod, String> superHelloInvoker =
    DefaultMethodAccess.getDefaultInvoker(
      MethodHandles.lookup(),
      InterfaceWithDefaultMethod.class,
      InterfaceWithDefaultMethod::hello
    )
  ;

  @Override
  public String hello() {
    return "Hello from class.";
  }

  @Override
  public String superHello() {
    return superHelloInvoker.apply(this);
  }

  @Override
  public String automaticHello() {
    throw new UnsupportedOperationException("This method should be implemented by a proxy.");
  }

}
