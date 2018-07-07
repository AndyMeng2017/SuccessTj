package com.hcicloud.sap.common.utils.pingan;

import com.hcicloud.sap.common.utils.PropertiesLoader;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ConvertPwdPropertyConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {

        System.out.println("=================="+propertyName+":"+propertyValue);


        if("jdbc_password_postgresql".equals(propertyName)){

            // TODO 1.提取平安科技侧的配置说明
//            url = https://prd-ccp.paic.com.cn/pidms/rest/pwd/getPassword
//            appId = App_GCC_WER__61f00a0d8587419d
//            safe = AIM_GCC_WER
//            folder = root
//            object = Oracle-ICSS-qctjs
//            reason = null
//            appKey_postgre = 1f0217f1ea988f0b
            PropertiesLoader propertiesLoader = new PropertiesLoader("system.properties");
            String url = propertiesLoader.getProperty("url");
            String appId = propertiesLoader.getProperty("appId");
            String safe = propertiesLoader.getProperty("safe");
            String folder = propertiesLoader.getProperty("folder");
            String object = propertiesLoader.getProperty("object");
            String reason = propertiesLoader.getProperty("reason");
            String appKey_postgre = propertiesLoader.getProperty("appKey_postgre");

            try {
                String password_postGre ="";
//                password_postGre = PasswordProvider.getPasswordFromRemote(url,appId,safe,folder,object,reason,appKey_postgre);
//                String password_postGre = "";
                // TODO 这里是暂时默认给定一个值
                password_postGre = "123456";
                System.out.println("当前连接数据库postgre的密码为"+password_postGre);
                return password_postGre;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("获取平安ITS的postgre的密码失败");
            }
        }
        return propertyValue;
    }
}
