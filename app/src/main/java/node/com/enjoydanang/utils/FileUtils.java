package node.com.enjoydanang.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import node.com.enjoydanang.GlobalApplication;

import static android.R.attr.data;
import static node.com.enjoydanang.GlobalApplication.getGlobalApplicationContext;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static void saveFilePrivateMode(String fileName, String allData) {
        FileOutputStream fos = null;
        try {
            fos = getGlobalApplicationContext()
                    .openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(allData.getBytes());

        } catch (Exception e) {
            Log.e(TAG, "saveFilePrivateMode: " + e.getMessage());
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

    public static void cleanAllFiles() {
        Context context = getGlobalApplicationContext();
        String strCleanData = "";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("", Context.MODE_PRIVATE);
            fos = context.openFileOutput("", Context.MODE_PRIVATE);
            fos = context.openFileOutput("", Context.MODE_PRIVATE);
            fos = context.openFileOutput("", Context.MODE_PRIVATE);
            fos = context.openFileOutput("", Context.MODE_PRIVATE);
            fos.write(strCleanData.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "cleanAllFiles: " + e.getMessage());
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

    public static JSONObject readFile(String filename) {

        FileInputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = GlobalApplication.getGlobalApplicationContext().openFileInput(filename);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            String strContent = new String(data);
            return new JSONObject(strContent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
