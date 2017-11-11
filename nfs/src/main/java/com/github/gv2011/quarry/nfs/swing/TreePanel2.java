package com.github.gv2011.quarry.nfs.swing;
import static com.github.gv2011.util.ex.Exceptions.run;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;

import com.github.gv2011.quarry.nfs.Invalidatable;
import com.github.gv2011.quarry.nfs.Node;
import com.github.gv2011.quarry.nfs.NodeValue;

class TreePanel2 extends JPanel{

  private final Object lock = new Object();
  private final JEditorPane htmlPane;
  private final JTree tree;

  private boolean invalid;
  private boolean invalidationPending;
  private final NodeTreeModel nodeTreeModel;

  TreePanel2(final Node root) {
    super(new GridLayout(0,1));
    final Invalidatable inv = this::invalidatePanel;
    nodeTreeModel = new NodeTreeModel(root, inv);
    tree = new JTree(nodeTreeModel){
      @Override
      public String convertValueToText(
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf, final
        int row,
        final boolean hasFocus
      ) {
        return ((Node) value).value(inv).map(NodeValue::name).orElse("-loading-");
      }
    };
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.addTreeSelectionListener(this::selectionChanged);

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

  private void selectionChanged(final TreeSelectionEvent e) {
    Optional.ofNullable(tree.getLastSelectedPathComponent()).ifPresent(node->{
      displayContent(node);
    });
  }

  private void displayContent(final Object value) {
    htmlPane.setText(value.getClass().getName());
  }

  private void invalidatePanel(final boolean invalidate){
    if(!invalidate) SwingUtilities.invokeLater(this::repaint);
    else{
      synchronized(lock){
        if(!invalid){
          if(!invalidationPending){
            invalidationPending = true;
            SwingUtilities.invokeLater(()->{
              nodeTreeModel.invalidate();
              repaint();
              synchronized(lock){
                invalid = true;
                invalidationPending = false;
                lock.notifyAll();
              }
            });
          }
        }
        while(!invalid) run(lock::wait);
      }
    }
  }

}

