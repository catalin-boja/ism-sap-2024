package ro.ase.ism.sap;

public class TestStrings {
	
	static String getByteUnsignedHexRepresentation(byte value) {
		String hex = Integer.toHexString(Byte.toUnsignedInt(value));
		if(hex.length() == 1)
			hex = "0" + hex;
		return hex;
	}
	
	static String getHexFromByteArray(byte[] values) {
		StringBuilder sb = new StringBuilder();
		for(byte value : values) {
			sb.append(String.format("%02x", value));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		
		String file1 = "Keys.txt";
		String file2 = "Keys.txt";
		
		if(file1 == file2) {
			System.out.println("The files are the same");
		}
		else {
			System.out.println("The files are different");
		}
		
		file2 = new String("Keys.txt");
		if(file1 == file2) {
			System.out.println("The files are the same");
		}
		else {
			System.out.println("The files are different");
		}
		
		if(file1.equals(file2)) {
			System.out.println("The files are the same");
		}
		else {
			System.out.println("The files are different");
		}
		
		//small numbers from 0 to 127 are managed by a Int constant pool
		int vb = 10;
		
//		Integer intVb = 10;
//		Integer intVb2 = 10;
		
		Integer intVb = 128;
		Integer intVb2 = 128;
		
		if(intVb == intVb2) {
			System.out.println("The 2 numbers are the same");
		} else {
			System.out.println("The 2 numbers are different");
		}
		
		if(intVb.equals(intVb2)) {
			System.out.println("The 2 numbers are the same");
		} else {
			System.out.println("The 2 numbers are different");
		}
		
		//converting numbers to strings
		int value = 33;
		String binaryRep = Integer.toBinaryString(value);
		String hexRep = Integer.toHexString(value);
		
		System.out.println("Binary string: " + binaryRep);
		System.out.println("Hex string: " + hexRep);

		byte smallValue = 23;
		System.out.println("Binary string: " +Integer.toBinaryString(smallValue));
		System.out.println("Hex string: " + Integer.toHexString(smallValue));
		

		///from string to numbers
		Integer initialValue = Integer.parseInt(hexRep,16);
		System.out.println("Initial value is: " + initialValue);
		initialValue = Integer.parseInt(binaryRep,2);
		System.out.println("Initial value is: " + initialValue);
		
		
		byte smallValue2 = -23;
		System.out.println("Binary string: " +Integer.toBinaryString(smallValue2));
		System.out.println("Hex string: " + Integer.toHexString(smallValue2));
			
		
		System.out.println(	Byte.toUnsignedInt(smallValue2));
		
		
		System.out.println(
				"Binary string: " + 
		Integer.toBinaryString(Byte.toUnsignedInt(smallValue2)));
		System.out.println("Hex string: " + 
		Integer.toHexString(Byte.toUnsignedInt(smallValue2)));
		
	
		byte[] hash = {(byte)23, (byte)-23, (byte)10, (byte)5};
		//wrong way
		String hashHexString = "";
		for(int i = 0; i < hash.length; i++) {
			hashHexString += Integer.toHexString(hash[i]);
		}
		
		System.out.println("The hash is " + hashHexString);
		
		
		//byte[] hash = {(byte)23, (byte)-23, (byte)10, (byte)5};
		//wrong way - because small byte values are converted to a single hex symbol
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < hash.length; i++) {
			sb.append(Integer.toHexString(Byte.toUnsignedInt(hash[i])));
		}
		System.out.println("The hash is " + sb.toString().toUpperCase());
		
		//ok
		sb = new StringBuilder();
		for(int i = 0; i < hash.length; i++) {
			sb.append(getByteUnsignedHexRepresentation(hash[i]));
		}
		System.out.println("The hash is " + sb.toString().toUpperCase());
		
		System.out.println("The hash is " + getHexFromByteArray(hash));
		
	}

}
