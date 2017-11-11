package com.github.gv2011.experiments.sec;

import java.lang.reflect.Field;

public class TestSetAccesibleAndSecurityManager {

  public static void main(final String[] args) throws Exception {
    final Field f1 = SecretHolder.class.getDeclaredField("SECRET1");
    f1.setAccessible(true);
    System.out.println(f1.get(null));

    System.setSecurityManager(new SecurityManager());
    System.out.println(f1.get(null));
    final Field f2 = SecretHolder.class.getDeclaredField("SECRET2");
    f2.setAccessible(true);
    System.out.println(f2.get(null));
  }

}
