package pg.swing;

import static com.github.gv2011.util.Verify.verify;

import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

abstract class VarImp<T> implements Var<T>{

  private final VarManager varManager;
  private final Object lock = new Object();
  private final ViewNode node;

  private Optional<T> value = Optional.empty();
  private boolean required;
  private boolean valid;
  private boolean inQueue;

  VarImp(final VarManager varManager, final ViewNode node) {
    this.varManager = varManager;
    this.node = node;
  }

//  @Override
//  public Value<T> get() {
//    synchronized(lock){return value;}
//  }
//
//  @Override
//  public void setRequired(final boolean required) {
//    synchronized(lock){
//      if(this.required!=required){
//        if(required && !inQueue) putInQueue();
//      }
//    }
//  }

  private void putInQueue() {
    varManager.requestUpdate(this);
    inQueue = true;
  }

//  @Override
//  public boolean valid() {
//    synchronized(lock){return valid;}
//  }

  void update(){
    final T v = updateValue(this::invalidate);
    final boolean notify;
    synchronized(lock){
      verify(inQueue);
      verify(!valid);
      inQueue = false;
      value = Optional.of(v);
      valid = true;
      notify = required;
    }
    if(notify){
      varManager.nodeUpdated(node);
    }
  }

  abstract T updateValue(Invalidator invalidator);

  private void invalidate(){

  }

}
