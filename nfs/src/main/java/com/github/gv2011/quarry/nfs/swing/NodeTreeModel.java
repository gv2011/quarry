package com.github.gv2011.quarry.nfs.swing;

import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;

import com.github.gv2011.quarry.nfs.Invalidatable;
import com.github.gv2011.quarry.nfs.Node;

class NodeTreeModel implements TreeModel{

  private static final Logger LOG = getLogger(NodeTreeModel.class);

  private final Node root;
  private final List<TreeModelListener> listeners = new ArrayList<>();
  private final Invalidatable display;

  NodeTreeModel(final Node root, final Invalidatable display) {
    this.root = root;
    this.display = display;
  }

  @Override
  public Node getRoot() {
    return root;
  }

  @Override
  public Node getChild(final Object parent, final int index) {
    return ((Node)parent).value(display).get().children().get(index);
  }

  @Override
  public int getChildCount(final Object parent) {
    final Node node = (Node)parent;
    final Optional<Integer> count = node.value(display).map(v->v.children().size());
    LOG.debug("{}: {} children.", node, count.map(Object::toString).orElse("unkown"));
    return count.orElse(0);
  }

  @Override
  public boolean isLeaf(final Object node) {
    final boolean isLeaf = getChildCount(node)==0;
    LOG.debug("{}: {}", node, isLeaf?"leaf":"not leaf");
    return isLeaf;
  }

  @Override
  public void valueForPathChanged(final TreePath path, final Object newValue) {
    throw notYetImplementedException();
  }

  @Override
  public int getIndexOfChild(final Object parent, final Object child) {
    if(parent==null || child==null) return -1;
    else return ((Node)parent).value(display).get().children().indexOf(child);
  }

  @Override
  public void addTreeModelListener(final TreeModelListener l) {
    listeners.add(l);
  }

  @Override
  public void removeTreeModelListener(final TreeModelListener l) {
    listeners.remove(l);
  }

  void invalidate() {
    final TreeModelEvent e = new TreeModelEvent(null, new Object[]{});
    for(final TreeModelListener l: listeners){
      l.treeStructureChanged(e);
    }
  }


}
