package com.github.gv2011.experiments.circle2;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

class CacheImp implements Cache{
	
	private final Map<Position,Closest> map;
	private Position basePosition;
	private NodeCn node;
	
	CacheImp(NodeCn node){
		this.node = node;
		basePosition = node.position();
		map = initMap();
	}
	
	@Override
	public void cache(Optional<NodeCn> n){
		n.ifPresent((node)->{
			if(this.node == node) throw new IllegalArgumentException();
			for(Closest closest: map.values()){
				closest.update(n);
			}
		});
	}
	
	private Map<Position,Closest> initMap(){
		Map<Position,Closest> map = new HashMap<>();
		BigInteger dist = Position.CIRCLE_SIZE.shiftRight(1); //half size
		Distance distance = new Distance(dist);
		Position p1 = basePosition.at(distance);
		map.put(p1, new Closest(p1));
		while(dist.compareTo(BigInteger.ONE)>0){
			addEntry(map, dist);
			dist = dist.shiftRight(1);
		}
		return Collections.unmodifiableMap(map);
	}

	private void addEntry(Map<Position,Closest> map, BigInteger dist) {
		Distance distance = new Distance(dist);
		Position p1 = basePosition.at(distance);
		map.put(p1, new Closest(p1));
		Position p2 = basePosition.at(distance.negate());
		map.put(p2, new Closest(p2));
	}

	public NodeCn getClosest(Position p) {
		final Closest closest = new Closest(p);
		updateClosest(closest);
		return closest.get();
	}
	
	@Override
	public void updateClosest(Closest closest) {
		map.values().stream()
		  .map(Closest::getOpt)
		  .forEach(closest::update)
		;
	}


	@Override
	public void remove(NodeCn oldNode) {
		map.values().stream().forEach((c)->c.remove(oldNode));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		SortedMap<Distance, Distance> sorted = new TreeMap<>();
		for(Entry<Position, Closest> e: map.entrySet()){
			Closest value = e.getValue();
			if(value.isPresent()){
			  sorted.put(
				basePosition.distanceTo(e.getKey()), 
				basePosition.distanceTo(value.get().position())
			  );
			}
		}
		int i=1;
		for(Entry<Distance, Distance> e: sorted.entrySet()){
			sb.append(i++).append(".: ").append(e.getKey()).append(": ").append(e.getValue()).append('\n');
		}
		return sb.toString();
	}


}
