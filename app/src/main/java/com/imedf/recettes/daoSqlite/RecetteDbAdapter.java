package com.imedf.recettes.daoSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.imedf.recettes.RecetteActivity;
import com.imedf.recettes.modele.Recette;
import com.imedf.recettes.modele.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class RecetteDbAdapter {
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;

    private ImageDbAdapter imageAdp;

    public RecetteDbAdapter(Context context) {
        this.context =context;
        this.dbHelper = new DbHelper(context,DbHelper.BD_NOM, null, DbHelper.VERSION);

        imageAdp = new ImageDbAdapter(context);
    }

    private void openMadb(){
        db = dbHelper.getWritableDatabase();
    }

    private void fermerBd(){
        db.close();
    }

    public void ajouterRecette (Recette recette){
        openMadb();
        String titre = recette.getTitre();
        String preparation = recette.getPreparation();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_TITRE, titre);
        cv.put(DbHelper.COL_PREPARATION,preparation);

        db.insert(DbHelper.TABLE_RECETTE, null, cv);
        fermerBd();
       // imageAdp.ajouterImages(recette);
        //Toast.makeText(context,"Ajout reussi",Toast.LENGTH_LONG).show();
    }

    public void selectionnerRecettes() {
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_ID, DbHelper.COL_TITRE, DbHelper.COL_PREPARATION};
        Cursor curseur = db.query(DbHelper.TABLE_RECETTE,cols,null,null,null,
                null,null,null );
        int id;
        String titre, preparation;
        curseur.moveToFirst();
        while(!curseur.isAfterLast()){
            id = curseur.getInt(0);
            titre = curseur.getString(1);
            preparation = curseur.getString(2);
            Toast.makeText(context,"Id : "+id+" ,Titre : "+titre+" ,Preparation : "+preparation,Toast.LENGTH_LONG).show();
            curseur.moveToNext();
        }
        fermerBd();
    }
    public int chercherDernierId() {
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_ID_RECETTE, DbHelper.COL_TITRE, DbHelper.COL_PREPARATION};
        Cursor curseur = db.query(DbHelper.TABLE_RECETTE,cols,null,null,null,
                null,null,null );
        int id;
        String titre, preparation;
        curseur.moveToLast();
       // while(!curseur.isAfterLast()){
            id = curseur.getInt(0);
        //    titre = curseur.getString(1);
        //    preparation = curseur.getString(2);
            Toast.makeText(context,"Last Id : "+id,Toast.LENGTH_LONG).show();
        fermerBd();
        return id;
    }

    public Recette rechercherRecette(Recette recette) {
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_ID_RECETTE, DbHelper.COL_TITRE, DbHelper.COL_PREPARATION};
        // 1ere technique : SQL
        // Cursor curseur = db.rawQuery("select * from utilisateur where courriel = ?", new String[] { user.getCourriel() });
        int id;
        // 2ieme technique : SQL
        Cursor curseur = db.query(DbHelper.TABLE_RECETTE,cols,"titre='"+recette.getTitre()+"'",null,null,
                null,null);
        curseur.moveToFirst();
        if(curseur.getCount() == 1){
            id = curseur.getInt(0);
            recette.setPreparation(curseur.getString(1));
            //Toast.makeText(context,"Bienvenue "+user.getPrenom()+" :)",Toast.LENGTH_LONG).show();
            fermerBd();
            return recette;
        } else {
            fermerBd();
            return null;
        }
    }
    public void mettreAjourRecette (Recette recette){
        openMadb();
        String titre = recette.getTitre();
        String preparation = recette.getPreparation();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_TITRE, titre);
        cv.put(DbHelper.COL_PREPARATION,preparation);
        db.update(DbHelper.TABLE_RECETTE, cv, "titre='"+titre+"'", null);
        fermerBd();
        //Toast.makeText(context,"Mise à jour user reussi",Toast.LENGTH_LONG).show();
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
          //  Toast.makeText(context,"Id : "+maRecette.getIdRecette()+" ,Titre : "+maRecette.getTitre(),Toast.LENGTH_SHORT).show();
            maListe.add(maRecette);
            curseur.moveToNext();
        }
        fermerBd();
        return maListe;
    }
}
