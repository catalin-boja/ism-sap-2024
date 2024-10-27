package ro.ase.ism.sap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class TestKeystore {
	
	public static KeyStore getKeyStore(
			String ksFileName, String ksPassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		File ksFile = new File(ksFileName);
		if(!ksFile.exists()) {
			throw new UnsupportedOperationException("KS file missin");
		}
		FileInputStream fis = new FileInputStream(ksFile);
		
		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(fis, ksPassword.toCharArray());
		fis.close();
		return ks;
	}
	
	public static void printKSContent(KeyStore ks) throws KeyStoreException {
		if(ks != null) {
			System.out.println("Key Store content: ");
			
			Enumeration<String> items = ks.aliases();
			
			while(items.hasMoreElements()) {
				String item = items.nextElement();
				System.out.println("Item: " + item);
				if(ks.isKeyEntry(item)) {
					System.out.println("\t - is a key pair");
				}
				if(ks.isCertificateEntry(item)) {
					System.out.println("\t - is a public key");
				}		
			}
		}
	}
	
	public static PublicKey getPublicKey(
			KeyStore ks, String alias) throws KeyStoreException {
		if(ks != null && ks.containsAlias(alias)) {
			PublicKey pub = ks.getCertificate(alias).getPublicKey();
			return pub;
		} else {
			throw new UnsupportedOperationException("No KS or no alias");
		}
	}
	
	public static PrivateKey getPrivateKey(
			KeyStore ks, String alias, String ksPass
			) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
		if(ks != null && ks.containsAlias(alias) && 
				ks.isKeyEntry(alias)) {
			PrivateKey priv = 
					(PrivateKey) ks.getKey(alias, ksPass.toCharArray());
			return priv;
		}
		else {
			throw new UnsupportedOperationException("KS issue");
		}
	}
	
	public static PublicKey getPublicFromX509(String filename) throws FileNotFoundException, CertificateException {
		File file = new File(filename);
		if(!file.exists()) {
			throw new UnsupportedOperationException("Missing file");
		}
		FileInputStream fis = new FileInputStream(file);
		CertificateFactory factory =  
				CertificateFactory.getInstance("X.509");
		X509Certificate cert = 
				(X509Certificate) factory.generateCertificate(fis);
		return cert.getPublicKey();
	}
	
	public static byte[] encrypt(Key key, byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(input);
	}
	
	public static byte[] decrypt(Key key, byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(input);
	}
	
	public static byte[] getSymmetricRandomKey(
			int noBits, String algorithm) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = 
				KeyGenerator.getInstance(algorithm);
		keyGenerator.init(noBits);
		return keyGenerator.generateKey().getEncoded();
	}
	
	public static byte[] getDigitalSignature(
			String file, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {
		File inputFile = new File(file);
		if(!inputFile.exists()) {
			throw new UnsupportedOperationException("No FILE");
		}
		FileInputStream fis = new FileInputStream(inputFile);
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(privateKey);
		
		//process the entire file on one round
		byte[] buffer = fis.readAllBytes();
		signature.update(buffer);
		
		fis.close();
		
		//TO DO: when the file is processed in blocks
		
		return signature.sign();	
	}
	
	public static boolean isValid(
			String filename, byte[] signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {
		
		File inputFile = new File(filename);
		if(!inputFile.exists()) {
			throw new UnsupportedOperationException("No FILE");
		}
		FileInputStream fis = new FileInputStream(inputFile);
		
		Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initVerify(publicKey);
		
		byte[] buffer = fis.readAllBytes();		
		fis.close();
		
		sign.update(buffer);
		return sign.verify(signature);
	}

	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException {
	
		KeyStore ks = getKeyStore("ismkeystore.ks", "passks");
		printKSContent(ks);
		
		//get public key
		PublicKey ism1Pub = getPublicKey(ks,"ismkey1");
		System.out.println("ISM1 Public: ");
		System.out.println(Utility.getHex(ism1Pub.getEncoded()));
		
		PublicKey ismAseRoPub = getPublicKey(ks,"ismasero");
		System.out.println("ISMASERO Public: ");
		System.out.println(Utility.getHex(ismAseRoPub.getEncoded()));
		
		//get private key
		PrivateKey ism1Priv = 
				getPrivateKey(ks, "ismkey1", "passks");
		System.out.println("ISM Private:");
		System.out.println(Utility.getHex(ism1Priv.getEncoded()));
		
		//get public key from X509 cert file
		PublicKey ism1pub2 = getPublicFromX509(
				"ISMCertificateX509.cer");
		System.out.println("ISMASERO Public: ");
		System.out.println(Utility.getHex(ism1pub2.getEncoded()));
		
		byte[] randomAESKey = 
				getSymmetricRandomKey(128, "AES");
		System.out.println("Random AES Key :");
		System.out.println(Utility.getHex(randomAESKey));
		
		
		byte[] encAESKey = 
				encrypt(ism1Pub, randomAESKey);
		System.out.println("Encrypted AES Key: " + 
				Utility.getHex(encAESKey));
		
		byte[] initialAESKey = 
				decrypt(ism1Priv, encAESKey);
		System.out.println("Initial AES Key: " + 
				Utility.getHex(initialAESKey));
		
		byte[] msgSignature = 
				getDigitalSignature("Msg.txt", ism1Priv);
		System.out.println("digital signature: ");
		System.out.println(Utility.getHex(msgSignature));
		
		//at client
		if(isValid("Msg2.txt", msgSignature, ism1pub2)) {
			System.out.println("The msg is valid");
		} else {
			System.out.println("Someone changed the msg");
		}
		
		
		
	}

}
