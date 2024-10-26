package ro.ase.ism.sap;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TestOTP {

	public static byte[] generateRandomKey(int keySizeInBytes) throws NoSuchAlgorithmException {
		SecureRandom secureRandom = 
				SecureRandom.getInstance("SHA1PRNG");
		byte[] random = new byte[keySizeInBytes];
		secureRandom.nextBytes(random);
		return random;
	}
	
	public static byte[] otpEncryptDecrypt(byte[] plaintext, byte[] key) {
		if(plaintext.length != key.length) {
			throw new UnsupportedOperationException(
					"Must have same size");
			
		}
		byte[] cipher = new byte[plaintext.length];
		
		for(int i = 0; i < plaintext.length; i++) {
			cipher[i] = (byte) ((byte)plaintext[i] ^ (byte)key[i]);
		}
		return cipher;		
	}
	
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		String msg = "The requirements for tomorrow are...";
		byte[] randomKey = generateRandomKey(msg.length());
		System.out.println("Random key: " + Utility.getHex(randomKey));
		
		byte[] encMsg = otpEncryptDecrypt(msg.getBytes(), randomKey);
		
		//DON'T
		//String randomKeyString = new String[randomKey];
		String randomKeyString = 
				Base64.getEncoder().encodeToString(randomKey);
		System.out.println("Random key: " + randomKeyString);
		
		System.out.println("Cipher: " + Utility.getHex(encMsg));
		
		//decryption
		byte[] initialMessage = otpEncryptDecrypt(encMsg, randomKey);
		String initialMsg = new String(initialMessage);
		
		System.out.println("Initial message: " + initialMsg);
		
		
		
	}

}
