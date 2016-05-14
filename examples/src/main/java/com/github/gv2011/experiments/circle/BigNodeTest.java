package com.github.gv2011.experiments.circle;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class BigNodeTest {

@Test
public void test() {
  final Node n1 = new Node();
  for(int i=0; i<100000; i++) n1.add(new Node());
  final List<Node> nodes = new ArrayList<>();
  for(int i=0; i<100; i++) nodes.add(new Node());
  for(int i=0; i<5; i++)
  {
    final AtomicInteger recursions = new AtomicInteger();
    final Instant start = Instant.now();
    for(final Node n: nodes) {
      n1.findLeft(n, recursions);
    }
    final Instant end = Instant.now();
    System.out.println(((float)Duration.between(start, end).toMillis())/100F);
    System.out.println(recursions.get()/100);
  }
  {
    final AtomicInteger recursions = new AtomicInteger();
    final Instant start = Instant.now();
    for(final Node n: nodes) {
      n1.add(n, recursions);
    }
    final Instant end = Instant.now();
    System.out.println(((float)Duration.between(start, end).toMillis())/100F);
    System.out.println(recursions.get()/100);
  }
}

}
