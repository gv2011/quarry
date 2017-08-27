package com.github.gv2011.experiments.protection.outside;

import com.github.gv2011.experiments.protection.p1.Parent;
import com.github.gv2011.experiments.protection.p1.Secret;

public class Spy extends Parent{
  
  public Spy(Secret secret) {
    //Throws exception:
    super(null);
  }

  public static Secret publish(Parent victim) {
    //Not possible:
    //  return victim.secret();
    return null;
  }

  //Not possible, because no Spy instance can be created:
  public Secret publish() {
    return secret();
  }

}
