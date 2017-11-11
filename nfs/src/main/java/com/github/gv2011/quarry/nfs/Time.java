package com.github.gv2011.quarry.nfs;

import java.time.Instant;

public interface Time extends Value{

  Instant toInstant();

}
