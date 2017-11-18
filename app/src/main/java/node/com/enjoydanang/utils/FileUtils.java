package node.com.enjoydanang.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import node.com.enjoydanang.GlobalApplication;

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

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public static String getFilePath(Context context, Uri uri) {
        int currentApiVersion;
        try {
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            //API 3 will crash if SDK_INT is called
            currentApiVersion = 3;
        }
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            String filePath = "";
            String wholeID = null;
            if(checkUri(uri)){
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
                int column_index
                        = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                wholeID = DocumentsContract.getDocumentId(uri);
            }

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        } else {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }


    private static boolean checkUri(Uri uri){
       return uri.toString().startsWith("content://com.google.android.apps.photos.content");
    }
}
