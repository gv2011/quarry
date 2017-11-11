package pg.swing;

import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;

import java.util.concurrent.ExecutorService;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

final class TestTreeModel implements TreeModel, TreeWillExpandListener{

  private final ViewNode root;
  private final EventListenerList listenerList = new EventListenerList();

  TestTreeModel(final ExecutorService es){
    root = new ViewNode(new VarSupplierImp(new VarManager(es, this)));
  }

  @Override
  public ViewNode getRoot() {
    return root ;
  }

  @Override
  public Object getChild(final Object parent, final int index) {
    return ((ViewNode)parent).get(index);
  }

  @Override
  public int getChildCount(final Object parent) {
    return ((ViewNode)parent).size();
  }

  @Override
  public boolean isLeaf(final Object node) {
    return ((ViewNode)node).isLeaf();
  }

  @Override
  public void valueForPathChanged(final TreePath path, final Object newValue) {
    // TODO Auto-generated method stub
    throw notYetImplementedException();
  }

  @Override
  public int getIndexOfChild(final Object parent, final Object child) {
    return ((ViewNode)child).index();
  }

  @Override
  public void addTreeModelListener(final TreeModelListener l) {
    listenerList.add(TreeModelListener.class, l);
  }

  @Override
  public void removeTreeModelListener(final TreeModelListener l) {
    listenerList.remove(TreeModelListener.class, l);
  }

  @Override
  public void treeWillExpand(final TreeExpansionEvent event) throws ExpandVetoException {
    final boolean allowed = ((ViewNode)event.getPath().getLastPathComponent()).expanded(true);
    if(!allowed) throw new ExpandVetoException(event);
  }

  @Override
  public void treeWillCollapse(final TreeExpansionEvent event) throws ExpandVetoException {
    ((ViewNode)event.getPath().getLastPathComponent()).expanded(false);
  }

  void fireTreeNodeChanged(final ViewNode node) {
    final Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
        // Lazily create the event:
        if (e == null) e = createTreeModelEvent(node);
        ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
      }
    }
  }

  private TreeModelEvent createTreeModelEvent(final ViewNode node) {
    return node.parent()
      .map(p->new TreeModelEvent(this, p.path(), new int[]{node.index()}, new ViewNode[]{node}))
      .orElseGet(()->new TreeModelEvent(this, node.path(), null, null))
    ;
  }

}
