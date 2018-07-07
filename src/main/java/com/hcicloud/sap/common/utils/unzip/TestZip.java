package com.hcicloud.sap.common.utils.unzip;

import com.hcicloud.sap.common.utils.unzip.file.FileUtils;
import com.hcicloud.sap.common.utils.unzip.util.extend.UnzipFrom;
import com.hcicloud.sap.common.utils.unzip.util.extend.ZipOutput;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


public class TestZip {

	public static final String zipDir = "D:\\home\\mhnzip";
	public static final String zipDirDecry = "D:\\home\\mhnzip\\decrypt";
	public static final String EncryptZipFile = "D:\\home\\mhnzip\\20170821-1552.zip";
	public static final String password = "123456";


	@Test
	public void TestEncryptZipFile() {

		//文件目录，就可能出现多个文件
		System.out.println("===== 加密 =====");
		File file = new File(zipDir);
		byte[] zipByte = ZipOutput.getEncryptZipByte(file.listFiles(), password);
		FileUtils.writeByteFile(zipByte, new File(EncryptZipFile));
		System.out.println("===== Encrypt Success =====");
	}

	@Test
	public void TestDecryptZipFile() {

		System.out.println("===== 解密 =====");
		File file = new File(EncryptZipFile);
		byte[] zipByte = FileUtils.readFileByte(file);
		try {
			UnzipFrom.unzipFiles(zipByte ,password, zipDirDecry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("===== Decrypt Success =====");

	}

	public static void main(String[] args) {
		final String EncryptZipFile = "D:\\home\\mhn\\20170821-1552.zip";
		final String password = "123456";
		final String zipDir = "D:\\home\\mhn\\20170821-1552.txt";
		System.out.println("===== 加密 =====");
		File file = new File(zipDir);
		byte[] zipByte = ZipOutput.getEncryptZipByte(file.listFiles(), password);
		FileUtils.writeByteFile(zipByte, new File(EncryptZipFile));
		System.out.println("===== Encrypt Success =====");

		//始终都是忧伤的，一直都学不会去梳理，似墙角的蛛网一样易碎，却也杂乱，剪断，拨乱，不论
		//怎样的撕碎，重建，都是一种断裂的人生。
		//说来也奇怪，我却总是在看似日理万机的梳理代码，有时还暗自庆幸，还暗自愉悦一番，
		//所以我渴望逃离当下，逃离工作，逃离家乡，逃离拥挤不堪，
		//同时也在不断的建构这种新的断裂，就这样子循环反复，
		//等待，犹豫不决，特别想着去打乱，断裂后的细碎，她时刻都包围在周遭，权衡不出哪些该被遗弃，哪些该停留
		//曾经以为，我如此的迷恋她，soulmate，
		//同样的，漫步于柔软的沙滩上，放肆的大笑，夹杂在扑面而来的海风中，
		//粘满沙砾的肉体，看起来象一头海牛
		//突然，哆嗦了一下

	}
}
