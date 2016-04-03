package com.github.gv2011.jsplit;

import java.io.InputStream;

import com.github.gv2011.quarry.util.Hash;

public interface SliceReader {

	InputStream get(Hash hash);

}
