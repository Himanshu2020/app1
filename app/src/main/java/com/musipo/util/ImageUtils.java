package com.musipo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by G510 on 29-05-2017.
 */

public class ImageUtils {

    public static void saveImage(Context context, Bitmap b, String name){
        name=name+"."+"png";
        FileOutputStream out;
        try {
            out = context.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static  Bitmap getImageBitmap(Context context,String name){
        name=name+"."+"png";
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }


    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getImageFrmString(String encodedImageString){
        Log.e("DDDDDDDDDDDD",encodedImageString);
        byte[] bytarray = Base64.decode(encodedImageString, Base64.DEFAULT);
        Bitmap bmimage = BitmapFactory.decodeByteArray(bytarray, 0,
                bytarray.length);
      /*  byte[] decodedBytes = Base64.decode(encodedImageString, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);*/
        return bmimage;
    }


}
