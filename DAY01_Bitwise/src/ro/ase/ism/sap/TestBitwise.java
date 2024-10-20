package ro.ase.ism.sap;

public class TestBitwise {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		byte value = 15;
		System.out.println("Value is " + value);
		value = 0b00001111;
		System.out.println("Value is " + value);
		value = 0x0F;
		System.out.println("Value is " + value);
		
		value = 1 << 3 | 1 << 2 | 1 << 1 | 1;
		System.out.println("Value is " + value);
		
		value = 8;
		value = (byte) (value << 1); //multiply by 2
		System.out.println("Value is " + value);
		
		value = (byte) (value >> 1); //divide by 2
		System.out.println("Value is " + value);
		
		value = 65;
		value = (byte) (value << 1); 
		System.out.println("Value is " + value);
		
		value = -1;
		System.out.println(String.format("%02x", value));
		value = (byte) (value >> 1);
		System.out.println("Value is " + value);
		value = (byte) (value >>> 1); //DOES NOT WORK on BYTES
		System.out.println("Value is " + value);
		
		int value2 = -1;
		value2 = value2 >> 1; //shifts the bit sign AND preserves the value sign
		System.out.println("Value2 is " + value2);
		
		int value3 = -1;
		value3 = value3 >>> 1; //shifts the bit sign but does not preserve the value sign
		System.out.println("Value2 is " + value3);
		
		//checking for bits;
		//check if a byte has the 3rd bit 1 (left to right, 1st is 1st)
		byte anyValue = 39;
		//use a bit mask
		byte bitMask = 1 << 5; //0b00100000;
		byte result = (byte)(anyValue & bitMask);//possible values: 0 or !0
		if(result == 0) {
			System.out.println("3rd bit is 0");
		}
		else {
			System.out.println("3rd bit is 1");
		}
		
		
	}
	
	

}
