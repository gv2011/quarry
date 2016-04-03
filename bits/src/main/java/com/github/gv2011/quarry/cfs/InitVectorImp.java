package com.github.gv2011.quarry.cfs;

import com.github.gv2011.quarry.bits.AbstractBytes;

public final class InitVectorImp extends AbstractBytes<InitVector> implements InitVector{
	
	static final int SIZE = 16;
	
	private byte[] iv;

	InitVectorImp(byte[] iv){
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
	public Class<InitVector> interfaze() {
		return InitVector.class;
	}
	
	

}
