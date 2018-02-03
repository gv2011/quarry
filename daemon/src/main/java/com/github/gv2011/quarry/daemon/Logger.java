package com.github.gv2011.quarry.daemon;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Logger {

  private final Path file;

  public Logger(final Class<?> clazz) {
    file = FileSystems.getDefault().getPath("log", clazz.getSimpleName()+".log");
  }

  public void log(final String[] args) {
    if(args!=null) for(int i=0; i<args.length; i++) {
      log("Argument "+i+": "+args[i]);
    }
  }

  public void log(final String msg){
    System.out.println(msg);
    try(Writer w = Files.newBufferedWriter(file, APPEND, CREATE)) {
      w.append(msg).append("\n");
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public void log(final Throwable t) {
    final StringWriter sw = new StringWriter();
    t.printStackTrace(new PrintWriter(sw));
    log(sw.toString());
  }

}
