package com.imedf.recettes.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;


import java.io.FileNotFoundException;
import java.io.InputStream;

public class AsyncChargementImg extends AsyncTask<Uri, String, Bitmap> {

    private Activity activite;
    private ImageView imgRecette;
    private Bitmap bitmapReslt;
    private ProgressDialog p;

    public AsyncChargementImg(Activity activite, ImageView imgRecette) {
        this.activite = activite;
        this.imgRecette = imgRecette;
    }
    public Bitmap getBitmapReslt() {
        return bitmapReslt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p = new ProgressDialog(activite);
        p.setMessage("Chargement image en cours ...");
        p.setIndeterminate(false);
        p.setCancelable(false);
        p.show();
    }
    @Override
    protected Bitmap doInBackground(Uri... uris) {
        InputStream imgStream = null;
        try {
            imgStream = activite.getContentResolver().openInputStream(uris[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(imgStream,null,new BitmapFactory.Options());
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(imgRecette!=null) {
            imgRecette.setVisibility(ImageView.VISIBLE);
            imgRecette.setImageBitmap(bitmap);
            bitmapReslt = bitmap;
            p.hide();

        }else {
            p.show();
        }
    }
}