package com.github.gv2011.experiments.invoke;

import static com.github.gv2011.testutil.Matchers.is;
import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

public class MethodLoaderTest {

  //Does not work:
  //public static final class
  //private static class
  //public class

  public static abstract class Foo{
    //private String
    //final String
    public String hello(){return "world";}
    public abstract String bye();
  }

  @Test
  public void test() throws Exception {
    final Method m = MethodLoader.method(Foo.class, Foo::hello);
    assertThat(m.getName(), is("hello"));
    assertThat(
      m.invoke(new Foo(){
        @Override
        public String bye() {throw notYetImplementedException();}
      }),
      is("world")
    );
    final Method m2 = MethodLoader.method(Foo.class, Foo::bye);
    assertThat(m2.getName(), is("bye"));
  }

}
