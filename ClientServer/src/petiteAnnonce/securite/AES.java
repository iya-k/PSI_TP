package petiteAnnonce.securite;


import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;


public class AES {

	private SecretKeySpec secretKey;
	private byte[] key;
	
public AES() {
		
	}

	public void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String strToEncrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}
	
	public String decrypt(String strToDecrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}
	
	/*
	 * 
	 * 
	 * Autres methodes
	 * 
	 */
	
	UserSecure user;
	byte[] user_public_key = null;

	PublicKey public_key;
	PrivateKey private_key;
	
	
	
	public AES(UserSecure u) {
		
		user = u;

		  try{
			  KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		      keyGen.initialize(1024);
		      
		      KeyPair key_pair = keyGen.generateKeyPair();
		      private_key = key_pair.getPrivate();
		      public_key = key_pair.getPublic();
		      
		        System.out.println("Algo "+key_pair.getPublic().getAlgorithm());
		        System.out.println("Format "+key_pair.getPublic().getFormat());
		        
		      }catch(NoSuchAlgorithmException e){
		    	  e.printStackTrace();
		    }
		
	}
	
	public void init() {
		
		  EncodedKeySpec public_keySpec = new X509EncodedKeySpec(user_public_key);
		  //EncodedKeySpec public_keySpec = new X509EncodedKeySpec(user.public_key);
		    KeyFactory serverKeyFac = null;
		    
			try {
				
				serverKeyFac = KeyFactory.getInstance("RSA");
				public_key = serverKeyFac.generatePublic(public_keySpec);
				
				
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
	}
	
	public String encryptRSA(String encrypt) {
		
	    try {
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, public_key);
			
			return Base64.getEncoder().encodeToString(cipher.doFinal(encrypt.getBytes(UTF_8)));
			
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
	    
		return null;
	}
	
	public String decryptRSA(String decrypt) {
		try {
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			cipher.init(Cipher.DECRYPT_MODE, private_key);
			
			return new String(cipher.doFinal(Base64.getDecoder().decode(decrypt)), UTF_8);
			
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}
	
}
