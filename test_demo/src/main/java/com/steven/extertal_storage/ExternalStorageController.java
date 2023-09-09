package com.steven.extertal_storage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ExternalStorageController {
    public static void saveData(String filePath, String fileName, String data) {
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdir();
        }
        File temp = new File(filePath);
        if (!temp.exists()) {
            temp.mkdir();
        }

        File t = new File(file, fileName);
        if (!t.exists()) {
            try {
                t.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("file##", "create file: " + file.getAbsolutePath());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(t);
            fos.write(data.getBytes()); // 将字节流写入文件中
            Log.i("file##", "write bytes to files done.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
          if (fos != null) {
              try {
                  fos.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        }
    }

    public static String readData(String filePath, String fileName) {
        InputStream is = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            File direct = new File(filePath);
            if (!direct.exists()) {
                direct.mkdir();
            }
            File file = new File(direct, fileName);
            if (!file.exists()) {
                return "";
            }
            is = new FileInputStream(file);
            reader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (is != null) {
                    is.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
