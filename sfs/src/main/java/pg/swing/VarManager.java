package pg.swing;

import java.util.concurrent.ExecutorService;

import javax.swing.SwingUtilities;

public class VarManager {

  private final ExecutorService es;
  private final TestTreeModel model;

  public VarManager(final ExecutorService es, final TestTreeModel model){
    this.es = es;
    this.model = model;
    SwingUtilities.invokeLater(this::swingLoop);
  }

  void requestUpdate(final VarImp<?> var) {
    es.submit(var::update);
  }

  void nodeUpdated(final ViewNode node) {
    model.fireTreeNodeChanged(node);
  }

  private void swingLoop(){

  }

}
