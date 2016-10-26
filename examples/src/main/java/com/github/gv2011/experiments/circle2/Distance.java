package com.github.gv2011.experiments.circle2;

import java.math.BigInteger;

public class Distance implements Comparable<Distance>{
	
	public static enum Direction{LEFT, RIGHT};
	
	final BigInteger d;

	Distance(BigInteger d) {
		if(d.abs().compareTo(Position.MAX)>0) throw new IllegalArgumentException();
		this.d = d;
	}

	@Override
	public int compareTo(Distance o) {
		assert o!=null;
		if(d.equals(o.d)) return 0;
		else if(d.abs().equals(o.d.abs())) return -d.signum();
		else return d.abs().subtract(o.d.abs()).signum();
	}
	
	public Direction direction(){
		return d.signum()==-1?Direction.LEFT:Direction.RIGHT;
	}

	@Override
	public int hashCode() {
		return d.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Distance)) return false;
		return d.equals(((Distance)obj).d);
	}

	@Override
	public String toString() {
//		return String.format("%+20f", d.doubleValue());
		return d.toString(16);
	}

	public Distance negate() {
		return new Distance(d.negate());
	}


}
