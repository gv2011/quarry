package com.github.gv2011.experiments.circle;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class NodeTest {

@Test
public void test() {
//  final Node n1 = new Node("n1", UUID.fromString("4b35c9fc-223b-4962-a71b-f500c6e9b173"));
//  final Node n2 = new Node("n2", UUID.fromString("fdfc4e36-ece3-48c0-a0ff-ab108c4c4d9b"));
  final Node n1 = new Node("n10", new Circle(8, 10));
  final Node n2 = new Node("n50", new Circle(8, 50));
  n1.add(n2);
  assertThat(n1.right(), is(n2));
  assertThat(n2.right(), is(n1));

//  final Node n3 = new Node("n3", UUID.fromString("02745ab0-9d05-4b94-848b-22b04f0130a6"));
  final Node n3 = new Node("n100", new Circle(8, 100));

  assertThat(n1.findLeft(n3), is(n2));
  assertThat(n2.findLeft(n3), is(n2));

  n1.add(n3);

  assertThat(n1.right(), is(n2));
  assertThat(n2.right(), is(n3));
  assertThat(n3.right(), is(n1));


//  final Node n4 = new Node("n4", UUID.fromString("dfd3248f-8e9f-429a-b288-520bfd714ec5"));
  final Node n4 = new Node("n20", new Circle(8, 20));
  n3.add(n4);

  System.out.println(n1.id().angle());
  System.out.println(n2.id().angle());
  System.out.println(n3.id().angle());
  System.out.println(n4.id().angle());

  assertThat(n1.right(), is(n4));
  assertThat(n2.right(), is(n3));
  assertThat(n3.right(), is(n1));
  assertThat(n4.right(), is(n2));

  assertThat(n1.findLeft(n1), is(n3));
  assertThat(n1.findLeft(n2), is(n4));
  assertThat(n1.findLeft(n3), is(n2));
  assertThat(n1.findLeft(n4), is(n1));

  assertThat(n2.findLeft(n1), is(n3));
  assertThat(n3.findLeft(n1), is(n3));
  assertThat(n4.findLeft(n1), is(n3));

  n1.remove();
  assertThat(n2.right(), is(n3));
  assertThat(n3.right(), is(n4));
  assertThat(n4.right(), is(n2));

}

@Test
public void testShortCuts() {
  final List<BigInteger> keys = new ArrayList<>(new Node(new Circle(8,255)).shortCuts.keySet());
  assertThat(keys.toString(), is("[2, 4, 8, 16, 32, 64, 128]"));
}

}
