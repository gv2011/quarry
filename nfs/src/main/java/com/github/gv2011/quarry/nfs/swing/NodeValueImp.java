package com.github.gv2011.quarry.nfs.swing;

import java.util.Optional;

import com.github.gv2011.quarry.nfs.Node;
import com.github.gv2011.quarry.nfs.NodeValue;
import com.github.gv2011.util.icol.ISortedSet;

public class NodeValueImp implements NodeValue{

  private final String name;
  private final ISortedSet<Node> children;
  private final Optional<String> data;

  NodeValueImp(final String name, final ISortedSet<Node> children, final Optional<String> data) {
    this.name = name;
    this.children = children;
    this.data = data;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public ISortedSet<Node> children() {
    return children;
  }

  @Override
  public Optional<String> data() {
    return data;
  }

}
