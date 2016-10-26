package com.github.gv2011.experiments.circle2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class PositionTest {

	@Test
	public void testDistanceTo() {
		assertThat(position(1).distanceTo(position(2)), is(distance(1)));
		assertThat(position(1).distanceTo(position(0)), is(distance(-1)));

		assertThat(new Position(Position.MAX).distanceTo(position(0)), is(distance(1)));
		assertThat(position(0).distanceTo(new Position(Position.MAX)), is(distance(-1)));

		assertThat(
			new Position(Position.MAX_RIGHT_DISTANCE).distanceTo(position(0)), 
			is(new Distance(Position.MAX_RIGHT_DISTANCE))
		);
	}

	private Distance distance(long i) {
		return new Distance(BigInteger.valueOf(i));
	}

	private Position position(long i) {
		return new Position(BigInteger.valueOf(i));
	}

}
