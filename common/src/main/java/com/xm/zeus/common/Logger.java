package com.xm.zeus.common;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 日志
 */
public class Logger {

    public static void writeLog(String content) {
        FileOutputStream fos = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Zeus");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, "log.txt");
            byte[] newLine = "\n".getBytes("UTF-8");
            fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.write(newLine);
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
