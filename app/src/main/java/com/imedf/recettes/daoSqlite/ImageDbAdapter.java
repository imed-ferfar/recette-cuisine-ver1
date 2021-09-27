package com.imedf.recettes.daoSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.imedf.recettes.RecetteActivity;
import com.imedf.recettes.modele.Recette;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageDbAdapter {
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;



    public ImageDbAdapter(Context context) {
        this.context =context;
        this.dbHelper = new DbHelper(context,DbHelper.BD_NOM, null, DbHelper.VERSION);

    }

    private void openMadb(){
        db = dbHelper.getWritableDatabase();
    }

    private void fermerBd(){
        db.close();
    }

    public void ajouterImages(Recette recette){
        openMadb();
        int idRecette = recette.getIdRecette();
        for (String image : recette.getImages()) {
            ContentValues cv = new ContentValues();
            cv.put(DbHelper.COL_ID_RECETTE, idRecette);
            cv.put(DbHelper.COL_IMAGE, image);
            db.insert(DbHelper.TABLE_IMAGE, null, cv);
            Toast.makeText(context,"image : "+image,Toast.LENGTH_LONG).show();
        }
        fermerBd();
        //Toast.makeText(context,"Ajout reussi",Toast.LENGTH_LONG).show();
    }

    public Set<String> chercherImages(int idRecette ) throws NoSuchFieldException, IllegalAccessException {
        Set<String> listImgs = new HashSet<>();
        //Toast.makeText(context,"Id : "+idRecette,Toast.LENGTH_SHORT).show();
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_ID_RECETTE, DbHelper.COL_IMAGE};
        Cursor curseur = db.query(DbHelper.TABLE_IMAGE,cols,"_id_rece='"+idRecette+"'",null,null,
                null,null,null );

        curseur.moveToFirst();
        while(!curseur.isAfterLast()){
            listImgs.add(curseur.getString(1));
          //  Toast.makeText(context,"Id : "+idRecette+" ,Image : "+curseur.getString(1),Toast.LENGTH_SHORT).show();
            curseur.moveToNext();
        }
        fermerBd();
        return listImgs;
    }
    public List<Recette> chercherRecettes() {

        List<Recette> maListe = new ArrayList<>();
        Recette maRecette;
        openMadb();
        String[] cols ={DbHelper.COL_ID_RECETTE, DbHelper.COL_TITRE, DbHelper.COL_PREPARATION};
        Cursor curseur = db.query(DbHelper.TABLE_RECETTE,cols,null,null,null,
                null,null,null );
        curseur.moveToFirst();
        while(!curseur.isAfterLast()){
            maRecette = new Recette();
            maRecette.setIdRecette(curseur.getInt(0));
            maRecette.setTitre(curseur.getString(1));
            maRecette.setPreparation(curseur.getString(2));
            //  maRecette.setImages(imageAdp.chercherImages(maRecette.getIdRecette()));
            Toast.makeText(context,"Id : "+maRecette.getIdRecette()+" ,Titre : "+maRecette.getTitre(),Toast.LENGTH_SHORT).show();
            maListe.add(maRecette);
            curseur.moveToNext();
        }
        fermerBd();
        return maListe;
    }

}
