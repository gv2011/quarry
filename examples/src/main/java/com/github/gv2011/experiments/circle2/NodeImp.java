package com.github.gv2011.experiments.circle2;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gv2011.experiments.circle2.Distance.Direction;

public class NodeImp implements NodeCn, NodeCtrl{
	
	private static final Logger LOG = LoggerFactory.getLogger(NodeImp.class);

	private final Position pos;
	
	final Cache cache;

	private boolean connected = false;
	
	NodeImp(Position position, Factory factory) {
		this.pos = position;
		cache = factory.createCache(this);
	}
	
	private Optional<NodeCn> left = Optional.empty();
	private Optional<NodeCn> right = Optional.empty();
	
	@Override
	public Position position(){
		return pos;
	}
	
	
	
	@Override
	public void setStandalone() {
		if(connected) throw new IllegalStateException();
		connected = true;
	}

	@Override
	public String toString() {
		return pos.p.toString(16);
	}

	public Optional<NodeCn> getLeft() {
		return left;
	}
	public Optional<NodeCn> getRight() {
		return right;
	}
	
	public boolean isResponsible(Position p){
		return connected && getClosestNeighbour(p).get()==this;
	}

	@Override
	public NodeCn getResponsible(Position p){
		if(!connected) throw new NotReachableException(this);
		return getResponsible(p, new AtomicInteger());
	}
	
	@Override
	public NodeCn getResponsible(Position p, AtomicInteger counter){
		if(!connected) throw new NotReachableException(this);
		counter.incrementAndGet();
		Closest closest = getClosestNeighbour(p);
		NodeCn result = null;
		if(closest.get()==this) result = this;
		else {
			while(result==null){
				cache.updateClosest(closest);
				NodeCn node = closest.get();
				try {
					result = (NodeCn) node.getResponsible(p, counter);
				} catch (NotReachableException e) {
					NodeCn n = e.getNode();
					closest.remove(n);
					cache.remove(n);
				}
			}
			cache.cache(Optional.of(result));
		}
		return result;
	}

	private Closest getClosestNeighbour(Position p) {
		Closest closest = new Closest(p);
		closest.update(Optional.of(this));
		closest.update(left);
		closest.update(right);
		return closest;
	}
	
	static Comparator<NodeImp> comparator(Position p) {
		return new Comparator<NodeImp>(){
			@Override
			public int compare(NodeImp n1, NodeImp n2) {
				return n1.position().distanceTo(p).compareTo(n2.position().distanceTo(p));
			}
		};
	}

	@Override
	public void leave(){
		this.connected = false;
		Optional<NodeCn> left = getLeft();
		Optional<NodeCn> right = getRight();
		setLeftInternal(Optional.empty());
		setRightInternal(Optional.empty());
		LOG.info("{} leaving.", this);
		right.ifPresent((n)->n.setLeftLeaving(this, left));
		left.ifPresent((n)->n.setRightLeaving(this, right));
	}
	
	@Override
	public void enter(NodeCn newNode){
		if(!connected) throw new NotReachableException(this);
		if(!isResponsible(newNode.position())) throw new IllegalArgumentException();
		if(equals(newNode)) throw new IllegalArgumentException();
		Direction d = pos.distanceTo(newNode.position()).direction();
		Optional<NodeCn> right = getRight();
		Optional<NodeCn> left = getLeft();
		if(d==Direction.RIGHT){
			newNode.setRight(right.orElse(left.orElse(this)));
			newNode.setLeft(this);
			right.ifPresent((r)->r.setLeft(newNode));
			setRightInternal(Optional.of(newNode));
		}else{
			newNode.setLeft(left.orElse(right.orElse(this)));
			newNode.setRight(this);
			left.ifPresent((l)->l.setRight(newNode));
			setLeftInternal(Optional.of(newNode));
		}
	}

	@Override
	public void setRight(NodeCn newNode) {
		if(!connected) throw new NotReachableException(this);
		if(left.isPresent() && right.isPresent()){
	      if(!isCloser(newNode, getRight().get(), Direction.RIGHT)){
		    throw new IllegalArgumentException();
	      }
	    }
		setRightInternal(Optional.of(newNode));
	}

	private boolean isCloser(NodeCn node, NodeCn than, Direction dir) {
		Distance firstDistance = pos.distanceTo(node.position(), dir);
		Distance secondDistance = pos.distanceTo(than.position(), dir);
		return firstDistance.compareTo(secondDistance)<0;
	}

	@Override
	public void setLeft(NodeCn newNode) {
		if(!connected) throw new NotReachableException(this);
		if(left.isPresent() && right.isPresent()){
		  if(!isCloser(newNode, getLeft().get(), Direction.LEFT)){
			throw new IllegalArgumentException();
		  }
		}
		setLeftInternal(Optional.of(newNode));
	}

	@Override
	public void setLeftLeaving(NodeCn oldNode, Optional<NodeCn> newNode) {
		if(!connected) throw new NotReachableException(this);
		cache.remove(oldNode);
		setLeftInternal(newNode);
	}

	@Override
	public void setRightLeaving(NodeCn oldNode, Optional<NodeCn> newNode) {
		if(!connected) throw new NotReachableException(this);
		assert newNode.orElse(null)!=this;
		cache.remove(oldNode);
		setRightInternal(newNode);
	}

	void setLeftInternal(Optional<NodeCn> newNode) {
		left = newNode;
		cache.cache(newNode);
	}

	void setRightInternal(Optional<NodeCn> newNode) {
		assert newNode.orElse(null)!=this;
		right = newNode;
		cache.cache(newNode);
	}
	
	@Override
	public void connectTo(Node other){
		if(connected) throw new IllegalStateException();
		connected = true;
		NodeCn r = (NodeCn) other.getResponsible(position());
		r.enter(this);
	}
}
