package com.web.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class MD5Utils {

	private static final Logger logger = Logger.getLogger(MD5Utils.class);

	public static String md5(String command) {
		try {
			byte[] bytes = MessageDigest.getInstance("MD5").digest(
					command.getBytes("UTF-8"));
			StringBuilder ret = new StringBuilder(bytes.length << 1);
			for (int a = 0; a < bytes.length; ++a) {
				ret.append(Character.forDigit((bytes[a] >> 4) & 0xf, 16));
				ret.append(Character.forDigit(bytes[a] & 0xf, 16));
			}
			return ret.toString();
		} catch ( UnsupportedEncodingException ex) {
			logger.error(ex);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return null;
	}

	public static void main(String[] args) {
		System.err.println(md5("reloadgamedata"));
		System.err.println(md5("aaaaaaaaaaaaaaa"));
		System.err.println(md5("aaaaaaaaaaaaaaa"));
		System.err.println(md5("aaaaaaaaaaaaaaa"));
		System.err.println(md5("aaaaaaaaaaaaaaa"));
	}
}
