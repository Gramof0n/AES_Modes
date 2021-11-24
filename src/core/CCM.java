package core;

import utils.Formating;

public class CCM extends CipherMode {

	private CTR ctr = new CTR();
	private CBC cbc = new CBC();

	public CCM() {

	}

	public byte[] generateMAC(byte[] plain, byte[] key) {
		cbc.initIVorNonce(16);

		byte[] cbcEnc = cbc.encrypt(plain, key);

		byte[] MAC = new byte[16];
		int j = 0;

		for (int i = cbcEnc.length - 16; i < cbcEnc.length; i++) {
			MAC[j++] = cbcEnc[i];
		}

		return MAC;
	}

	@Override
	public byte[] encrypt(byte[] plain, byte[] key) {
		byte[] mac = generateMAC(plain, key);

		byte[] plainPlusMac = Formating.concatenateByteArrays(plain, mac);

		byte[] enc = ctr.encrypt(plainPlusMac, key);

		return enc;
	}

	@Override
	public byte[] decrypt(byte[] cipher, byte[] key) {
		byte[] plainPlusMacDec = ctr.decrypt(cipher, key);
		byte[] cipherMac = new byte[16];
		int j = 0;

		byte[] plainWithoutMac = new byte[plainPlusMacDec.length - 16];

		for (int i = 0; i < plainWithoutMac.length; i++) {
			plainWithoutMac[i] = plainPlusMacDec[i];
		}

		for (int i = plainWithoutMac.length; i < plainPlusMacDec.length; i++) {
			cipherMac[j++] = plainPlusMacDec[i];
		}

		// Opet sifriraj cbcom pa poredi kraj sa macom jelte

		byte[] cbcEnc = cbc.encrypt(plainWithoutMac, key);

		byte[] ogMac = new byte[16];
		j = 0;

		System.out.println("\ncbcEnc.length = " + cbcEnc.length);

		for (int i = cbcEnc.length - ogMac.length; i < cbcEnc.length; i++) {
			ogMac[j++] = cbcEnc[i];
		}

		System.out.println("\nOG mac skinut sa kraja sifriranog: " + Formating.byte2HexStr(ogMac));

		if (!Formating.byte2HexStr(ogMac).equals(Formating.byte2HexStr(cipherMac))) {
			System.err.println("Macs don't match. Message has been tampered with");
			return null;
		}

		return plainWithoutMac;
	}

	@Override
	public void initIVorNonce(int blockSize) {
		// TODO Auto-generated method stub

	}

}
