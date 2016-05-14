package com.github.gv2011.experiments.circle;

import java.math.BigInteger;
import java.util.UUID;

public class Circle {

public static enum Direction{LEFT,SHORTEST,RIGHT}

private static BigInteger LONG_OFFSET = BigInteger.ONE.shiftLeft(64);

public static void main(final String[] args) {
  Circle last = fromUUID(UUID.randomUUID());
  System.out.println(last.angle());
  for(int i=0; i<25; i++){
    final Circle c = fromUUID(UUID.randomUUID());
    System.out.println(c.angle()+"    "+c.angle(c.distance(last)));
    last = c;
  }
}


public double angle(final BigInteger distance) {
  if(distance.abs().compareTo(half)>0) throw new IllegalArgumentException(distance.toString(16));
  return distance.doubleValue()/size.doubleValue()*360D;
}


private final BigInteger size;
private final BigInteger half;

private final BigInteger value;



public Circle(final int bits, final BigInteger value){
  if(bits<2) throw new IllegalArgumentException();
  this.size = BigInteger.ONE.shiftLeft(bits);
  this.half = size.shiftRight(1);
  this.value = value;
  assert inRange(value);
}

public Circle(final int bits, final long value){
  this(bits, BigInteger.valueOf(value));
}

private Circle(final BigInteger size, final BigInteger half, final BigInteger value) {
  this.size = size;
  this.half = half;
  this.value = value;
  assert inRange(value);
}

public BigInteger subtract(final Circle other) {
  check(other);
  return distance(other.toInt(), Direction.SHORTEST);
}

private void check(final Circle other) {
  if(!other.size.equals(size)) throw new IllegalArgumentException();
}

public BigInteger distance(final Circle other) {
  return distance(other.toInt(), Direction.SHORTEST);
}

public BigInteger distance(final Circle other, final Direction dir) {
  check(other);
  final BigInteger distance = distance(other.toInt(), dir);
  assert this.add(distance).equals(other) : this+" dist "+other+" : "+distance.toString(16)+ " : "+other.add(distance);
  return distance;
}

private BigInteger distance(final BigInteger i2, final Direction dir) {
  assert inRange(i2);
  BigInteger dist1 = i2.subtract(value);
  if(dir==Direction.RIGHT){
    if(dist1.signum()==-1) dist1 = dist1.add(size);
  }
  else if(dir==Direction.LEFT){
    if(dist1.signum()==1) dist1 = dist1.subtract(size);
  }
  else if(dir==Direction.SHORTEST){
    if(dist1.abs().compareTo(half)>0){
      if(value.compareTo(i2)>0){//i2 is smaller
        dist1 = i2.add(size).subtract(value);
      }else{
        assert i2.compareTo(value)>0; //this is smaller
        dist1 = i2.subtract(value.add(size));
      }
    }
  }
  else throw new IllegalArgumentException();
  return dist1;
}

public Circle add(BigInteger i){
  if(i.equals(BigInteger.ZERO)) return this;
  else{
    while(i.signum()==-1) i = i.add(size);
    return new Circle(size, half, value.add(i).remainder(size));
  }
}

public double angle() {
  return value.doubleValue()/size.doubleValue()*360D;
}

public BigInteger toInt() {
  return value;
}

public boolean inRange(final BigInteger i) {
  return i.signum()!=-1 && size.subtract(i).signum()==1;
}

public static BigInteger toUnsigned(final long l) {
  return l>=0 ? BigInteger.valueOf(l) : BigInteger.valueOf(l).add(LONG_OFFSET);
}

public static Circle fromUUID(final UUID uuid) {
  BigInteger result = toUnsigned(uuid.getMostSignificantBits());
  result = result.shiftLeft(64);
  result = result.or(toUnsigned(uuid.getLeastSignificantBits()));
  return new Circle(128, result);
}


@Override
public int hashCode() {
  return value.hashCode();
}


@Override
public boolean equals(final Object obj) {
  if (this == obj) { return true; }
  else if (obj == null) { return false; }
  else if (!(obj instanceof Circle)) { return false; }
  else{
    final Circle other = (Circle) obj;
    return other.size.equals(size) && other.value.equals(value);
  }
}


@Override
public String toString() {
  return value.toString(16);
}


public int bits() {
  return size.bitLength()-1;
}


}
