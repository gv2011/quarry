package com.github.gv2011.quarry.daemon;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestService {

  private static final Logger LOG = new Logger(TestService.class);

  public static void main(final String[] args) throws Exception{
    new TestService().execute();
  }

  private final AtomicBoolean shouldRun = new AtomicBoolean(true);
  private final ServerSocket ss;

  private TestService() throws Exception{
    ss = new ServerSocket(299, 0, InetAddress.getLoopbackAddress());
  }

  private void execute(){
    boolean success = false;
    try {
      LOG.log("Started");
      Runtime.getRuntime().addShutdownHook(
        new Thread(
          ()->{
            LOG.log("Shutdown");
            shouldRun.set(false);
          },
          "shutdownHook"
        )
      );
      while(shouldRun.get()) {
        try {
          Socket s = null;
          try {
            s = ss.accept();
          } catch (final SocketException e) {
            if(shouldRun.get()) throw e;
          }
          if(s!=null) {
            final Socket s2 = s;
            final Thread t = new Thread(()->handle(s2));
            t.start();
          }
        } catch (final Throwable t) {
          LOG.log(t);
          Thread.sleep(100);
        }
      }
      success = true;
    } catch (final Exception t) {
      LOG.log(t);
    }
    finally{
      LOG.log("Finished");
      if(!success) System.exit(1);
    }
  }

  private void handle(final Socket s){
    try {
      try {
        final InputStream in = s.getInputStream();
        final int command = in.read();
        LOG.log("Received "+(char)command);
        final OutputStream out = s.getOutputStream();
        out.write(command=='S' ? 'C' : 'N');
        out.flush();
        s.close();
        if(command=='S') {
          shouldRun.set(false);
          ss.close();
        }
      }finally {
        //s.close();
      }
    } catch (final Throwable t) {
      LOG.log(t);
    }
  }
}
