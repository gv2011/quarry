package com.github.gv2011.experiments.circle2;

import java.math.BigInteger;
import java.util.UUID;

import com.github.gv2011.experiments.circle2.Distance.Direction;
import com.github.gv2011.util.IntUtils;

public class Position {
	
	private static final int BYTES = 16;
	static final int BITS = BYTES*8;
	
	static final  BigInteger CIRCLE_SIZE = BigInteger.ONE.shiftLeft(BITS);
	static final  BigInteger MIN = BigInteger.ZERO;
	static final  BigInteger MAX = CIRCLE_SIZE.subtract(BigInteger.ONE);
	static final  BigInteger MAX_RIGHT_DISTANCE = CIRCLE_SIZE.shiftRight(1);
	static final  BigInteger MIN_LEFT_DISTANCE = CIRCLE_SIZE.shiftRight(1).subtract(BigInteger.ONE).negate();
	
	final BigInteger p;

	public Position() {
		this(UUID.randomUUID());
	}

	Position(UUID uuid) {
		this(IntUtils.toInt(uuid));
	}

	Position(BigInteger i) {
		p = i;
		assert p.compareTo(MAX)<=0 && p.compareTo(MIN)>=0;
	}

	@Override
	public String toString() {
		return p.toString();
	}

	public Distance distanceTo(Position other){
		BigInteger d;
		BigInteger right = dist(other, Direction.RIGHT);
		BigInteger left = dist(other, Direction.LEFT);
		//Select shorter distance
		if(right.compareTo(left.negate())<=0) d = right;
		else d = left;
		assert d.compareTo(MAX_RIGHT_DISTANCE)<=0 && d.compareTo(MIN_LEFT_DISTANCE)>=0;
		return new Distance(d);
	}
	
	public Distance distanceTo(Position other, Direction direction){
		return new Distance(dist(other, direction));
	}
	
	BigInteger dist(Position other, Direction direction){
		BigInteger dist = other.p.subtract(p);
		if(dist.equals(BigInteger.ZERO));
		else if(direction == Direction.RIGHT){
			if(dist.signum()==-1) dist = dist.add(CIRCLE_SIZE);
			assert(dist.signum()==1);
		}
		else{
			assert direction == Direction.LEFT;
			if(dist.signum()==1) dist = dist.subtract(CIRCLE_SIZE);
			assert(dist.signum()==-1);
		}
		return dist;
	}
	
	public Position at(Distance d){
		BigInteger pos = p.add(d.d);
		if(pos.compareTo(MAX)>0) pos = pos.subtract(CIRCLE_SIZE);
		else if(pos.compareTo(MIN)<0) pos = pos.add(CIRCLE_SIZE);
		return new Position(pos);
	}
	
	@Override
	public int hashCode() {
		return p.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position)) return false;
		return p.equals(((Position)obj).p);
	}

}
