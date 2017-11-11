package com.github.gv2011.quarry.nfs.swing;

import static com.github.gv2011.util.CollectionUtils.iCollections;
import static com.github.gv2011.util.Verify.verify;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import com.github.gv2011.quarry.nfs.Invalidatable;
import com.github.gv2011.quarry.nfs.Node;
import com.github.gv2011.quarry.nfs.NodeValue;
import com.github.gv2011.util.ann.Nullable;
import com.github.gv2011.util.icol.ISet;

public class TestNode implements Node{

  private final UUID id = UUID.randomUUID();
  private final Object lock = new Object();
  private final Supplier<NodeValue> valueSupplier;
  private boolean requested;
  private @Nullable Loader loader;
  private boolean invalidating;
  private Optional<NodeValue> value = Optional.empty();
  private final Set<Invalidatable> dependants = new HashSet<>();

  TestNode(final Supplier<NodeValue> valueSupplier) {
    this.valueSupplier = valueSupplier;
  }

  @Override
  public int compareTo(final Node o) {
    return id.compareTo(((TestNode)o).id);
  }

  @Override
  public Optional<NodeValue> value(final Invalidatable invalidatable) {
    synchronized(lock){
      dependants.add(invalidatable);
      requested = true;
      if(!value.isPresent() && loader==null && !invalidating){
        loader = new Loader();
        loader.start();
        System.out.println("loading");
      }
      return value;
    }
  }

  @Override
  public String toString() {
    return id.toString().substring(0, 8);
  }

  @Override
  public void invalidate(final boolean invalidate) {
    verify(invalidate);
    boolean repeatLoading = false;
    boolean invalidateDeps = false;
    ISet<Invalidatable> dependants = iCollections().emptySet();
    synchronized(lock){
      if(loader!=null){
        verify(requested && !value.isPresent() && !invalidating);
        loader.cancelled = true;
        repeatLoading = true;
        loader = null;
      }else if(!invalidating){
        invalidating = true;
        verify(value.isPresent());
        value = Optional.empty();
        requested = false;
        invalidateDeps = true;
        dependants = iCollections().setOf(this.dependants);
        this.dependants.clear();
      }
    }
    if(repeatLoading){
      verify(!invalidateDeps);
      synchronized(lock){
        loader = new Loader();
        loader.start();
      }
    }else if(invalidateDeps){
      dependants.parallelStream().forEach(d->d.invalidate(false));
      synchronized(lock){
        verify(invalidating);
        invalidating = false;
      }
    }
  }

  private class Loader extends Thread{
    private boolean cancelled = false;
    @Override
    public void run() {
      ISet<Invalidatable> dependants = iCollections().emptySet();
      final NodeValue value = valueSupplier.get();
      final boolean loaded;
      synchronized(lock){
        if(!cancelled){
          verify(requested && loader!=null && !invalidating && !TestNode.this.value.isPresent());
          TestNode.this.value = Optional.of(value);
          System.out.println("loaded");
          dependants = iCollections().setOf(TestNode.this.dependants);
          TestNode.this.dependants.clear();
          loaded = true;
        }else loaded = false;
      }
      if(loaded){
        dependants.parallelStream().forEach(d->d.invalidate(false));
        synchronized(lock){
          loader = null;
        }
      }
    }
  }

}
