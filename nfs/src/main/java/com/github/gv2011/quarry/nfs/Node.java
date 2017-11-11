package com.github.gv2011.quarry.nfs;

import java.util.Optional;

public interface Node extends Comparable<Node>, Invalidatable{

  Optional<NodeValue> value(Invalidatable invalidatable);


}