package pg.swing;

import javax.swing.tree.TreePath;

import com.github.gv2011.util.ann.Nullable;

public class ViewTreePath extends TreePath{

  ViewTreePath(final ViewNode node) {
    super(null, node);
  }


  private ViewTreePath(final ViewTreePath parentPath, final ViewNode node) {
    super(parentPath, node);
  }


  @Override
  public ViewNode[] getPath() {
    return (ViewNode[]) super.getPath();
  }

  @Override
  public ViewNode getLastPathComponent() {
    return (ViewNode) super.getLastPathComponent();
  }

  @Override
  public ViewNode getPathComponent(final int index) {
    return (ViewNode) super.getPathComponent(index);
  }

  @Override
  public ViewTreePath pathByAddingChild(final Object child) {
    return new ViewTreePath(this, (ViewNode)child);
  }

  @Override
  public @Nullable ViewTreePath getParentPath() {
    return (ViewTreePath) super.getParentPath();
  }

}
