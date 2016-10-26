package com.github.gv2011.experiments.circle2;

import java.util.Optional;

interface Cache {

	void updateClosest(Closest closest);

	void remove(NodeCn n);

	void cache(Optional<NodeCn> result);

}
