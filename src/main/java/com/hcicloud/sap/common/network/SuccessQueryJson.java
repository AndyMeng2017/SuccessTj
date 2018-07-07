package com.hcicloud.sap.common.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by songxueyan on 2016/12/19.
 */
public class SuccessQueryJson {
    //private String[] _source = {"ORDER_ID","VOICE_ID","PLAT_FORM","USER_PHONE","CALL_LENGTH","CALL_COENGTENT","QUALITY_NAME","QUALITY_DETAIL","QUALITY_DATA","QUALITY_DETAIL","CALL_START_TIME","CALL_END_TIME","CREATE_TIME"};
    private Integer from;
    private Integer size;

    private JSONObject query;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public JSONObject getQuery() {
        return query;
    }

    public void setQuery(JSONObject query) {
        this.query = query;
    }

/*    public static String getJson(Integer from, Integer size){
        SuccessQueryJson json = new SuccessQueryJson();
        json.setFrom(from);
        json.setSize(size);
        return JSON.toJSONString(json);
    }*/

    public static String getJson(Integer from, Integer size, String query){
    	JSONObject obj = null;
        if(query!=null){
        	obj = JSON.parseObject(query);
        }else {
        	obj = new JSONObject();
        }
        obj.put("from",from);
        obj.put("size",size);
        JSONObject sort = new JSONObject();
        JSONObject createTime = new JSONObject();
        createTime.put("order", "desc");
        createTime.put("ignore_unmapped", "true");
        sort.put("CREATE_TIME", createTime);
        obj.put("sort", sort);
        return obj.toJSONString();
    }
}
