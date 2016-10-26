package com.github.gv2011.examples.crypt;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import org.junit.Test;

public class RSAEncryption {

	@Test
	public void encryptAndDecryptTest() throws Exception {
		KeyPair keyPair = createKeyPair();

		PublicKey publicKey = keyPair.getPublic();
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, publicKey);
		final byte[] data = getData();
		final byte[] encrypted = c.doFinal(data, 0, data.length);
		assertFalse(Arrays.equals(data, encrypted));

		PrivateKey privateKey = keyPair.getPrivate();
		final Cipher cipher2 = Cipher.getInstance("RSA");
		cipher2.init(Cipher.DECRYPT_MODE, privateKey);
		final byte[] decrypted = cipher2.doFinal(encrypted, 0,
				encrypted.length);

		assertTrue(Arrays.equals(data, decrypted));
	}

	private KeyPair createKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keyPair = keyGen.genKeyPair();
		return keyPair;
	}

	@Test
	public void signAndVerifyTest() throws Exception {
		KeyPair keyPair = createKeyPair();

		Cipher c = Cipher.getInstance("RSA");
		PrivateKey privateKey = keyPair.getPrivate();
		c.init(Cipher.ENCRYPT_MODE, privateKey);

		final byte[] data = getData();
		final byte[] encrypted = c.doFinal(data, 0, data.length);

		PublicKey publicKey = keyPair.getPublic();
		final Cipher cipher2 = Cipher.getInstance("RSA");
		cipher2.init(Cipher.DECRYPT_MODE, publicKey);
		final byte[] decrypted = cipher2.doFinal(encrypted, 0,
				encrypted.length);
		assertTrue(Arrays.equals(data, decrypted));
	}

	@Test
	public void signAndVerifyTest2() throws Exception {
		KeyPair keyPair = createKeyPair();

		Signature dsa = Signature.getInstance("SHA256withRSA");
		PrivateKey privateKey = keyPair.getPrivate();
		dsa.initSign(privateKey);

		final byte[] data = getData();
		dsa.update(data);
		final byte[] signature = dsa.sign();
		assertThat(signature.length, is(256));

		Signature verifier = Signature.getInstance("SHA256withRSA");
		verifier.initVerify(keyPair.getPublic());
		verifier.update(data);
		boolean verified = verifier.verify(signature);
		assertThat(verified, is(true));

		Signature verifier2 = Signature.getInstance("SHA256withRSA");
		verifier2.initVerify(keyPair.getPublic());

		byte[] dataMod = data.clone();
		dataMod[0] = (byte) (dataMod[0] ^ 0x01);
		verifier2.update(dataMod);
		verified = verifier2.verify(signature);
		assertThat(verified, is(false));
	}

	private byte[] getData() throws NoSuchAlgorithmException {
		final byte[] data = new byte[245];
		SecureRandom random = SecureRandom.getInstanceStrong();
		random.nextBytes(data);
		return data;
	}

	@Test
	public void signAndVerifyNegativeTest() throws Exception {
		KeyPair keyPair1 = createKeyPair();
		KeyPair keyPair2 = createKeyPair();
		assertThat(keyPair1, not(is(keyPair2)));

		final byte[] data = getData();

		Cipher signer1 = Cipher.getInstance("RSA");
		PrivateKey privateKey = keyPair1.getPrivate();
		signer1.init(Cipher.ENCRYPT_MODE, privateKey);
		final byte[] signed1 = signer1.doFinal(data, 0, data.length);
		assertFalse(Arrays.equals(data, signed1));

		PublicKey publicKey = keyPair1.getPublic();
		final Cipher verifier = Cipher.getInstance("RSA");
		verifier.init(Cipher.DECRYPT_MODE, publicKey);
		final byte[] verified = verifier.doFinal(signed1, 0, signed1.length);
		assertTrue(Arrays.equals(data, verified));

		Cipher signer2 = Cipher.getInstance("RSA");
		PrivateKey privateKey2 = keyPair2.getPrivate();
		signer2.init(Cipher.ENCRYPT_MODE, privateKey2);
		final byte[] signed2 = signer2.doFinal(data, 0, data.length);
		assertFalse(Arrays.equals(data, signed2));
		assertFalse(Arrays.equals(signed1, signed2));

		final Cipher verifier2 = Cipher.getInstance("RSA");
		verifier2.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] verified2;
		try {
			verified2 = verifier2.doFinal(signed2, 0, signed2.length);
			assertFalse(Arrays.equals(data, verified2));
		} catch (BadPaddingException e) {
			// expected.
		}
	}
}
