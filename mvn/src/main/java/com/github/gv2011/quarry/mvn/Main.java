package com.github.gv2011.quarry.mvn;

import java.io.FileInputStream;
import java.io.InputStream;

import org.codehaus.plexus.classworlds.launcher.Launcher;

import com.github.gv2011.util.FileUtils;

public class Main {

//  "C:\programs\java\jdk8\bin\java.exe"    -classpath "C:\programs\apache-maven-3.3
//  .9\bin\..\boot\plexus-classworlds-2.5.2.jar" "-Dclassworlds.conf=C:\programs\apa
//  che-maven-3.3.9\bin\..\bin\m2.conf" "-Dmaven.home=C:\programs\apache-maven-3.3.9
//  \bin\.." "-Dmaven.multiModuleProjectDirectory=C:\Users\
//  u1" org.codehaus.plexus.classworlds.launcher.Launcher -version

  public static void main(final String[] args) throws Exception {
    System.setProperty("maven.home", "C:\\programs\\apache-maven-3.3.9");
    System.setProperty("maven.multiModuleProjectDirectory", FileUtils.WORK_DIR.toString());
    System.exit(launch(new String[]{"dependency:build-classpath"}));
  }

  public static int launch(final String[] args) throws Exception {
    final Launcher launcher = new Launcher();
    launcher.setSystemClassLoader(Thread.currentThread().getContextClassLoader());
    try(InputStream is = new FileInputStream("C:\\programs\\apache-maven-3.3.9\\bin\\m2.conf")){
      launcher.configure(is);
    }
    launcher.launch(args);
    return launcher.getExitCode();
  }
//  this.launcher.setAppMain( mainClassName, mainRealmName );
//  org.apache.maven.cli.MavenCli plexus.core

  //systemProperties.setProperty(maven.home,C:\\Users\\u1/m2
}
