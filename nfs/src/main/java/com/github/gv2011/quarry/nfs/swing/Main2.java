package com.github.gv2011.quarry.nfs.swing;

import static com.github.gv2011.util.ex.Exceptions.call;
import static com.github.gv2011.util.icol.ICollections.iCollections;

import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.github.gv2011.quarry.nfs.Node;
import com.github.gv2011.quarry.nfs.NodeValue;
import com.github.gv2011.util.icol.ISortedSet;

public class Main2 {

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(Main2::createAndShowGUI);
  }

  private static void createAndShowGUI() {
    call(()->UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
    final JFrame frame = new JFrame("TreeDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final NodeValue nodeValue = createNodeValue("root");
    final TestNode node = new TestNode(()->{
      call(()->Thread.sleep(2000));
      return nodeValue;
    });
    final TreePanel tree = new TreePanel(node);
    frame.add(tree);
    frame.pack();
    frame.setVisible(true);
  }

  private static NodeValue createNodeValue(final String name, final ISortedSet<Node> children) {
    return new NodeValueImp(name, children, Optional.empty());
  }

  private static NodeValue createNodeValue(final String name) {
    final ISortedSet<Node> children = iCollections().<Node>sortedSetBuilder()
      .add(createNode("a"))
      .add(createNode(
        "b",
        iCollections().<Node>sortedSetBuilder()
        .add(createNode("b1"))
        .add(createNode("b2"))
        .build()
      ))
      .add(createNode("c"))
      .build()
    ;
    return createNodeValue(name, children);
  }

  private static Node createNode(final String name) {
    return createNode(name, iCollections().emptySortedSet());
  }

  private static Node createNode(final String name, final ISortedSet<Node> children) {
    return new TestNode(()->new NodeValueImp(name, children, Optional.empty()));
  }

}
