package com.github.gv2011.examples;

import java.math.BigInteger;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class Ints {

	@Test
	public void testLong(){
		Assert.assertThat(BigInteger.valueOf(Long.MAX_VALUE), 
				CoreMatchers.is(BigInteger.valueOf(2).pow(63).subtract(BigInteger.ONE)));
	}
}
