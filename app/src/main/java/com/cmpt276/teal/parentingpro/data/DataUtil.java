package com.cmpt276.teal.parentingpro.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * the class contain methods for general saving and reading data from android app
 * the goal for this is having a standard for handling data from the app
 */
public class DataUtil
{
    private final static String APP_SHARE = "parenting_pro";
    public final static String DEFAULT_STRING_VALUE = "NaN";
    public final static int DEFAULT_INT_VALUE = -1;
    public final static int MAX_BITMAP_WIDTH = 274;     // get from layout
    public final static int MAx_BITMAP_HEIGHT = 255;    // get from layout





    /**
     * get the SharedPreferences for the app
     * so we have a standrad sharedPreferences
     * @param context can be any subclass for the context like Activity
     * @return  SharedPreferences for the app
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        if(context == null)
            return null;
        return context.getSharedPreferences(APP_SHARE, Context.MODE_PRIVATE);
    }



    /**
     * a way to get editor for the app
     * @param context can be any subclass for the context like Activity
     * @return an editor for SharePreference
     */
    public static SharedPreferences.Editor getSharedEditor(Context context) {
        if(context == null)
            return null;
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit();
    }


    /**
     * writing one stored data in the app
     * data type is String
     * @param context can be any subclass for the context like Activity
     * @param key a String as key too loop in for the data
     * @param value data to store into the app
     */
    public static void writeOneStringData(Context context, String key, String value) {
        if(context == null)
            throw new IllegalArgumentException("context cannot be null");

        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * get a stored data from app
     * @param context can be any subclass for the context like Activity
     * @param key use as index for getting the data
     * @return  String format of data
     */
    public static String getStringData(Context context, String key) {
        if(context == null)
            throw new IllegalArgumentException("context cannot be null");

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, DEFAULT_STRING_VALUE);
    }


    /**
     * writing one stored data in the app
     * data type is int
     * @param context can be any subclass for the context like Activity
     * @param key a String as key too loop in for the data
     * @param value data to store into the app
     */
    public static void writeOneIntData(Context context, String key, int value) {
        if (context == null)
            throw new IllegalArgumentException("context cannot be null");

        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putInt(key, value);
        editor.apply();
    }


    /**
     * get a stored data from app
     * @param context can be any subclass for the context like Activity
     * @param key use as index for getting the data
     * @return  int format of data
     */
    public static int getIntData(Context context, String key) {
        if(context == null)
            throw new IllegalArgumentException("context cannot be null");

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(key, DEFAULT_INT_VALUE);
    }


    /**
     * the method get the internal file and return byte array as data for the file
     * @param context can be any subclass for the context like Activity
     * @param fileName a string represent the file name
     * @return the byte array represent the file in internal storage
     */
    public static byte[] getInteralFileInBytes(Context context, String fileName){
        if(fileName == null){
            throw new IllegalArgumentException("file name can not be null");
        }

        FileInputStream input = null;
        byte[] data = null;
        try {
            input = context.openFileInput(fileName);
            data = new byte[input.available()];
            input.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }


    /**
     * the the byte array data to the disk
     * @param context can be any subclass for the context like Activity
     * @param fileName string represent the file name
     * @param data byte array represent data in a file
     * @return boolean represent the write is success or not
     */
    public static boolean writeToInternalStorage(Context context, String fileName, byte[] data) {
        if(fileName == null){
            throw new IllegalArgumentException("file name can not be null");
        }
        boolean success = true;
        FileOutputStream out = null;
        try {
            // get the output stream for the file
            out = context.openFileOutput(fileName, context.MODE_PRIVATE);
            out.write(data);
            out.flush();
        } catch (FileNotFoundException e) {
            success = false;
            e.printStackTrace();
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }
        finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }



    /**
     * the method remove a file in internal storage
     * @param context can be any subclass for the context like Activity
     * @param fileName string represent the file name
     */
    public static void removeInternalFile(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        if(file.exists()){
            file.delete();
        }
    }


    /**
     * the method get the image from internal storage
     * @param context  can be any subclass for the context like Activity
     * @param fileName string represent the file name
     * @return  the bitmap image
     */
    public static Bitmap getInternalImage(Context context, String fileName){
        if(fileName == null){
            throw new IllegalArgumentException("file name can not be null");
        }
        Bitmap outputImage = null;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;   // use for just getting with and height of image with out loading in memory
        FileInputStream input = null;
        try {
             input = context.openFileInput(fileName);
             BitmapFactory.decodeStream(input, null, option);
             option.inSampleSize = getInSampleSize(option, MAX_BITMAP_WIDTH, MAx_BITMAP_HEIGHT);
             option.inJustDecodeBounds = false;
             input.close();
             input = context.openFileInput(fileName); // open it again
             outputImage =  BitmapFactory.decodeStream(input, null, option);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e){
            System.gc();
            outputImage = null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       return outputImage;
    }


    /**
     * get the optimated size of the image
     * @param option option that run the image first and get the height and width for the image
     * @param inWidth  the desired size for the image width
     * @param inHeight  the desired size for the image height
     * @return the ratio to cover the actual image size to desired image size
     */
    public static int getInSampleSize(BitmapFactory.Options option, int inWidth, int inHeight){
        int originalWidth = option.outWidth;
        int originalHeight = option.outHeight;

        int inSampleSize = 1;

        if (originalHeight > inWidth || originalWidth > inHeight){
            // get the width and height ratio
            final int heightRatio = Math.round((float) originalHeight / (float) inHeight);
            final int widthRatio = Math.round((float) originalWidth / (float) inWidth);
            // pick the largest ration
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }
        System.out.println(inSampleSize);
        return inSampleSize;
    }
}
