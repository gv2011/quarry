package com.github.gv2011.quarry.nfs;

@FunctionalInterface
public interface Invalidatable {

  void invalidate(boolean invalidate);

}
