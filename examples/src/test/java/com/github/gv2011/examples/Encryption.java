package com.github.gv2011.examples;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;



public class Encryption {

@Test
public void encryptAndDecryptTest() throws Exception{
  final Random random =  new SecureRandom();
  final byte[] ivb = new byte[16];
  random.nextBytes(ivb);
  final IvParameterSpec iv = new IvParameterSpec(ivb);
  final byte[] key = new byte[32];
  random.nextBytes(key);
  final SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//  PKCS5PADDING
  final Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
  cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

  final byte[] data = new byte[8192];
  random.nextBytes(data);
  final byte[] encrypted = cipher.doFinal(data, 0, data.length);

  final Cipher cipher2 = Cipher.getInstance("AES/CBC/NOPADDING");
  cipher2.init(Cipher.DECRYPT_MODE, skeySpec, iv);
  final byte[] decrypted = cipher2.doFinal(encrypted, 0, encrypted.length);


  assertTrue(Arrays.equals(data, decrypted));

  assertThat(encrypted.length, is(8192));
}

}
