package com.github.gv2011.quarry.cfs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.github.gv2011.quarry.bits.AbstractBytes;
import com.github.gv2011.quarry.util.Hash;

public final class HashImp extends AbstractBytes<Hash> implements Hash{

	public static final int SIZE = 16;

  @Override
  public int write(final byte[] buffer, final int offset) {
    final int size = super.write(buffer, offset);
    //contract:
    assert size == SIZE;
    return size;
  }

  public static Hash digest(final byte[] input) {
    return digest(input, 0, input.length);
  }


  public static Hash digest(final byte[] input, final int offset, final int len) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(input, offset, len);
      return new HashImp(digest.digest());
    }
    catch (final NoSuchAlgorithmException e) {throw new RuntimeException(e);}
  }

  public static Hash parse(final String hexString) {
    assert hexString.length()==SIZE*2;
    //contract.
    final byte[] digest = new byte[SIZE];
    for(int i=0; i<SIZE; i++){
      digest[i] = (byte) Short.parseShort(hexString.substring(i*2,(i+1)*2), 16);
    }
    return new HashImp(digest);
  }


	private final byte[] digest;

	private HashImp(final byte[] digest){
		assert digest.length==SIZE;
		this.digest = digest;
	}

	@Override
	public int size() {
		return SIZE;
	}

	@Override
	public byte getByte(final int index) {
		return digest[index];
	}

	@Override
	public Class<Hash> interfaze() {
		return Hash.class;
	}





}
