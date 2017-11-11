package com.github.gv2011.quarry.mvn;

import java.nio.file.Path;

import com.github.gv2011.quarry.mvn.cwl.Launcher;
import com.github.gv2011.util.FileUtils;

public class OrigMain {

  //"C:\programs\java\jdk8\bin\java.exe"
  //-classpath
  //"C:\programs\apache-maven-3.3.9\bin\..\boot\plexus-classworlds-2.5.2.jar"
  //"-Dclassworlds.conf=C:\programs\apache-maven-3.3.9\bin\..\bin\m2.conf"
  //"-Dmaven.home=C:\programs\apache-maven-3.3.9\bin\.."
  //"-Dmaven.multiModuleProjectDirectory=C:\work"
  //org.codehaus.plexus.classworlds.launcher.Launcher
  //-version

  //  C:\Dateien\src\quarry\mvn>mvn -f work dependency:build-classpath
  //  "C:\programs\java\jdk8\bin\java.exe"    -classpath "C:\programs\apache-maven-3.3
  //  .9\bin\..\boot\plexus-classworlds-2.5.2.jar" "-Dclassworlds.conf=C:\programs\apa
  //  che-maven-3.3.9\bin\..\bin\m2.conf" "-Dmaven.home=C:\programs\apache-maven-3.3.9
  //  \bin\.." "-Dmaven.multiModuleProjectDirectory=C:\Dateien\src\quarry\mvn" org.cod
  //  ehaus.plexus.classworlds.launcher.Launcher -f work dependency:build-classpath

  public static void main(final String[] args){
    final Runnable r = ()->{
      final Path m2Home = FileUtils.WORK_DIR.resolve("m2");
      System.setProperty("maven.home", m2Home.toString());
      System.setProperty("classworlds.conf", m2Home.resolve("m2.conf").toString());
      final Path dir = FileUtils.WORK_DIR.resolve("work");
      System.setProperty("maven.multiModuleProjectDirectory", dir.toString());
      Launcher.main(new String[]{
        "-f",
        dir.toString(),
//        "org.apache.maven.plugins:example-maven-plugin:3.0.0:generate",
        "testgroup:maven-dependency2-plugin:3.0.0:build-classpath",
        "-e"
      });
    };
    runInClassloaderWrapper(r);
  }


  public static void runInClassloaderWrapper(final Runnable r){
    final ClWrapper cl = new ClWrapper(Thread.currentThread().getContextClassLoader());
    final Thread t = new Thread(r);
    t.setContextClassLoader(cl);
    t.start();
    while(t.isAlive()){
      try {t.join();}
      catch (final InterruptedException e) {
        t.interrupt();
      }
    }
  }

}
