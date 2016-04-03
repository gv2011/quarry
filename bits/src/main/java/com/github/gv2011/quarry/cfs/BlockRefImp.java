package com.github.gv2011.quarry.cfs;


import com.github.gv2011.quarry.bits.AbstractBytes;
import com.github.gv2011.quarry.util.Hash;

class BlockRefImp extends AbstractBytes<BlockRef> implements BlockRef{
	
	static final int SIZE = HashImp.SIZE+InitVectorImp.SIZE+KeyImp.SIZE;
	
	Hash hash;
	InitVector initVector;
	Key key;
	
	@Override
	public Class<BlockRef> interfaze() {
		return BlockRef.class;
	}

	@Override
	public byte getByte(int index) {
		if(index<HashImp.SIZE) return hash.getByte(index);
		else if(index<HashImp.SIZE+InitVectorImp.SIZE) return initVector.getByte(index-HashImp.SIZE);
		else return key.getByte(index-(HashImp.SIZE+InitVectorImp.SIZE));
	}

	@Override
	public int size() {
		return SIZE;
	}
}
