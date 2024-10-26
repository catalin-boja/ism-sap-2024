package ro.ase.ism.sap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class TestECB {
	
	public static void encrypt(String inputFile, 
			String encrypteFile, 
			byte[] key, 
			String algorithm) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		File inputF = new File(inputFile);
		if(!inputF.exists()) {
			throw new UnsupportedOperationException("File missing");
		}
		File outputF = new File(encrypteFile);
		if(!outputF.exists()) {
			outputF.createNewFile();
		}
		
		FileInputStream fis = new FileInputStream(inputF);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		FileOutputStream fos = new FileOutputStream(outputF);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		Cipher cipher = 
				Cipher.getInstance(
						algorithm+"/ECB/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		
		byte[] buffer = new byte[cipher.getBlockSize()];
		
		while(true) {
			int noBytes = bis.read(buffer);
			if(noBytes == -1) {
				break;
			}
			byte[] output = cipher.update(buffer,0,noBytes);
			bos.write(output);
		}
		byte[] output = cipher.doFinal();
		bos.write(output);
		
		bis.close();
		bos.close();	
	}
	
	public static void decrypt(String inputFile,
			String outputFile, byte[] key, String algorithm) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		File inputF = new File(inputFile);
		if(!inputF.exists()) {
			throw new UnsupportedOperationException("No File");
		}
		File outputF = new File(outputFile);
		if(!outputF.exists()) {
			outputF.createNewFile();
		}
		
		FileInputStream fis = new FileInputStream(inputF);
		FileOutputStream fos = new FileOutputStream(outputF);
		
		Cipher cipher = Cipher.getInstance(
				algorithm+"/ECB/PKCS5Padding");
		SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		
		byte[] buffer = new byte[cipher.getBlockSize()];
		while(true) {
			int noBytes = fis.read(buffer);
			if(noBytes == -1) {
				break;
			}
			byte[] output = cipher.update(buffer);
			fos.write(output);
		}
		byte[] output = cipher.doFinal();
		fos.write(output);
		
		fis.close();
		fos.close();
	}
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		encrypt("Message.txt", 
				"Message.enc", 
				"ism12345password".getBytes(), 
				"AES");
		
		decrypt("Message.enc", 
				"MessageCopy.txt", 
				"ism12345password".getBytes(),
				"AES");
		
		System.out.println("The end");
	}

}
