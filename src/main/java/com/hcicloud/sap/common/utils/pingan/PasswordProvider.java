package com.hcicloud.sap.common.utils.pingan;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordProvider {
	
	static Pattern pattern = Pattern.compile("\"?([^\":,{}]+)\"?\\s*:\\s*\"?([^\":,{}]+)\"?");
	
    public static String getPasswordFromRemote(String url, String appId, String safe, String folder, String object, String reason, String appKey) {

        /*
        * 参数说明
        * url = "https://prd-ccp.paic.com.cn/pidms/rest/pwd/getPassword"
        * appId = "App_GCC_WER__61f00a0d8587419d"
        * safe = "AIM_GCC_WER"
        * folder = "root"
        * object = "Oracle-ICSS-qctjs"
        * reason = null
        * appKey ="1f0217f1ea988f0b"
        * */
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("appId", appId);
            param.put("safe", safe);
            param.put("folder", folder);
            param.put("object", object);
            param.put("reason", reason);
            param.put("sign", SignUtil.makeSign(param, appKey));
            param.put("requestId", "20170101001001");
            param.put("requestTime", "20170227140342332");

            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);//需要输出
            connection.setDoInput(true);//需要输入
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);//不允许缓存
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            StringBuilder sb = new StringBuilder("{");
            for (Entry<String, Object> e:param.entrySet()) {
            	if(e.getKey()==null) continue;
				sb.append('"').append(e.getKey()).append('"');
				sb.append(':');
				Object v = e.getValue();
				if(v==null){
					sb.append("null");
				}else if(v instanceof Number){
					sb.append(v.toString());
				}else{
					sb.append('"').append(v.toString()).append('"');
				}
				sb.append(',');
			}
            sb.setCharAt(sb.length()-1, '}');
            out.writeBytes(sb.toString());
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder sb2 = new StringBuilder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                sb2.append(line);
            }
            Map<String, Object> result = new HashMap<String, Object>();
    		for(Matcher m = pattern.matcher(sb2.toString());m.find();){
    			String k = m.group(1);
    			String v = m.group(2);
    			if(k.length()>0&&!"null".equals(k)){
    				result.put(k, "null".equals(v)?null:v);
    			}
    		}
            if (result != null && "200".equals(result.get("code"))) {
                return SecurityUtil.decrypt((String) result.get("password"), appKey);
            }
//            throw new Exception((String) result.get("msg"));
        } catch (Exception e) {
//            throw new RuntimeException("getpassword from remote error", e);
            e.printStackTrace();
            System.out.println("getpassword from remote error");
        }

        return "000000";
    }

    public static void main(String[] args) {
    	String abc= PasswordProvider.getPasswordFromRemote("https://prd-ccp.paic.com.cn/pidms/rest/pwd/getPassword","App_GCC_WER__61f00a0d8587419d", "AIM_GCC_WER", "root", "Oracle-ICSS-qctjs", null, "1f0217f1ea988f0b");

    	System.out.println(abc);
	}
        
}
