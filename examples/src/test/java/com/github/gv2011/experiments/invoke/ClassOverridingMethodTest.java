package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.testutil.Matchers.is;
import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;
import static org.junit.Assert.*;

import org.junit.Test;

public class ClassOverridingMethodTest {

  private final ClassOverridingMethod obj = new ClassOverridingMethod();

  @Test
  public void testHello() {
    assertThat(obj.hello(), is("Hello from class."));

    final InterfaceWithDefaultMethod anonymous = new InterfaceWithDefaultMethod(){
      @Override
      public String superHello() {throw notYetImplementedException();}
      @Override
      public String automaticHello() {throw notYetImplementedException();}
    };
    assertThat(anonymous.hello(), is("Hello from interface."));
  }

  @Test
  public void testSuperHello() {
    assertThat(obj.superHello(), is("Hello from interface."));
  }

}
