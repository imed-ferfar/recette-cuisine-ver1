package com.imedf.recettes.daoSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.imedf.recettes.modele.Ingredient;
import com.imedf.recettes.modele.Recette;

public class IngredientDbAdapter {
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;

    public IngredientDbAdapter(Context context) {
        this.context =context;
        this.dbHelper = new DbHelper(context,DbHelper.BD_NOM, null, DbHelper.VERSION);
    }

    private void openMadb(){
        db = dbHelper.getWritableDatabase();
    }

    private void fermerBd(){
        db.close();
    }

    public void ajouterIngredients (Recette recette){
        openMadb();
        int id = recette.getIdRecette();
        for (Ingredient ingred : recette.getIngredients()) {
            ContentValues cv = new ContentValues();
            cv.put(DbHelper.COL_ID_RECETTE, id);
            cv.put(DbHelper.COL_DESC, ingred.getDescription());
            cv.put(DbHelper.COL_UNITE, ingred.getUnite());
            cv.put(DbHelper.COL_QUTE, ingred.getQuantite());
            db.insert(DbHelper.TABLE_INGREDIENT, null, cv);
        }
        fermerBd();
    }

    public void selectionnerIngredients() {
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

    public Recette rechercherIngredient(Recette recette) {
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
    public void ajouterngredients(Recette recette){
        openMadb();
        int idRecette = recette.getIdRecette();
        for (Ingredient ingred : recette.getIngredients()) {
            ContentValues cv = new ContentValues();
            cv.put(DbHelper.COL_ID_RECETTE, idRecette);
            cv.put(DbHelper.COL_DESC, ingred.getDescription());
            cv.put(DbHelper.COL_UNITE, ingred.getUnite());
            cv.put(DbHelper.COL_QUTE, ingred.getQuantite());
            db.insert(DbHelper.TABLE_INGREDIENT, null, cv);
           // Toast.makeText(context,"image : "+image,Toast.LENGTH_LONG).show();
        }
        fermerBd();
        //Toast.makeText(context,"Ajout reussi",Toast.LENGTH_LONG).show();
    }
}

