package com.github.gv2011.quarry.cfs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.gv2011.quarry.util.Hash;

public class HashImpTest {

  @Test
  public void testSize() {
     final Hash hash = HashImp.digest(new byte[1000], 0, 1000);
    assertThat(hash.size(), is(16));
  }

  @Test
  public void testDigest() {
    final Hash hash = HashImp.digest(new byte[1000], 0, 1000);
    assertThat(hash.toString(), is("E3B0C44298FC1C149AFBF4C8996FB924"));
  }

  @Test
  public void testGetByte() {
    final Hash hash = HashImp.digest(new byte[1000], 0, 1000);
    assertThat(hash.getByte(3), is((byte)0x42));
    assertThat(hash.getByte(0), is((byte)0xE3));
  }

}
