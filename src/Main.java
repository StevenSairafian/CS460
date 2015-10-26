import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Main {

	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the key length: ");
		int keyLen = sc.nextInt();
		
		SecureRandom sRand = new SecureRandom();
		try {
			
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keyLen, sRand);
            KeyPair sKey = keyGen.generateKeyPair();
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("PublicKeyFile"));
			out.writeObject(sKey.getPublic());
			
			out = new ObjectOutputStream(new FileOutputStream("PrivateKeyFile"));
			out.writeObject(sKey.getPrivate());
			
			System.out.println("Enter input text file path");
			File inFile = new File(sc.nextLine());
			FileInputStream fIS = new FileInputStream(inFile);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
			
			//Was not specified to encrypt using private or public. 
			//As times are compared, it is assumed that key used for encryption does not matter.
			//Seeing as communication is not being used, it is reasonable to assume encryption for 
			//self use (encrypt with public) is intended.
			cipher.init(Cipher.ENCRYPT_MODE, sKey.getPublic());
						
			byte[] inBytes = new byte[(int)inFile.length()];
			fIS.read(inBytes);
			
			File encryptedFile = new File("EncryptedFile");
			
			FileOutputStream fOut = new FileOutputStream(encryptedFile);
			out.write(cipher.doFinal(inBytes));
			
			cipher.init(Cipher.DECRYPT_MODE, sKey.getPrivate());
			
			fIS = new FileInputStream(encryptedFile);
			inBytes = new byte[(int)encryptedFile.length()];
			
			fOut = new FileOutputStream(new File("DecryptedFile"));
			out.write(cipher.doFinal(inBytes));
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
