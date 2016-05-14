package com.github.gv2011.experiments.circle;

import static com.github.gv2011.util.ex.Exceptions.bug;

import java.math.BigInteger;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gv2011.experiments.circle.Circle.Direction;

public class Node {

private static final Logger LOG = LoggerFactory.getLogger(Node.class);

private final Circle id;
private Node right;
private final String name;
private volatile boolean closed;
final SortedMap<BigInteger,Optional<Node>> shortCuts = new TreeMap<>();

public Node() {
  this(UUID.randomUUID());
}

public Node(final UUID uuid) {
  this(uuid.toString(), uuid);
}

public Node(final String name) {
  this(name, UUID.randomUUID());
}

public Node(final String name, final UUID uuid) {
  this(name, Circle.fromUUID(uuid));
}

public Node(final String name, final Circle id) {
  this.name = name;
  this.id = id;
  right = this;
  final int maxBits = Math.min(id.bits()-1, 32);
  for(int i=0; i<maxBits; i++){
    final BigInteger offset = BigInteger.ONE.shiftLeft(id.bits()-1-i);
    shortCuts.put(offset, Optional.empty());
  }
  LOG.trace(shortCuts.keySet().stream().map((i)->i.toString(16)).collect(Collectors.joining("\n")));
}

public Node(final Circle id) {
  this(id.toString(), id);
}

public Circle id(){
  return id;
}

Node right(){
  if(closed) throw new ClosedException();
  return right;
}

public BigInteger distance(final Node other){
  final BigInteger distance = id.distance(other.id(), Direction.RIGHT);
  assert (distance.signum()==0) == equals(other);
  return distance;
}

public void add(final Node node){
  final AtomicInteger recursions = new AtomicInteger();
  add(node, recursions);
  LOG.info("{} reursions.", recursions.get());
}

public void add(final Node n2, final AtomicInteger recursions){
  if(closed) throw new ClosedException();
  final boolean added = findLeft(n2, recursions).addNeighbour(n2);
  assert added;
}

public boolean addNeighbour(final Node n2) {
  if(closed) throw new ClosedException();
  if(n2.equals(this)) throw new IllegalArgumentException();
  else{
    boolean result;
    final BigInteger distance = distance(n2);
    if(distance.signum()!=1) throw bug();
    final boolean isCloser = distance.compareTo(distance(right))<0 || right.equals(this);
    if(isCloser){
      final Node before = right;
      right = n2;
      LOG.trace("{}: right set to {}.", this, n2);
      n2.addNeighbour(before);
      result = true;
    }
    else result = false;
    return result;
  }
}

private Node findLeftInternal(final Node node, final AtomicInteger recursions) {
  Node result = null;
  Node candidate = right;
  final BigInteger target = distance(node);
  BigInteger candidateDistance = distance(candidate);
  for(final Entry<BigInteger, Optional<Node>> e: shortCuts.entrySet()){
    if(e.getValue().isPresent()){
      final Node cached = e.getValue().get();
      final BigInteger cachedDistance = distance(cached);
      if(cachedDistance.compareTo(target)<0){
        if(cachedDistance.compareTo(candidateDistance)>0) {
          candidate=cached;
          candidateDistance = cachedDistance;
        }
      }
    }
  }
  assert !candidate.equals(node);
  assert !equals(candidate);
  assert distance(candidate).compareTo(distance(right))>=0;
  result = candidate.findLeft(node, recursions);
  updateShortCuts(result);
  return result;
}


public Node findLeft(final Node node) {
  return findLeft(node, new AtomicInteger());
}

public Node findLeft(final Node n2, final AtomicInteger recursions) {
  if(closed) throw new ClosedException();
  recursions.incrementAndGet();
  Node result;
  if(right.equals(n2)){
    result = this;
  }else{
    final BigInteger thisDistance = distance(n2);
    final boolean rightCloser;
    if(thisDistance.equals(BigInteger.ZERO)){
      assert this.equals(n2);
      if(right.equals(this)) throw new NoSuchElementException();
      rightCloser = true;
    }else{
      final BigInteger rightDistance = right.distance(n2);
      rightCloser = rightDistance.compareTo(thisDistance)<0;
    }
    result = rightCloser ? findLeftInternal(n2, recursions) : this;
  }
  return result;
}

private void updateShortCuts(final Node node) {
  final BigInteger distance = distance(node);
  for(final Entry<BigInteger, Optional<Node>> e: new TreeMap<>(shortCuts).entrySet()){
    final BigInteger key = e.getKey();
    if(distance.compareTo(key)<0){
      if(!e.getValue().isPresent()){
        assert distance.compareTo(key)<0;
        shortCuts.put(key, Optional.of(node));
      }else{
        final Node old = e.getValue().get();
        final BigInteger distanceOld = distance(old);
        assert distanceOld.compareTo(key)<0;
        if(distance.compareTo(distanceOld)>0) {
          assert distance.compareTo(key)<0;
          shortCuts.put(key, Optional.of(node));
        }
      }
    }
  }

}

public void remove(){
  if(equals(right)) throw new IllegalStateException();
  findLeft(this).setNeighbour(right);
  right = this;
  closed = true;
}

public void setNeighbour(final Node right) {
  if(closed) throw new ClosedException();
  this.right = right;
}

@Override
public String toString() {
  return name;
}



}
