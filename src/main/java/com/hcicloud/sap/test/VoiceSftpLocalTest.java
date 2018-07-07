package com.hcicloud.sap.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hcicloud.sap.common.utils.DateConversion;
import com.hcicloud.sap.common.utils.FileUtils;
import com.hcicloud.sap.common.utils.RedisUtil;
import com.hcicloud.sap.common.utils.ftp.SftpClient;
import redis.clients.jedis.Jedis;

import java.io.File;

public class VoiceSftpLocalTest {




    private static int MAX_NUM = 1;
    //    private static String successFtpVoicePath = "/home/";
    private static String successFtpVoicePath = "D:\\pinganftp\\";
    //磁盘的根目录下的子目录   /nfsc/gcc……/下的
//    private static String remoteVoicePath = "/SpeechAnalytics/sinovoice/text/fsr/";
//    private static String remoteVoicePath = "fsr/aa/";
    private static String remoteVoicePath = "fsr22/aa22/";
    private static String localTextName = successFtpVoicePath + "success.txt";

    public static void main(String[] args){
        // TODO 1.连接redis，读取数据   2.本地文件记录推送的文件名  3.推送到指定的目录

        int num = 0;
        long time1 = System.currentTimeMillis();

        while (true) {
            String sendPostGreKey = "testKey";
            Jedis jedis = null;
            SftpClient sftpClient = new SftpClient();
            JSONArray array = new JSONArray();
            String localName = "";
            String remoteName = "";

            try {
                jedis = RedisUtil.getJedis();
                System.out.println("Redis连接成功！");

                for (int i = 0; i < MAX_NUM; i++) {
                    String successMsg = jedis.lpop(sendPostGreKey);

                    successMsg = "{\"fileId\":\"/home/szzxsftp/fsrnotify/20170913090452/voice/20170913090021223\",\"content\":\"[{\\\"content\\\":\\\"喂;time=40 780\\\",\\\"talkertype\\\":\\\"2\\\"},{\\\"content\\\":\\\"呃先生你好请问您是幺八三尾号是六七四四的机主对吧;time=1170 5440\\\",\\\"talkertype\\\":\\\"1\\\"},{\\\"content\\\":\\\"对你好女士;time=6330 7360\\\",\\\"talkertype\\\":\\\"1\\\"},{\\\"content\\\":\\\"啊你好打扰您了这边是中国平安客户专员工号是九二四;time=7970 12840\\\",\\\"talkertype\\\":\\\"1\\\"},{\\\"content\\\":\\\"通话录音本次来电;time=12950 14820\\\",\\\"talkertype\\\":\\\"2\\\"},{\\\"content\\\":\\\"是平安公司做一个回访活动给您免费赠送一个;time=14990 18420\\\",\\\"talkertype\\\":\\\"1\\\"},{\\\"content\\\":\\\"三百八十八元那个e钱包礼;time=18590 20620\\\",\\\"talkertype\\\":\\\"2\\\"},{\\\"content\\\":\\\"稍后呢;time=20930 21560\\\",\\\"talkertype\\\":\\\"1\\\"},{\\\"content\\\":\\\"是以短信形式发送个例了不需要了好吗;time=21770 25260\\\",\\\"talkertype\\\":\\\"2\\\"},{\\\"content\\\":\\\"不像您退;time=25750 26410\\\",\\\"talkertype\\\":\\\"1\\\"}]\",\"callTime\":\"2017-09-13 09:00:21\",\"voiceId\":\"ZX20170913201709130900210002830491\",\"seatId\":\"830491\",\"callPhone\":\"018317896744\",\"voicePath\":\"/home/szzxsftp/fsrnotify/20170913090452/voice/20170913090021223.pcm\",\"platForm\":\"XQD-ZX\"}";

                    if (successMsg != null && successMsg.trim().length() > 0) {
                        array.add(JSONObject.parseObject(successMsg));
                    } else {
                        break;
                    }
                }

                if(array.size()>0) {
                    //这里是推送到平安方的FTP服务器中
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject successJsonObject = array.getJSONObject(i);
//                        JSONObject successJsonObject =  new JSONObject(array.getJSONObject(i));
                        String content = successJsonObject.getString("content");//内容
                        String callTime = successJsonObject.getString("callTime");//录音开始时间
                        String voiceId = successJsonObject.getString("voiceId");//录音流水号

                        System.out.println("当前录音流水号为："+voiceId);

                        String callContentReal = "";
                        boolean flagWirteText = false;

                        //TODO 1.生成本地的文件，同时写入文本   2.推送ftp  3.删除文件

                        //SpeechAnalytics/sinovoice/text/

                        //这里转化为  年月日时分
                        callTime = DateConversion.DateToStr(DateConversion.StrToDate(callTime,"yyyy-MM-dd HH:mm:ss"),"yyyyMMddHHmm");

                        //开始文件的创建与校验
                        if(!successFtpVoicePath.endsWith(File.separator)){
                            successFtpVoicePath += File.separator;
                        }

                        localName = successFtpVoicePath + callTime + "_" + voiceId + ".txt";
                        remoteName = remoteVoicePath + callTime + "_" + voiceId + ".txt";
                        if(!FileUtils.isFileExist(localName)){
                            JSONArray contentArray = JSONObject.parseArray(content);
                            if (contentArray != null) {
                                for (int k = 0; k < contentArray.size(); k++) {
                                    JSONObject jsonObject = contentArray.getJSONObject(k);
                                    String result = jsonObject.getString("content");
                                    if (result != null) {
                                        String resultNew = result.substring(0, result.indexOf(";time="));
                                        if (resultNew != null && resultNew.trim().length() > 0 && resultNew != "\"\"") {
                                            callContentReal += result.substring(0, result.indexOf(";time=")) + "☆";
                                        }
                                    }
                                }
                            }
                            flagWirteText = FileUtils.writeFile(localName,callContentReal,false);
                            String textMeg = callTime + "_" + voiceId + ".txt" +"\r\n";
                            FileUtils.writeFile(localTextName,textMeg,true);
                        }else{
                            flagWirteText = true;
                        }

                        //推送FTP服务器，创建文件成功的时候
                        boolean flag = false;
                        if (flagWirteText) {
                            System.out.println("成功创建目录" + localName);
                            flag = sftpClient.uploadFile(localName,remoteName);
                        }

                        jedis.rpush(sendPostGreKey, array.get(i).toString());

                        if (!flagWirteText || !flag) {
                            FileUtils.deleteFile(localName);
                            sftpClient.removeFile(remoteName);
                        }

                        num++;
                        if (num/100 == 0 ) {
                            System.out.println("已经写入文本个数为" + num);
                            long time2 = System.currentTimeMillis();
                            System.out.println("当前耗时为：" + DateConversion.formatTime(time2 - time1));
                        }

                        if (num == 2000) {
                            long time3 = System.currentTimeMillis();
                            System.out.println("一共耗时为：" + DateConversion.formatTime(time3 - time1));
                            break;
                        }

                        Thread.sleep(2000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileUtils.deleteFile(localName);
            } finally {
                RedisUtil.returnResource(jedis);
                //关闭sftp连接
                try {
                    sftpClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
