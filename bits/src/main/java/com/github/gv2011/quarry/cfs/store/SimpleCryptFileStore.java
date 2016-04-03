package com.github.gv2011.quarry.cfs.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;

import com.github.gv2011.quarry.cfs.CryptFile;
import com.github.gv2011.quarry.cfs.CryptFileStore;
import com.github.gv2011.quarry.util.Hash;

public class SimpleCryptFileStore implements CryptFileStore{

private final Path directory;

	public SimpleCryptFileStore(final Path directory) {
		this.directory = directory;
	}

  @Override
  public CryptFile add(final Hash hash, final byte[] encryptedBlock) {
  	final Path p = path(hash);
  	final CryptFile result = getFile(p,hash);
  	if(!Files.exists(p)){
  		try {
  			Files.write(p, encryptedBlock);
  		} catch (final IOException e) {throw new RuntimeException(e);}
  	}
  	return result;
  }

  private Path path(final Hash hash) {
    return directory.resolve(hash.toString());
  }

  @Override
  public Optional<CryptFile> get(final Hash hash) {
    final Path p = path(hash);
    if (Files.exists(p)) return Optional.of(getFile(p,hash));
    else return Optional.empty();
  }

	private CryptFile getFile(final Path p, final Hash hash) {
		return new CryptFileImp(
			() -> {
				try {return Files.newInputStream(p);} catch (final IOException e) {throw new RuntimeException(e);}
			},
			() -> hash
		);
	}

  @Override
  public Iterator<CryptFile> getAll() {
    assert false:"auto generated";
    return null;
  }

}
