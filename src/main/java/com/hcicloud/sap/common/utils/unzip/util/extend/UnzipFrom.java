package com.hcicloud.sap.common.utils.unzip.util.extend;


import com.hcicloud.sap.common.utils.unzip.file.FileUtils;
import com.hcicloud.sap.common.utils.unzip.util.zip.EncryptZipEntry;
import com.hcicloud.sap.common.utils.unzip.util.zip.EncryptZipInput;

import java.io.*;

/**
 * 解密zip文件
 * @author menghaonan
 * @Date 2017年9月1日 14:16:08
 */
public class UnzipFrom {

    /**
     * 传入zip文件的字节流，解密密码，解密后文件写入的目录
     * @param zipBytes
     * @param password
     * @param dir
     * @throws IOException
     */
    public static void unzipFiles(byte[] zipBytes, String password, String dir)
            throws IOException {
        InputStream bais = new ByteArrayInputStream(zipBytes);
        EncryptZipInput zin = new EncryptZipInput(bais, password);
        EncryptZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            ByteArrayOutputStream toScan = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = zin.read(buf)) > 0) {
                toScan.write(buf, 0, len);
            }
            byte[] fileOut = toScan.toByteArray();
            toScan.close();
            FileUtils.writeByteFile(fileOut, new File(dir + File.separator + ze.getName()));
        }
        zin.close();
        bais.close();
    }
}