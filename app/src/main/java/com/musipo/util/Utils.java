package com.musipo.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {

	public static Integer toInteger(String str) {
		return Integer.parseInt(str);
	}

	public static Double toDouble(String str) {
		return Double.parseDouble(str);
	}

	final public static int length(String str) {
		return str.length();
	}

	public static String getFromateDate(Date date) {
		String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return str;
	}

	public static String getDeviceId(Context context) {

		TelephonyManager tm = (TelephonyManager)context.getSystemService(Activity.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static final String md5(final String toEncrypt) {
	    try {
	        final MessageDigest digest = MessageDigest.getInstance("md5");
	        Log.e("toEncrypt", toEncrypt);
	        digest.update(toEncrypt.getBytes());
	        final byte[] bytes = digest.digest();
	        final StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < bytes.length; i++) {
	            sb.append(String.format("%02X", bytes[i]));
	        }
	        return sb.toString().toLowerCase();
	    } catch (Exception exc) {
	        return "";
	    }
	}


	public static void pause(long duration){
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static boolean isEmpty(String text) {
		if(text == null)
			return true;
		else if(text.equals(""))
			return true;
		return false;
	}

	public static String getOtpNumber(int size) {

		StringBuilder generatedToken = new StringBuilder();
		try {
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			// Generate 20 integers 0..20
			for (int i = 0; i < size; i++) {
				generatedToken.append(number.nextInt(9));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return generatedToken.toString();
	}

	public static int getOtpNum(int size) {

		StringBuilder generatedToken = new StringBuilder();
		try {
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			// Generate 20 integers 0..20
			for (int i = 0; i < size; i++) {
				generatedToken.append(number.nextInt(9));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return Integer.parseInt(generatedToken.toString());
	}
}