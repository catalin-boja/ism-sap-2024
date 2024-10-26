package ro.ase.ism.sap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestCBC {
	
	public static void desEncrypt(
			String inputFile, 
			String outputFile,
			byte[] key) throws IOException {
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
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

