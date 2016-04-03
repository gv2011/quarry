package com.github.gv2011.quarry.cfs;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.gv2011.quarry.bits.BinMath;
import com.github.gv2011.quarry.cfs.store.CryptFileImp;

public class CfsImp implements Cfs{

  private static final int BLOCK_SIZE = CryptFileImp.SIZE;
  private static final int HEADER_SIZE = 2;
  private static final int PAYLOAD_SIZE = BLOCK_SIZE-HEADER_SIZE;

  static{
    assert PAYLOAD_SIZE>=1;
    assert BinMath.twoToThePowerOf(HEADER_SIZE*8-1)>=PAYLOAD_SIZE;
  }

  private final Random random = new SecureRandom();

  private final CryptFileStore store;



  public CfsImp(final CryptFileStore store) {
    this.store = store;
  }

  @Override
  public BlockRef storeEncrypted(final InputStream in) {
    IndexBlock index = new IndexBlock();
    final byte[] buffer = new byte[BLOCK_SIZE];
    int size = readBlock(in, buffer);
    boolean done = false;
    while(!done){
      doPaddingAndHeader(buffer, size, false);
      BlockRefImp blockRef = storeEncrypted(buffer);
      index = index.add(blockRef);
      if(size<BLOCK_SIZE) done = true;
      else{
        size = readBlock(in, buffer);
        if(size==0) done = true;
        else{
          blockRef = storeEncrypted(buffer);
          index = index.add(blockRef);
        }
      }
    }
    return index.getRef();
  }


  private BlockRefImp storeEncrypted(final byte[] block) {
    try {
		final MessageDigest digest = MessageDigest.getInstance("SHA256");
		digest.digest(block);
		final BlockRefImp ref = new BlockRefImp();
		ref.hash = new HashImp(digest.digest());
		store.add(ref.hash, block);
		return ref;
	} catch (final NoSuchAlgorithmException e) {throw new RuntimeException(e);}
  }

  private void doPaddingAndHeader(final byte[] block, final int size, final boolean isIndex) {
    assert size>=0 && size <= PAYLOAD_SIZE && block.length==BLOCK_SIZE;
    final int unused = PAYLOAD_SIZE-size;
    if(unused>0){
      final byte[] tail = new byte[unused];
      random.nextBytes(tail);
      System.arraycopy(tail, 0, block, BLOCK_SIZE-unused, unused);
    }
    final int header = isIndex?-size:size;
    block[0] = (byte) (header>>8);
    block[1] = (byte) header;
  }

  private int readBlock(final InputStream in, final byte[] block) {
    boolean finished = false;
    int offset = HEADER_SIZE;
    int length = PAYLOAD_SIZE;
    int total = 0;
    while(!finished){
      int count;
      try {
        count = in.read(block, offset, length);
      } catch (final IOException e) {throw new RuntimeException(e);}
      if(count==-1) finished = true;
      else{
        total  += count;
        offset += count;
        length -= count;
        if(length==0){
          finished = true;
        }
      }
    }
    assert total>=0 && total<=PAYLOAD_SIZE;
    return total;
  }

private class IndexBlock{

  private static final int MAX_ENTRIES = PAYLOAD_SIZE/BlockRefImp.SIZE;

  private final List<BlockRefImp> blocks = new ArrayList<>();

  private IndexBlock add(final BlockRefImp blockRef) {
    if(blocks.size()<=MAX_ENTRIES){
      blocks.add(blockRef);
    }else{
      final BlockRefImp h = store();
      blocks.clear();
      blocks.add(h);
      blocks.add(blockRef);
    }
    return this;
  }

  private BlockRefImp getRef() {
    assert !blocks.isEmpty();
    if(blocks.size()==1) return blocks.get(0);
    else return store();
  }

  private BlockRefImp store() {
    final byte[] data = new byte[BLOCK_SIZE];
    final int size = buildData(data);
    doPaddingAndHeader(data, size, true);
    return storeEncrypted(data);
  }

  private int buildData(final byte[] data) {
  	int total = 0;
    for(int i=0; i<blocks.size(); i++){
		blocks.get(i).write(data, HEADER_SIZE + total);
		total += BlockRefImp.SIZE;
    }
    return total;
  }
}

}
