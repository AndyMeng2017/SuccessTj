package com.hcicloud.sap.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SuccessTokenTj {

    private static String voicePath = "D:\\home\\mhn";
//        private static String txtName = "20170821-1552";
//    private static String txtName = "201708212008";
    private static String txtName = "201708212009";


    public static void main(String[] args) {
        //TODO 1.读取文本  2.统计下发的个数

        File txtFile = new File(voicePath + File.separator + txtName + ".txt");
        if (!txtFile.exists()) {
            System.out.println("文件 not found");
        }

        //TODO 1.按行读取文件，格式为GBK   2.按行读取文件
        StringBuffer tempBuffer = new StringBuffer();
        String temp = "";


        HashMap<String, String> map = new HashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(txtFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            BufferedReader br1 = new BufferedReader(inputStreamReader);

            while ((temp = br1.readLine()) != null) {

                //汉字的Unicode码 /u4e00-/u9fa5 (中文) 19968-40869
//                for(int i = 0; i < temp.length(); i++) {
                    int n = (int)temp.charAt(0);
                    if((19968 <= n && n <40869)) {
                        continue;
                    }
//                }

                JSONObject jsonObject = JSONObject.parseObject(temp);
                if (jsonObject != null) {
                    if (jsonObject.getString("ret") != null && jsonObject.getString("ret").equals("0")) {
                        String data = jsonObject.getString("data");
                        if (!StringUtils.isEmpty(data)) {
                            String resultList = JSONObject.parseObject(data).getString("resultList");

                            JSONArray jsonArray = JSON.parseArray(resultList);
                            if (jsonArray != null && jsonArray.size() > 0) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    System.out.println(temp);
                                    map.put(object.getString("business_id"), object.toString());
                                }

                            }
                        }

                    }
                }
            }
            br1.close();
            inputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("加入到下发列表的个数为" + map.size());  //2576

        int num = 0;//记录青牛平台的个数
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(JSONObject.parseObject(entry.getValue()).getString("call_platform").equals("XQD-CCOD")){
                num++;
            }
        }
        System.out.println("青牛平台的个数为" + num); //746

        //TODO   0817号
        //加入到下发列表的个数为2141
        //青牛平台的个数为796

    }

}
