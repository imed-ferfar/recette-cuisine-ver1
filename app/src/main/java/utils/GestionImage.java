package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.imedf.recettes.R;

import java.io.ByteArrayOutputStream;

public class GestionImage {

    public  static Bitmap convertirStringAbitmap(String imageStr){

        byte[] decodedStr = Base64.decode(imageStr,Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedStr,0,decodedStr.length);
        return decodedByte;
    }


    public static String convertirBitmapEnString(Bitmap image){
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.imgprofil) ;
        ByteArrayOutputStream monByte = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 20, monByte);
        image.recycle();
        byte[] byteArray = monByte.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }
}
