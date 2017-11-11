package pg.swing;

import static com.github.gv2011.util.Verify.verify;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;


public class ViewNode {

  private static final Logger LOG = getLogger(ViewNode.class);

  private final int index;
  private final Var<String> name;
  private final Map<Integer, ViewNode> children = new HashMap<>();
  private final Optional<ViewNode> parent;
  private boolean expanded;

  private final VarSupplier vars;

  private final ViewTreePath path;

  ViewNode(final ViewNode parent, final int index, final VarSupplier vars) {
    this.parent = Optional.of(parent);
    path = parent.path.pathByAddingChild(this);
    this.index = index;
    this.vars = vars;
    name = vars.name();
  }

  ViewNode(final VarSupplier vars) {
    parent = Optional.empty();
    path = new ViewTreePath(this);
    index = -1;
    this.vars = vars;
    name = vars.name();
//    size = vars.size();
    expanded = true;
  }

  Optional<ViewNode> parent(){
    return parent;
  }

//  private boolean valid(){
//    final boolean valid = size.valid() && name.valid();
//    return valid  ? parent.map(ViewNode::valid).orElse(true): false;
//  }

  boolean isLeaf(){
    return false;
  }

  int index() {
    return index;
  }

  int size() {
    return 0;
  }

  ViewNode get(final int index) {
    verify(expanded);
    final ViewNode result = children.computeIfAbsent(
      index,
      i->new ViewNode(this, i, vars.forChild(index))
    );
    LOG.debug("{}: child {}: {}", this, index, result);
    return result;
  }

  @Override
  public String toString() {
    return "root";
  }

  boolean expanded(final boolean expanded) {
    final boolean result;
    if(this.expanded!=expanded){
      if(false) result = false;
      else{
        this.expanded = expanded;
        result = true;
      }
    }
    else result = true;
    return result;
  }

  public ViewTreePath path() {
    return path;
  }



}
