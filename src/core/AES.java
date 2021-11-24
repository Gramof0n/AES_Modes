package core;

public class AES {

	private CipherMode cipher;

	public AES() {

	}

	public void setMode(String mode) {
		switch (mode) {
		case "ECB":
			System.out.println("Mode set as ECB");
			cipher = new ECB();
			break;
		case "CBC":
			System.out.println("Mode set as CBC");
			cipher = new CBC();
			cipher.initIVorNonce(16);
			break;
		case "CTR":
			System.out.println("Mode set as CTR");
			cipher = new CTR();
			cipher.initIVorNonce(8);
			break;
		case "CCM":
			System.out.println("Mode set as CCM");
			cipher = new CCM();
			break;
		}
	}

	public byte[] cipher(byte[] plain, byte[] key) {

		if (cipher == null) {
			System.err.println("Mode not chosen.");
			return null;
		}
		return cipher.encrypt(plain, key);
	}

	public byte[] decipher(byte[] plain, byte[] key) {
		if (cipher == null) {
			System.err.println("Mode not chosen.");
			return null;
		}
		return cipher.decrypt(plain, key);
	}

}
