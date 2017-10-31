package com.murat.demo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.murat.demo.model.User;


public class PasswordUtil {
	private static MessageDigest md;
	
	
	public boolean checkPassword(String password){
		User user = UserUtil.getUser();
		return (user.getPassword().equals(cryptWithMD5(password)));
	}
	
	public static String cryptWithMD5(String password){
	    	
    	try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	byte[] passwordBytes = password.getBytes();
    	md.reset();
    	byte[] digested = md.digest(passwordBytes);
    	StringBuffer sb = new StringBuffer();
    	for(int i = 0; i<digested.length; i++){
    		sb.append(Integer.toHexString(0xff & digested[i]));
    	}
    	return sb.toString();
	}

}
