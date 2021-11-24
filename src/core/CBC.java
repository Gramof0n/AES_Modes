package core;

import java.util.Random;

import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import utils.Formating;

public class CBC extends CipherMode {

	private RijndaelEngine re;
	private byte[] IV;

	public CBC() {
		re = new RijndaelEngine();
	}

	public byte[] getIV() {
		return this.IV;
	}

	public void setIV(byte[] iv) {
		this.IV = iv;
	}

	@Override
	public void initIVorNonce(int blockSize) {
		IV = new byte[blockSize];
		Random r = new Random();
		r.nextBytes(IV);
	}

	private byte[] xorBlocks(byte[] block1, byte[] block2, int offset) {
		for (int i = 0; i < 16; i++) {
			block1[i + offset] = (byte) (block1[i + offset] ^ block2[i]);
		}

		return block1;
	}

	public byte[] encrypt(byte[] plain, byte[] keyBytes) {
		if (this.IV == null) {
			System.err.println("IV not initialized");
			return null;
		}
		re.init(true, new KeyParameter(keyBytes));

		plain = Formating.padWithPKCS7(plain, 16);

		byte[] xored = new byte[plain.length];
		byte[] ciphered = new byte[plain.length];

		xored = xorBlocks(plain, this.IV, 0);
		re.processBlock(xored, 0, ciphered, 0);

		System.err.println("Plain length: " + plain.length);

		for (int i = 16; i < plain.length; i += 16) {
			xored = xorBlocks(xored, ciphered, i);
			re.processBlock(xored, i, ciphered, i);

		}
		return ciphered;
	}

	public byte[] decrypt(byte[] cipher, byte[] keyBytes) {

		re.init(false, new KeyParameter(keyBytes));

		byte[] decrypted = new byte[cipher.length];

		re.processBlock(cipher, 0, decrypted, 0);
		decrypted = xorBlocks(decrypted, this.IV, 0);

		for (int i = 16; i < cipher.length; i += 16) {
			re.processBlock(cipher, i, decrypted, i);
			decrypted = xorBlocks(decrypted, cipher, i);
		}

		decrypted = Formating.unpadPKCS7(decrypted, 16);

		return decrypted;
	}

}
