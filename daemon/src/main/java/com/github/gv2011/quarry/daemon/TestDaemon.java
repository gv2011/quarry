package com.github.gv2011.quarry.daemon;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class TestDaemon{

  private static final Logger LOG = new Logger(TestDaemon.class);

  public static final void main(final String[] args) throws Exception {
    final String cmd = args.length==0 ? "start" : args[0];
    LOG.log(cmd);
    LOG.log(args);
    LOG.log(new File(".").getAbsoluteFile().getCanonicalPath());
    if(cmd.equals("start")) {
      if(isRunning()) throw new IllegalStateException("Already running.");
      else{
        startProcess();
      }
    }
    else if(cmd.equals("stop")) {
      try{
        @SuppressWarnings("resource")
        final Socket s = new Socket(InetAddress.getLoopbackAddress(), 299);
        final OutputStream out = s.getOutputStream();
        out.write('S');
        out.flush();
        LOG.log("Sent S");
        final int read = s.getInputStream().read();
        LOG.log("Received "+(char)read);
      }catch(final ConnectException c) {
        LOG.log("Not running.");
      }
    }
  }

  private static boolean isRunning() throws IOException{
    Socket s = null;
    try{
      s = new Socket(InetAddress.getLoopbackAddress(), 299);
      try{s.close();}catch (final Exception e){}
    }
    catch(final ConnectException c) {/*expected*/}
    return s!=null;
  }

  private static void startProcess() throws InterruptedException{
    boolean success = false;
    while(!success) {
      try {
        final ProcessBuilder pb = new ProcessBuilder();
        pb.command("java","-cp","target/classes", TestService.class.getName());
        final Process p = pb.start();
        try {
          //LOG.log("pid:"+p.pid()); TODO Java9
          if(isRunning()) success = true;
          if(!success) {
            Thread.sleep(1000);
            if(isRunning()) success = true;
          }
        }finally {
          if(!success) p.destroyForcibly();
        }
      }
      catch(final Throwable t) {
        LOG.log(t);
        LOG.log("Trying again in 10 s.");
        Thread.sleep(10000);
      }
    }
  }

}
