package com.github.gv2011.quarry.cfs;

import java.io.InputStream;

public interface Cfs {

BlockRef storeEncrypted(InputStream stream);

}
