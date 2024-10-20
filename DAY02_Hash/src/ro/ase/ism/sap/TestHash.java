package ro.ase.ism.sap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TestHash {
	
	public static void printHex(byte[] values) {
		System.out.println("HEX: ");
		for(byte b : values) {
			System.out.printf(" %02x", b);
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		// TODO Auto-generated method stub

		
		//checking and using different providers - BouncyCastle
		String BouncyCastleProvider = "BC";
		
		//check if the provider is available
		Provider provider = 
				Security.getProvider(BouncyCastleProvider);
		if(provider == null) {
			System.out.println("Bouncy Castle is not available");
		} else {
			System.out.println("Bouncy Castle is available");
		}
		
		//load BC provider
		Security.addProvider(new BouncyCastleProvider());
		
		//check if the provider is available
		provider = 
				Security.getProvider(BouncyCastleProvider);
		if(provider == null) {
			System.out.println("Bouncy Castle is not available");
		} else {
			System.out.println("Bouncy Castle is available");
		}
		
		//check if the SUN provider is available
		provider = Security.getProvider("SUN");
		if(provider == null) {
			System.out.println("SUN is not available");
		} else {
			System.out.println("SUN is available");
		}
		
		String message = "ISM";
		
		//hashing a string
		MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
		//compute the hash in one step - the input is small enough
		byte[] hashValue = md.digest(message.getBytes());
		
		printHex(hashValue);
		
		md = MessageDigest.getInstance("SHA-1");
		//compute the hash in one step - the input is small enough
		hashValue = md.digest(message.getBytes());
		
		printHex(hashValue);
		
		//compute the hash of a file
		//we read all file types as binary
		File file = new File("Message.txt");
		if(!file.exists())
			System.out.println("************* The file is not there");
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		md = MessageDigest.getInstance("MD5", "BC");
		byte[] buffer = new byte[8];
		
		do {
			int noBytes = bis.read(buffer); //we try to read 8 bytes
			if(noBytes != -1) {
				md.update(buffer, 0, noBytes);
			} else {break;}
		}while(true);
		
		//get final hash
		hashValue = md.digest();
		
		bis.close();
		
		printHex(hashValue);
		
	}

}
