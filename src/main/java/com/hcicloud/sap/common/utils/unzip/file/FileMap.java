package com.hcicloud.sap.common.utils.unzip.file;

/*    */ 
/*    */ public class FileMap
/*    */ {
/*  5 */   private String fileName = "";
/*    */   private Long fileSize;
/*  7 */   private byte[] fileData = null;
/*  8 */   private String status = "";
/*    */ 
/*    */   public String getFileName() {
/* 11 */     return this.fileName;
/*    */   }
/*    */ 
/*    */   public void setFileName(String fileName) {
/* 15 */     this.fileName = fileName;
/*    */   }
/*    */ 
/*    */   public Long getFileSize() {
/* 19 */     return this.fileSize;
/*    */   }
/*    */ 
/*    */   public void setFileSize(Long fileSize) {
/* 23 */     this.fileSize = fileSize;
/*    */   }
/*    */ 
/*    */   public byte[] getFileData() {
/* 27 */     return this.fileData;
/*    */   }
/*    */ 
/*    */   public void setFileData(byte[] fileData) {
/* 31 */     this.fileData = fileData;
/*    */   }
/*    */ 
/*    */   public String getStatus() {
/* 35 */     return this.status;
/*    */   }
/*    */ 
/*    */   public void setStatus(String status) {
/* 39 */     this.status = status;
/*    */   }
/*    */ }

