package com.imedf.recettes.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import utils.GestionImage;

public class AsyncEnregistremantImg extends AsyncTask<Bitmap, String, String> {
    private Activity activite;
    private String imgStrReslt;
    private ProgressDialog p;

    public AsyncEnregistremantImg(Activity activite) {
        this.activite = activite;
    }

    public String getImgStrReslt() {
        return imgStrReslt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p = new ProgressDialog(activite);
        p.setMessage("Enregistrement image en cours ...");
        p.setIndeterminate(false);
        p.setCancelable(false);
        p.show();
    }
    @Override
    protected String doInBackground(Bitmap... bitmaps) {

        return GestionImage.convertirBitmapEnString(bitmaps[0]);
    }
    @Override
    protected void onPostExecute(String imageStr) {
        super.onPostExecute(imageStr);
        p.hide();
        imgStrReslt = imageStr;
        Toast.makeText(activite, "Image enregistrée.",Toast.LENGTH_LONG).show();

    }
}