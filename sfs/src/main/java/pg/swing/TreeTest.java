package pg.swing;

import static com.github.gv2011.util.ex.Exceptions.callWithCloseable;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import com.github.gv2011.util.ExecutorUtils;

public class TreeTest {

  public static void main(final String[] args) throws InterruptedException{
    callWithCloseable(
      Executors::newCachedThreadPool,
      es->{
        final CountDownLatch terminate = new CountDownLatch(1);
        SwingUtilities.invokeLater(()->{
          final JFrame jFrame = new JFrame();
          final TestTreeModel model = new TestTreeModel(es);
          final JTree tree = new JTree(model);
          tree.addTreeWillExpandListener(model);
          jFrame.add(tree);
          jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
          jFrame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(final WindowEvent e) {
              jFrame.dispose();
              terminate.countDown();
            }
          });
          jFrame.setMinimumSize(new Dimension(300,400));
          jFrame.pack();
          jFrame.setVisible(true);
        });
        terminate.await();
      },
      es->ExecutorUtils.asCloseable(es).close()
    );
  }
}
