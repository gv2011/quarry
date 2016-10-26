package com.github.gv2011.experiments.circle2;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import org.junit.Test;

public class DistanceTest {

	@Test
	public void testCompareTo() {
		Distance d0 = new Distance(BigInteger.valueOf(0));
		Distance dp1 = new Distance(BigInteger.valueOf(1));
		Distance dm1 = new Distance(BigInteger.valueOf(-1));
		Distance dp2 = new Distance(BigInteger.valueOf(2));
		
		assertThat(dp1, comparesEqualTo(dp1));
		
		assertThat(dp2, greaterThan(d0));
		assertThat(dp2, greaterThan(dp1));
		assertThat(dp2, greaterThan(dm1));
		
		assertThat(dp1, lessThan(dm1));
		assertThat(dm1, greaterThan(dp1));
	}

}
