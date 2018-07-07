package com.hcicloud.sap.common.utils.pingan;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class SecurityUtil {

	/**
	 * 加密算法
	 * @param s
	 * @param k
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
    public static String encrypt(String s,String k) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (s == null || k == null) {
			throw new IllegalArgumentException();
		}
		SecretKeySpec key = new SecretKeySpec(k.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(s.getBytes());// 加密
		return HexUtil.byte2hex(result);
	}

	/**
	 * 解密算法
	 * @param s
	 * @param k
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
    public static String decrypt(String s,String k) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		if (s == null || k == null) {
			throw new IllegalArgumentException();
		}
		SecretKeySpec key = new SecretKeySpec(k.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(HexUtil.hex2byte(s)); // 解密
		return new String(result);
    }

    public static void main(String[] args) {
    	//JDK默认使用的AES算法最高只能支持128位。如需要更高的支持需要从Oracle官网下载更换JAVA_HOME/jre/lib/
		// security目录下的： local_policy.jar和US_export_policy.jar。<br/>
    	// 对应的AES加解密的KEY的长度：128-16、192-24、256-32.
    	try {
			String aa = SecurityUtil.encrypt("123456", "menghaonan123456");
			System.out.println("aa为:" +aa);
			String bb = "";
			bb = SecurityUtil.decrypt(aa, "menghaonan123456");
			System.out.println("bb:" +bb);
    	} catch (Exception e) {
    	    // TODO: handle exception
    	    e.printStackTrace();
    	}
    }


}
