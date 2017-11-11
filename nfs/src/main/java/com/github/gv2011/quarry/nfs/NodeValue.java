package com.github.gv2011.quarry.nfs;

import java.util.Optional;

import com.github.gv2011.util.icol.ISortedSet;

public interface NodeValue {

  String name();

  ISortedSet<Node> children();

  Optional<String> data();

}
