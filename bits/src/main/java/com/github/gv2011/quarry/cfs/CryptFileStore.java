package com.github.gv2011.quarry.cfs;

import java.util.Iterator;
import java.util.Optional;

import com.github.gv2011.quarry.util.Hash;

public interface CryptFileStore {

CryptFile add(Hash hash, byte[] encryptedBlock);

Optional<CryptFile> get(Hash hash);

Iterator<CryptFile> getAll();

}
