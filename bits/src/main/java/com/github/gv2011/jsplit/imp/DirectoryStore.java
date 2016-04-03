package com.github.gv2011.jsplit.imp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.github.gv2011.jsplit.SliceWriter;
import com.github.gv2011.quarry.util.Hash;

public class DirectoryStore implements SliceWriter{

  private final Path directory;

  public DirectoryStore(final Path directory) {
    this.directory = directory;
  }

  @Override
  public OutputStream getOutputStream(final Hash hash, final long size) {
    final Path path = directory.resolve(hash.toString()+".bin");
    try {
      return Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    catch (final IOException e) {throw new RuntimeException();}
  }

}
