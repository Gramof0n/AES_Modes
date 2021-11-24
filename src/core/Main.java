package core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import utils.Formating;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		RijndaelEngine re = new RijndaelEngine();
		Random r = new Random();
//
		String test = "Hello world how are you now atm this moment";
		byte[] strBytes = new byte[16];
		byte[] keyBytes = new byte[16];

		File f = new File("C:\\Users\\Korisnik\\Desktop\\mau_slikice\\image00015.jpeg");
		byte[] fileBytes = Files.readAllBytes(f.toPath());

		r.nextBytes(keyBytes);
		strBytes = Formating.hexStr2Byte(Formating.stringToHex(test));

		System.out.println("KEY FOR ALL CIPHERS: " + Formating.byte2HexStr(keyBytes));

//		ECB ecb = new ECB();
//		CBC cbc = new CBC();
//		CTR ctr = new CTR();
//
//		byte[] enc = ecb.encrypt(strBytes, keyBytes);
//
//		System.out.println("==================ENKRIPTOVANO===================");
//		System.out.println(new String(enc));
//		System.out.println(Formating.byte2HexStr(enc));
//		System.out.println("=================================================");
//
//		System.out.println("\n==================DEKRIPTOVANO===================");
//		System.out.println(new String(ecb.decrypt(enc, keyBytes)));
//		System.out.println("===================================================");
//
//		System.out.println("\n\n=====================CBC======================");
//
//		cbc.initIVorNonce(16);
//		byte[] cbcEnc = cbc.encrypt(strBytes, keyBytes);
//
//		System.err.println("CBC ENKRIPTOVANO: " + Formating.byte2HexStr(cbcEnc));
//		byte[] cbcDec = cbc.decrypt(cbcEnc, keyBytes);
//		System.err.println("CBC DEKRIPTOVANO: " + Formating.byte2HexStr(cbcDec));
//		System.err.println("CBC STRING: " + new String(cbcDec));
//		System.out.println("\n");
//		cbcEnc = cbc.encrypt(strBytes, keyBytes);
//		System.out.println("CBC ENKRIPTOVANO: " + Formating.byte2HexStr(cbcEnc));
//
//		System.out.println("\n\n=========================CTR===========================");
//		ctr.initIVorNonce(8);
//		byte[] ctrEnc = ctr.encrypt(strBytes, keyBytes);
//
//		System.out.println("==================ENKRIPTOVANO===================");
//		System.out.println(new String(ctrEnc));
//		System.out.println("=================================================");
//
//		System.out.println("\n==================DEKRIPTOVANO===================");
//		System.out.println(new String(ctr.decrypt(ctrEnc, keyBytes)));
//		System.out.println("===================================================");
//
//		System.out.println("\n\n===========================CCM============================");
//		CCM ccm = new CCM();
//
//		byte[] ccmEnc = ccm.encrypt(strBytes, keyBytes);
//
//		byte[] ccmDec = ccm.decrypt(ccmEnc, keyBytes);
//
//		System.out.println("Dekriptovano iz CCM-a: " + new String(ccmDec));

		AES aes = new AES();

		aes.setMode("ECB");
		System.out.println(new String(aes.cipher(strBytes, keyBytes)));

	}

}
