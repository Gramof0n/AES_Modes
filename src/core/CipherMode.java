package core;

public abstract class CipherMode {

	public abstract byte[] encrypt(byte[] plain, byte[] key);

	public abstract byte[] decrypt(byte[] plain, byte[] key);

	public abstract void initIVorNonce(int blockSize);

}
