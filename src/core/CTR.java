package core;

import java.util.Random;

import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import utils.Formating;

public class CTR extends CipherMode {

	private byte[] nonce;
	private RijndaelEngine re;
	private long counter;
	private byte[] iv;

	public CTR() {
		re = new RijndaelEngine();
		nonce = new byte[8];
		counter = 0;
		iv = new byte[8];
	}

	@Override
	public void initIVorNonce(int blockSize) {
		Random r = new Random();

		r.nextBytes(nonce);
	}

	private byte[] generateKeystream(byte[] key, int plaintextLength, byte[] nonce) {
		if (nonce == null) {
			System.err.println("Nonce not initialized");
			return null;
		}
		counter = 0;
		re.init(true, new KeyParameter(key));
		byte[] keystream = new byte[plaintextLength];

		String ivString = Formating.byte2HexStr(nonce) + String.format("%016x", counter);

		for (int i = 0; i < plaintextLength; i++) {
			ivString = Formating.byte2HexStr(nonce) + String.format("%016x", counter);
			re.processBlock(Formating.hexStr2Byte(ivString), 0, keystream, i);

			if (keystream[keystream.length - 1] != 0) {
				break;
			}

			counter++;
		}

		this.iv = Formating.hexStr2Byte(ivString);

		// Keystream je malo predugacak iskreno, valjalo bi ga skratit NEKAKO
		// Nalijepi nonce na kraj ciphertexta
		return keystream;
	}

	@Override
	public byte[] encrypt(byte[] plaintext, byte[] key) {
		byte[] keystream = generateKeystream(key, plaintext.length, this.nonce);
		byte[] enc = new byte[plaintext.length];

		for (int i = 0; i < plaintext.length; i++) {
			enc[i] = (byte) (keystream[i] ^ plaintext[i]);
		}

		return Formating.concatenateByteArrays(enc, this.iv);

	}

	@Override
	public byte[] decrypt(byte[] cipher, byte[] key) {
		byte[] ivBytes = new byte[16];
		byte[] nonce = new byte[8];
		int j = 0;

		for (int i = cipher.length - 16; i < cipher.length; i++) {
			ivBytes[j++] = cipher[i];
		}

		for (int i = 0; i < ivBytes.length / 2; i++) {
			nonce[i] = ivBytes[i];
		}
		byte[] keystream = generateKeystream(key, cipher.length - 16, nonce);

		byte[] dec = new byte[cipher.length - 16];

		for (int i = 0; i < cipher.length - 16; i++) {
			dec[i] = (byte) (keystream[i] ^ cipher[i]);
		}

		return dec;
	}

}
