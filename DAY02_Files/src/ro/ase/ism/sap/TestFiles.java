package ro.ase.ism.sap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class TestFiles {
	
	public static void listFolder(File repository) {
		if(repository.exists() && repository.isDirectory()) {
			//print location content
			File[] items = repository.listFiles();
			for(File item : items) {
				System.out.println(item.getName() + " - " +
						(item.isFile() ? " FILE" : "FOLDER"));
				System.out.println(item.getAbsolutePath());
				if(item.isDirectory())
					listFolder(item);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//file system management
		File repository = new File("D:\\2024-2025\\ism-sap-2024");
//		if(repository.exists() && repository.isDirectory()) {
//			//print location content
//			File[] items = repository.listFiles();
//			for(File item : items) {
//				System.out.println(item.getName() + " - " +
//						(item.isFile() ? " FILE" : "FOLDER"));
//				System.out.println(item.getAbsolutePath());
//			}
//		}
		
		listFolder(repository);
		
		//text files
		//reading & writing
		
		File msgFile = new File("Message.txt");
		if(!msgFile.exists()) {
			msgFile.createNewFile();
		}
		
		//write into a text file, append mode
		FileWriter fileWriter = new FileWriter(msgFile, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println("This is a secret message");
		printWriter.println("Don't tell anyone");
		printWriter.close();
		
		//reading from text files
		FileReader fileReader = new FileReader(msgFile);
		BufferedReader bufferReader = new BufferedReader(fileReader);
		System.out.println("File content");
		String line = null;
		do {
			line = bufferReader.readLine();
			if(line != null)
				System.out.println(line);
		}while(line != null);
		
//		while((line = bufferReader.readLine())!= null) {
//		
//	}
	
		
		fileReader.close();
		
		//binary files
		File binaryFile = new File("myData.bin");
		if(!binaryFile.exists()) {
			binaryFile.createNewFile();
		}
		
		float floatValue = 23.5f;
		double doubleValue = 10;
		int intValue = 10;
		boolean flag = true;
		String text = "Hello";
		byte[] values = new byte[]{(byte)0xff, (byte)0x0A};
		
		FileOutputStream fos = new FileOutputStream(binaryFile);
		DataOutputStream dos = new DataOutputStream(fos);
		
		dos.writeFloat(floatValue);
		dos.writeDouble(doubleValue);
		dos.writeInt(intValue);
		dos.writeBoolean(flag);
		dos.writeUTF(text);
		dos.write(values); //we forgot to put the size of the array before it
		
		dos.close();
		
		//read binary file
		FileInputStream fis = new FileInputStream(binaryFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
		
		floatValue = dis.readFloat();
		doubleValue = dis.readDouble();
		intValue = dis.readInt();
		flag = dis.readBoolean();
		text = dis.readUTF();
		values = dis.readAllBytes(); //we know the array is the last one
		
		dis.close();
		
		System.out.println(intValue);
		System.out.println(text);
		
		RandomAccessFile raf = 
				new RandomAccessFile(binaryFile, "r");
		raf.seek(12); //jump the float and the double
		int vb = raf.readInt();
		raf.close();
		
		System.out.println(vb);
		

		
		
	}

}
