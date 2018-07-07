package com.hcicloud.sap.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hcicloud.sap.common.network.ESMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CommonMethod {

	/**
	 * 将base64字符保存文本文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void toFile(String base64Code, String targetPath)
			throws Exception {

		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	public static String getPath() {
		try {
			if (RequestContextHolder.getRequestAttributes() != null) {
				String path = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest()
						.getServletContext().getRealPath("/");
				if (path == null || path.equals(""))
					try {
						path = ((ServletRequestAttributes) RequestContextHolder
								.getRequestAttributes()).getRequest()
								.getServletContext().getResource("/")
								.toString().replaceAll("file:", "");
					} catch (Exception e) {
						e.printStackTrace();
					}
				return path;

			} else {
				/*String path = CommonMethod.class.getClassLoader()
						.getResource("").toString().replace("file:", "")
						.replace("/WEB-INF/classes/", "");*/
				URL url = CommonMethod.class.getClassLoader().getResource("spring.xml");
				String path = url.getPath(); 
				int endingIndex = path.length()-"spring.xml".length(); 
				path = path.substring(0, endingIndex).replace("/WEB-INF/classes/", "");; 
				return path;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String doPostQuery(String url, String query, int time)
			throws Exception {
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/json;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		} // 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					throw e;
				}
			}
			method.abort();
			try {
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).shutdown();
			} catch (Exception e) {
				throw e;
			}
		}
		return result;
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String getRemoteHost(
			javax.servlet.http.HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	
	/**
	 * java 根据数字获得指定区间
	 */
	public static List<String> getNumIntervalStr(Long value,Integer Interval){
		List<String> list = new ArrayList<String>();
		long temp1 = 1;
		long temp = Interval;
		System.out.println(value/Interval);
		for(int i=0;i<Math.ceil(value.doubleValue()/Interval);i++){
			list.add(temp1+"~"+temp);
			temp1 = temp+1;
			if(temp<value){
				temp = temp+Interval;
			}else{
				temp = temp+value;
			}
		}
		return list;
	}
	
	/**
	 * 读取文本内容
	 * @param path
	 * @return
	 */
	public static String getFileContent(String path){
	    File file = new File(path);
	    if(!file.exists()||file.isDirectory()){
	        return null;
	    }
	    StringBuffer content = new StringBuffer();
	    FileReader fileReader = null;
	    BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line=bufferedReader.readLine())!=null){
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileReader!=null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	    
	    return content.toString();
	    
	}
	
	/**
     * 获取录音信息
     * @param uuid
     * @return
     */
    public static JSONObject getVoiceInfo(String uuid) {
        StringBuilder searchBuilder = new StringBuilder();
        searchBuilder.append("{\"query\":{")
                        .append("\"filtered\":{")
                        .append("\"query\":{")
                        .append("\"match\":{")
                        .append("\"UUID\":\"")
                        .append(uuid)
                        .append("\"}}}}}");
        JSONObject resultJObject = ESMethod.find("*", searchBuilder.toString());
        JSONArray voiceJObject = resultJObject.getJSONArray("voices");
        return voiceJObject.getJSONObject(0);
    }
    
    
    public static String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
    public static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    } 
}
