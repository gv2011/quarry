package com.github.gv2011.experiments.circle2;

import java.util.concurrent.atomic.AtomicInteger;

public interface Node {
	
	Node getResponsible(Position p);
	
	Node getResponsible(Position p, AtomicInteger counter);

	Position position();

}
