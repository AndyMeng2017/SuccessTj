package com.hcicloud.sap.common.utils.unzip.util.zip;

/*    */ 
/*    */ import java.util.Date;
import java.util.zip.ZipEntry;
/*    */ 
/*    */ public class EncryptZipEntry extends ZipEntry
/*    */ {
/*    */   String name;
/* 15 */   long time = -1L;
/* 16 */   long crc = -1L;
/* 17 */   long size = -1L;
/* 18 */   long csize = -1L;
/* 19 */   int method = -1;
/*    */   byte[] extra;
/*    */   String comment;
/*    */   int flag;
/*    */   int version;
/*    */   long offset;
/*    */ 
/*    */   public EncryptZipEntry(String name)
/*    */   {
/* 10 */     super(name);
/* 11 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public void setTime(long time)
/*    */   {
/* 28 */     this.time = javaToDosTime(time);
/*    */   }
/*    */ 
/*    */   @SuppressWarnings("deprecation")
private static long javaToDosTime(long time)
/*    */   {
/* 36 */     Date d = new Date(time);
/* 37 */     int year = d.getYear() + 1900;
/* 38 */     if (year < 1980) {
/* 39 */       return 2162688L;
/*    */     }
/* 41 */     return (year - 1980 << 25 | d.getMonth() + 1 << 21 | 
/* 42 */       d.getDate() << 16 | 
/* 42 */       d.getHours() << 11 | 
/* 42 */       d.getMinutes() << 5 | 
/* 43 */       d.getSeconds() >> 1);
/*    */   }
/*    */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.zip.EncryptZipEntry
 * JD-Core Version:    0.5.3
 */