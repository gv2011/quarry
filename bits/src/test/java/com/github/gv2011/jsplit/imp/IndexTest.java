package com.github.gv2011.jsplit.imp;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.gv2011.jsplit.SliceWriter;
import com.github.gv2011.quarry.cfs.HashImp;
import com.github.gv2011.quarry.util.Hash;

public class IndexTest {

  private final Map<Hash,ByteArrayOutputStream> out = new HashMap<>();

  @Test
  public void testSplit0() {
    testSplit(createData(0));
  }

  @Test
  public void testSplit1() {
    testSplit(createData(1));
  }

  @Test
  public void testSplit64() {
    testSplit(createData(64));
  }

  private byte[] createData(final int size) {
    final byte[] result = new byte[size];
    for(int i=0; i<size; i++) result[i]=(byte) i;
    return result;
  }

  private void testSplit(final byte[] data) {
    final Index index = new Index(new TestStore(),64);
    final Hash dataHash = HashImp.digest(data);
    final InputStream in = new ByteArrayInputStream(data);
    final Hash indexHash = index.split(in);
    final int expectedEntriesCount = (data.length+63)/64;

    final int len = 5+expectedEntriesCount*16;
    final byte[] expectedIndex = Arrays.copyOf(new byte[]{0,(byte) expectedEntriesCount,0,(byte) data.length,0}, len);
    if(expectedEntriesCount>0) dataHash.write(expectedIndex, 5);

    assertThat(out.get(indexHash).toByteArray(), equalTo(expectedIndex));


    final Hash expectedIndexHash = HashImp.digest(expectedIndex);
    assertThat(indexHash, equalTo(expectedIndexHash));

    if(expectedEntriesCount>0) assertThat(out.get(dataHash).toByteArray(), equalTo(data));


  }

private class TestStore implements SliceWriter {

  @Override
  public OutputStream getOutputStream(final Hash hash, final long size) {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final ByteArrayOutputStream before = out.put(hash, bos);
    assertThat(before, nullValue());
    return bos;
  }

}
}
