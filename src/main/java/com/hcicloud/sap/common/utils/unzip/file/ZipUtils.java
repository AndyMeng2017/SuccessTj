package com.hcicloud.sap.common.utils.unzip.file;


 import java.io.BufferedInputStream;
 import java.io.BufferedOutputStream;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.zip.ZipEntry;
 import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

 public final class ZipUtils
 {
   public static final byte[] MAGIC_NUMBER = { 80, 75, 3, 4 };

     /**
      * 判断是否为 .zip 文件
      * @param rawZipFile
      * @return
      */
     public static boolean isZipFile(byte[] rawZipFile) {
         if (rawZipFile.length < MAGIC_NUMBER.length) {
             return false;
         }
         for (int i = 0; i < MAGIC_NUMBER.length; ++i) {
             if (MAGIC_NUMBER[i] != rawZipFile[i]) {
                 return false;
             }
         }
         return true;
     }

public static byte[] toRawZipFile(List<ZipEntry> entries, List<byte[]> files)
/*     */     throws IOException
/*     */   {
/*  49 */     if (entries.size() != files.size()) {
/*  50 */       return null;
/*     */     }
/*  52 */     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
/*  53 */     ZipOutputStream zip = new ZipOutputStream(bytes);
/*  54 */     Iterator entriesItr = entries.iterator();
/*  55 */     Iterator filesItr = files.iterator();
/*  56 */     while (entriesItr.hasNext()) {
/*  57 */       byte[] file = (byte[])filesItr.next();
/*  58 */       ZipEntry entry = (ZipEntry)entriesItr.next();
/*  59 */       zip.putNextEntry(entry);
/*  60 */       zip.write(file, 0, file.length);
/*     */     }
/*  62 */     zip.close();
/*  63 */     return bytes.toByteArray();
/*     */   }
/*     */
/*     */   @SuppressWarnings("unchecked")
public static List<ZipEntry> toZipEntryList(byte[] rawZipFile) throws IOException
/*     */   {
/*  68 */     ArrayList entries = new ArrayList();
/*  69 */     ByteArrayInputStream bytes = new ByteArrayInputStream(rawZipFile);
/*  70 */     ZipInputStream zip = new ZipInputStream(bytes);
/*  71 */     ZipEntry entry = zip.getNextEntry();
/*  72 */     while (entry != null) {
/*  73 */       entries.add(entry);
/*  74 */       entry = zip.getNextEntry();
/*     */     }
/*  76 */     zip.close();
/*  77 */     return entries;
/*     */   }
/*     */
/*     */   @SuppressWarnings("unchecked")
public static List<byte[]> toByteArrayList(byte[] rawZipFile) throws IOException
/*     */   {
/*  82 */     ArrayList files = new ArrayList();
/*  83 */     ByteArrayInputStream bytes = new ByteArrayInputStream(rawZipFile);
/*  84 */     ZipInputStream zip = new ZipInputStream(bytes);
/*     */ 
/*  87 */     ZipEntry entry = zip.getNextEntry();
/*  88 */     while (entry != null) {
/*  89 */       ByteArrayOutputStream file = new ByteArrayOutputStream();
/*  90 */       byte[] buf = new byte[4096];
/*     */       int len;
/*  91 */       while ((len = zip.read(buf, 0, 4096)) != -1) {
/*  92 */         file.write(buf, 0, len);
/*     */       }
/*  94 */       files.add(file.toByteArray());
/*  95 */       entry = zip.getNextEntry();
/*     */     }
/*  97 */     zip.close();
/*  98 */     return files;
/*     */   }

    /**
     * 读取多个文件，转为 .zip输出流
     * @param srcfile
     * @return
     */
    public static byte[] readZipByte(File[] srcfile) {
        ByteArrayOutputStream tempOStream = new ByteArrayOutputStream(1024);
        byte[] tempBytes = (byte[]) null;
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(tempOStream);
            for (int i = 0; i < srcfile.length; ++i) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            tempOStream.flush();
            out.close();
            tempBytes = tempOStream.toByteArray();
            tempOStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempBytes;
    }

/*     */   @SuppressWarnings("unchecked")
public static byte[] zipFiles(Map<String, byte[]> files)
/*     */     throws Exception
/*     */   {
/* 152 */     ByteArrayOutputStream dest = new ByteArrayOutputStream();
/* 153 */     ZipOutputStream out = new ZipOutputStream(
/* 154 */       new BufferedOutputStream(dest));
/* 155 */     byte[] data = new byte[2048];
/* 156 */     Iterator itr = files.keySet().iterator();
/* 157 */     while (itr.hasNext()) {
/* 158 */       String tempName = (String)itr.next();
/* 159 */       byte[] tempFile = (byte[])files.get(tempName);
/*     */ 
/* 161 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(tempFile);
/* 162 */       BufferedInputStream origin = new BufferedInputStream(bytesIn, 2048);
/* 163 */       ZipEntry entry = new ZipEntry(tempName);
/* 164 */       out.putNextEntry(entry);
/*     */       int count;
/* 166 */       while ((count = origin.read(data, 0, 2048)) != -1) {
/* 167 */         out.write(data, 0, count);
/*     */       }
/* 169 */       bytesIn.close();
/* 170 */       origin.close();
/*     */     }
/* 172 */     out.close();
/* 173 */     byte[] outBytes = dest.toByteArray();
/* 174 */     dest.close();
/* 175 */     return outBytes;
/*     */   }
/*     */
/*     */   @SuppressWarnings("unchecked")
public static byte[] zipEntriesAndFiles(Map<ZipEntry, byte[]> files) throws Exception
/*     */   {
/* 180 */     ByteArrayOutputStream dest = new ByteArrayOutputStream();
/* 181 */     ZipOutputStream out = new ZipOutputStream(
/* 182 */       new BufferedOutputStream(dest));
/* 183 */     byte[] data = new byte[2048];
/* 184 */     Iterator itr = files.keySet().iterator();
/* 185 */     while (itr.hasNext()) {
/* 186 */       ZipEntry entry = (ZipEntry)itr.next();
/* 187 */       byte[] tempFile = (byte[])files.get(entry);
/* 188 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(tempFile);
/* 189 */       BufferedInputStream origin = new BufferedInputStream(bytesIn, 2048);
/* 190 */       out.putNextEntry(entry);
/*     */       int count;
/* 192 */       while ((count = origin.read(data, 0, 2048)) != -1) {
/* 193 */         out.write(data, 0, count);
/*     */       }
/* 195 */       bytesIn.close();
/* 196 */       origin.close();
/*     */     }
/* 198 */     out.close();
/* 199 */     byte[] outBytes = dest.toByteArray();
/* 200 */     dest.close();
/* 201 */     return outBytes;
/*     */   }
/*     */
/*     */   @SuppressWarnings("unchecked")
public static Map<String, byte[]> unzipFiles(byte[] zipBytes)
/*     */     throws IOException
/*     */   {
/* 207 */     InputStream bais = new ByteArrayInputStream(zipBytes);
/* 208 */     ZipInputStream zin = new ZipInputStream(bais);
/*     */ 
/* 210 */     Map extractedFiles = new HashMap();
/*     */     ZipEntry ze;
/* 211 */     while ((ze = zin.getNextEntry()) != null) {
/* 212 */       ByteArrayOutputStream toScan = new ByteArrayOutputStream();
/* 213 */       byte[] buf = new byte[1024];
/*     */       int len;
/* 215 */       while ((len = zin.read(buf)) > 0) {
/* 216 */         toScan.write(buf, 0, len);
/*     */       }
/* 218 */       byte[] fileOut = toScan.toByteArray();
/* 219 */       toScan.close();
/* 220 */       extractedFiles.put(ze.getName(), fileOut);
/*     */     }
/* 222 */     zin.close();
/* 223 */     bais.close();
/* 224 */     return extractedFiles;
/*     */   }
/*     */
/*     */   @SuppressWarnings("unchecked")
public static Map<String, byte[]> unzipFiles(InputStream bais) throws IOException
/*     */   {
/* 229 */     ZipInputStream zin = new ZipInputStream(bais);
/*     */ 
/* 231 */     Map extractedFiles = new HashMap();
/*     */     ZipEntry ze;
/* 232 */     while ((ze = zin.getNextEntry()) != null) {
/* 233 */       ByteArrayOutputStream toScan = new ByteArrayOutputStream();
/* 234 */       byte[] buf = new byte[1024];
/*     */       int len;
/* 236 */       while ((len = zin.read(buf)) > 0) {
/* 237 */         toScan.write(buf, 0, len);
/*     */       }
/* 239 */       byte[] fileOut = toScan.toByteArray();
/* 240 */       toScan.close();
/* 241 */       extractedFiles.put(ze.getName(), fileOut);
/*     */     }
/* 243 */     zin.close();
/* 244 */     bais.close();
/* 245 */     return extractedFiles;
/*     */   }
/*     */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     com.training.commons.file.ZipUtils
 * JD-Core Version:    0.5.3
 */