package com.github.gv2011.quarry.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.gv2011.quarry.bits.BinMath;

public class BinMathTest {

	@Test
	public void testTwoToThePowerOf() {
		for(int i=0; i<=62; i++){
			assertThat(BinMath.twoToThePowerOf(i), is((long)Math.pow(2, i)));
		}
	}

}
