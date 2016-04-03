package com.github.gv2011.quarry.bits;

import java.util.AbstractList;

import com.github.gv2011.quarry.util.Lazy;

public abstract class AbstractBytes<B extends TypedBytes<B>> extends AbstractList<Byte> implements TypedBytes<B>{

	private final Lazy<Integer> hash = Lazy.create(()->Integer.valueOf(super.hashCode()*31+interfaze().hashCode()));


	@Override
	public String toString() {
		return Formatter.toString(this);
	}

	@Override
	public Byte get(final int index) {
		return Byte.valueOf(getByte(index));
	}

	@Override
	public int hashCode() {
		return hash.get().intValue();
	}

	@Override
	public int write(final byte[] buffer, final int offset) {
		for(int i=0; i<size(); i++){
			buffer[i+offset] = getByte(i);
		}
		return size();
	}

	@Override
	public boolean equals(final Object o) {
		if(this==o) return true;
		else if(!(o instanceof TypedBytes)) return false;
		else{
			if(!interfaze().equals(((TypedBytes<?>)o).interfaze())) return false;
			else if(hashCode()!=o.hashCode()) return false;
			else return super.equals(o);
		}
	}



}
