package com.hcicloud.sap.common.utils.pingan;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class SignUtil {

	public static String makeSign(String s){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(s.getBytes());
			byte[] messageDigest = digest.digest();
			return HexUtil.byte2hex(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String makeSign(Map<String,Object> data,String key){
    	final StringBuilder sb = new StringBuilder();
    	// TODO 这里需要指定一个  APPID  的值
		//暂时默认给定一个值
//    	String appCode = (String)data.get(Fields.APPID);
    	String appCode = "App_GCC_WER__61f00a0d8587419d";
    	sb.append(appCode).append('&').append(key);
    	return sb.length()>0?makeSign(sb.toString()):null;
	}

}
