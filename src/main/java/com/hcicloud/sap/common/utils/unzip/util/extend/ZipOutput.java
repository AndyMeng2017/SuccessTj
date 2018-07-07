package com.hcicloud.sap.common.utils.unzip.util.extend;


import com.hcicloud.sap.common.utils.unzip.util.zip.EncryptZipEntry;
import com.hcicloud.sap.common.utils.unzip.util.zip.EncryptZipOutput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * zip文件加密，转为字节流
 * @author menghaonan
 * @date 2017年9月1日 11:56:19
 */
public final class ZipOutput {
    public static byte[] getEncryptZipByte(File[] srcfile, String password) {
        ByteArrayOutputStream tempOStream = new ByteArrayOutputStream(1024);
        byte[] tempBytes = (byte[]) null;
        byte[] buf = new byte[1024];
        try {
            EncryptZipOutput out = new EncryptZipOutput(tempOStream, password);
            //这里是扩展为读取多个文件的方式
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new EncryptZipEntry(srcfile[i].getName()));
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
}
