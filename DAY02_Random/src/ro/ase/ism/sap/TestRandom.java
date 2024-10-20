package ro.ase.ism.sap;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TestRandom {

	public static void printHex(byte[] values) {
		System.out.println("HEX: ");
		for(byte b : values) {
			System.out.printf(" %02x", b);
		}
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {

		//use crypto safe PRNG
		//DON't USE Random
		
		SecureRandom secureRandom = 
				SecureRandom.getInstance("SHA1PRNG");
		byte[] desKey = new byte[8];
		
		//random, you don't get this value again
		//secureRandom.nextBytes(desKey);
		//printHex(desKey);
		
		//using a seed
		secureRandom.setSeed(
				new byte[] {(byte)0xFF, (byte)0xA8});
		secureRandom.nextBytes(desKey);
		
		printHex(desKey);
		
		//destination
		SecureRandom secureRandom2 = 
				SecureRandom.getInstance("SHA1PRNG");
		byte[] desKey2 = new byte[8];
		secureRandom2.setSeed(
				new byte[] {(byte)0xFF, (byte)0xA8});
		secureRandom2.nextBytes(desKey2);
		
		printHex(desKey2);
		
	}

}
