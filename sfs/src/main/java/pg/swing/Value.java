package pg.swing;

import static com.github.gv2011.util.ex.Exceptions.call;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.SwingUtilities;

public class Value<T> {

  public static interface Invalidatable<T>{
    Value<T> value();
    Invalidator invalidator();
  }

  public static <T> Invalidatable<T> invalidatable(final T value){
    final Value<T> v = new Value<>(value);
    return new Invalidatable<T>(){
      @Override
      public Value<T> value() {
        return v;
      }
      @Override
      public Invalidator invalidator() {
        return v::invalidate;
      }
    };
  }

  private final T value;

  private final Object lock = new Object();
  private boolean valid;
  private List<Invalidator> invalidators = new ArrayList<>();

  private Value(final T value){
    this.value = value;
  }

  public Optional<T> get(final Invalidator swingInvalidationCallback){
    synchronized(lock){
      if(valid){
        invalidators.add(swingInvalidationCallback);
        return Optional.of(value);
      }else return Optional.empty();
    }
  }

  private void invalidate(){
    final List<Invalidator> invalidators;
    synchronized(lock){
      if(valid){
        valid = false;
        invalidators = this.invalidators;
        this.invalidators = new ArrayList<>();
      }else{
        invalidators = new ArrayList<>();
      }
    }
    call(()->SwingUtilities.invokeAndWait(()->{
      for(final Invalidator i: invalidators) i.invalidate();
    }));
  }

}
