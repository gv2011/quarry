package com.github.gv2011.experiments.protection.p1;

import static com.github.gv2011.util.Verify.notNull;

import com.github.gv2011.experiments.protection.p2.Child2;

public class Parent {
  
  public static final Child1 newChild1() {
    return new Child1(new Secret());
  }
  
  public static final Child2 newChild2() {
    return new Child2(new Secret());
  }
  
  private final Secret secret;
  
  protected Parent(Secret secret) {
    this.secret = notNull(secret);
  }

  protected Secret secret() {
    return secret;
  }

}
