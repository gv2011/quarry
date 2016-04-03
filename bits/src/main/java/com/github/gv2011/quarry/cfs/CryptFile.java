package com.github.gv2011.quarry.cfs;

import java.io.InputStream;

import com.github.gv2011.quarry.util.Hash;

public interface CryptFile {

Hash hash();

int size();

InputStream newStream();

}
