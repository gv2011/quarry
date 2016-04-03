package com.github.gv2011.jsplit;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.gv2011.quarry.util.Hash;

public interface Splitter {

	Hash split(InputStream in, SliceWriter sliceSink);

	void join(Hash ref, SliceReader sliceSource, OutputStream out);

}
