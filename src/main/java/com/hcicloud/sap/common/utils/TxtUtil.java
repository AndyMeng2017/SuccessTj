package com.hcicloud.sap.common.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TxtUtil {
	/** 
	  * 创建文件 
	  * @param fileName 
	  * @return 
	  */  
	 public static boolean createFile(File fileName)throws Exception{  
	  boolean flag=false;  
	  try{  
	   if(!fileName.exists()){  
	    fileName.createNewFile();  
	    flag=true;  
	   }  
	  }catch(Exception e){  
	   e.printStackTrace();  
	  }  
	  return true;  
	 }   
	   
	 /** 
	  * 使用gbk读TXT文件内容 
	  * @param fileName 
	  * @return 
	  */  
	 public static List<String> readTxtFile(File fileName)throws Exception{  
		List<String> results = new ArrayList<String>();
		BufferedReader reader = null;
	 	try{  
	 		reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "gbk"));
	 		String read=null;  
	 		while((read=reader.readLine())!=null){  
	 			results.add(read);
	 		}  
	 	}catch(Exception e){  
	 		e.printStackTrace();  
	 	}finally{  
	 		if(reader!=null){  
	 			reader.close();  
	 		}  
	 	}  
	 	//System.out.println("读取出来的文件内容是："+"\r\n"+results.toString());  
	 	return results;  
	 }  
	   
	 /**
	  * 写文件  
	  * @param content
	  * @param fileName
	  * @return
	  * @throws Exception
	  */
	 public static boolean writeTxtFile(String content,File  fileName)throws Exception{  
	  RandomAccessFile mm=null;  
	  boolean flag=false;  
	  FileOutputStream o=null;  
	  try {  
	   o = new FileOutputStream(fileName);  
	      o.write(content.getBytes("GBK"));  
	      o.close();  
	   flag=true;  
	  } catch (Exception e) {  
	   e.printStackTrace();  
	  }finally{  
	   if(mm!=null){  
	    mm.close();  
	   }  
	  }  
	  return flag;  
	 }  
	  
	 
	 /** 
	  * 删除单个文件 
	  * @param   sPath    被删除文件的文件名 
	  * @return 单个文件删除成功返回true，否则返回false 
	  */  
	 public static boolean deleteFile(String sPath) {  
	     boolean flag = false;  
	     File file = new File(sPath);  
	     // 路径为文件且不为空则进行删除  
	     if (file.isFile() && file.exists()) {  
	         file.delete();  
	         flag = true;  
	     }  
	     return flag;  
	 }
	 
	 /**
	  * 使用gbk写文件
	  * @param filePath
	  * @param content
	  */
	 public static void writeToFile(String filePath, String content) { 
		 BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName("GBK")));
			bw.write(content);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	public static void main(String[] args) {
		File file = new File("C:\\Users\\maguosheng\\Desktop\\hotword.gbk");
		try {
			List<String> list= readTxtFile(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\maguosheng\\Desktop\\sql.txt"), Charset.forName("UTF-8")));
			for(String str : list) {
				String str1 = "insert into quality_hot_word values('"+UUID.randomUUID().toString().replace("-", "")+"','"+str+"',to_date('080916','dd-mm-yy'),'0',1);";
				bw.write(str1);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
