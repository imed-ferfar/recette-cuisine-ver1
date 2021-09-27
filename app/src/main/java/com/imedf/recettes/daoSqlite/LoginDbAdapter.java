package com.imedf.recettes.daoSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imedf.recettes.daoSqlite.DbHelper;
import com.imedf.recettes.modele.Utilisateur;

public class LoginDbAdapter {
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;

    public LoginDbAdapter(Context context) {
        this.context =context;
        this.dbHelper = new DbHelper(context,DbHelper.BD_NOM, null, DbHelper.VERSION);

    }

    private void openMadb(){
        db = dbHelper.getWritableDatabase();
    }

    private void fermerBd(){
        db.close();
    }

    public void ajouterLogin (Utilisateur user){
        openMadb();
        String nom = user.getNom();
        String prenom = user.getPrenom();
        String courriel = user.getCourriel();
        String motPasse = user.getMotPasse();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_LAST_NAME, nom);
        cv.put(DbHelper.COL_FIRST_NAME,prenom);
        cv.put(DbHelper.COL_E_MAIL, courriel);
        cv.put(DbHelper.COL_PWD, motPasse);
        db.insert(DbHelper.TABLE_2, null, cv);
        fermerBd();
        //Toast.makeText(context,"Ajout login reussi",Toast.LENGTH_LONG).show();
    }

    public Utilisateur selectionnerLogin() {
        Utilisateur user;
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_LAST_NAME, DbHelper.COL_FIRST_NAME, DbHelper.COL_E_MAIL, DbHelper.COL_PWD };
        Cursor curseur = db.query(DbHelper.TABLE_2,cols,null,null,null,
                null,null,null );
        curseur.moveToFirst();

        if(curseur.getCount() == 1){
            user = new Utilisateur();
            user.setNom(curseur.getString(0));
            user.setPrenom(curseur.getString(1));
            user.setCourriel(curseur.getString(2));
            user.setMotPasse(curseur.getString(3));
            //Toast.makeText(context,"Login selectionne : "+user.toString(),Toast.LENGTH_LONG).show();
            fermerBd();
            return user;
        }
        fermerBd();
        return null;
    }

    public void mettreAjourLogin (Utilisateur user){
        openMadb();
        String nom = user.getNom();
        String prenom = user.getPrenom();
        String courriel = user.getCourriel();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_LAST_NAME, nom);
        cv.put(DbHelper.COL_FIRST_NAME,prenom);
        db.update(DbHelper.TABLE_2, cv, "courriel='"+courriel+"'", null);
        fermerBd();
       // Toast.makeText(context,"Mise à jour login reussi",Toast.LENGTH_LONG).show();
    }
    public void changerPasseLogin (Utilisateur user){
        openMadb();
        String motPasse = user.getMotPasse();
        String courriel = user.getCourriel();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_PWD, motPasse);
        db.update(DbHelper.TABLE_2, cv, "courriel='"+courriel+"'", null);
        fermerBd();
        // Toast.makeText(context,"Mise à jour login reussi",Toast.LENGTH_LONG).show();
    }


    public void viderLogin(){
        openMadb();
        db.delete(DbHelper.TABLE_2,null,null);
      //  context.deleteDatabase(DbHelper.TABLE_2);
        fermerBd();
    }
}
