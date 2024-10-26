package ro.ase.ism.sap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class TestHMAC {

	public static byte[] getHMAC(
		String fileName, String algorithm, String password) 
				throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		
		Mac hmac = Mac.getInstance(algorithm);
		SecretKeySpec key = new SecretKeySpec(
				password.getBytes(), algorithm);
		hmac.init(key);
		
		//read the file and process it
		File inputFile = new File(fileName);
		if(!inputFile.exists()) {
			throw new UnsupportedOperationException("File is missing");
		}
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		byte[] buffer = new byte[8];
		while(true) {
			int noBytes = bis.read(buffer);
			if(noBytes == -1) {
				break;
			}
			hmac.update(buffer, 0, noBytes);
		}
		
		fis.close();
		
		byte[] result = hmac.doFinal();
		
		return result;
	}
	
	public static byte[] getPBKDF(
			String userPass, 
			String algorithm, 
			String salt, 
			int noIterations,
			int outputSize) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		PBEKeySpec pbeKeySpec = 
				new PBEKeySpec(userPass.toCharArray(),
						salt.getBytes(), 
						noIterations, 
						outputSize);
		SecretKeyFactory pbkdf = 
				SecretKeyFactory.getInstance(algorithm);
		
		SecretKey key = pbkdf.generateSecret(pbeKeySpec);
		
		return key.getEncoded();
		
	}
	
	
	
	public static void main(String[]  args) throws InvalidKeyException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		
		//test hmac
		byte[] hmac = getHMAC("Message.txt", "HmacSHA1", "ism1234");
		System.out.println("HMAC: "  + Utility.getHex(hmac));
		hmac = getHMAC("Message.txt", "HmacSHA1", "ism12345");
		System.out.println("HMAC: "  + Utility.getHex(hmac));
		
		//test pbkdf
		byte[] key = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", 
				"rd@h1", 1000, 160);
		System.out.println(
				"Value stored in DB:" + Utility.getHex(key));
		key = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", 
				"dfhfghj", 1000, 160);
		System.out.println(
				"Value stored in DB:" + Utility.getHex(key));
		
		//benchmark PBKDF performance
		int noIterations = (int) ((int) 3*1e5);
		double tStart = System.currentTimeMillis();
		key = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", 
				"dfhfghj", noIterations, 160);
		double tFinal = System.currentTimeMillis();
		
		System.out.printf(
				"PBKDF with %d iterations computed in %f seconds",
				noIterations, (tFinal - tStart)/1000);
		System.out.println(
				"\n Value stored in DB:" + Utility.getHex(key));
		
	}
	
}
