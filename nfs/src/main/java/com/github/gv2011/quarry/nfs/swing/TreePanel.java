package com.github.gv2011.quarry.nfs.swing;
import static com.github.gv2011.util.ex.Exceptions.run;
import static org.slf4j.LoggerFactory.getLogger;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;

import com.github.gv2011.quarry.nfs.Invalidatable;
import com.github.gv2011.quarry.nfs.Node;
import com.github.gv2011.quarry.nfs.NodeValue;

class TreePanel extends JPanel{

  private static final Logger LOG = getLogger(TreePanel.class);

  private final Object lock = new Object();
  private final JEditorPane htmlPane;
  private final JTree tree;

  private boolean invalid;
  private boolean invalidationPending;

  private final DefaultMutableTreeNode treeNode;

  private Node root;
  final Invalidatable inv;

  TreePanel(final Node root) {
    super(new GridLayout(0,1));
    inv = this::invalidatePanel;
    this.root = root;
    treeNode = new DefaultMutableTreeNode(root, true);
    updateTreeNode();
    tree = new JTree(treeNode){
      @Override
      public String convertValueToText(
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf, final
        int row,
        final boolean hasFocus
      ) {
        return ((Node)((DefaultMutableTreeNode) value).getUserObject())
            .value(inv).map(NodeValue::name).orElse("-loading-");
      }
    };
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.addTreeSelectionListener(this::selectionChanged);
    tree.addTreeWillExpandListener(new Tel());

    final JScrollPane treeView = new JScrollPane(tree);

    htmlPane = new JEditorPane();
    htmlPane.setEditable(false);
    final JScrollPane htmlView = new JScrollPane(htmlPane);
    final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setLeftComponent(treeView);
    splitPane.setRightComponent(htmlView);

    final Dimension minimumSize = new Dimension(100, 50);
    htmlView.setMinimumSize(minimumSize);
    treeView.setMinimumSize(minimumSize);
    splitPane.setDividerLocation(400);
    splitPane.setPreferredSize(new Dimension(1000, 600));
    add(splitPane);
  }

  private void updateTreeNode() {
    updateTreeNode(treeNode, root);
  }

  private void updateTreeNode(final DefaultMutableTreeNode treeNode, final Node node) {
    final Optional<NodeValue> value = node.value(inv);
    treeNode.removeAllChildren();
    if(value.isPresent()){
      final NodeValue v = value.get();
      for(final Node c: v.children()){
        final DefaultMutableTreeNode childTn = new DefaultMutableTreeNode(c,true);
        treeNode.add(childTn);
        final Optional<NodeValue> cv = c.value(inv);
        if(cv.isPresent()){
          for(final Node gc: cv.get().children()){
            childTn.add(new DefaultMutableTreeNode(gc,true));
          }
        }
      }
    }
  }

  private void selectionChanged(final TreeSelectionEvent e) {
    Optional.ofNullable(tree.getLastSelectedPathComponent()).ifPresent(node->{
      displayContent(node);
    });
  }

  private void displayContent(final Object value) {
    htmlPane.setText(value.getClass().getName());
  }

  private void invalidatePanel(final boolean invalidate){
    run(()->SwingUtilities.invokeAndWait(()->{
      updateTreeNode();
      repaint();
    }));
  }

  private class Tel implements TreeWillExpandListener{

    @Override
    public void treeWillExpand(final TreeExpansionEvent e) throws ExpandVetoException {
      LOG.debug("Expansion:{}", e.getPath().getLastPathComponent());
      final DefaultMutableTreeNode tn = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
      final Node n = (Node) tn.getUserObject();
      updateTreeNode(tn, n);
    }

    @Override
    public void treeWillCollapse(final TreeExpansionEvent event) throws ExpandVetoException {
    }
  }

}

