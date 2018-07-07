package com.hcicloud.sap.common.utils.keywordutil;

import com.hcicloud.sap.common.constant.GlobalConstant;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关键词硬匹配
 */
public class KeywordMatch {
	
	/**
	 * @Description 查找trans文件，从voicePath中
	 * @param voicePath 文件所在大路径
	 * @param fileNo 文件名
	 * @param callTime 文件所在日期文件夹
	 * @return
	 */
	public File queryXML(String voicePath, String fileNo, String callTime) {
        if(fileNo == null || callTime == null) {
        	return null;
        }
        String[] callDate = callTime.split(" ");
        File audioFile = new File(voicePath + File.separator + callDate[0] + File.separator + fileNo + ".trans");
        System.out.println("文件：" + audioFile.getName() + "状态：" + audioFile.exists() + " , " + audioFile.canRead());
        if(!(audioFile.exists() && audioFile.canRead())) {
            System.out.println(audioFile + " can not access");
            return null;
        }
        return audioFile;
	}
	
	/**
	 * @Description 检测单边静音
	 * @param voicePath
	 * @param fileNo
	 * @param callTime
	 * @return
	 */
	public boolean isSingleSilense(String voicePath, String fileNo, String callTime) {
		File transFile = queryXML(voicePath, fileNo, callTime);
		List<String> sentenses = null;
		try {
			sentenses = parseXML(transFile, GlobalConstant.USER_ONLY);
			System.out.println("xml解析结果：" + sentenses);
			if (sentenses == null || sentenses.size() == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @Description 对关键词列表进行词频匹配
	 * @param keywords
	 * @param voicePath
	 * @param fileNo
	 * @param callTime
	 * @return
	 */
	public Map<String, Integer> keywordsCountMatch (List<String> keywords, String voicePath, String fileNo, String callTime) {
		return keywordsCountMatch(keywords, voicePath, fileNo, callTime, null, false);
	}
	
	/**
	 * @Description 对关键词进行词频匹配，一般方法
	 * @param keywords
	 * @param voicePath
	 * @param fileNo
	 * @param callTime
	 * @param timePoint
	 * @param isStart
	 * @return
	 */
	public Map<String, Integer> keywordsCountMatch (List<String> keywords, String voicePath, String fileNo,
			String callTime, Integer timePoint, boolean isStart) {
		File transFile = queryXML(voicePath, fileNo, callTime);
		List<String> sentenses = null;
		try {
			sentenses = parseXML(transFile, GlobalConstant.AGENT_ONLY, timePoint != null);
			System.out.println("xml解析结果：" + sentenses);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Integer> counts = new HashMap<String, Integer>();
		Integer time = 0;
		for (String k : keywords) {
			Integer count = 0;
			if (timePoint != null) {
				time = timePoint * 1000;
				Integer durationTime = parseXMLDuration(transFile);
				count = countMatch(k, sentenses, time, isStart, durationTime);
			}
			else {
				count = countMatch(k, sentenses);
			}
			counts.put(k, count);
			System.out.println("关键词-词频：" + k + "-" + countMatch(k, sentenses));
		}
		return counts;
	}
	
	/**
	 * @Description 在整个AGENT句子列表中匹配单个关键词频次
	 * @param keyword
	 * @param sentences
	 * @return
	 */
	private int countMatch(String keyword, List<String> sentences) {
		if (keyword == null || sentences == null) {
			return 0;
		}
		int count = 0;
		for (String s : sentences) {
			//每个句子打头为A：或U：以区别为客服或客户，所以要去掉前2个字符
			if (s.length() <= 2) {
				continue;
			}
			else {
				s = s.substring(2);
				if (keyword.length() > s.length()) {
					continue;
				}
				for (int i = 0; i <= s.length() - keyword.length(); i++) {
					String subSentence = s.substring(i, i + keyword.length());
					if (keyword.equals(subSentence)) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * @Description 在整个AGENT句子列表中匹配单个关键词频次及检测欢迎语结束语
	 * @param keyword
	 * @param sentences
	 * @param time
	 * @param isStart
	 * @return
	 */
	private int countMatch(String keyword, List<String> sentences, Integer time, boolean isStart, Integer duration) {
		if (keyword == null || sentences == null) {
			return 0;
		}
		int count = 0;
		for (String s : sentences) {
			//句子结构为：role:sentence-start-end
			String[] subsent = s.split("-");
			if ((isStart && Integer.valueOf(subsent[2]) <= time)
					||
				((!isStart) && Integer.valueOf(subsent[1]) >= (duration - time))) {
				//每个句子打头为A：或U：以区别为客服或客户，所以要去掉前2个字符
				if (subsent[0].length() <= 2) {
					continue;
				}
				else {
					subsent[0] = subsent[0].substring(2);
					if (keyword.length() > s.length()) {
						continue;
					}
					for (int i = 0; i <= subsent[0].length() - keyword.length(); i++) {
						String subSentence = subsent[0].substring(i, i + keyword.length());
						if (keyword.equals(subSentence)) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * @Description 解析trans文件中的duration属性
	 * @param transFile
	 * @return
	 */
	public static Integer parseXMLDuration(File transFile) {
		Integer duration = 0;
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(transFile);
			Element result = document.getRootElement();
			String sentenceList = result.elementText("duration");
			duration = Integer.valueOf(sentenceList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.valueOf(duration);
	}
	
	/**
	 * @Description 解析trans文件，提取句子
	 * @param transFile trans文件
	 * @param parseMode 句子提取模式
	 * @return
	 * @throws Exception
	 */
	public List<String> parseXML(File transFile, int parseMode) throws Exception {
		return parseXML(transFile, parseMode, false);
	}
	
	/**
	 * @Description 解析trans文件，提取句子，同时提取对应时间点
	 * @param transFile
	 * @param parseMode
	 * @param needTime 是否需要提取时间
	 * @return
	 * @throws Exception
	 */
	public List<String> parseXML(File transFile, int parseMode, boolean needTime) throws Exception {
		SAXReader reader = new SAXReader();
        Document document = reader.read(transFile);
        Element result = document.getRootElement();
        Element sentenceList = result.element("sentence_list");
        List<Element> sentences = sentenceList.elements("sentence");
        List<String> resultSentences = new ArrayList<String>();
        //只解析trans
        for (Element s : sentences) {
        	String timeStr = "";
        	//如果需要解析时间，则
        	if (needTime) {
        		StringBuffer timeBuf = new StringBuffer();
        		timeBuf.append("-").append(s.attribute("start").getText()).append("-").append(s.attribute("end").getText());
        		timeStr = timeBuf.toString();
        	}
        	//解析句子
			String sentence = s.elementText("text");
			if (!(sentence.startsWith("#") && sentence.endsWith("#"))) {
				//获得agent
		        if (parseMode == GlobalConstant.AGENT_ONLY
		        		||
		        		parseMode == GlobalConstant.BOTH_USER_AND_AGENT) {
	        		if ("AGENT".equals(s.attribute("role").getText())) {
	        			resultSentences.add("A:" + sentence + timeStr);
	        		}
		        }
		        //获得user
		        if (parseMode == GlobalConstant.USER_ONLY
		        		||
		        		parseMode == GlobalConstant.BOTH_USER_AND_AGENT) {
	        		if ("USER".equals(s.attribute("role").getText())) {
	        			resultSentences.add("U:" + sentence + timeStr);
	        		}
		        }
        	}
        }
        return resultSentences;
	}
	
//	public static void main(String [] args) throws Exception {
//		List<String> keywords = new ArrayList<String>();
//		keywords.add("北京接通华声语音技术有限公司");
//		keywords.add("八");
//		keywords.add("的");
//		KeywordMatch keywordMatch = new KeywordMatch();
//		Map<String, Integer> keys = keywordMatch.keywordsCountMatch(keywords, "E:\\data\\asr_trans\\voice_dir", "1644282", "2015-12-10 00:00:00");
//		parseXMLDuration(new File("C:\\Users\\zhaoaijie\\Desktop\\2016-01-15\\33333333.trans"));
//	}
	
}
