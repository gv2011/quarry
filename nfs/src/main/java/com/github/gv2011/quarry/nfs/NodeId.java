package com.github.gv2011.quarry.nfs;

public interface NodeId extends Value, NodeIdentificator{

  @Override
  default NodeId id(){return this;}

}
