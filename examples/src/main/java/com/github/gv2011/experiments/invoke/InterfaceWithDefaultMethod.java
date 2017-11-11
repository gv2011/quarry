package com.github.gv2011.experiments.invoke;

public interface InterfaceWithDefaultMethod {

  default String hello(){
    return "Hello from interface.";
  }

  String superHello();

  /**
   * This method should be implemented by a proxy.
   */
  String automaticHello();
}
