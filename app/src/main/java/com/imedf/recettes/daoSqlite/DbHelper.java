package com.imedf.recettes.daoSqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    //Declaration de constantes
    public static final String TABLE_1 = "utilisateur";
    public static final String TABLE_2 = "login";
    public static final String TABLE_RECETTE = "recette";
    public static final String TABLE_IMAGE = "image";
    public static final String TABLE_INGREDIENT = "ingredient";

    public static final String COL_ID = "_id";
    public static final String COL_LAST_NAME = "nom";
    public static final String COL_FIRST_NAME = "prenom";
    public static final String COL_E_MAIL = "courriel";
    public static final String COL_PWD = "motPasse";

    public static final String COL_ID_RECETTE = "_id_rece";
    public static final String COL_TITRE = "titre";
    public static final String COL_PREPARATION = "preparation";
    public static final String COL_IMAGE = "image";
    public static final String COL_DESC = "description";
    public static final String COL_UNITE = "unite";
    public static final String COL_QUTE = "quentite";

    public static final String BD_NOM = "recette";
    public static final int VERSION = 1;

    public static final String UTILISATEUR_DDL = "CREATE TABLE "+ TABLE_1 + " ("+COL_ID +" INTEGER primary key autoincrement, "+
            COL_LAST_NAME + " TEXT, "+COL_FIRST_NAME + " TEXT, " +COL_E_MAIL + " TEXT, " +COL_PWD +" TEXT)";
    public static final String AUTHENTIFICATION_DDL = "CREATE TABLE "+ TABLE_2 + " ("+COL_LAST_NAME + " TEXT, "+COL_FIRST_NAME +
            " TEXT, " +COL_E_MAIL + " TEXT, " +COL_PWD +" TEXT)";

    public static final String AUTHENTIFICATION_DDL_RECETTE = "CREATE TABLE "+ TABLE_RECETTE + " ("+COL_ID_RECETTE +" INTEGER primary key autoincrement, "+
            COL_TITRE + " TEXT, "+COL_PREPARATION + " TEXT)";
    public static final String AUTHENTIFICATION_DDL_IMAGE = "CREATE TABLE "+ TABLE_IMAGE + " ("+COL_ID_RECETTE +" INTEGER, "+
            COL_IMAGE + " TEXT)";
    public static final String AUTHENTIFICATION_DDL_INGREDIENT = "CREATE TABLE "+ TABLE_INGREDIENT + " ("+COL_ID_RECETTE +" INTEGER, "+
            COL_DESC + " TEXT, "+COL_UNITE + " TEXT, "+COL_QUTE + " REAL)";

    public DbHelper(@Nullable Context context, @Nullable  String name,
                        @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        /*=== Reamplacé par firebase database ===*/
        //sqliteDatabase.execSQL(UTILISATEUR_DDL);
        //sqliteDatabase.execSQL(AUTHENTIFICATION_DDL);

        sqliteDatabase.execSQL(AUTHENTIFICATION_DDL_RECETTE);
        sqliteDatabase.execSQL(AUTHENTIFICATION_DDL_IMAGE);
        sqliteDatabase.execSQL(AUTHENTIFICATION_DDL_INGREDIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
