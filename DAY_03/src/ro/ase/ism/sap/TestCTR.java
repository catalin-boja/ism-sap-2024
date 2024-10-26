package ro.ase.ism.sap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TestCTR {

	public static void desEncrypt(
			String inputFile, 
			String outputFile,
			byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		File inputF = new File(inputFile);
		if(!inputF.exists()) {
			throw new UnsupportedOperationException("No FILE");
		}
		File outputF = new File(outputFile);
		if(!outputF.exists()) {
			outputF.createNewFile();
		}
		FileInputStream fis = new FileInputStream(inputF);
		FileOutputStream fos = new FileOutputStream(outputF);
		
		Cipher cipher = Cipher.getInstance("DES/CTR/NoPadding");
		SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
		
		byte[] buffer = new byte[cipher.getBlockSize()];
		
		//IV values:
		//1. hard coded known value
		//2. known value or any value stored 
		//	in the ciphertext as the 1st block
		
		//option 2
		//IV has the 3rd byte with all bits 1
		byte[] IV = new byte[cipher.getBlockSize()];
		IV[2] = (byte) 0xFF;
		
		//write IV into file
		fos.write(IV);
		
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivSpec);
		
		while(true) {
			int noBytes = fis.read(buffer);
			if(noBytes == -1) {
				break;
			}
			byte[] output = cipher.update(buffer, 0, noBytes);
			fos.write(output);
		}
		
		byte[] output = cipher.doFinal();
		fos.write(output);
		
		fis.close();
		fos.close();
		
	}
	
	public static void desDecrypt(
			String inputFile, String outputFile, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		File inputF = new File(inputFile);
		if(!inputF.exists()) {
			throw new UnsupportedOperationException("No File");
		}
		File outputF = new File(outputFile);
		if(!outputF.exists()){
			outputF.createNewFile();
		}
		
		FileInputStream fis  = new FileInputStream(inputF);
		FileOutputStream fos = new FileOutputStream(outputF);
		
		Cipher cipher = Cipher.getInstance("DES/CTR/NoPadding");
		
		//read IV
		byte[] IV = new byte[cipher.getBlockSize()];
		fis.read(IV);
		
		
		SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.DECRYPT_MODE, keySpec,ivSpec);
		
		byte[] buffer = new byte[cipher.getBlockSize()];
		while(true) {
			int noBytes = fis.read(buffer);
			if(noBytes == -1) {
				break;
			}
			byte[] output = cipher.update(buffer,0,noBytes);
			fos.write(output);
		}
		byte[] output = cipher.doFinal();
		fos.write(output);
		
		fis.close();
		fos.close();
		
	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		// TODO Auto-generated method stub

		desEncrypt("Message.txt", 
				"desCipher.enc", 
				"ism12345".getBytes());
		
		desDecrypt("desCipher.enc",
				"InitialMessage.txt",
				"ism12345".getBytes());
		
		System.out.println("The end");
	}

}
