package com.mc.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
	/*
	 * encryption.stringEncryption(password,"");
	 * encryption.stringEncryption(password,"SHA-512");
	 */
	public static String stringEncryption(String message, String algorithm){
		if("".equals(message) || message == null){
			return "";
		}
		String encryption = "";
		try {
			StringBuffer sb = new StringBuffer();
			if(algorithm == null || algorithm.equals("")){
				algorithm = "MD5";
			}
			MessageDigest md;
			md= MessageDigest.getInstance(algorithm);
			md.update(message.getBytes());
			byte[] mb = md.digest();
			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				sb.append(s.substring(s.length() - 2));
			}
			encryption = sb.toString();
			//System.out.println(sb.length());
			//System.out.println("CRYPTO"+"("+algorithm+"): " + sb + "   length : "+sb.length() );
		} catch (NoSuchAlgorithmException e) {
			//System.out.println("ERROR: " + e.getMessage());
		}
		return encryption;
	}
}
