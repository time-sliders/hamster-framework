package com.noob.storage.security;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {

	/**
	 * 对输入的byte数组进行base64加密
	 * 
	 * @param input
	 *            要加密byte数组
	 */
	public static String base64Encode(byte input[]) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * 对字符串进行解密
	 * 
	 * @param input
	 *            要解密的字符串
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}

	public static String encode(String input, String charsetName) throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(input.getBytes("UTF-8")), charsetName);
	}

	public static String decode(String input, String charsetName) throws UnsupportedEncodingException {
		return new String(Base64.decodeBase64(input.getBytes(charsetName)), charsetName);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(base64Encode("zhwwashere".getBytes()));
		System.out.println(new String(base64Decode("emh3d2FzaGVyZQ==")));
		System.out.println(encode("zhwwashere", "UTF-8"));
		
	}
}
