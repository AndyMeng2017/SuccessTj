package com.hcicloud.sap.study.offer;

/**
 * 题目描述【2.替换空格】
 * 请实现一个函数，将一个字符串中的空格替换成“%20”。例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
 *
 *
 * 解答：
 * 这就很尴尬了
 */
public class Offer002 {
    public static String replaceSpace(StringBuffer str) {
        return str.toString().replace(" ", "%20");
    }

    public static void main(String[] args){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("We Are Happy.");
        System.out.println(replaceSpace(stringBuffer));
    }
}
