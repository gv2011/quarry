package com.github.gv2011.experiments.circle2;

import java.util.Optional;

public class EmptyCache implements Cache{

	@Override
	public void updateClosest(Closest closest) {
	}

	@Override
	public void remove(NodeCn n) {
	}

	@Override
	public void cache(Optional<NodeCn> result) {
	}

}
