package ro.ase.ism.sap;

import java.util.Base64;

public class TestEncoding {
	
	public static void printHex(byte[] values) {
		System.out.println("HEX: ");
		for(byte b : values) {
			System.out.printf(" %02x", b);
		}
	}
	
	public static void main(String[] args) {
		//managing binary data as strings
		
		byte[] values = 
				new byte[] {(byte)0x00, (byte)0x01, (byte)0x30, (byte)0x62 };
		byte[] values2 = 
				new byte[] {(byte)0x00, (byte)0x02, (byte)0x30, (byte)0x62 };
		//convert to String - DON'T
		String stringValue = new String(values);
		String stringValue2 = new String(values2);
		
		System.out.println("Values: " + stringValue);
		System.out.println("Values: " + stringValue2);
		
		//enconding base64
		String value1Base64 = Base64.getEncoder().encodeToString(values);
		String value2Base64 = Base64.getEncoder().encodeToString(values2);
		
		System.out.println(value1Base64);
		System.out.println(value2Base64);
		
		//decoding
		byte[] initialValues = Base64.getDecoder().decode(value1Base64);
		printHex(initialValues);
	}
}
