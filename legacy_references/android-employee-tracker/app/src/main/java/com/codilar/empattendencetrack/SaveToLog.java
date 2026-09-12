package com.codilar.empattendencetrack;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveToLog {

       public static final String FILE_NAME = "Location_log.txt";
      // public static final String DIR_NAME = "Yelligo_Logs";

    public static   void saveLogToFile(Context context, String sBody) {
        try {
            boolean flag = false;
           
            File root = new File(String.valueOf(Environment.getExternalStorageDirectory()));
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, FILE_NAME);
            if (!gpxfile.exists()) {

                flag = true;
            }
            FileWriter writer = new FileWriter(gpxfile, true);
            if (flag) {
                String fileHeader = "---------------------------------------------\n" +
                        "\n Model  :" + Build.MODEL +
                        "\n Manufacturer :" + Build.MANUFACTURER +
                        "\n Android OS Version : " + Build.VERSION.RELEASE +
                        "\n----------------------------------------------";
                writer.append(fileHeader);
            }
           
            writer.append(sBody);
            writer.flush();
            writer.close();


           
        } catch (IOException e) {
        }
    }
}