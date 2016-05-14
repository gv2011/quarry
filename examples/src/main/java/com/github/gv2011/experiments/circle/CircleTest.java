package com.github.gv2011.experiments.circle;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import org.junit.Test;

public class CircleTest {

@Test
public void test() {
  final Circle v14 = new Circle(4,BigInteger.valueOf(14));
  final Circle v15 = new Circle(4,BigInteger.valueOf(15));
  final Circle v0 = new Circle(4,BigInteger.valueOf(0));
  final Circle v1 = new Circle(4,BigInteger.valueOf(1));

  assertThat(v15.add(BigInteger.valueOf(-1)), is(v14));
  assertThat(v0.add(BigInteger.valueOf(-1)), is(v15));
  assertThat(v14.add(BigInteger.valueOf(1)), is(v15));
  assertThat(v15.add(BigInteger.valueOf(1)), is(v0));

  assertThat(v15.distance(v14), is(BigInteger.valueOf(-1)));
  assertThat(v0.distance(v15), is(BigInteger.valueOf(-1)));
  assertThat(v0.distance(v14), is(BigInteger.valueOf(-2)));
  assertThat(v14.distance(v14), is(BigInteger.valueOf(0)));
  assertThat(v15.distance(v1), is(BigInteger.valueOf(2)));

  assertThat(v14.distance(v15), is(BigInteger.valueOf(1)));
  assertThat(v15.distance(v0), is(BigInteger.valueOf(1)));
  assertThat(v14.distance(v0), is(BigInteger.valueOf(2)));

}

public void testBits() {
  assertThat(new Circle(4,1).bits(), is(4));
}

}
