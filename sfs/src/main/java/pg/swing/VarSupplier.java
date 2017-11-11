package pg.swing;

public interface VarSupplier {

  VarSupplier forChild(int index);

  Var<String> name();

  Var<Integer> size();

}
