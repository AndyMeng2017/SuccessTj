/**
 * @Project sap
 * @Package com.hcicloud.sap.common.network
 * @author lixianji
 * @date 2016年1月19日 下午3:40:50
 */
package com.hcicloud.sap.common.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hcicloud.sap.common.utils.SystemParamUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author lixianji
 * @Title ESMethod.java
 * @Package com.hcicloud.sap.common.network
 * @date 2016年1月19日 下午3:40:50
 */
public class ESMethod {

    private static Logger logger = Logger.getLogger(ESMethod.class);

    public static String indexUrl = "";

    static {
        //PropertiesLoader pLoader = new PropertiesLoader("system.properties");
        indexUrl = SystemParamUtil.getValueByName("index_url");//pLoader.getProperty("index_url");//
        /*if(!indexUrl.endsWith("/")){
            indexUrl+="/";
        }*/
    }

    /**
     * 创建或更新索引
     *
     * @param type
     * @param indexData
     */
    public static void index(String type, String indexData) {
        logger.warn("索引的index方法中");
        try {
            if (indexData != null) {
                String uuid = JSON.parseObject(indexData).getString("UUID");
                String result = HTTPMethod.doPutQuery(indexUrl + type + "/trans/" + uuid, indexData, 30000);
                System.out.println(result);
            }

        } catch (Exception e) {
            logger.error("索引的index方法中，出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void map(String type, String mapData) {
        logger.warn("索引的map方法中");
        try {
            if (mapData != null) {
                String result = HTTPMethod.doPostQuery(indexUrl + type, mapData, 30000);
                System.out.println(result);
            }

        } catch (Exception e) {
            logger.error("索引的map方法中，出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     *
     * @param type
     * @param uuid
     */
    public static void delete(String type, String uuid) {
        logger.warn("索引的delete方法中");
        try {
            String result = HTTPMethod.doDeleteQuery(indexUrl + type + "/trans/" + uuid, 5000);
            System.out.println(result);
        } catch (Exception e) {
            logger.error("索引的delete方法中,出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 批量建立索引
     *
     * @param type
     * @param dataList
     */
    public static void indexBatch(String type, List<String> dataList) {
        logger.warn("索引的indexBatch方法中");
        try {
            String query = "";
            if (dataList != null && dataList.size() > 0) {
                for (String data : dataList) {
                    String uuid = JSON.parseObject(data).getString("UUID");
                    query += "{ \"index\": {\"_id\":\"" + uuid + "\"}}\n";
                    query += data + "\n";
                }
                String result = HTTPMethod.doPutQuery(indexUrl + type + "/trans/" + "_bulk", query, 5000);
                //改为日志
                System.out.println(result);
            }
        } catch (Exception e) {
            logger.error("索引的indexBatch方法中,出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引
     *
     * @param type
     * @param dataList
     */
    public static void deleteBatch(String type, List<String> dataList) {
        logger.warn("索引的deleteBatch方法中");
        try {
            String query = "";
            if (dataList != null && dataList.size() > 0) {
                for (String data : dataList) {
                    String uuid = JSON.parseObject(data).getString("UUID");
                    query += "{ \"delete\": {\"_id\":\"" + uuid + "\"}}\n";
                    //query+=JSONObject.toJSONString(data);
                }
                String result = HTTPMethod.doPutQuery(indexUrl + type + "/trans/" + "_bulk", query, 5000);
                System.out.println(result);
            }
        } catch (Exception e) {
            logger.error("索引的deleteBatch方法中，出现异常" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取单篇文档
     *
     * @param type
     * @param uuid
     * @return
     */
    public static JSONObject get(String type, String uuid) {
        logger.warn("索引的get方法中");
        try {
            String result = HTTPMethod.doGetQuery(indexUrl + type + "/trans/" + URLEncoder.encode(uuid, "utf-8"), 30000);
            System.out.println(result);
            JSONObject resultObject = JSON.parseObject(result);
            boolean found = resultObject.getBooleanValue("found");
            if (found) {
                JSONObject sourceObject = resultObject.getJSONObject("_source");
                return sourceObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("索引的get方法中，出现异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param queryContent 查询字段列表
     * @param type
     * @return
     * @Description 按关键词查找文档，分页
     */
    public static JSONObject find(String type, String queryContent) {
        System.out.println(queryContent);
        logger.warn("索引的find方法中");
        JSONObject finalResult = new JSONObject();
        try {
            String result = HTTPMethod.doPostQuery(indexUrl + type + "/trans/" + "_search", queryContent, 30000);
            System.out.println(result);
            JSONObject resultObject = JSON.parseObject(result);
            JSONObject hitsObject = resultObject.getJSONObject("hits");
            JSONArray hitsArray = hitsObject.getJSONArray("hits");
            JSONArray voices = new JSONArray();
            JSONArray highLight = new JSONArray();
            JSONArray ids = new JSONArray();
            for (Object v : hitsArray) {
                voices.add(((JSONObject) v).getJSONObject("_source"));
                JSONObject highJsonObject = ((JSONObject) v).getJSONObject("highlight");
                if (highJsonObject != null)
                    highLight.add(highJsonObject);
                JSONObject source = ((JSONObject) v).getJSONObject("_source");
                String id = (String) source.get("UUID");
                if (id != null) {
                    ids.add(id);
                }
            }
            finalResult.put("voices", voices);
            finalResult.put("highlight", highLight);
            finalResult.put("total", hitsObject.getLongValue("total"));
            finalResult.put("aggregations", resultObject.getJSONObject("aggregations"));
            finalResult.put("ids", ids);
            return finalResult;
        } catch (Exception e) {
            logger.error("索引的find方法中,出现异常：" + e.getMessage());
            e.printStackTrace();
            return finalResult;
        }
    }

    /**
     * @param type
     * @param queryContent 查询字段列表
     * @return
     * @Description 按关键词查找热词
     */
    public static JSONObject findHotWord(String type, String queryContent) {
        System.out.println(queryContent);
        JSONObject finalResult = new JSONObject();
        try {
            String result = HTTPMethod.doPostQuery(indexUrl + type + "/trans/" + "_search", queryContent, 30000);
            System.out.println(result);
            JSONObject resultObject = JSON.parseObject(result);
            JSONObject aggregationsObject = resultObject.getJSONObject("aggregations");
            JSONObject significantWordsObject = aggregationsObject.getJSONObject("significantWords");
            JSONArray bucketsArray = significantWordsObject.getJSONArray("buckets");
            finalResult.put("bucketsArray", bucketsArray);
            return finalResult;
        } catch (Exception e) {
            logger.error("索引的find方法中,出现异常：" + e.getMessage());
            e.printStackTrace();
            return finalResult;
        }
    }

    /*********************************这里是针对批量转写的 ES操作  START********************************************/
    /**
     * 新增索引数据
     *
     * @param indexName
     * @param dataList
     */

    public static void addIndexBatch(String indexName, List<String> dataList) {
        logger.warn("索引的indexBatch方法中");
        try {
            String query = "";
            if (dataList != null && dataList.size() > 0) {
                for (String data : dataList) {
                    String uuid = JSON.parseObject(data).getString("VOICE_ID");
                    query += "{ \"index\": {\"_id\":\"" + uuid + "\"}}\n";
                    query += data + "\n";
                }
                String result = HTTPMethod.doPutQuery(indexName + "_bulk", query, 5000);
                //改为日志
                System.out.println(result);
            }
        } catch (Exception e) {
            logger.error("索引的indexBatch方法中,出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新索引数据
     */

    public static boolean updateIndex(String indexName, String updateData) {
        try {
            if (updateData != null) {
                String result = HTTPMethod.doPostQuery(indexName + "_update", updateData, 30000);
                System.out.println(result);
                if (!StringUtils.isEmpty(result)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("索引的updateIndex方法中,出现异常：" + e.getMessage());
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 批量删除索引
     *
     * @param indexName 访问的url
     * @param idSArray  主键ID 这里为voiceId
     * @Date 2017年8月9日 15:26:20
     */
    public static boolean deleteIndexBatch(String indexName, String idSArray) {
        logger.warn("索引的deleteBatch方法中");
        try {
            String query = "";
            String[] ids = idSArray.split(",");
            if (ids != null && ids.length > 0) {
                for (String id : ids) {
                    query += "{ \"delete\": {\"_id\":\"" + id + "\"}}\n";
                }
                String result = HTTPMethod.doPutQuery(indexName + "/_bulk", query, 5000);
                System.out.println(result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                    Set<String> keySet = jsonObject1.keySet();
                    Iterator<String> it = keySet.iterator();
                    String key = it.next();
                    JSONObject jsonObject2 = jsonObject1.getJSONObject(key);
                    if (!jsonObject2.getString("status").equals("200")) {
                        System.out.println("对" + jsonObject2.getString("_id") + "执行" + key + "操作失败");
                        stringBuffer.append("对第" + jsonObject2.getString("_version") + "行执行" + key + "操作失败，");
                    }
                }
                if (!StringUtils.isEmpty(stringBuffer.toString())) {
                    System.out.println(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
                }
                return true;
            }
        } catch (Exception e) {
            logger.error("索引的deleteBatch方法中，出现异常" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }


    /*********************************这里是针对批量转写的 ES操作  END********************************************/

}

