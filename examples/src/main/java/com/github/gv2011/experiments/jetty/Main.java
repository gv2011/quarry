package com.github.gv2011.experiments.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {

  public static void main(final String[] args) throws Exception {
    final Server server = new Server(8080);
    final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new TestServlet()),"/*");
    try{
      server.start();
      Thread.sleep(Long.MAX_VALUE);
    }finally{
      server.stop();
      server.join();
    }
  }

}
