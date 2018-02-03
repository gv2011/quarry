package com.github.gv2011.quarry.jsvc;

import static com.github.gv2011.util.FileUtils.path;

import com.github.gv2011.util.FileUtils;
import com.github.gv2011.util.ResourceUtils;
import com.github.gv2011.util.StreamUtils;

public class CustomizeScript {

  public static void main(final String[] args) {
    String template = StreamUtils.readText(
      ResourceUtils.tryGetResourceUrl("testdaemon.sh").get()::openStream
    );
    final String name = "testdaemon";
    template = template.replace("<NAME>", name);

    final String description = "Example init script";
    template = template.replace("<DESCRIPTION>", description);

    final String command = "'java -jar /root/quarry-jsvc-0.0.1-SNAPSHOT.jar'";
    template = template.replace("<COMMAND>", command);

    final String username = "root";
    template = template.replace("<USERNAME>", username);

    System.out.println(template);
    FileUtils.writeText(template, path(name+".sh"));
  }

}
