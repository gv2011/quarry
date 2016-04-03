package com.github.gv2011.jsplit;

import java.io.OutputStream;

import com.github.gv2011.quarry.util.Hash;

public interface SliceWriter {

	OutputStream getOutputStream(Hash hash, long size);

}
