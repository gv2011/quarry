package com.github.gv2011.experiments.circle2;

import java.util.Optional;
import java.util.stream.Stream;

import com.github.gv2011.util.ann.Nullable;

public class Closest {
private final Position pos;
private @Nullable NodeCn node;
private @Nullable Distance currentDistance;

public Closest(Position pos) {
	this.pos = pos;
};

public Closest(Position pos, NodeCn node) {
	this.pos = pos;
	this.node = node;
	currentDistance = pos.distanceTo(node.position());
	assert currentDistance!=null;
};

public void update(Optional<NodeCn> node){
	assert node!=null;
	node.ifPresent((n)->{
		Distance newDistance = pos.distanceTo(n.position());
		if(this.node==null?true:newDistance.compareTo(currentDistance)<0){
			this.node = n;
			currentDistance = newDistance;
			assert currentDistance!=null;
		}
	});
}

public void remove(NodeCn n){
	if(node==n){
		node = null;
	    currentDistance = null;
	}
}

public NodeCn get() {
	if(node==null) throw new IllegalStateException();
	return node;
}

public Optional<NodeCn> getOpt() {
	return Optional.ofNullable(node);
}

public Stream<NodeCn> stream() {
	return node==null?Stream.empty():Stream.of(node);
}

public boolean isPresent() {
	return node!=null;
}


}
