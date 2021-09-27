package com.imedf.recettes.daoSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imedf.recettes.daoSqlite.DbHelper;
import com.imedf.recettes.modele.Utilisateur;

public class UserDbAdapter {
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;

    public UserDbAdapter(Context context) {
        this.context =context;
        this.dbHelper = new DbHelper(context,DbHelper.BD_NOM, null, DbHelper.VERSION);

    }

    private void openMadb(){
        db = dbHelper.getWritableDatabase();
    }

    private void fermerBd(){
        db.close();
    }

    public void ajouterUtilisateur (Utilisateur perso){
        openMadb();
        String nom = perso.getNom();
        String prenom = perso.getPrenom();
        String courriel = perso.getCourriel();
        String motPasse = perso.getMotPasse();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_LAST_NAME, nom);
        cv.put(DbHelper.COL_FIRST_NAME,prenom);
        cv.put(DbHelper.COL_E_MAIL, courriel);
        cv.put(DbHelper.COL_PWD, motPasse);
        db.insert(DbHelper.TABLE_1, null, cv);
        fermerBd();
        //Toast.makeText(context,"Ajout reussi",Toast.LENGTH_LONG).show();
    }

   /* public void selectionnerUtilisateur() {
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_ID, DbHelper.COL_LAST_NAME, DbHelper.COL_FIRST_NAME, DbHelper.COL_E_MAIL, DbHelper.COL_PWD };
        Cursor curseur = db.query(DbHelper.TABLE_1,cols,null,null,null,
                null,null,null );
        int id;
        String nom, prenom, courriel, motPasse;
        curseur.moveToFirst();
        while(!curseur.isAfterLast()){
            id = curseur.getInt(0);
            nom = curseur.getString(1);
            prenom = curseur.getString(2);
            courriel = curseur.getString(3);
            motPasse = curseur.getString(4);
            Toast.makeText(context,"Id : "+id+" ,Nom : "+nom+" ,Prenom : "+prenom+" ,Courriel : "+courriel+" ,Mot de passe : "+motPasse,Toast.LENGTH_LONG).show();
            curseur.moveToNext();
        }
        fermerBd();
    }*/

    public Utilisateur rechercherUtilisateur(Utilisateur user) {
        openMadb();
        //indiquer les colonne de select
        String[] cols ={DbHelper.COL_ID, DbHelper.COL_LAST_NAME, DbHelper.COL_FIRST_NAME, DbHelper.COL_E_MAIL, DbHelper.COL_PWD };
        // 1ere technique : SQL
       // Cursor curseur = db.rawQuery("select * from utilisateur where courriel = ?", new String[] { user.getCourriel() });
        int id;
        // 2ieme technique : SQL
        Cursor curseur = db.query(DbHelper.TABLE_1,cols,"courriel='"+user.getCourriel()+"'",null,null,
                null,null);
        curseur.moveToFirst();
        if(curseur.getCount() == 1){
            id = curseur.getInt(0);
            user.setNom(curseur.getString(1));
            user.setPrenom(curseur.getString(2));
            user.setCourriel(curseur.getString(3));
            user.setMotPasse(curseur.getString(4));
            //Toast.makeText(context,"Bienvenue "+user.getPrenom()+" :)",Toast.LENGTH_LONG).show();
            fermerBd();
            return user;
        } else {
            fermerBd();
            return null;
        }
    }
    public void mettreAjourUtilisateur (Utilisateur user){
        openMadb();
        String nom = user.getNom();
        String prenom = user.getPrenom();
        String courriel = user.getCourriel();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_LAST_NAME, nom);
        cv.put(DbHelper.COL_FIRST_NAME,prenom);
        db.update(DbHelper.TABLE_1, cv, "courriel='"+courriel+"'", null);
        fermerBd();
        //Toast.makeText(context,"Mise à jour user reussi",Toast.LENGTH_LONG).show();
    }
    public void changerPasseUtilisateur (Utilisateur user){
        openMadb();
        String motPasse = user.getMotPasse();
        String courriel = user.getCourriel();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_PWD, motPasse);
        db.update(DbHelper.TABLE_1, cv, "courriel='"+courriel+"'", null);
        fermerBd();
        //Toast.makeText(context,"Mise à jour user reussi",Toast.LENGTH_LONG).show();
    }
}
