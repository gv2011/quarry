package com.github.gv2011.quarry.bits;

import java.util.List;

public interface Bytes extends List<Byte>{

	byte getByte(int index);

	int write(byte[] buffer, int offset);

}
