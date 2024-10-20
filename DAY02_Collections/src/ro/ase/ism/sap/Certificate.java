package ro.ase.ism.sap;

import java.util.List;

public class Certificate {
	String owner;
	List<Byte> publicKey;
	int keySize;
	String domain;
	
	public Certificate(String owner, List<Byte> publicKey, int keySize, String domain) {
		super();
		this.owner = owner;
		//this.publicKey = publicKey; //shallow copy
		this.publicKey = List.copyOf(publicKey);
		this.keySize = keySize;
		this.domain = domain;
	}
	
	void print() {
		System.out.println("Owner: " + this.owner);
		System.out.println("Domain: " + this.domain);
		System.out.println("Key size: " + this.keySize);
		System.out.println("Public key: ");
		for(byte b : this.publicKey) {
			System.out.printf(" %02x", b);
		}
		System.out.println();		
	}

	@Override
	public int hashCode() {
		return this.domain.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null || !(obj instanceof Certificate)) {
			return false;
		}
		return this.domain.equals(((Certificate)obj).domain);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Certificate(
				this.owner, 
				this.publicKey, 
				this.keySize, 
				this.domain);
				
	}
	
	
	
	
}
