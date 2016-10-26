package com.github.gv2011.experiments.webdav;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import io.milton.http.ResourceFactory;
import io.milton.http.fs.FileSystemResourceFactory;
import io.milton.http.fs.NullSecurityManager;

public class Main {

  public static void main(final String[] args) throws Exception {
    final Server server = new Server(8080);
    final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    final File sRoot = new File("C:\\Dateien\\webdav");
    final io.milton.http.SecurityManager sm = new NullSecurityManager();
    final ResourceFactory rf = new FileSystemResourceFactory(sRoot, sm);
    context.addServlet(new ServletHolder(new MServlet(new Configurator(rf))),"/*");
    try{
      server.start();
      Thread.sleep(Long.MAX_VALUE);
    }finally{
      server.stop();
      server.join();
    }
  }

}
