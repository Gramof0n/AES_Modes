package core;

import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import utils.Formating;

public class ECB extends CipherMode {
	private RijndaelEngine re;

	public ECB() {
		this.re = new RijndaelEngine();
	}

	@Override
	public byte[] encrypt(byte[] plain, byte[] keyBytes) {

		byte[] padded = Formating.padWithPKCS7(plain, 16);
		byte[] output = new byte[padded.length];

		re.init(true, new KeyParameter(keyBytes));

		for (int i = 0; i < padded.length; i += 16) {
			re.processBlock(padded, i, output, i);
		}

		return output;
	}

	@Override
	public byte[] decrypt(byte[] cipher, byte[] keyBytes) {
		byte[] output = new byte[cipher.length];

		re.init(false, new KeyParameter(keyBytes));

		for (int i = 0; i < cipher.length; i += 16) {
			re.processBlock(cipher, i, output, i);
		}

		byte[] unpadded = Formating.unpadPKCS7(output, 16);

		return unpadded;
	}

	@Override
	public void initIVorNonce(int blockSize) {
		// TODO Auto-generated method stub

	}
}
