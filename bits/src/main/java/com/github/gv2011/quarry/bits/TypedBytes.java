package com.github.gv2011.quarry.bits;

public interface TypedBytes<B extends TypedBytes<B>> extends Bytes{

	Class<B> interfaze();

}
