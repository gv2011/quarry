package com.github.gv2011.quarry.cfs;

import com.github.gv2011.quarry.bits.AbstractBytes;

public final class KeyImp extends AbstractBytes<Key> implements Key{
	
	static final int SIZE = 16;
	
	private byte[] iv;

	KeyImp(byte[] iv){
		assert iv.length==SIZE;
		this.iv = iv;
	}

	@Override
	public int size() {
		return SIZE;
	}

	@Override
	public byte getByte(int index) {
		return iv[index];
	}

	@Override
	public Class<Key> interfaze() {
		return Key.class;
	}
	
	

}
