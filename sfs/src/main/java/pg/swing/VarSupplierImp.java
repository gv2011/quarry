package pg.swing;

import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;

import java.util.Optional;

public class VarSupplierImp implements VarSupplier{

  private final VarManager varManager;
  private final Optional<VarSupplierImp> parent;
  private final int index;

  VarSupplierImp(final VarManager varManager) {
    this(varManager, Optional.empty(), -1);
  }

  private VarSupplierImp(final VarManager varManager, final Optional<VarSupplierImp> parent, final int index) {
    this.varManager = varManager;
    this.parent = parent;
    this.index = index;
  }

  @Override
  public VarSupplier forChild(final int index) {
    return new VarSupplierImp(varManager, Optional.of(this), index);
  }

  @Override
  public Var<String> name() {
    // TODO Auto-generated method stub
    throw notYetImplementedException();
  }

  @Override
  public Var<Integer> size() {
    // TODO Auto-generated method stub
    throw notYetImplementedException();
  }

//  @Override
//  public Var<String> name() {
//    return new VarImp<String>(varManager::enqueue){
//      @Override
//      void update() {
//        value = Optional.of(Integer.toString(index));
//      }
//    };
//  }

//  @Override
//  public Var<Integer> size() {
//    return new VarImp<Integer>(varManager::enqueue){
//      @Override
//      void update() {
//        value = Optional.of(index);
//      }
//    };
//  }

}
