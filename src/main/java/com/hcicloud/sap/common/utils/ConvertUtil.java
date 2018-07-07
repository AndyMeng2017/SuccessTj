/** 
* @Project pingan_cti
* @Package com.sinovoice.hcicloud.common
* @author lixianji
* @date 2016年3月29日 下午4:44:28
*/
package com.hcicloud.sap.common.utils;


/** 
 * @Title ConvertUtil.java
 * @Package com.sinovoice.hcicloud.common
 * @author lixianji
 * @date 2016年3月29日 下午4:44:28
 */
public class ConvertUtil {

    /**
     * 本地长度和网络长度互转
     * @param d
     * @return
     */
    public static int htonl(int d){
        int rslt = 0;
        byte [] bs1 = new byte[4];
        putInt(bs1, d, 0);
        byte[] bs2 = new byte[4];
        bs2[0] = bs1[3];
        bs2[1] = bs1[2];
        bs2[2] = bs1[1];
        bs2[3] = bs1[0];
        rslt = getInt(bs2, 0);
        return rslt;
    }
    /** 
     * 转换int为byte数组 
     *  
     * @param bb 
     * @param x 
     * @param index 
     */  
    public static void putInt(byte[] bb, int x, int index) {  
        bb[index + 3] = (byte) (x >> 24);  
        bb[index + 2] = (byte) (x >> 16);  
        bb[index + 1] = (byte) (x >> 8);  
        bb[index + 0] = (byte) (x >> 0);  
    } 
    /** 
     * 通过byte数组取到int 
     *  
     * @param bb 
     * @param index 
     *            第几位开始 
     * @return 
     */  
    public static int getInt(byte[] bb, int index) {  
        return (int) ((((bb[index + 3] & 0xff) << 24)  
                | ((bb[index + 2] & 0xff) << 16)  
                | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));  
    } 
}
